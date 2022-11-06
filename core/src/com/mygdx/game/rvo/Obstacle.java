package com.mygdx.game.rvo;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/** Defines static obstacles in the simulation. */
class Obstacle {
    Obstacle next = null;
    Obstacle previous = null;
    Vector2D direction = Vector2D.ZERO;
    Vector2D point = Vector2D.ZERO;
    int id = 0;
    boolean convex = false;
}
