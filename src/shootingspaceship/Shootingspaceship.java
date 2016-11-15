package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th;
    private Player player;
    private ShotType[][] shots;
    private ArrayList enemies;
    private final int shotSpeed = -5;
    private final int playerLeftSpeed = -3;
    private final int playerRightSpeed = 3;
    private final int playerUpSpeed = -3;
    private final int playerDownSpeed = 3;
    private final int width = 500;
    private final int height = 500;
    private final int playerMargin = 10;
    private final int enemyMaxDownSpeed = 1;
    private final int enemyMaxHorizonSpeed = 1;
    private final int enemyTimeGap = 500; //unit: msec
    private final float enemyDownSpeedInc = 0.3f;
    private final int maxEnemySize = 1000;
    private int enemySize;
    private boolean pause = true;
    private javax.swing.Timer timer;
    private boolean playerMoveLeft;
    private boolean playerMoveRight;
    private boolean playerMoveUp;
    private boolean playerMoveDown;
    private boolean playerFire;
    
    private Image dbImage;
    private Graphics dbg;
    private Random rand;
    private int countFire;
    private int maxFirstShotNum = 200;
    private int maxSecondShotNum = 100;
    private int maxThirdShotNum = 50;
    private int maxFourthShotNum = 25;
    private final int shotType = 4;
    private int currentShot;
    
    public enum GameStatus {
    	Menu, Gamming, Option, Ending
    }
    private GameStatus gStatus;
    
    
    public Shootingspaceship() {
    	gStatus = GameStatus.Menu;
    	
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width - playerMargin, playerMargin, height - playerMargin);
        shots = new ShotType[4][];
        shots[0] = new ShotType[maxFirstShotNum];
        shots[1] = new ShotType[maxSecondShotNum];
        shots[2] = new ShotType[maxThirdShotNum];
        shots[3] = new ShotType[maxFourthShotNum];
        currentShot = 0;
        enemies = new ArrayList();
        enemySize = 0;

        rand = new Random(1);
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy());
        timer.start();

        addKeyListener(new ShipControl());
        addMouseListener(new ShipControl());
        setFocusable(true);
        countFire = 0;
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }

    private class addANewEnemy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (++enemySize <= maxEnemySize) {
                float downspeed;
                do {
                    downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                } while (downspeed == 0);

                float horspeed = rand.nextFloat() * 2 * enemyMaxHorizonSpeed - enemyMaxHorizonSpeed;
                //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);

                Enemy newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width, height, enemyDownSpeedInc);
                enemies.add(newEnemy);
            } else {
                timer.stop();
            }
        }
    }

    private class ShipControl implements KeyListener,MouseListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    playerMoveLeft = true;
                    break;
                case KeyEvent.VK_D:
                    playerMoveRight = true;
                    if(selectedMenu==0) gStatus = GameStatus.Gamming;
                    else if(selectedMenu==1) gStatus = GameStatus.Option;
                    else gStatus = GameStatus.Ending;
                    break;
                case KeyEvent.VK_W:
                    --selectedMenu;
                    if(selectedMenu<0) selectedMenu = 2;
                    playerMoveUp = true;
                    break;
                case KeyEvent.VK_S:
                    ++selectedMenu;
                    if(selectedMenu>2) selectedMenu = 0;
                    playerMoveDown = true;
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    playerMoveLeft = false;
                    break;
                case KeyEvent.VK_D:
                    playerMoveRight = false;
                    break;
                case KeyEvent.VK_W:
                    playerMoveUp = false;
                    break;
                case KeyEvent.VK_S:
                    playerMoveDown = false;
                    break;
                case KeyEvent.VK_P:
                    if(pause == true)
                    {
                       th.suspend();
                       pause = false;
                    }
                    else
                    {
                        th.resume();
                        pause = true;
                    }
                    break;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
        public void mousePressed(MouseEvent e)
        {
            switch(e.getButton()) {
                case MouseEvent.BUTTON1:
                    playerFire = true;
                    break;
            }
        }
        public void mouseReleased(MouseEvent e)
        {
            switch(e.getButton()) {
                case MouseEvent.BUTTON1:
                    playerFire = false;
                    countFire = 0;
                    break;
                case MouseEvent.BUTTON3:
                    currentShot = ++currentShot % shotType;
                    break;
            }            
        }
        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
    }

    
    
    public void run() {
        //int c=0;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        
        while (true) {
        	
        	switch (gStatus) {
			case Menu:
				Menu();
				
				break;
			case Gamming:
				Gamming();
				
				break;
                        case Option:
                                Option();
                                
                                break;
			case Ending:
				Ending();
				
				break;

			default:
				break;
			}
        	
         
        	repaint();

        	try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);    	

        }
    }

    private void Menu()
    {
    	
    }
    
    private void Gamming()
    {
    	//System.out.println( ++c );
        // do operations on shots in shots array
        for(int j = 0; j < shotType; ++j) {
            for (int i = 0; i < shots[j].length; i++) {
                if (shots[j][i] != null) {
                    // move shot
                    shots[j][i].moveShot(shotSpeed);

                    // test if shot is out
                    if (shots[j][i].getY() < 0) {
                        // remove shot from array
                        shots[j][i] = null;
                    }
                }
            }
        }
        
        if (playerMoveLeft) {
            player.moveX(playerLeftSpeed);
        }
        if (playerMoveRight) {
            player.moveX(playerRightSpeed);
        }
        if (playerMoveUp) {
            player.moveY(playerUpSpeed);
        }
        if (playerMoveDown) {
            player.moveY(playerDownSpeed);
        }

        if(playerFire) {
            // generate new shot and add it to shots array
            if(countFire == 10) {
                for (int i = 0; i < shots[currentShot].length; i++) {
                    if (shots[currentShot][i] == null) {
                        shots[currentShot][i] = player.generateShot(currentShot + 1);
                        break;
                    }
                }
                countFire = 0;
            }
            else {
                ++countFire;
            }
        }
        
        Iterator enemyList = enemies.iterator();
        while (enemyList.hasNext()) {
            Enemy enemy = (Enemy) enemyList.next();
            enemy.move();
        }

    }
    private void Option(){
        
    }
    
    private void Ending()
    {
    	System.exit(0);
    }
    
    public void initImage(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
       
        dbg.setColor(getForeground());
        //paint (dbg);
       
        g.drawImage(dbImage,0,0, this);
      //   g.drawImage(img,0,0,this.getSize().width,this.getSize().height, this);
    }
    
    
    int selectedMenu = 0;
    
    public void paintComponent(Graphics g) {
    	
    	initImage(g);
    	
        String[] menu={"Menu","Start","Option","End"};
    	switch (gStatus) {
		case Menu:
			g.setColor(Color.WHITE);
			
			g.setFont(new Font("돋움", 0, 70));
			g.drawString(menu[0], width-(menu[0].length()*85), 100);
			
			if(selectedMenu == 0)
				g.setColor(Color.BLUE);
			else 
				g.setColor(Color.WHITE);
			
			g.setFont(new Font("돋움", 0, 50));
			g.drawString(menu[1], width-(menu[0].length()*80), 150);

			if(selectedMenu == 1)
				g.setColor(Color.BLUE);
			else 
				g.setColor(Color.WHITE);
			
			g.setFont(new Font("돋움", 0, 50));
			g.drawString(menu[2], width-(menu[0].length()*85), 250);                        
                        
                        
                        
			if(selectedMenu == 2)
				g.setColor(Color.BLUE);
			else 
				g.setColor(Color.WHITE);

			g.setFont(new Font("돋움", 0, 50));
			g.drawString(menu[3], width-(menu[0].length()*75), 350);
			
			break;

		case Gamming:
			
			// draw player
	        player.drawPlayer(g);
	       

	        Iterator enemyList = enemies.iterator();
	        while (enemyList.hasNext()) {
	            Enemy enemy = (Enemy) enemyList.next();
	            enemy.draw(g);
	            if (enemy.isCollidedWithShot(shots)) {
	                if(enemy.getHealth() < 0) {
	                    enemyList.remove();
	                    --enemySize;
	                }
	            }
	            if (enemy.isCollidedWithPlayer(player)) {
	                enemyList.remove();
	                System.exit(0);
	            }
	        }

	        // draw shots
	        for(int j = 0; j < shotType; ++j) {
	            for (int i = 0; i < shots[j].length; i++) {
	                if (shots[j][i] != null) {
	                    shots[j][i].drawShot(g, j);
	                }
	            }
	        }
			break;
			
		case Ending:
			
			break;
			
		default:
			break;
		}

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame frame = new JFrame("Shooting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Shootingspaceship ship = new Shootingspaceship();
        frame.getContentPane().add(ship);
        frame.pack();
        frame.setVisible(true);
        ship.start();
    }
}
