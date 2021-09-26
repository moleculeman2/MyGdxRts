package com.mygdx.game.components;


public class Hp {
	int id;
	short hp;
	
	public Hp(int id, short hp) {
		//super(); do I need this?
		this.id = id;
		this.hp = hp;
	}
	
	public int getId() {
		return id;
	}

	public short getHp() {
		return hp;
	}

	public void setHp(short hp) {
		this.hp = hp;
	}
	
	public void modifyHp(short hp) {
		this.hp += hp;
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
