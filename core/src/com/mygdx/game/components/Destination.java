package com.mygdx.game.components;

import com.badlogic.gdx.math.Vector2;

public class Destination {
	int id;
	Vector2 destination;
	
	public Destination(int id, Vector2 destination) {
		//super(); do I need this?
		this.id = id;
		this.destination = destination;
	}
	
	public int getId() {
		return id;
	}

	public Vector2 getDestination() {
		return destination;
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
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
