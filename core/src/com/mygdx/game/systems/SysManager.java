package com.mygdx.game.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.*;

public class SysManager {
	public HpSystem hpSystem = new HpSystem();
	public IdSystem idSystem = new IdSystem();
	public MoveSystem moveSystem = new MoveSystem();
	public SelectSystem selectSystem = new SelectSystem();
	public Array<BoundingBox> boundingBoxList = new Array<BoundingBox>(false, 10000);
	public Array<Sprite> spriteList = new Array<Sprite>(false, 10000);
	int idCounter = 0;
	
	public SysManager() {
	}
	
	public int addId() {
		this.idCounter++;
		this.idSystem.addId(idCounter);
		return this.idCounter;
	}
	
	public void addHp(int id, short hp) {
		hpSystem.addHp(id, hp);
	}

	public void addArmor(int id, short armor) {
		hpSystem.addArmor(id, armor);
	}

	public void addSelectable(int id, boolean selectable) {
		selectSystem.selectableList.add(new Selectable(id, selectable));
	}
	
	public void addMoveSpeed(int id, float moveSpeed) {
		moveSystem.moveSpeedList.add(new MoveSpeed(id, moveSpeed));
	}

	public Position addPosition(int id, Vector2 p, boolean b) {
		if (b == true) {
			Position temp = new Position(id, p);
			moveSystem.positionList.add(temp);
			return temp;
		}
		else{
			moveSystem.positionList.add(new Position(id, p));
			return null;
		}

	}
	
	public void addDestination(int id, Vector2 d) {
		moveSystem.destinationList.add(new Destination(id, d));	
	}

	public void addBoundingBox(int id, float x, float y, float width, float height) {
		boundingBoxList.add(new BoundingBox(id, new Rectangle(x,y,width,height)));
	}

	public void addSprite(int id, Texture newSprite) {
		spriteList.add(new Sprite(id, newSprite));
	}


}
