package com.mygdx.game.components;

import com.badlogic.gdx.utils.Queue;

import java.awt.*;

public class MoveQueue {
	int id;
	Queue<Point> moveQueue;
	
	public MoveQueue(int id, Queue<Point> moveQueue) {
		//super(); do I need this?
		this.id = id;
		this.moveQueue = moveQueue;
	}

	public Queue<Point> getMoveQueue() {
		return moveQueue;
	}

	public Point popQueue(){
		Point first = moveQueue.first();
		moveQueue.removeFirst();
		return first;
	}

	public void setMoveQueue(Queue<Point> moveQueue) {
		this.moveQueue = moveQueue;
	}

	public int getId() {
		return id;
	}
}
