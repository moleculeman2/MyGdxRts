package com.mygdx.game.components;

public class Selectable {
	int id;
	boolean selectable;
	
	public Selectable(int id, boolean selectable) {
		this.id = id;
		this.selectable = selectable;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public int getId() {
		return id;
	}
}
