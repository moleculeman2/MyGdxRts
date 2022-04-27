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

	public int getId() {
		return id;
	}
	
	public void setCenter(Vector2 p) {
		this.boundingBox.setCenter(p.x, p.y);
	}
}
