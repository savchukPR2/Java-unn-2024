package Server;

import Entity.*;
import Messages.*;
import Customer.*;
import org.hibernate.*;
import Target.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private int port;
    private int maxConnections;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static Map<Integer, Customer> playersMap = new HashMap<>();
    private int countsOfPlayer;
    private int copyOfPlayer;
    private String[] serverInfo = new String[3];
    private ArrayList<Target> rounds;
    private Thread gameThread;
    private SessionFactory sessionFactory;
    private Map<Integer, Customer> DBPlayerMap = new HashMap<>();
    private boolean connectToDB = false;
    private int time;
    private Thread timeThread;
    private Thread timeShoserThread;
    private ServerThread serverThread;
    private boolean isSolo;

    // Конструктор сервера
    public Server(String serverName, int port, int maxConnections, int time) {
        this.port = port;
        this.maxConnections = maxConnections + 1;
        countsOfPlayer = 0;
        this.time = time;

        serverInfo[0] = serverName;
        serverInfo[1] = "1";
        serverInfo[2] = String.valueOf(this.maxConnections);
    }

    // Запуск сервера
    public void startServer() throws IOException {
        serverThread = new ServerThread(port, maxConnections);
        serverThread.start();
    }

    // Получение IP-адреса сервера
    public InetAddress getServerIPAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Обновление информации о сервере
    private void updateServerInfo() {
        serverInfo[1] = String.valueOf(countsOfPlayer);
    }

    // Установка сессий для работы с базой данных
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        connectToDB = true;
    }

    // Остановка сервера
    public void stopServer() throws IOException {
        serverThread.serverSocket.close();
        serverThread.interrupt();
        clients.clear();
        playersMap.clear();
    }

    // Отправка информации в базу данных
    public void sendToDBInfo(String name) {
        if(connectToDB) {
            Session session = sessionFactory.openSession();
            try {
                Transaction transaction = session.beginTransaction();

                Comparison player = session.createQuery("FROM Comparison WHERE username = :name", Comparison.class)
                        .setParameter("name", name)
                        .uniqueResult();

                if (player != null) {
                    String playerInfo = player.getInfo();
                    int wins = Integer.parseInt(playerInfo);
                    wins++;
                    player.setInfo(Integer.toString(wins));

                    session.update(player);
                    transaction.commit();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                session.close();
            }
        }
    }

    // Поток для обработки подключений клиентов
    private class ServerThread extends Thread {
        int port;
        int maxConnections;
        ServerSocket serverSocket;

        // Конструктор потока сервера
        public ServerThread(int port, int maxConnections) throws IOException {
            this.port = port;
            this.maxConnections = maxConnections + 1;
            serverSocket = new ServerSocket(port, maxConnections);
        }

        // Ожидание подключений
        @Override
        public void run() {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    clientHandler.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Поток для обработки клиентов
    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        // Конструктор обработчика клиента
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.out = new ObjectOutputStream(clientSocket.getOutputStream());
                this.in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Обработка сообщений от клиента
        @Override
        public void run() {
            try {
                while (true) {
                    Object message = in.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                clients.remove(this);
            }
        }

        // Отправка сообщения всем клиентам
        public void sendMessageToAll(Object message) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.sendMessage(message);
                }
            }
        }

        // Отправка сообщения клиенту
        private void sendMessage(Object message) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                clients.remove(this);
            }
        }

        // Отправка информации о сервере
        private void sendServerInfo() {
            try {
                out.writeObject(serverInfo);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Закрытие клиента
        private void closeClient(){
            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Начало игры
        private void startGame() {
            int delayMillis = 10;
            Map<String, Integer> coordinateTargets = new HashMap<>();
            for (Target round : rounds) {
                coordinateTargets.put(round.getName(), round.getRoundX());
            }

            gameThread = new Thread(() ->{
                try {
                    while (true) {
                        Map<String, Integer> copyCoordinateTargets = new HashMap<>(coordinateTargets);
                        synchronized (rounds) {
                            for (Target round : rounds) {
                                if (round.getName().equals("smallLeft")) {
                                    round.move(true, false);
                                } else if (round.getName().equals("smallRight")) {
                                    round.move(false, true);
                                } else {
                                    round.move(false, false);
                                }
                                copyCoordinateTargets.put(round.getName(), round.getRoundX());
                            }
                        }
                        sendMessageToAll(copyCoordinateTargets);
                        Thread.sleep(delayMillis);
                    }
                }

                catch (InterruptedException e) {

                }
            });
            gameThread.start();
        }

        // Запуск таймера
        private void startTimer() {
            timeThread = new Thread(() -> {
                try {
                    int oneSecond = 1000;
                    while (time > 0) {
                        Thread.sleep(oneSecond);
                        time--;
                        Object message = new TimeMessagee(time, false);
                        sendMessageToAll(message);
                    }
                    if (gameThread.isAlive()) {
                        gameThread.interrupt();
                        endGame();
                    }
                }

                catch (InterruptedException e) {

                }
            });
            timeThread.start();
        }

        // Запуск таймера выбора
        private void startChoseTimer() {
            timeShoserThread = new Thread(() -> {
                int timer = 20; // Устанавливаем начальное время в 20 секунд
                try {
                    int oneSecond = 1000;
                    while (timer > 0) {
                        Thread.sleep(oneSecond);
                        timer--;
                        Object message = new TimeMessagee(timer, true);
                        sendMessageToAll(message);
                    }
                    sendMessageToAll("CloseSmall");
                } catch (InterruptedException e) {

                }
            });
            timeShoserThread.start();
        }

        // Завершение игры
        private void endGame() {
            int scoreMax = 0;
            int ID = -1;
            boolean isEquals = false;
            String namePlayer = "";

            // Определение победителя
            for(Map.Entry<Integer, Customer> entry : playersMap.entrySet()) {
                Integer IDs = entry.getKey();
                Customer player = entry.getValue();
                int score = player.getScore();
                namePlayer = player.getName();
                if(score > scoreMax) {
                    scoreMax = player.getScore();
                    ID = IDs;
                } else if(score == scoreMax && score > 0){
                    isEquals = true;
                }
            }

            // Отправка сообщения о завершении игры
            Object message = new EndGameMessage(ID, scoreMax, isEquals);
            sendMessageToAll(message);

            // Отправка информации в базу данных, если нет ничьей
            if(!isEquals) {
                sendToDBInfo(namePlayer);
            }
        }

        // Обработка сообщений от клиентов
        private void handleMessage(Object message) throws IOException {
            // Обработка строковых сообщений
            if (message instanceof String) {
                String request = (String) message;
                if(request.equals("GetServerInfo")) {
                    sendServerInfo();
                    closeClient();
                }
                if(request.equals("CreateGame")) {
                    sendMessageToAll("CreateGame");
                    message = new TimeMessagee(time, false);
                    sendMessageToAll(message);
                    return;
                }
            }

            // Обработка сообщений подключения клиента
            if(message instanceof ConnectMessage) {
                ConnectMessage conmes = (ConnectMessage) message;
                Customer client = conmes.getPlayer();

                for (Map.Entry<Integer, Customer> entry : playersMap.entrySet()) {
                    Customer value = entry.getValue();
                    if(Objects.equals(client.getName(), value.getName())){
                        sendMessage("ChangeName");
                        closeClient();
                        return;
                    } else if(countsOfPlayer >= maxConnections) {
                        sendMessage("MaxPlayers");
                        closeClient();
                        return;
                    }
                }

                client.setID(countsOfPlayer);
                client.setXPosition(110 * countsOfPlayer);

                message = new SetPlayerMessage(client);
                sendMessage(message);

                playersMap.put(countsOfPlayer, client);
                Map<Integer, Customer> copyPlayerMap = new HashMap<>(playersMap);
                message = new UpdatePlayersMessage(copyPlayerMap);
                sendMessageToAll(message);

                sendMessage("CreateHub");
                sendMessageToAll("UpdateHub");

                countsOfPlayer++;
                copyOfPlayer = countsOfPlayer;
                updateServerInfo();
                if(playersMap.size() <= 1) {
                    isSolo = true;
                } else {
                    isSolo = false;
                }
            }

            // Обработка игровых событий
            if(message instanceof GameEventMessage) {
                sendMessageToAll(message);
            }

            // Обработка сообщений о начале игры
            if(message instanceof StartMessage) {
                StartMessage start = (StartMessage) message;
                if(timeShoserThread == null || !timeShoserThread.isAlive()) {
                    startChoseTimer();
                }
                if(start.getStart()) {
                    copyOfPlayer--;
                    sendMessageToAll(message);
                    if(copyOfPlayer == 0) {
                        sendMessageToAll("CloseSmall");
                        sendMessageToAll("StartGame");
                        timeShoserThread.interrupt();
                        startGame();
                        startTimer();
                        copyOfPlayer = countsOfPlayer;
                    }
                }

                else {
                    sendMessageToAll(message);
                    sendMessageToAll("CloseSmall");
                    copyOfPlayer = countsOfPlayer;
                }
            }

            // Обработка сообщений о завершении игры
            if(message instanceof StopMessage) {
                StopMessage stop = (StopMessage) message;
                if(timeShoserThread == null || !timeShoserThread.isAlive()) {
                    startChoseTimer();
                }
                if(stop.getStop()) {
                    copyOfPlayer--;
                    sendMessageToAll(message);
                    if(copyOfPlayer == 0) {
                        sendMessageToAll("CloseSmall");
                        sendMessageToAll("StopGame");
                        timeShoserThread.interrupt();
                        gameThread.interrupt();
                        timeThread.interrupt();
                        copyOfPlayer = countsOfPlayer;
                    }
                }

                else {
                    sendMessageToAll(message);
                    sendMessageToAll("CloseSmall");
                    copyOfPlayer = countsOfPlayer;
                }
            }

            // Обработка сообщений о выходе клиента
            if(message instanceof ExitMessage) {
                ExitMessage exit = (ExitMessage) message;
                if(exit.getID() == 0 || countsOfPlayer == 0) {
                    sendMessageToAll("HostLeave");
                    Thread.currentThread().interrupt();
                    stopServer();
                }
                countsOfPlayer--;
                clients.remove(exit.getID());
                playersMap.remove(exit.getID());
            }

            // Обработка сообщений о повторной игре
            if(message instanceof RepeatGame) {
                RepeatGame repeat = (RepeatGame) message;
                if(repeat.isRepeat()) {
                    sendMessageToAll(message);
                    copyOfPlayer--;
                    if(copyOfPlayer == 0) {
                        copyOfPlayer = countsOfPlayer;
                    }
                } else {
                    sendMessageToAll(message);
                }
            }

            // Обработка сообщений о раундах
            if(message instanceof ArrayList) {
                rounds = (ArrayList<Target>) message;
            }

            // Обработка сообщений о выстрелах
            if(message instanceof ShootMessage) {
                ShootMessage shoot = (ShootMessage) message;
                sendMessageToAll(message);
                playersMap.get(shoot.getID()).increaseHits();
            }

            // Обработка сообщений о счете
            if(message instanceof ScoreMessage) {
                sendMessageToAll(message);
            }

            // Обработка сообщений о завершении игры
            if(message instanceof EndGameMessage) {
                EndGameMessage scoreMessage = (EndGameMessage) message;
                playersMap.get(scoreMessage.getID()).increaseScore(scoreMessage.getScore());
            }

            // Обработка сообщений о цвете
            if(message instanceof SetColorMessage) {
                SetColorMessage setColor = (SetColorMessage) message;
                Customer client = setColor.getPlayer();
                client.setColor(setColor.getColor());
                message = new SetPlayerMessage(client);
                sendMessage(message);

                playersMap.replace(client.getID(), client);
                Map<Integer, Customer>copyPlayerMap = new HashMap<>(playersMap);
                message = new UpdatePlayersMessage(copyPlayerMap);
                sendMessageToAll(message);
                sendMessageToAll("UpdateHub");
            }
        }
    }
}