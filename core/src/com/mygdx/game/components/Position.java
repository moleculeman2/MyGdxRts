package com.mygdx.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

public class Position {

	int id;
	Vector2 position;
	Vector2 destination;
	BoundingBox box;
	Texture sprite;
	float moveSpeed;
	Queue<Vector2> moveQueue;
	boolean pathTest;

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	int player;

	
	public Position(int id, Vector2 position, Vector2 destination, float moveSpeed, BoundingBox box, Texture sprite, int player, boolean pathTest) {
		//super(); do I need this?
		this.id = id;
		this.position = position;
		this.destination = destination;
		this.moveSpeed = moveSpeed;
		this.box = box;
		this.sprite = sprite;
		this.player = player;
		this.moveQueue = new Queue<Vector2>();
		this.pathTest = pathTest;
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

	/**
	 * @param delta system param for time passed since last frame
	 */
	public void modifyPosition(float delta) {
		Vector2 mod = new Vector2();
		mod.set(destination);
		mod.sub(position).nor();
		if (position.dst2(destination) > moveSpeed){
			this.position.mulAdd(mod, (delta*moveSpeed*75));
		}else{
			if (moveQueue.notEmpty()){
				this.destination = moveQueue.removeFirst();
			}
			else{
				this.destination = null;
			}

		}
		updateComponents();
	}

	private void updateComponents(){
		this.box.setCenter(position);
		//float x = box.getBoundingBox().getWidth() / 2;
		//float y = box.getBoundingBox().getHeight() / 2;
		//this.box.getBoundingBox().setCenter( position.x - x, position.y - y);
		//box.getBoundingBox().setPosition(position);
	}

	public BoundingBox getBox() {
		return box;
	}

	public void setBox(BoundingBox box) {
		this.box = box;
	}

	public Texture getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = sprite;
	}

	public Vector2 getDestination() {
		return destination;
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public Queue<Vector2> getMoveQueue() {
		return moveQueue;
	}

	public void setMoveQueue(Queue<Vector2> moveQueue) {
		this.moveQueue = moveQueue;
	}
}
