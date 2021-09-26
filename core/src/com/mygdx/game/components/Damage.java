package com.mygdx.game.components;


public class Damage {
	int id;
	short damage;
	
	public Damage(int id, short damage) {
		//super(); do I need this?
		this.id = id;
		this.damage = damage;
	}
	
	public int getId() {
		return id;
	}

	public short getDamage() {
		return damage;
	}

	public void setDamage(short damage) {
		this.damage = damage;
	}
	
	public void modifyDamage(short damage) {
		this.damage += damage;
		if (this.damage <= 0) this.damage = 1;
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
