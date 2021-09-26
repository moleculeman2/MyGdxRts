package com.mygdx.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.mygdx.game.components.Destination;
import com.mygdx.game.components.MoveSpeed;
import com.mygdx.game.components.Position;
import com.mygdx.game.components.BoundingBox;

public class MoveSystem {

	Array<MoveSpeed> moveSpeedList = new Array<MoveSpeed>(false, 10000);
	Array<Destination> destinationList = new Array<Destination>(false, 10000);
	Array<Position> positionList = new Array<Position>(false, 10000);
	Array<BoundingBox> boundingBoxList = new Array<BoundingBox>(false, 10000);
	// an array with a list of coordinates in the texture atlas, for each object w/ a sprite?
	//      or, an array that stores id and texture object?
	
	public void updateMoveSpeed() {
		for (ArrayIterator<MoveSpeed> iter = moveSpeedList.iterator(); iter.hasNext(); ) {
			
		}
	}
	
	public void updateDestination(int id, Vector2 d) {
		for (ArrayIterator<Destination> iter = destinationList.iterator(); iter.hasNext(); ) {
			Destination x = iter.next();
			if (x.getId() == id) {
				x.setDestination(d);
			}
		}
	}
	
	// loop through every position component, get ID
	// loop through every destination, check for ID match
	// if match, loop through movespeed for match, then move it.
	// also update bounding box
	public void updatePosition() {
		for (ArrayIterator<Position> iter = positionList.iterator(); iter.hasNext(); ) {
			Position p = iter.next();
			int id = p.getId();
			for (ArrayIterator<Destination> iter2 = destinationList.iterator(); iter.hasNext(); ) {
				Destination d = iter2.next();
				if (d.getId() == id) {
					for (ArrayIterator<MoveSpeed> iter3 = moveSpeedList.iterator(); iter.hasNext(); ) {
						MoveSpeed m = iter3.next();
						if  (m.getId() == id) {
							p.modifyPosition(d.getDestination()); //still need movespeed scalar
							for (ArrayIterator<BoundingBox> iter4 = boundingBoxList.iterator(); iter.hasNext(); ) {
								BoundingBox b = iter4.next();
								if (b.getId() == id) {
									b.setCenter(p);
								}
							}
						}
					}
				}
			}
			
		}
	}
	
}
