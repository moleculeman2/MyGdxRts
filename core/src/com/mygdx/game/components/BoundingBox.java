package com.mygdx.game.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundingBox {
	int id;
	Rectangle boundingBox;
	
	public BoundingBox(int id, Rectangle boundingBox) {
		this.id = id;
		this.boundingBox = boundingBox;
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Vector2 getCenter() {
		Vector2 center = new Vector2(boundingBox.getX() + (boundingBox.getWidth()/2), boundingBox.getY() + (boundingBox.getHeight()/2));
		return center;
	}

	public int getId() {
		return id;
	}
	
	public void setCenter(Vector2 p) {
		this.boundingBox.setCenter(p.x, p.y);
	}
}
