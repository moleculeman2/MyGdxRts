package com.mygdx.game.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.mygdx.game.components.*;

public class MoveSystem {

	Array<MoveSpeed> moveSpeedList = new Array<MoveSpeed>(false, 10000);
	Array<Position> positionList = new Array<Position>(false, 10000);
	// an array with a list of coordinates in the texture atlas, for each object w/ a sprite?
	//      or, an array that stores id and texture object?
	
	public void updateMoveSpeed() {
		for (ArrayIterator<Position> iter = positionList.iterator(); iter.hasNext(); ) {
			Position p = iter.next();
			p.getMoveSpeed();
			//will need methods both for scaling percentage movespeed and setting to flat amounts
		}
	}
	
	public void updateDestination(int id, Vector2 d) {
		for (ArrayIterator<Position> iter = positionList.iterator(); iter.hasNext(); ) {
			Position p = iter.next();
			if (p.getId() == id) {
				p.setDestination(d);
			}
		}
	}

	public void updatePosition(float delta) {
		for (ArrayIterator<Position> iter = positionList.iterator(); iter.hasNext(); ) {
			Position p = iter.next();
			if (p.getDestination() != null && p.getMoveSpeed() > 0){
				p.modifyPosition(delta);
			}
		}
	}

	public Array<Position> getPositionList() {
		return positionList;
	}
}
