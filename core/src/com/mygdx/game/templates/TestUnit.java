package com.mygdx.game.templates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.systems.SysManager;

public class TestUnit {
	
	//this stuff below is like the blueprint of the unit
	//these values are only used to pass to the individuals components.
	static int hp = 50;
	static float moveSpeed = 5;
	static int damage = 10;
	static int armor = 1;
	static int sight = 9;
	static float atkSpeed = 1;
	static String[] type = new String[]{"unit", "biological", "light"};
	static Texture sprite = new Texture(Gdx.files.internal("unit.png")); // info and location for sprite in texture atlas
	static float width = 50;
	static float length = 50;

	public static void createUnit(SysManager sysManager, Vector2 p, Vector2 d, int player){
		int id = sysManager.addId();
		sysManager.addHp(id, hp);
		sysManager.addSelectable(id, true);
		sysManager.addMoveSpeed(id, moveSpeed);
		sysManager.addPosition(id, p);
		sysManager.addDestination(id, d);
		//sysManager.addBoundingBox(id, p.x, p.y, length, width);
		//sysManager.addSprite(sprite); //eventually, this would be the coordinates in atlas
		//sysManager.addArmor
		//sysManager.addDamage
		//sysManager.addOwner
	}
}