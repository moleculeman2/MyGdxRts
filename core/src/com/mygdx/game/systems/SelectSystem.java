package com.mygdx.game.systems;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.Selectable;

public class SelectSystem {
	Array<Selectable> selectableList = new Array<Selectable>(false, 10000);
}
