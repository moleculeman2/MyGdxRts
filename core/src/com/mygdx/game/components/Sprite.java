package com.mygdx.game.components;

import com.badlogic.gdx.graphics.Texture;

public class Sprite {

	//when I make a new sprite, I pass it a texture.

    int id;
    Texture currentSprite;

    public Sprite(int id, Texture newSprite){
        this.id = id;
        this.currentSprite = newSprite;
    }

    public void setCurrentSprite(Texture newSprite){
        this.currentSprite = newSprite;
    }
	
}
