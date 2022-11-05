package com.mygdx.game.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BoundingBox;
import com.mygdx.game.components.MoveSpeed;
import com.mygdx.game.components.Position;
import com.mygdx.game.components.Selectable;

public class SysManager {
	public HpSystem hpSystem = new HpSystem();
	public IdSystem idSystem = new IdSystem();
	public MoveSystem moveSystem = new MoveSystem();
	public SelectSystem selectSystem = new SelectSystem();
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

	public Position addPosition(int id, Vector2 p, Vector2 d, float ms, BoundingBox box, Texture sprite, boolean b, int player) {

		if (b) {
			Position temp = new Position(id, p, d, ms, box, sprite, player);
			moveSystem.positionList.add(temp);
			return temp;
		}
		else{
			moveSystem.positionList.add(new Position(id, p, d, ms, box, sprite, player));
			return null;
		}

	}

}
