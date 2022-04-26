package com.mygdx.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

public class Position {
	int id;
	Vector2 position;
	BoundingBox box;
	Sprite sprite;
	Vector2 destination;
	Queue<Vector2> moveQueue;

	
	public Position(int id, Vector2 position) {
		//super(); do I need this?
		this.id = id;
		this.position = position;

	}
	
	public int getId() {
		return id;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void modifyPosition(Vector2 mod, float delta) {
		this.position.mulAdd(mod, delta); //Gdx.graphics.getDeltaTime());
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
