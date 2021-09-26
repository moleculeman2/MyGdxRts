package com.mygdx.game.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.*;

public class SysManager {
	public HpSystem hpSystem = new HpSystem();
	public IdSystem idSystem = new IdSystem();
	public MoveSystem moveSystem = new MoveSystem();
	public SelectSystem selectSystem = new SelectSystem();
	public Array<BoundingBox> list2 = new Array<BoundingBox>(false, 10000);
	int idCounter = 0;
	
	public SysManager() {
		
	}
	
	public int addId() {
		this.idCounter++;
		this.idSystem.addId(idCounter);
		return this.idCounter;
	}
	
	public void addHp(int id, int hp) {
		hpSystem.hpList.add(new Hp (id, (short)hp));
	}
	
	public void addSelectable(int id, boolean selectable) {
		selectSystem.selectableList.add(new Selectable(id, selectable));
	}
	
	public void addMoveSpeed(int id, float moveSpeed) {
		moveSystem.moveSpeedList.add(new MoveSpeed(id, moveSpeed));
	}

	public void addPosition(int id, Vector2 p) {
		moveSystem.positionList.add(new Position(id, p));	
	}
	
	public void addDestination(int id, Vector2 d) {
		moveSystem.destinationList.add(new Destination(id, d));	
	}
}
