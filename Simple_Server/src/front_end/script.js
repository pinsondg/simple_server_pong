var leftPaddle, rightPaddle, ball, paused, leftScore, rightScore;

var myGameArea = {
  gamearea: document.getElementById('gamearea'),
  start: function () {
    console.log('Begin setup');
    if (this.gamearea == null ) {
      this.gamearea = document.createElement('canvas');
      this.gamearea.id = 'gmaearea'
      this.gamearea.height = 500;
      this.gamearea.width = 500;
      this.gamearea.style = "border:1px solid #000000;";
      document.body.insertBefore(this.gamearea,
        document.body.childNodes[document.body.childElementCount + 3]);
    }
    this.context = this.gamearea.getContext('2d');
    this.interval = setInterval(updateGameArea, 20);
  },
  reset: function () {
    this.context.clearRect(0, 0, this.gamearea.width, this.gamearea.height);
  }
}

function Paddle (x, y, id) {
  this.x = x;
  this.y = y;
  this.lenght = 100;
  this.width = 10;
  this.id = id;

  this.update = function () {
    myGameArea.context.fillStyle = '#000000';
    myGameArea.context.fillRect(this.x, this.y, this.width, this.lenght);
  }

  this.moveUp = function () {
    this.y = this.y - 5;
    if (this.y <= 0) {
      this.y = 0;
    }
  }

  this.moveDown = function () {
    this.y = this.y + 5;
    if (this.y + 100 >= myGameArea.gamearea.height) {
      this.y = myGameArea.gamearea.height - 100;
    }
  }

}

function Ball(x, y, xspeed, yspeed) {

  this.x = x;
  this.y = y;
  this.xdir = Math.floor(Math.random() * 3) + 1;
  this.ydir = Math.floor(Math.random() * 3);
  this.xspeed = xspeed;
  this.yspeed = yspeed;

  this.update = function () {
    myGameArea.context.fillStyle = '#000000';
    myGameArea.context.fillRect(this.x, this.y, 10, 10);
  }
}

function updateGameArea() {
  myGameArea.reset();
  leftPaddle.update();
  rightPaddle.update();
  updateBall();
  updateRightPaddle();
  checkWinner()
}

async function checkWinner() {
  if (leftScore >= 5) {
    sendDataToServer(false);
    reset();
  } else if (rightScore >= 5) {
    reset();
  }
}

function updateRightPaddle() {
  var ydist = ball.y + 5 - (rightPaddle.y + 50);
  var xdist = ball.x + 10 - rightPaddle.x;
  if (ball.xdir > 0 && ball.x >= myGameArea.gamearea.width / 2) {
    if (ydist < -5) {
      rightPaddle.moveUp();
    } else if (ydist > 5) {
      rightPaddle.moveDown();
    }
  }

}

function updateBall() {
  if (hitPaddle()) {
    console.log("HIT!")
    determineBallMovement()
  }
  if (ball.x <= 0 || ball.x + 10 >= myGameArea.gamearea.width) {
    if (ball.x <= 0) {
      rightScore++;
    } else {
      leftScore++;
    }
    resetBallPosition();
    document.getElementById('score').innerHTML = leftScore + ' - ' + rightScore;
  }
  if (ball.y <= 0 || ball.y + 10 >= myGameArea.gamearea.height) {
    ball.ydir = ball.ydir * -1;
  }
  ball.x = ball.xspeed * ball.xdir + ball.x;
  ball.y = ball.yspeed * ball.ydir + ball.y;
  ball.update();
}

function determineBallMovement() {
  var centerLeftPaddle = leftPaddle.y + leftPaddle.lenght / 2
  var centerRightPaddle = rightPaddle.y + rightPaddle.lenght / 2

  ball.xdir = ball.xdir * -1;
  var speed = 0;
  if (ball.x <= leftPaddle.x + leftPaddle.width) {
    console.log("Left-HIT");
    speed = (ball.y + 5) - centerLeftPaddle;
  } else {
    speed = (ball.y + 5) - centerRightPaddle;
  }
  console.log(speed);
  ball.yspeed = Math.abs(speed % 4);
  console.log(ball.yspeed);
  if (speed !== 0) {
    ball.ydir = speed / Math.abs(speed);
    console.log(ball.ydir);
  } else {
    ball.ydir = 0;
  }
  ball.xspeed = Math.abs(4 - speed % 4);
  console.log(ball.xspeed);
}

function resetBallPosition() {
  ball.x = myGameArea.gamearea.width / 2;
  ball.y = myGameArea.gamearea.height / 2;
  ball.xspeed = 0;
  ball.yspeed = 0;
  ball.xdir = Math.floor(Math.random() * 3) - 1;
  if (ball.xdir === 0) {
    ball.xdir = 1;
  }
  paused = true;
}

function hitPaddle() {
  return ((ball.x <= leftPaddle.x + 10 && ball.x >= leftPaddle.x) && (ball.y >= leftPaddle.y && ball.y <= leftPaddle.y + 100))
  || ((ball.x + 10 >= rightPaddle.x && ball.x <= rightPaddle.x) && (ball.y >= rightPaddle.y && ball.y <= rightPaddle.y + 100));
}

function startGame() {
  myGameArea.start();
  paused = true;
  leftPaddle = new Paddle(40, myGameArea.gamearea.height / 2 - 50, 'leftPaddle')
  rightPaddle = new Paddle(myGameArea.gamearea.width - 50, myGameArea.gamearea.height / 2 - 50, 'rightPaddle')
  ball = new Ball(myGameArea.gamearea.height / 2, myGameArea.gamearea.width / 2, 0, 0);
  setUpKeyListener();
  leftScore = 0;
  rightScore = 0;
  sendDataToServer(true);
}


function setUpKeyListener() {
  window.onkeydown = function (event) {
    var key = event.key;

    if (key === 'ArrowUp') {
      event.preventDefault();
      leftPaddle.moveUp();
    } else if (key === 'ArrowDown') {
      leftPaddle.moveDown();
      event.preventDefault();
    } else if (key === 'Enter' && paused) {
      paused = false;
      ball.xspeed = 3;
    } else if (key === 'r' || key === 'R') {
      reset();
    }
  }
}

function reset() {
  myGameArea.reset();
  resetBallPosition();
  leftScore = 0;
  rightScore = 0;
  document.getElementById('score').innerHTML = leftScore + ' - ' + rightScore;
  rightPaddle.y = myGameArea.gamearea.height / 2 - 50;
  leftPaddle.y = myGameArea.gamearea.height / 2 - 50;
}

function sendDataToServer(initial) {
  var xmlhttp
  if (window.XMLHttpRequest) {
     xmlhttp = new XMLHttpRequest();
   } else {
     xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
   }
   xmlhttp.onreadystatechange = function() {
    //console.log(this.readyState);
    if (this.readyState === 4 && this.status === 200) {
      document.getElementById("table").innerHTML =
      this.responseText;
    }
  };
   xmlhttp.open("POST", "table.dat", true);
   if (!initial) {
     xmlhttp.send("Message: Daniel," + leftScore + "," + rightScore + ";");
   } else {
     xmlhttp.send("Message: Initial");
   }

}
