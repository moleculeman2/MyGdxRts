package com.mygdx.game.components;

public class AtkSpeed {
	int id;
	float atkSpeed;
	
	public AtkSpeed(int id, float atkSpeed) {
		//super(); do I need this?
		this.id = id;
		this.atkSpeed = atkSpeed;
	}
	
	public int getId() {
		return id;
	}

	public float getAtkSpeed() {
		return atkSpeed;
	}

	public void setAtkSpeed(float atkSpeed) {
		this.atkSpeed = atkSpeed;
	}
	
	public void modifyAtkSpeed(float atkSpeed) {
		this.atkSpeed += atkSpeed;
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
