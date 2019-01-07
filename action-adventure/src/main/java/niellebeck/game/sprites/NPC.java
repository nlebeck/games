package niellebeck.game.sprites;

import niellebeck.game.MyGameLogic;
import niellebeck.game.dialogues.EnemiesDefeatedDialogue;
import niellebeck.game.dialogues.InitialDialogue;
import niellebeck.game.scenes.BaseGameScene;
import niellebeck.gameengine.CollisionManager;
import niellebeck.gameengine.Dialogue;
import niellebeck.gameengine.DialogueManager;
import niellebeck.gameengine.GameLogic;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.Interactable;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class NPC extends Sprite implements Interactable {

	public NPC(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/initial_npc/initial_npc.png");
	}

	@Override
	public void update(KeyboardInput keyboard) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollideTilemap() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String getInteractionMessage() {
		return "talk with NPC";
	}
	
	public void interact(GameLogic gameLogic, GameScene gameScene) {
		MyGameLogic myGameLogic = (MyGameLogic)gameLogic;
		BaseGameScene baseGameScene = (BaseGameScene)gameScene;
		Dialogue dialogue = null;
		if (baseGameScene.allEnemiesDestroyed()) {
			dialogue = new EnemiesDefeatedDialogue();
		}
		else {
			dialogue = new InitialDialogue();
		}
		DialogueManager.getInstance().startDialogue(dialogue);
	}
}
