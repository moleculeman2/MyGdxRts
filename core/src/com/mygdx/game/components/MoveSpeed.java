package com.mygdx.game.components;

public class MoveSpeed {
	int id;
	float moveSpeed;
	
	public MoveSpeed(int id, float moveSpeed) {
		//super(); do I need this?
		this.id = id;
		this.moveSpeed = moveSpeed;
	}
	
	public int getId() {
		return id;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public void modifyMoveSpeed(float moveSpeed) {
		this.moveSpeed += moveSpeed;
	}
	
	public void finalize(){
		try {
			//stuff
		} 
		catch(Exception e) {
			//stuff
		}
	}
	
}
