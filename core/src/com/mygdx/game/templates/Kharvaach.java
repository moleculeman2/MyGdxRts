package com.mygdx.game.templates;

import com.badlogic.gdx.graphics.Texture;

public class Kharvaach {
	int hp;
	float moveSpeed;
	int damage;
	int armor;
	int sight;
	float atkSpeed;
	int owner;
	String[] type;
	Texture sprite; // info/location for sprite in texture atlas
	
	
	public Kharvaach() {
		this.hp = 50;
		this.moveSpeed = 5;
		this.damage = 10;
		this.armor = 1;
		this.sight = 9;
		this.atkSpeed = 1;
		this.type =  new String[]{"biological", "light"}; //create enums for unit types
		this.sprite = new Texture("");
	}

	public Texture getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = sprite;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getSight() {
		return sight;
	}

	public void setSight(int sight) {
		this.sight = sight;
	}

	public float getAtkSpeed() {
		return atkSpeed;
	}

	public void setAtkSpeed(float atkSpeed) {
		this.atkSpeed = atkSpeed;
	}

	public String[] getType() {
		return type;
	}

	public void setType(String[] type) {
		this.type = type;
	}	

}
