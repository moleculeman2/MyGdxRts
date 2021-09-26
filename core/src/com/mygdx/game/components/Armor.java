package com.mygdx.game.components;


public class Armor {
	int id;
	short armor;
	
	public Armor(int id, short armor) {
		//super(); do I need this?
		this.id = id;
		this.armor = armor;
	}
	
	public int getId() {
		return id;
	}

	public short getArmor() {
		return armor;
	}

	public void setArmor(short armor) {
		this.armor = armor;
	}
	
	public void modifyArmor(short armor) {
		this.armor += armor;
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
