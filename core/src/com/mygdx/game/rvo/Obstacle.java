package com.mygdx.game.rvo;


import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

class Obstacle {
    Obstacle next = null;
    Obstacle previous = null;
    Vector2D direction;
    Vector2D point;
    int id;
    boolean convex;

    Obstacle() {
        this.direction = Vector2D.ZERO;
        this.point = Vector2D.ZERO;
        this.id = 0;
        this.convex = false;
    }
}
