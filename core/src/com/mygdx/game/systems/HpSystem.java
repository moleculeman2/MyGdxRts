package com.mygdx.game.systems;




import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.mygdx.game.components.*;

public class HpSystem {
	
	Array<Hp> hpList = new Array<Hp>(false, 10000);
	Array<Armor> armorList = new Array<Armor>(false, 10000);

	public void addHp(int id, short hp){
		hpList.add(new Hp (id, hp));
	}

	public void addArmor(int id, short armor){
		armorList.add(new Armor (id, armor));
	}
	//Hp[] hpList = array.begin();
	//List<Hp> hpList = new ArrayList<Hp>();
	
	//method for adding new health components when new units are made
	
	//method that accepts an ID number and a damage/heal value, then loops through and applies the health change
	public void hpChange(int id, Hp hp, int amount) {
		for (ArrayIterator<Hp> iter = hpList.iterator(); iter.hasNext(); ) {
			Hp x = iter.next();
			if (x.getId() == id) {
				x.modifyHp((short) amount);
				if (x.getHp() < 0) {
					iter.remove();
				}
			}
		}
	}
}
