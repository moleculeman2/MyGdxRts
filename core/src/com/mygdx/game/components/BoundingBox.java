package com.mygdx.game.components;

import com.badlogic.gdx.math.Rectangle;

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
	
	public void setCenter(Position p) {
		this.boundingBox.setCenter(p.getPosition().x, p.getPosition().y);
	}
}
