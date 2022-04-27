package com.mygdx.game.templates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BoundingBox;
import com.mygdx.game.components.Position;
import com.mygdx.game.systems.SysManager;

public class TestUnit {
	
	//this stuff below is like the blueprint of the unit
	//these values are only used to pass to the individuals components.
	static short hp = 50;
	static float moveSpeed = 5;
	static int damage = 10;
	static short armor = 1;
	static int sight = 9;
	static float atkSpeed = 1;
	static String[] type = new String[]{"unit", "biological", "light"};
	static Texture baseSprite = new Texture(Gdx.files.internal("unit.png")); // info and location for sprite in texture atlas
	static float width = 50;
	static float height = 50;

	public TestUnit(){
		//probably don't need this. I thought I was making a new test unit, but really this is
		//just a blueprint, and the unit is made by the components. nothing is saved here.
	}
	public static void createUnit(SysManager sysManager, Vector2 p, Vector2 d, int player){
		int id = sysManager.addId();
		sysManager.addHp(id, hp);
		sysManager.addSelectable(id, true);
		BoundingBox box = new BoundingBox(id, new Rectangle(p.x, p.y, width, height));
		Position p1 = sysManager.addPosition(id, p, d, moveSpeed, box, baseSprite,true);
		sysManager.addArmor(id, armor);
		//sysManager.addDamage
		//sysManager.addOwner
	}
}