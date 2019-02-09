package niellebeck.gameengine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Sprite {
	protected int posX; //x- and y-coordinates of the center of the sprite
	protected int posY;
	protected boolean destroyed;
	protected int width;
	protected int height;
	protected Image img;
	
	private InteractionHandler interactionHandler;
		
	public Sprite(int initX, int initY, int initWidth, int initHeight, String imagePath) {
		this(initX, initY, initWidth, initHeight);
		img = ResourceLoader.loadImage(imagePath);
	}
	
	protected Sprite(int initX, int initY, int initWidth, int initHeight) {
		posX = initX;
		posY = initY;
		width = initWidth;
		height = initHeight;
		destroyed = false;
		img = null;
		interactionHandler = null;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		destroyed = true;
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
	
	public boolean isInteractable() {
		return (interactionHandler != null);
	}
	
	public InteractionHandler getInteractionHandler() {
		return interactionHandler;
	}
	
	public void setInteractionHandler(InteractionHandler handler) {
		interactionHandler = handler;
	}
	
	public void draw(Graphics g, int cameraX, int cameraY) {
		if (img != null) {
			g.drawImage(img, posX - (width / 2) - cameraX, posY - (height / 2) - cameraY, width, height, null);
		}
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX - (width / 2), posY - (height / 2), width, height);
	}
	
	public void move(Direction dir, int distance) {
		int lastPosX = posX;
		int lastPosY = posY;
		
		tempMove(dir, distance);
		
		EventManager collisionManager = GameEngine.getGameEngine().getEventManager();
		boolean collision = collisionManager.testAndAddCollisions(this);
		if (collision) {
			posX = lastPosX;
			posY = lastPosY;
			
			List<Direction> componentDirs = DirectionUtils.getComponentDirections(dir);
			for (Direction componentDir : componentDirs) {
				tempMove(componentDir, distance);
				collision = collisionManager.testAndAddCollisions(this);
				if (collision) {
					posX = lastPosX;
					posY = lastPosY;
				}
				else {
					break;
				}
			}
		}
	}
	
	public Set<Sprite> getCollisionSet(List<Sprite> spriteList) {
		Set<Sprite> collisionSet = new HashSet<Sprite>();
		Rectangle spriteBoundingBox = this.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox()) && otherSprite != this) {
				collisionSet.add(otherSprite);
			}
		}
		return collisionSet;
	}
		
	public void tempMove(Direction dir, int distance) {
		if (dir == Direction.LEFT || dir == Direction.UP_LEFT
				|| dir == Direction.DOWN_LEFT) {
			posX -= distance;
		}
		if (dir == Direction.RIGHT || dir == Direction.UP_RIGHT
				|| dir == Direction.DOWN_RIGHT) {
			posX += distance;
		}
		if (dir == Direction.UP || dir == Direction.UP_LEFT
				|| dir == Direction.UP_RIGHT) {
			posY -= distance;
		}
		if (dir == Direction.DOWN || dir == Direction.DOWN_LEFT
				|| dir == Direction.DOWN_RIGHT) {
			posY += distance;
		}
	}

	public abstract void update(KeyboardInput keyboard);
	
	public abstract void onCollideTilemap();
}
