import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener{
  
  static final int SCREEN_WIDTH = 931;
  
  static final int SCREEN_HEIGHT = 700;
  
  static final int UNIT_SIZE = 25;
  
  static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
  
  static final int DELAY = 75;
  
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  
  int bodyParts = 6;
  int burritoEaten;
  int burritoX;
  int burritoY;
  char direction = 'R';
  boolean running = false;
  
  boolean finalAnswer;
  
  Timer timer;
  Random rand;
  
  BufferedImage backgroundSteast;
  BufferedImage steastBurrito;
  BufferedImage steastCake;
  BufferedImage lose;
  BufferedImage askOut;
  BufferedImage answer;
  File bg;
  File burrito; 
  File cake;
  File ans;
  File los;
  File ask;
  
  Graphics g2;
  
  int score;
  
  GamePanel() {
    rand = new Random();
    //read image
    try{
      bg = new File("/Users/josephdispirito/Desktop/Desktop/Image_700.jpg"); //image file path
      burrito = new File("/Users/josephdispirito/Desktop/Desktop/steastBurrito_37.png");
      cake = new File("/Users/josephdispirito/Desktop/Desktop/steastCake_35.png"); //image file path
      
      los = new File("/Users/josephdispirito/Desktop/Desktop/gameOver.png");
      ask = new File("/Users/josephdispirito/Desktop/Desktop/askOut3.png");
      ans = new File("/Users/josephdispirito/Desktop/Desktop/answer.png");
      backgroundSteast = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
      backgroundSteast = ImageIO.read(bg);
      steastBurrito = ImageIO.read(burrito);
      steastCake = ImageIO.read(cake);
      
      lose = ImageIO.read(los);
      askOut =ImageIO.read(ask);
      answer = ImageIO.read(ans);
      System.out.println("Reading complete.");
    }catch(IOException e){
      System.out.println("Error: "+e);
    }
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame();
  }

  public void startGame() {
    newBurrito();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
    if (finalAnswer) {
      g.drawImage(answer, 0, 0, null);
    }
  }
  
  public void draw(Graphics g) {
    
    if (running) {
    
    g.drawImage(backgroundSteast, 0, 0, null);
    if (score % 2 == 0) {
      g.drawImage(steastBurrito, burritoX, burritoY, null);
    }
    else {
      g.drawImage(steastCake, burritoX, burritoY, null);
    }
    
    for (int i = 0; i<bodyParts; i++) {
      if(i==0) {
        g.setColor(Color.green);
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
      }
      else {
        g.setColor(new Color(45, 180, 0));
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
      }
    }
    
    }
    else {
      gameOver(g);
    }
    
  }
  
  public void move() {

    for (int i = bodyParts; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];

    }
    switch (direction) {
      case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }
  }
  
  public void newBurrito() {
    burritoX = rand.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
    burritoY = rand.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
  }
  
  public void checkBurrito() {
    if (x[0] == burritoX && y[0] == burritoY) {
      bodyParts++;
      burritoEaten++;
      score++;
      newBurrito();
      
    }

  }
  
  public void checkCollisions() {
  
// check if head collides with body
   for (int i = bodyParts; i > 0; i--) {
     if ((x[0] == x[i]) && (y[0] == y[i])) {
       running = false;
     }

   }
   // check if head touches left border
   if (x[0] < 0) {
     running = false;
   }

   // check if head touches right border
   if (x[0] > SCREEN_WIDTH) {
     running = false;
   }
   // check if head touches top border
   if (y[0] < 0) {
     running = false;
   }
   // check if head touches bottom border
   if (y[0] > SCREEN_HEIGHT) {
     running = false;
   }
//   if (!running) {
//     timer.stop();
//   }
  }
  
  public void gameOver(Graphics g) {
    if (score >=2) {
      g.drawImage(askOut, 0, 0, null);
    }
    else {
      g.drawImage(lose, 0, 0, null);
    }
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    
    if (running) {
      move();
      checkBurrito();
      checkCollisions();
    }
    repaint();
  }
  
  public class MyKeyAdapter extends KeyAdapter {
    
    
    @Override
    public void keyPressed(KeyEvent e) {
      
//      if (e.getKeyChar() == 'r') {
//        System.out.println("r");
//        startGame();
//      }
//      
      if (e.getKeyChar() == 'y') {
        System.out.println("y");
        finalAnswer = true;
//        g2.drawImage(answer, 0, 0, null);
      }
      if (e.getKeyChar() == 'n') {
        System.out.println("n");
        finalAnswer = true;
//          g2.drawImage(answer, 0, 0, null);
      }
      
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          if (direction != 'R') {
            direction = 'L';
          }
          break;
        case KeyEvent.VK_RIGHT:
          if (direction != 'L') {
            direction = 'R';
          }
          break;
        case KeyEvent.VK_UP:
          if (direction != 'D') {
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN:
          if (direction != 'U') {
            direction = 'D';
          }
          break;
      }
      
      
  }
}
}
  
