package com.mygdx.game.components;


public class Owner {
	int id;
	short owner;
	
	public Owner(int id, short owner) {
		//super(); do I need this?
		this.id = id;
		this.owner = owner;
	}
	
	public int getId() {
		return id;
	}

	public short getOwner() {
		return owner;
	}

	public void setOwner(short owner) {
		this.owner = owner;
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
