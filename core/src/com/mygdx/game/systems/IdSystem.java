package com.mygdx.game.systems;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;

public class IdSystem {
	
	Array<Integer> IdList = new Array<Integer>(false, 100000);
	
	public void addId(int id) {
		this.IdList.add(id);
	}
	
	public void removeId(int id) {
		for (ArrayIterator<Integer> iter = IdList.iterator(); iter.hasNext(); ) {
			int x = iter.next();
			if (x == id) {
				iter.remove();
			}
		}
	}
}
