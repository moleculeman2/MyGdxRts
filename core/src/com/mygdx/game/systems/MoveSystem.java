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

	/**
	 *  this is for changing base movespeed values
	 */
	public void updateMoveSpeed() {
		for (ArrayIterator<Position> iter = positionList.iterator(); iter.hasNext(); ) {
			Position p = iter.next();
			p.getMoveSpeed();
			//will need methods both for scaling percentage movespeed and setting to flat amounts
		}
	}

	/** this is for teleporting units to a new point
	 * @param id ID of the unit
	 * @param d Destination vector
	 */
	public void updateDestination(int id, Vector2 d) {
		for (ArrayIterator<Position> iter = positionList.iterator(); iter.hasNext(); ) {
			Position p = iter.next();
			if (p.getId() == id) {
				p.setDestination(d);
			}
		}
	}

	/** this is for moving a unit by walking/flying
	 * @param delta
	 */
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
