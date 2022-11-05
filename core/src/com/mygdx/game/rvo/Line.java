
package com.mygdx.game.rvo;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Line {
    public Vector2D direction;
    public Vector2D point;

    public Line() {
        this.direction = Vector2D.ZERO;
        this.point = Vector2D.ZERO;
    }

    public Line(Vector2D point, Vector2D direction) {
        this.direction = Vector2D.ZERO;
        this.point = Vector2D.ZERO;
        this.direction = direction;
        this.point = point;
    }
}
