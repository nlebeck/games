package niellebeck.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private static final int BULLET_COOLDOWN = 10; //in frames
	
	PlayerCharacter playerChar;
	List<Sprite> sprites;
	Direction lastPlayerDir;
	int timeUntilNextBullet;
	
	//graphics state
	Tilemap tilemap;
	
	public Game() {
		playerChar = new PlayerCharacter(300, 220);
		sprites = new ArrayList<Sprite>();
		sprites.add(new Enemy(400, 400));
		sprites.add(new Enemy(320, 100));
		sprites.add(playerChar);
		lastPlayerDir = Direction.RIGHT;
		timeUntilNextBullet = BULLET_COOLDOWN;
		
		tilemap = new Tilemap("/tilemap.txt");
	}
	
	public void draw(Graphics bufferGraphics) {
		//clear buffer
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		
		int cameraX = playerChar.getX() - (GamePanel.PANEL_WIDTH / 2) + (playerChar.getWidth() / 2);
		int cameraY = playerChar.getY() - (GamePanel.PANEL_HEIGHT / 2) + (playerChar.getHeight() / 2);
		
		//draw background
		tilemap.draw(bufferGraphics, cameraX, cameraY);
	
		//render game
		playerChar.draw(bufferGraphics, cameraX, cameraY);
		for (Sprite s : sprites) {
			s.draw(bufferGraphics, cameraX, cameraY);
		}
	}
	
	public GameState update(KeyboardInput keyboard) {
		GameState nextState = GameState.PLAYING;
		if (keyboard.keyPressed(KeyEvent.VK_TAB)) {
			nextState = GameState.MENU;
		}
		else {
			Direction dir = keyboard.getArrowKeyDirection();
			if (keyboard.keyIsDown(KeyEvent.VK_A) && timeUntilNextBullet <= 0) {
				shootBullet(dir);
			}
			for (Sprite sprite : sprites) {
				sprite.update(keyboard, sprites, tilemap);
			}
			List<Sprite> newSpriteList = new ArrayList<Sprite>();
			for (Sprite sprite : sprites) {
				if (!sprite.isDestroyed()) {
					newSpriteList.add(sprite);
				}
			}
			if (playerChar.isDestroyed()) {
				nextState = GameState.GAME_OVER;
			}
			sprites = newSpriteList;
			if (dir != Direction.NONE) {
				lastPlayerDir = dir;
			}
			if (timeUntilNextBullet > 0) {
				timeUntilNextBullet--;
			}
		}
		return nextState;
	}
	
	
	public void shootBullet(Direction dir) {
		int offsetX = 0;
		int offsetY = 0;
		Direction bulletDir = dir;
		if (bulletDir == Direction.NONE) {
			bulletDir = lastPlayerDir;
		}
		bulletDir = DirectionUtils.getComponentDirections(bulletDir).get(0);
		if (bulletDir == Direction.LEFT) {
			offsetX = -(playerChar.getWidth() / 2) - Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.RIGHT) {
			offsetX = (playerChar.getWidth() / 2) + Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.UP) {
			offsetY = -(playerChar.getHeight() / 2) - Bullet.BULLET_HEIGHT;
		}
		else if (bulletDir == Direction.DOWN) {
			offsetY = (playerChar.getHeight() / 2) + Bullet.BULLET_HEIGHT;
		}
		Bullet bullet = new Bullet(playerChar.getX() + offsetX, playerChar.getY() + offsetY, bulletDir);
		sprites.add(bullet);
		timeUntilNextBullet = BULLET_COOLDOWN;
	}
}
