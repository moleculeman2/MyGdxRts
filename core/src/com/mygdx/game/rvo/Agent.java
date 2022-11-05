
package com.mygdx.game.rvo;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Agent {
    final List<Pair<Double, Agent>> agentNeighbors = new ArrayList();
    final List<Pair<Double, Obstacle>> obstacleNeighbors = new ArrayList();
    final List<Line> lines = new ArrayList();
    Vector2D position;
    Vector2D preferredVelocity;
    Vector2D velocity;
    int id;
    int maxNeighbors;
    double maxSpeed;
    double neighborDistance;
    double radius;
    double timeHorizonAgents;
    double timeHorizonObstacles;
    private Vector2D newVelocity;

    Agent() {
        this.position = Vector2D.ZERO;
        this.preferredVelocity = Vector2D.ZERO;
        this.velocity = Vector2D.ZERO;
        this.id = 0;
        this.maxNeighbors = 0;
        this.maxSpeed = 0.0;
        this.neighborDistance = 0.0;
        this.radius = 0.0;
        this.timeHorizonAgents = 0.0;
        this.timeHorizonObstacles = 0.0;
        this.newVelocity = Vector2D.ZERO;
    }

    void computeNeighbors() {
        this.obstacleNeighbors.clear();
        double range = this.timeHorizonObstacles * this.maxSpeed + this.radius;
        Simulator.instance.kdTree.computeObstacleNeighbors(this, range * range);
        this.agentNeighbors.clear();
        if (this.maxNeighbors > 0) {
            Simulator.instance.kdTree.computeAgentNeighbors(this, this.neighborDistance * this.neighborDistance);
        }

    }

    void computeNewVelocity() {
        this.lines.clear();
        double invTimeHorizonObstacle = 1.0 / this.timeHorizonObstacles;
        Iterator var3 = this.obstacleNeighbors.iterator();

        while(true) {
            while(true) {
                Obstacle obstacle1;
                Obstacle obstacle2;
                Vector2D relativePosition1;
                Vector2D relativePosition2;
                boolean alreadyCovered;
                double distanceSqLine;
                Vector2D leftLegDirection;
                Vector2D rightCutOff;
                do {
                    if (!var3.hasNext()) {
                        int numObstacleLines = this.lines.size();
                        double invTimeHorizon = 1.0 / this.timeHorizonAgents;
                        Iterator var44 = this.agentNeighbors.iterator();

                        while(var44.hasNext()) {
                            Pair<Double, Agent> agentNeighbor = (Pair)var44.next();
                            Agent other = (Agent)agentNeighbor.getSecond();
                            Vector2D relativePosition = other.position.subtract(this.position);
                            Vector2D relativeVelocity = this.velocity.subtract(other.velocity);
                            double distanceSq = relativePosition.getNormSq();
                            double combinedRadius = this.radius + other.radius;
                            double combinedRadiusSq = combinedRadius * combinedRadius;
                            Vector2D u;
                            Vector2D direction;
                            Vector2D w;
                            double dotProduct1;
                            if (distanceSq > combinedRadiusSq) {
                                w = relativeVelocity.subtract(invTimeHorizon, relativePosition);
                                double wLengthSq = w.getNormSq();
                                dotProduct1 = w.dotProduct(relativePosition);
                                double leg;
                                if (dotProduct1 < 0.0 && dotProduct1 * dotProduct1 > combinedRadiusSq * wLengthSq) {
                                    leg = FastMath.sqrt(wLengthSq);
                                    rightCutOff = w.scalarMultiply(1.0 / leg);
                                    direction = new Vector2D(rightCutOff.getY(), -rightCutOff.getX());
                                    u = rightCutOff.scalarMultiply(combinedRadius * invTimeHorizon - leg);
                                } else {
                                    leg = FastMath.sqrt(distanceSq - combinedRadiusSq);
                                    if (RVOMath.det(relativePosition, w) > 0.0) {
                                        direction = (new Vector2D(relativePosition.getX() * leg - relativePosition.getY() * combinedRadius, relativePosition.getX() * combinedRadius + relativePosition.getY() * leg)).scalarMultiply(1.0 / distanceSq);
                                    } else {
                                        direction = (new Vector2D(relativePosition.getX() * leg + relativePosition.getY() * combinedRadius, -relativePosition.getX() * combinedRadius + relativePosition.getY() * leg)).scalarMultiply(-1.0 / distanceSq);
                                    }

                                    double dotProduct2 = relativeVelocity.dotProduct(direction);
                                    u = direction.scalarMultiply(dotProduct2).subtract(relativeVelocity);
                                }
                            } else {
                                distanceSqLine = 1.0 / Simulator.instance.timeStep;
                                leftLegDirection = relativeVelocity.subtract(distanceSqLine, relativePosition);
                                dotProduct1 = leftLegDirection.getNorm();
                                Vector2D unitW = leftLegDirection.scalarMultiply(1.0 / dotProduct1);
                                direction = new Vector2D(unitW.getY(), -unitW.getX());
                                u = unitW.scalarMultiply(combinedRadius * distanceSqLine - dotProduct1);
                            }

                            w = this.velocity.add(0.5, u);
                            this.lines.add(new Line(w, direction));
                        }

                        int lineFail = this.linearProgram2(this.lines, this.preferredVelocity, false);
                        if (lineFail < this.lines.size()) {
                            this.linearProgram3(numObstacleLines, lineFail);
                        }

                        return;
                    }

                    Pair<Double, Obstacle> obstacleNeighbor = (Pair)var3.next();
                    obstacle1 = (Obstacle)obstacleNeighbor.getSecond();
                    obstacle2 = obstacle1.next;
                    relativePosition1 = obstacle1.point.subtract(this.position);
                    relativePosition2 = obstacle2.point.subtract(this.position);
                    alreadyCovered = false;
                    Iterator var10 = this.lines.iterator();

                    while(var10.hasNext()) {
                        Line orcaLine = (Line)var10.next();
                        if (RVOMath.det(relativePosition1.scalarMultiply(invTimeHorizonObstacle).subtract(orcaLine.point), orcaLine.direction) - invTimeHorizonObstacle * this.radius >= -1.0E-5 && RVOMath.det(relativePosition2.scalarMultiply(invTimeHorizonObstacle).subtract(orcaLine.point), orcaLine.direction) - invTimeHorizonObstacle * this.radius >= -1.0E-5) {
                            alreadyCovered = true;
                            break;
                        }
                    }
                } while(alreadyCovered);

                double distanceSq1 = relativePosition1.getNormSq();
                double distanceSq2 = relativePosition2.getNormSq();
                double radiusSq = this.radius * this.radius;
                Vector2D obstacleVector = obstacle2.point.subtract(obstacle1.point);
                double s = -relativePosition1.dotProduct(obstacleVector) / obstacleVector.getNormSq();
                distanceSqLine = relativePosition1.add(s, obstacleVector).getNormSq();
                if (s < 0.0 && distanceSq1 <= radiusSq) {
                    if (obstacle1.convex) {
                        leftLegDirection = (new Vector2D(-relativePosition1.getY(), relativePosition1.getX())).normalize();
                        this.lines.add(new Line(Vector2D.ZERO, leftLegDirection));
                    }
                } else if (s > 1.0 && distanceSq2 <= radiusSq) {
                    if (obstacle2.convex && RVOMath.det(relativePosition2, obstacle2.direction) >= 0.0) {
                        leftLegDirection = (new Vector2D(-relativePosition2.getY(), relativePosition2.getX())).normalize();
                        this.lines.add(new Line(Vector2D.ZERO, leftLegDirection));
                    }
                } else if (s >= 0.0 && s < 1.0 && distanceSqLine <= radiusSq) {
                    leftLegDirection = obstacle1.direction.negate();
                    this.lines.add(new Line(Vector2D.ZERO, leftLegDirection));
                } else {
                    Vector2D rightLegDirection;
                    double leg2;
                    if (s < 0.0 && distanceSqLine <= radiusSq) {
                        if (!obstacle1.convex) {
                            continue;
                        }

                        obstacle2 = obstacle1;
                        leg2 = FastMath.sqrt(distanceSq1 - radiusSq);
                        leftLegDirection = (new Vector2D(relativePosition1.getX() * leg2 - relativePosition1.getY() * this.radius, relativePosition1.getX() * this.radius + relativePosition1.getY() * leg2)).scalarMultiply(1.0 / distanceSq1);
                        rightLegDirection = (new Vector2D(relativePosition1.getX() * leg2 + relativePosition1.getY() * this.radius, -relativePosition1.getX() * this.radius + relativePosition1.getY() * leg2)).scalarMultiply(1.0 / distanceSq1);
                    } else if (s > 1.0 && distanceSqLine <= radiusSq) {
                        if (!obstacle2.convex) {
                            continue;
                        }

                        obstacle1 = obstacle2;
                        leg2 = FastMath.sqrt(distanceSq2 - radiusSq);
                        leftLegDirection = (new Vector2D(relativePosition2.getX() * leg2 - relativePosition2.getY() * this.radius, relativePosition2.getX() * this.radius + relativePosition2.getY() * leg2)).scalarMultiply(1.0 / distanceSq2);
                        rightLegDirection = (new Vector2D(relativePosition2.getX() * leg2 + relativePosition2.getY() * this.radius, -relativePosition2.getX() * this.radius + relativePosition2.getY() * leg2)).scalarMultiply(1.0 / distanceSq2);
                    } else {
                        if (obstacle1.convex) {
                            leg2 = FastMath.sqrt(distanceSq1 - radiusSq);
                            leftLegDirection = (new Vector2D(relativePosition1.getX() * leg2 - relativePosition1.getY() * this.radius, relativePosition1.getX() * this.radius + relativePosition1.getY() * leg2)).scalarMultiply(1.0 / distanceSq1);
                        } else {
                            leftLegDirection = obstacle1.direction.negate();
                        }

                        if (obstacle2.convex) {
                            leg2 = FastMath.sqrt(distanceSq2 - radiusSq);
                            rightLegDirection = (new Vector2D(relativePosition2.getX() * leg2 + relativePosition2.getY() * this.radius, -relativePosition2.getX() * this.radius + relativePosition2.getY() * leg2)).scalarMultiply(1.0 / distanceSq2);
                        } else {
                            rightLegDirection = obstacle1.direction;
                        }
                    }

                    boolean leftLegForeign = false;
                    boolean rightLegForeign = false;
                    if (obstacle1.convex && RVOMath.det(leftLegDirection, obstacle1.previous.direction.negate()) >= 0.0) {
                        leftLegDirection = obstacle1.previous.direction.negate();
                        leftLegForeign = true;
                    }

                    if (obstacle2.convex && RVOMath.det(rightLegDirection, obstacle2.direction) <= 0.0) {
                        rightLegDirection = obstacle2.direction;
                        rightLegForeign = true;
                    }

                    Vector2D leftCutOff = obstacle1.point.subtract(this.position).scalarMultiply(invTimeHorizonObstacle);
                    rightCutOff = obstacle2.point.subtract(this.position).scalarMultiply(invTimeHorizonObstacle);
                    Vector2D cutOffVector = rightCutOff.subtract(leftCutOff);
                    double t = obstacle1 == obstacle2 ? 0.5 : this.velocity.subtract(leftCutOff).dotProduct(cutOffVector) / cutOffVector.getNormSq();
                    double tLeft = this.velocity.subtract(leftCutOff).dotProduct(leftLegDirection);
                    double tRight = this.velocity.subtract(rightCutOff).dotProduct(rightLegDirection);
                    Vector2D direction;
                    Vector2D unitW;
                    Vector2D point;
                    if (t < 0.0 && tLeft < 0.0 || obstacle1 == obstacle2 && tLeft < 0.0 && tRight < 0.0) {
                        unitW = this.velocity.subtract(leftCutOff).normalize();
                        direction = new Vector2D(unitW.getY(), -unitW.getX());
                        point = leftCutOff.add(this.radius * invTimeHorizonObstacle, unitW);
                        this.lines.add(new Line(point, direction));
                    } else if (t > 1.0 && tRight < 0.0) {
                        unitW = this.velocity.subtract(rightCutOff).normalize();
                        direction = new Vector2D(unitW.getY(), -unitW.getX());
                        point = rightCutOff.add(this.radius * invTimeHorizonObstacle, unitW);
                        this.lines.add(new Line(point, direction));
                    } else {
                        double distanceSqCutOff = !(t < 0.0) && !(t > 1.0) && obstacle1 != obstacle2 ? this.velocity.distanceSq(leftCutOff.add(cutOffVector.scalarMultiply(t))) : Double.POSITIVE_INFINITY;
                        double distanceSqLeft = tLeft < 0.0 ? Double.POSITIVE_INFINITY : this.velocity.distanceSq(leftCutOff.add(leftLegDirection.scalarMultiply(tLeft)));
                        double distanceSqRight = tRight < 0.0 ? Double.POSITIVE_INFINITY : this.velocity.distanceSq(rightCutOff.add(rightLegDirection.scalarMultiply(tRight)));
                        if (distanceSqCutOff <= distanceSqLeft && distanceSqCutOff <= distanceSqRight) {
                            point = obstacle1.direction.negate();
                            point = leftCutOff.add(this.radius * invTimeHorizonObstacle, new Vector2D(-point.getY(), point.getX()));
                            this.lines.add(new Line(point, point));
                        } else if (distanceSqLeft <= distanceSqRight) {
                            if (!leftLegForeign) {
                                point = leftCutOff.add(this.radius * invTimeHorizonObstacle, new Vector2D(-leftLegDirection.getY(), leftLegDirection.getX()));
                                this.lines.add(new Line(point, leftLegDirection));
                            }
                        } else if (!rightLegForeign) {
                            point = rightLegDirection.negate();
                            point = rightCutOff.add(this.radius * invTimeHorizonObstacle, new Vector2D(-point.getY(), point.getX()));
                            this.lines.add(new Line(point, point));
                        }
                    }
                }
            }
        }
    }

    double insertAgentNeighbor(Agent agent, double rangeSq) {
        if (this != agent) {
            double distSq = this.position.distanceSq(agent.position);
            if (distSq < rangeSq) {
                if (this.agentNeighbors.size() < this.maxNeighbors) {
                    this.agentNeighbors.add(new Pair(distSq, agent));
                }

                int i;
                for(i = this.agentNeighbors.size() - 1; i != 0 && distSq < (Double)((Pair)this.agentNeighbors.get(i - 1)).getFirst(); --i) {
                    this.agentNeighbors.set(i, (Pair)this.agentNeighbors.get(i - 1));
                }

                this.agentNeighbors.set(i, new Pair(distSq, agent));
                if (this.agentNeighbors.size() == this.maxNeighbors) {
                    rangeSq = (Double)((Pair)this.agentNeighbors.get(this.agentNeighbors.size() - 1)).getFirst();
                }
            }
        }

        return rangeSq;
    }

    void insertObstacleNeighbor(Obstacle obstacle, double rangeSq) {
        Obstacle nextObstacle = obstacle.next;
        double r = this.position.subtract(obstacle.point).dotProduct(nextObstacle.point.subtract(obstacle.point)) / nextObstacle.point.distanceSq(obstacle.point);
        double distSq;
        if (r < 0.0) {
            distSq = this.position.distanceSq(obstacle.point);
        } else if (r > 1.0) {
            distSq = this.position.distanceSq(nextObstacle.point);
        } else {
            distSq = this.position.distanceSq(obstacle.point.add(nextObstacle.point.subtract(obstacle.point).scalarMultiply(r)));
        }

        if (distSq < rangeSq) {
            this.obstacleNeighbors.add(new Pair(distSq, obstacle));

            int i;
            for(i = this.obstacleNeighbors.size() - 1; i != 0 && distSq < (Double)((Pair)this.obstacleNeighbors.get(i - 1)).getFirst(); --i) {
                this.obstacleNeighbors.set(i, (Pair)this.obstacleNeighbors.get(i - 1));
            }

            this.obstacleNeighbors.set(i, new Pair(distSq, obstacle));
        }

    }

    void update() {
        this.velocity = this.newVelocity;
        this.position = this.position.add(Simulator.instance.timeStep, this.velocity);
    }

    private boolean linearProgram1(List<Line> lines, int lineNo, Vector2D optimizationVelocity, boolean optimizeDirection) {
        double dotProduct = ((Line)lines.get(lineNo)).point.dotProduct(((Line)lines.get(lineNo)).direction);
        double discriminant = dotProduct * dotProduct + this.maxSpeed * this.maxSpeed - ((Line)lines.get(lineNo)).point.getNormSq();
        if (discriminant < 0.0) {
            return false;
        } else {
            double sqrtDiscriminant = FastMath.sqrt(discriminant);
            double tLeft = -sqrtDiscriminant - dotProduct;
            double tRight = sqrtDiscriminant - dotProduct;

            for(int i = 0; i < lineNo; ++i) {
                double denominator = RVOMath.det(((Line)lines.get(lineNo)).direction, ((Line)lines.get(i)).direction);
                double numerator = RVOMath.det(((Line)lines.get(i)).direction, ((Line)lines.get(lineNo)).point.subtract(((Line)lines.get(i)).point));
                if (FastMath.abs(denominator) <= 1.0E-5) {
                    if (numerator < 0.0) {
                        return false;
                    }
                } else {
                    double t = numerator / denominator;
                    if (denominator >= 0.0) {
                        tRight = FastMath.min(tRight, t);
                    } else {
                        tLeft = FastMath.max(tLeft, t);
                    }

                    if (tLeft > tRight) {
                        return false;
                    }
                }
            }

            if (optimizeDirection) {
                if (optimizationVelocity.dotProduct(((Line)lines.get(lineNo)).direction) > 0.0) {
                    this.newVelocity = ((Line)lines.get(lineNo)).point.add(tRight, ((Line)lines.get(lineNo)).direction);
                } else {
                    this.newVelocity = ((Line)lines.get(lineNo)).point.add(tLeft, ((Line)lines.get(lineNo)).direction);
                }
            } else {
                double t = ((Line)lines.get(lineNo)).direction.dotProduct(optimizationVelocity.subtract(((Line)lines.get(lineNo)).point));
                if (t < tLeft) {
                    this.newVelocity = ((Line)lines.get(lineNo)).point.add(tLeft, ((Line)lines.get(lineNo)).direction);
                } else if (t > tRight) {
                    this.newVelocity = ((Line)lines.get(lineNo)).point.add(tRight, ((Line)lines.get(lineNo)).direction);
                } else {
                    this.newVelocity = ((Line)lines.get(lineNo)).point.add(t, ((Line)lines.get(lineNo)).direction);
                }
            }

            return true;
        }
    }

    private int linearProgram2(List<Line> lines, Vector2D optimizationVelocity, boolean optimizeDirection) {
        if (optimizeDirection) {
            this.newVelocity = optimizationVelocity.scalarMultiply(this.maxSpeed);
        } else if (optimizationVelocity.getNormSq() > this.maxSpeed * this.maxSpeed) {
            this.newVelocity = optimizationVelocity.normalize().scalarMultiply(this.maxSpeed);
        } else {
            this.newVelocity = optimizationVelocity;
        }

        for(int lineNo = 0; lineNo < lines.size(); ++lineNo) {
            if (RVOMath.det(((Line)lines.get(lineNo)).direction, ((Line)lines.get(lineNo)).point.subtract(this.newVelocity)) > 0.0) {
                Vector2D tempResult = this.newVelocity;
                if (!this.linearProgram1(lines, lineNo, optimizationVelocity, optimizeDirection)) {
                    this.newVelocity = tempResult;
                    return lineNo;
                }
            }
        }

        return lines.size();
    }

    private void linearProgram3(int numObstacleLines, int beginLine) {
        double distance = 0.0;

        for(int i = beginLine; i < this.lines.size(); ++i) {
            if (RVOMath.det(((Line)this.lines.get(i)).direction, ((Line)this.lines.get(i)).point.subtract(this.newVelocity)) > distance) {
                List<Line> projectedLines = new ArrayList(numObstacleLines);

                int j;
                for(j = 0; j < numObstacleLines; ++j) {
                    projectedLines.add((Line)this.lines.get(j));
                }

                for(j = numObstacleLines; j < i; ++j) {
                    double determinant = RVOMath.det(((Line)this.lines.get(i)).direction, ((Line)this.lines.get(j)).direction);
                    Vector2D point;
                    if (FastMath.abs(determinant) <= 1.0E-5) {
                        if (((Line)this.lines.get(i)).direction.dotProduct(((Line)this.lines.get(j)).direction) > 0.0) {
                            continue;
                        }

                        point = ((Line)this.lines.get(i)).point.add(((Line)this.lines.get(j)).point).scalarMultiply(0.5);
                    } else {
                        point = ((Line)this.lines.get(i)).point.add(((Line)this.lines.get(i)).direction.scalarMultiply(RVOMath.det(((Line)this.lines.get(j)).direction, ((Line)this.lines.get(i)).point.subtract(((Line)this.lines.get(j)).point)) / determinant));
                    }

                    Vector2D direction = ((Line)this.lines.get(j)).direction.subtract(((Line)this.lines.get(i)).direction).normalize();
                    projectedLines.add(new Line(point, direction));
                }

                Vector2D tempResult = this.newVelocity;
                if (this.linearProgram2(projectedLines, new Vector2D(-((Line)this.lines.get(i)).direction.getY(), ((Line)this.lines.get(i)).direction.getX()), true) < projectedLines.size()) {
                    this.newVelocity = tempResult;
                }

                distance = RVOMath.det(((Line)this.lines.get(i)).direction, ((Line)this.lines.get(i)).point.subtract(this.newVelocity));
            }
        }

    }
}
