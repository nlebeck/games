package niellebeck.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import java.util.Set;

enum SpriteType {
	BASE_SPRITE,
	ROCK,
	PLAYER_CHARACTER,
	BULLET
}

public class Sprite {
	protected int posX;
	protected int posY;
	protected int speed;
	protected SpriteType type;
	protected boolean destroyed;
	protected int width;
	protected int height;
	protected Image img;
		
	public Sprite(int initX, int initY, int initWidth, int initHeight, String imagePath) {
		this(initX, initY, initWidth, initHeight);
		img = ResourceLoader.loadImage(imagePath);
	}
	
	protected Sprite(int initX, int initY, int initWidth, int initHeight) {
		posX = initX;
		posY = initY;
		width = initWidth;
		height = initHeight;
		speed = 0;
		type = SpriteType.BASE_SPRITE;
		destroyed = false;
		img = null;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	public SpriteType getType() {
		return type;
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void draw(Graphics g) {
		if (img != null) {
			g.drawImage(img, posX, posY, width, height, null);
		}
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX - (width / 2), posY - (height / 2), width, height);
	}
	
	public void move(Direction dir, List<Sprite> objects) {
		int lastPosX = posX;
		int lastPosY = posY;
		
		tempMove(dir);
		
		Set<Sprite> collisionSet = CollisionHandler.getCollisionSet(this, objects);
		if (collisionSet.size() > 0) {
			posX = lastPosX;
			posY = lastPosY;
			
			List<Direction> componentDirs = DirectionUtils.getComponentDirections(dir);
			for (Direction componentDir : componentDirs) {
				tempMove(componentDir);
				Set<Sprite> tempCollisionSet = CollisionHandler.getCollisionSet(this, objects);
				collisionSet.addAll(tempCollisionSet);
				if (tempCollisionSet.size() > 0) {
					posX = lastPosX;
					posY = lastPosY;
				}
				else {
					break;
				}
			}
		}
		
		for (Sprite collidedObject : collisionSet) {
			CollisionHandler.handleCollision(this, collidedObject);
		}
	}
		
	public void tempMove(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.UP_LEFT
				|| dir == Direction.DOWN_LEFT) {
			posX -= speed;
		}
		if (dir == Direction.RIGHT || dir == Direction.UP_RIGHT
				|| dir == Direction.DOWN_RIGHT) {
			posX += speed;
		}
		if (dir == Direction.UP || dir == Direction.UP_LEFT
				|| dir == Direction.UP_RIGHT) {
			posY -= speed;
		}
		if (dir == Direction.DOWN || dir == Direction.DOWN_LEFT
				|| dir == Direction.DOWN_RIGHT) {
			posY += speed;
		}
	}

	public void update(List<Sprite> objects) {}
}
