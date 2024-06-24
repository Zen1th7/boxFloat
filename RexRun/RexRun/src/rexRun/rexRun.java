package rexRun;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class rexRun implements ActionListener, KeyListener 
{
	public static rexRun rexRun;
	
	public graphic render;
	public Rectangle rex;
	
	public final int WIDTH = 1000, HEIGHT = 700;
	public boolean gameOver,started;
	public int sec, yMove, score;
	
	public ArrayList<Rectangle> obss;
	
	public Random rand;
	
	public rexRun() {
		JFrame screen = new JFrame();
		Timer timer = new Timer(20, this);
		
		
		render = new graphic();
		rand = new Random();
		
		screen.add(render);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setSize(WIDTH, HEIGHT);
		screen.addKeyListener(this);
		screen.setResizable(false);
		screen.setVisible(true);
		
		rex = new Rectangle(WIDTH/2 -10, HEIGHT/2 - 10, 20 ,20);
		obss = new ArrayList<Rectangle>();
		
		addObs(true);
		addObs(true);
		addObs(true);
		addObs(true); 
		
		timer.start();
	}
	public void addObs(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50+rand.nextInt(300);
		
		if (start) {
			obss.add(new Rectangle(WIDTH + width + obss.size() * 300, HEIGHT - height - 150, width, height));
			obss.add(new Rectangle(WIDTH + width + (obss.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else {
			obss.add(new Rectangle(obss.get(obss.size() - 1).x + 600, HEIGHT - height - 150, width, height));
			obss.add(new Rectangle(obss.get(obss.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}
	
	
	public void obstacles(Graphics g, Rectangle obs) {
		g.setColor(Color.gray);
		g.fillRect(obs.x, obs.y, obs.width, obs.height);
	}
	
	public void jump() {
		if(gameOver) {
			rex = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20 ,20);
			obss.clear();
			yMove = 0;
			score = 0;
			
			addObs(true);
			addObs(true);
			addObs(true);
			addObs(true);
			
			gameOver = false;
		}
		if(!started) {
			started = true;
		}
		else if (!gameOver) {
			if (yMove>0) {
				yMove = 0;
			}
			yMove -= 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int speed = 10; 
		sec ++;
		
		if(started) {
			for(int i=0; i < obss.size(); i++) { //obstacle moves
				Rectangle obs = obss.get(i);
				obs.x -= speed;
			}
			
			if(sec % 2 == 0 && yMove < 15) { //Gravity
				yMove+=2;
			}
			
			for(int i=0; i < obss.size(); i++) { //infinite obstacle, so it will generate infinitely
				Rectangle obs = obss.get(i);
				if(obs.x + obs.width < 0 ) {
					obss.remove(obs);
					if (obs.y == 0) {
						addObs(false);
					}
				}
			}
			
			rex.y += yMove;
			
			for (Rectangle obs : obss) { //score point
				if (obs.y == 0 && rex.x + rex.width/2 > obs.x + obs.width/2 - 10 && rex.x + rex.width/2 <obs.x + obs.width/2 + 10 ) { //if the player go pass in th middle of the obstacle, the score increases by 1.
					score++;
				}
				
				if (obs.intersects(rex)) { 
					gameOver = true;
					
					rex.x = obs.x - rex.width; //so when the player die, the body got hold by the moving obstacles.
				}
			}
			if (rex.y > HEIGHT - 150 || rex.y < 0 ) { //you fall after game over
				gameOver = true;
			}
			if (rex.y + yMove >= HEIGHT - 140) { //stuck to floor after game over
				rex.y = HEIGHT - 150 - rex.height;
			}
		}
		render.repaint(); 
	}

	public void repaint(Graphics g) { 
		g.setColor(Color.cyan);
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 150, WIDTH, HEIGHT);
		
		g.setColor(Color.red);
		g.fillRect(rex.x, rex.y, rex.width, rex.height);
		
		for (Rectangle obs : obss) {
			obstacles(g, obs);
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial",1,100));
		
		if (!started) {
			g.drawString("Tap Spacebar!", 100, HEIGHT/2 );
		}
		if (gameOver) {
			g.drawString("Game Over !!!", 100, HEIGHT/2 );
		}
		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH/2 , 100);
		}
	}
	
	public static void main(String[] args) {
		rexRun = new rexRun();

	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
	}

}
