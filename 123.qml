import QtQuick 2.0

Rectangle {
    anchors.fill: parent
    color: "transparent"

    Rectangle {
        width: parent.width
        height: parent.height
        border.color: "black"
        border.width: 2
    }

    Rectangle {
        id: bottomPaddle
        width: 100
        height: 20
        color: "blue"
        y: parent.height - height - 10  // Размещаем нижнюю ракетку с отступом 10 пикселей от нижнего края
    }

    Rectangle {
        id: topPaddle
        width: 100
        height: 20
        color: "red"
        y: 10  // Размещаем верхнюю ракетку с отступом 10 пикселей от верхнего края
    }

    Rectangle {
        id: ball
        width: 20
        height: 20
        color: "green"
        radius: 10
        x: (parent.width - width) / 2
        y: (parent.height - height) / 2
        property real dx: 10
        property real dy: 10
    }

    Rectangle {
        id: topGoal
        width: parent.width / 2
        height: 10
        color: "yellow"
        x: (parent.width - width) / 2
        y: 0
    }

    Rectangle {
        id: bottomGoal
        width: parent.width / 2
        height: 10
        color: "yellow"
        x: (parent.width - width) / 2
        y: parent.height - height
    }

    Text {
        id: topScoreText
        text: "Top: 0"
        font.pixelSize: 24
        color: "black"
        anchors.left: parent.left
        anchors.leftMargin: 20
        anchors.top: parent.top
        anchors.topMargin: 20
    }

    Text {
        id: bottomScoreText
        text: "Bottom: 0"
        font.pixelSize: 24
        color: "black"
        anchors.left: parent.left
        anchors.leftMargin: 20
        anchors.top: topScoreText.bottom
        anchors.topMargin: 10
    }

    MouseArea {
        id: mouseArea
        anchors.fill: parent

        onPositionChanged: {
            bottomPaddle.x = mouseX - bottomPaddle.width / 2
        }
    }

    Timer {
        id: gameLoop
        interval: 16 // Примерно 60 кадров в секунду
        repeat: true
        running: true
        onTriggered: {
            var ballNextX = ball.x + ball.dx
            var ballNextY = ball.y + ball.dy

            // Проверка столкновения с боковыми стенами
            if (ballNextX <= 0  ballNextX + ball.width >= parent.width) {
                ball.dx *= -1
            }

            // Проверка столкновения с верхней ракеткой
            if (ballNextY <= topPaddle.y + topPaddle.height && ballNextY >= topPaddle.y &&
                ball.x + ball.width >= topPaddle.x && ball.x <= topPaddle.x + topPaddle.width) {
                ball.dy *= -1
                ball.y = topPaddle.y + topPaddle.height
            }

            // Проверка столкновения с нижней ракеткой
            if (ballNextY + ball.height >= bottomPaddle.y && ballNextY + ball.height <= bottomPaddle.y + bottomPaddle.height &&
                ball.x + ball.width >= bottomPaddle.x && ball.x <= bottomPaddle.x + bottomPaddle.width) {
                ball.dy *= -1
                ball.y = bottomPaddle.y - ball.height
            }

            // Проверка попадания в ворота
            if (ballNextY <= topGoal.y + topGoal.height && ballNextY >= topGoal.y &&
                ball.x + ball.width >= topGoal.x && ball.x <= topGoal.x + topGoal.width) {
                bottomScore += 1
                bottomScoreText.text = "Bottom: " + bottomScore
                resetBall()
            }

            if (ballNextY + ball.height >= bottomGoal.y && ballNextY + ball.height <= bottomGoal.y + bottomGoal.height &&
                ball.x + ball.width >= bottomGoal.x && ball.x <= bottomGoal.x + bottomGoal.width) {
                topScore += 1
                topScoreText.text = "Top: " + topScore
                resetBall()
            }

            // Проверка столкновения с верхней и нижней границей
            if (ballNextY <= 0  ballNextY + ball.height >= parent.height) {
                ball.dy *= -1
            }

            // Обновление позиции мяча
            ball.x += ball.dx
            ball.y += ball.dy
// Улучшенный интеллект компьютера
            if (ball.dy < 0) { // Мяч движется к верхней ракетке
                if (ball.x < topPaddle.x + topPaddle.width / 2) {
                    topPaddle.x -= 5 // Скорость движения ракетки компьютера
                } else {
                    topPaddle.x += 5 // Скорость движения ракетки компьютера
                }
                // Ограничение движения ракетки
                if (topPaddle.x < 0) {
                    topPaddle.x = 0
                }
                if (topPaddle.x + topPaddle.width > parent.width) {
                    topPaddle.x = parent.width - topPaddle.width
                }
            }
        }
    }

    function resetBall() {
        ball.x = (parent.width - ball.width) / 2
        ball.y = (parent.height - ball.height) / 2
        ball.dx = 10
        ball.dy = 10
    }

    Component.onCompleted: {
        ball.dx = 10
        ball.dy = 10
        gameLoop.start()
    }

    property int topScore: 0
    property int bottomScore: 0
}