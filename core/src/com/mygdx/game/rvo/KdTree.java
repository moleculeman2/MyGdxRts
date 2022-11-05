package com.mygdx.game.rvo;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

class KdTree {
    private static final int MAX_LEAF_SIZE = 10;
    private Agent[] agents = null;
    private AgentTreeNode[] agentTree = null;
    private ObstacleTreeNode obstacleTree = null;

    KdTree() {
    }

    void buildAgentTree() {
        if (this.agents == null || this.agents.length != Simulator.instance.agents.size()) {
            this.agents = new Agent[Simulator.instance.agents.size()];

            int nodeNo;
            for(nodeNo = 0; nodeNo < this.agents.length; ++nodeNo) {
                this.agents[nodeNo] = (Agent)Simulator.instance.agents.get(nodeNo);
            }

            this.agentTree = new AgentTreeNode[2 * this.agents.length];

            for(nodeNo = 0; nodeNo < this.agentTree.length; ++nodeNo) {
                this.agentTree[nodeNo] = new AgentTreeNode();
            }
        }

        if (this.agents.length != 0) {
            this.buildAgentTreeRecursive(0, this.agents.length, 0);
        }

    }

    void buildObstacleTree() {
        this.obstacleTree = new ObstacleTreeNode();
        List<Obstacle> obstacles = new ArrayList(Simulator.instance.obstacles.size());

        for(int obstacleNo = 0; obstacleNo < Simulator.instance.obstacles.size(); ++obstacleNo) {
            obstacles.add((Obstacle)Simulator.instance.obstacles.get(obstacleNo));
        }

        this.obstacleTree = buildObstacleTreeRecursive(obstacles);
    }

    void computeAgentNeighbors(Agent agent, double rangeSq) {
        this.queryAgentTreeRecursive(agent, rangeSq, 0);
    }

    void computeObstacleNeighbors(Agent agent, double rangeSq) {
        queryObstacleTreeRecursive(agent, rangeSq, this.obstacleTree);
    }

    boolean queryVisibility(Vector2D q1, Vector2D q2, double radius) {
        return queryVisibilityRecursive(q1, q2, radius, this.obstacleTree);
    }

    private void buildAgentTreeRecursive(int begin, int end, int node) {
        this.agentTree[node].begin = begin;
        this.agentTree[node].end = end;
        this.agentTree[node].maxX = this.agents[begin].position.getX();
        this.agentTree[node].maxY = this.agents[begin].position.getY();
        this.agentTree[node].minX = this.agentTree[node].maxX;
        this.agentTree[node].minY = this.agentTree[node].maxY;

        for(int i = begin + 1; i < end; ++i) {
            this.agentTree[node].maxX = FastMath.max(this.agentTree[node].maxX, this.agents[i].position.getX());
            this.agentTree[node].minX = FastMath.min(this.agentTree[node].minX, this.agents[i].position.getX());
            this.agentTree[node].maxY = FastMath.max(this.agentTree[node].maxY, this.agents[i].position.getY());
            this.agentTree[node].minY = FastMath.min(this.agentTree[node].minY, this.agents[i].position.getY());
        }

        if (end - begin > 10) {
            boolean isVertical = this.agentTree[node].maxX - this.agentTree[node].minX > this.agentTree[node].maxY - this.agentTree[node].minY;
            double splitValue = 0.5 * (isVertical ? this.agentTree[node].maxX + this.agentTree[node].minX : this.agentTree[node].maxY + this.agentTree[node].minY);
            int left = begin;
            int right = end;

            while(left < right) {
                while(left < right && (isVertical ? this.agents[left].position.getX() : this.agents[left].position.getY()) < splitValue) {
                    ++left;
                }

                while(right > left && (isVertical ? this.agents[right - 1].position.getX() : this.agents[right - 1].position.getY()) >= splitValue) {
                    --right;
                }

                if (left < right) {
                    Agent tempAgent = this.agents[left];
                    this.agents[left] = this.agents[right - 1];
                    this.agents[right - 1] = tempAgent;
                    ++left;
                    --right;
                }
            }

            if (left == begin) {
                ++left;
            }

            this.agentTree[node].left = node + 1;
            this.agentTree[node].right = node + 2 * (left - begin);
            this.buildAgentTreeRecursive(begin, left, this.agentTree[node].left);
            this.buildAgentTreeRecursive(left, end, this.agentTree[node].right);
        }

    }

    private static ObstacleTreeNode buildObstacleTreeRecursive(List<Obstacle> obstacles) {
        if (obstacles.isEmpty()) {
            return null;
        } else {
            ObstacleTreeNode node = new ObstacleTreeNode();
            int optimalSplit = 0;
            int minLeft = obstacles.size();
            int minRight = obstacles.size();

            int leftSize;
            int rightSize;
            Obstacle obstacleI1;
            Obstacle obstacleJ1;
            for(int i = 0; i < obstacles.size(); ++i) {
                leftSize = 0;
                rightSize = 0;
                obstacleI1 = (Obstacle)obstacles.get(i);
                obstacleI1 = obstacleI1.next;

                for(int j = 0; j < obstacles.size(); ++j) {
                    if (i != j) {
                        obstacleJ1 = (Obstacle)obstacles.get(j);
                        obstacleJ1 = obstacleJ1.next;
                        double j1LeftOfI = RVOMath.leftOf(obstacleI1.point, obstacleI1.point, obstacleJ1.point);
                        double j2LeftOfI = RVOMath.leftOf(obstacleI1.point, obstacleI1.point, obstacleJ1.point);
                        if (j1LeftOfI >= -1.0E-5 && j2LeftOfI >= -1.0E-5) {
                            ++leftSize;
                        } else if (j1LeftOfI <= 1.0E-5 && j2LeftOfI <= 1.0E-5) {
                            ++rightSize;
                        } else {
                            ++leftSize;
                            ++rightSize;
                        }

                        Pair<Integer, Integer> pair1 = new Pair(FastMath.max(leftSize, rightSize), FastMath.min(leftSize, rightSize));
                        Pair<Integer, Integer> pair2 = new Pair(FastMath.max(minLeft, minRight), FastMath.min(minLeft, minRight));
                        if ((Integer)pair1.getFirst() >= (Integer)pair2.getFirst() && ((Integer)pair1.getFirst() > (Integer)pair2.getFirst() || (Integer)pair1.getSecond() >= (Integer)pair2.getSecond())) {
                            break;
                        }
                    }
                }

                Pair<Integer, Integer> pair1 = new Pair(FastMath.max(leftSize, rightSize), FastMath.min(leftSize, rightSize));
                Pair<Integer, Integer> pair2 = new Pair(FastMath.max(minLeft, minRight), FastMath.min(minLeft, minRight));
                if ((Integer)pair1.getFirst() < (Integer)pair2.getFirst() || (Integer)pair1.getFirst() <= (Integer)pair2.getFirst() && (Integer)pair1.getSecond() < (Integer)pair2.getSecond()) {
                    minLeft = leftSize;
                    minRight = rightSize;
                    optimalSplit = i;
                }
            }

            List<Obstacle> leftObstacles = new ArrayList(minLeft);

            for(leftSize = 0; leftSize < minLeft; ++leftSize) {
                leftObstacles.add((Obstacle) null);
            }

            List<Obstacle> rightObstacles = new ArrayList(minRight);

            for(rightSize = 0; rightSize < minRight; ++rightSize) {
                rightObstacles.add((Obstacle) null);
            }

            rightSize = 0;
            int rightCounter = 0;
            obstacleI1 = (Obstacle)obstacles.get(optimalSplit);
            Obstacle obstacleI2 = obstacleI1.next;

            for(int j = 0; j < obstacles.size(); ++j) {
                if (optimalSplit != j) {
                    obstacleJ1 = (Obstacle)obstacles.get(j);
                    Obstacle obstacleJ2 = obstacleJ1.next;
                    double j1LeftOfI = RVOMath.leftOf(obstacleI1.point, obstacleI2.point, obstacleJ1.point);
                    double j2LeftOfI = RVOMath.leftOf(obstacleI1.point, obstacleI2.point, obstacleJ2.point);
                    if (j1LeftOfI >= -1.0E-5 && j2LeftOfI >= -1.0E-5) {
                        leftObstacles.set(rightSize++, (Obstacle)obstacles.get(j));
                    } else if (j1LeftOfI <= 1.0E-5 && j2LeftOfI <= 1.0E-5) {
                        rightObstacles.set(rightCounter++, (Obstacle)obstacles.get(j));
                    } else {
                        double t = RVOMath.det(obstacleI2.point.subtract(obstacleI1.point), obstacleJ1.point.subtract(obstacleI1.point)) / RVOMath.det(obstacleI2.point.subtract(obstacleI1.point), obstacleJ1.point.subtract(obstacleJ2.point));
                        Vector2D splitPoint = obstacleJ1.point.add(obstacleJ2.point.subtract(obstacleJ1.point).scalarMultiply(t));
                        Obstacle newObstacle = new Obstacle();
                        newObstacle.point = splitPoint;
                        newObstacle.previous = obstacleJ1;
                        newObstacle.next = obstacleJ2;
                        newObstacle.convex = true;
                        newObstacle.direction = obstacleJ1.direction;
                        newObstacle.id = Simulator.instance.obstacles.size();
                        Simulator.instance.obstacles.add(newObstacle);
                        obstacleJ1.next = newObstacle;
                        obstacleJ2.previous = newObstacle;
                        if (j1LeftOfI > 0.0) {
                            leftObstacles.set(rightSize++, obstacleJ1);
                            rightObstacles.set(rightCounter++, newObstacle);
                        } else {
                            rightObstacles.set(rightCounter++, obstacleJ1);
                            leftObstacles.set(rightSize++, newObstacle);
                        }
                    }
                }
            }

            node.obstacle = obstacleI1;
            node.left = buildObstacleTreeRecursive(leftObstacles);
            node.right = buildObstacleTreeRecursive(rightObstacles);
            return node;
        }
    }

    private static double sqr(double d) {
        return d * d;
    }

    private double queryAgentTreeRecursive(Agent agent, double rangeSq, int node) {
        if (this.agentTree[node].end - this.agentTree[node].begin <= 10) {
            for(int agentNo = this.agentTree[node].begin; agentNo < this.agentTree[node].end; ++agentNo) {
                rangeSq = agent.insertAgentNeighbor(this.agents[agentNo], rangeSq);
            }
        } else {
            double distanceSqLeft = sqr(FastMath.max(0.0, this.agentTree[this.agentTree[node].left].minX - agent.position.getX())) + sqr(FastMath.max(0.0, agent.position.getX() - this.agentTree[this.agentTree[node].left].maxX)) + sqr(FastMath.max(0.0, this.agentTree[this.agentTree[node].left].minY - agent.position.getY())) + sqr(FastMath.max(0.0, agent.position.getY() - this.agentTree[this.agentTree[node].left].maxY));
            double distanceSqRight = sqr(FastMath.max(0.0, this.agentTree[this.agentTree[node].right].minX - agent.position.getX())) + sqr(FastMath.max(0.0, agent.position.getX() - this.agentTree[this.agentTree[node].right].maxX)) + sqr(FastMath.max(0.0, this.agentTree[this.agentTree[node].right].minY - agent.position.getY())) + sqr(FastMath.max(0.0, agent.position.getY() - this.agentTree[this.agentTree[node].right].maxY));
            if (distanceSqLeft < distanceSqRight) {
                if (distanceSqLeft < rangeSq) {
                    rangeSq = this.queryAgentTreeRecursive(agent, rangeSq, this.agentTree[node].left);
                    if (distanceSqRight < rangeSq) {
                        rangeSq = this.queryAgentTreeRecursive(agent, rangeSq, this.agentTree[node].right);
                    }
                }
            } else if (distanceSqRight < rangeSq) {
                rangeSq = this.queryAgentTreeRecursive(agent, rangeSq, this.agentTree[node].right);
                if (distanceSqLeft < rangeSq) {
                    rangeSq = this.queryAgentTreeRecursive(agent, rangeSq, this.agentTree[node].left);
                }
            }
        }

        return rangeSq;
    }

    private static void queryObstacleTreeRecursive(Agent agent, double rangeSq, ObstacleTreeNode node) {
        if (node != null) {
            Obstacle obstacle1 = node.obstacle;
            Obstacle obstacle2 = obstacle1.next;
            double agentLeftOfLine = RVOMath.leftOf(obstacle1.point, obstacle2.point, agent.position);
            queryObstacleTreeRecursive(agent, rangeSq, agentLeftOfLine >= 0.0 ? node.left : node.right);
            double distanceSqLine = agentLeftOfLine * agentLeftOfLine / obstacle2.point.distanceSq(obstacle1.point);
            if (distanceSqLine < rangeSq) {
                if (agentLeftOfLine < 0.0) {
                    agent.insertObstacleNeighbor(node.obstacle, rangeSq);
                }

                queryObstacleTreeRecursive(agent, rangeSq, agentLeftOfLine >= 0.0 ? node.right : node.left);
            }
        }

    }

    private static boolean queryVisibilityRecursive(Vector2D q1, Vector2D q2, double radius, ObstacleTreeNode node) {
        if (node == null) {
            return true;
        } else {
            Obstacle obstacle1 = node.obstacle;
            Obstacle obstacle2 = obstacle1.next;
            double q1LeftOfI = RVOMath.leftOf(obstacle1.point, obstacle2.point, q1);
            double q2LeftOfI = RVOMath.leftOf(obstacle1.point, obstacle2.point, q2);
            double invLengthI = 1.0 / obstacle2.point.distanceSq(obstacle1.point);
            if (q1LeftOfI >= 0.0 && q2LeftOfI >= 0.0) {
                return queryVisibilityRecursive(q1, q2, radius, node.left) && (q1LeftOfI * q1LeftOfI * invLengthI >= radius * radius && q2LeftOfI * q2LeftOfI * invLengthI >= radius * radius || queryVisibilityRecursive(q1, q2, radius, node.right));
            } else if (q1LeftOfI <= 0.0 && q2LeftOfI <= 0.0) {
                return queryVisibilityRecursive(q1, q2, radius, node.right) && (q1LeftOfI * q1LeftOfI * invLengthI >= radius * radius && q2LeftOfI * q2LeftOfI * invLengthI >= radius * radius || queryVisibilityRecursive(q1, q2, radius, node.left));
            } else if (q1LeftOfI >= 0.0 && q2LeftOfI <= 0.0) {
                return queryVisibilityRecursive(q1, q2, radius, node.left) && queryVisibilityRecursive(q1, q2, radius, node.right);
            } else {
                double point1LeftOfQ = RVOMath.leftOf(q1, q2, obstacle1.point);
                double point2LeftOfQ = RVOMath.leftOf(q1, q2, obstacle2.point);
                double invLengthQ = 1.0 / q2.distanceSq(q1);
                return point1LeftOfQ * point2LeftOfQ >= 0.0 && point1LeftOfQ * point1LeftOfQ * invLengthQ > radius * radius && point2LeftOfQ * point2LeftOfQ * invLengthQ > radius * radius && queryVisibilityRecursive(q1, q2, radius, node.left) && queryVisibilityRecursive(q1, q2, radius, node.right);
            }
        }
    }

    private static class ObstacleTreeNode {
        Obstacle obstacle = null;
        ObstacleTreeNode left = null;
        ObstacleTreeNode right = null;

        private ObstacleTreeNode() {
        }
    }

    private static class AgentTreeNode {
        int begin = 0;
        int end = 0;
        int left = 0;
        int right = 0;
        double maxX = 0.0;
        double maxY = 0.0;
        double minX = 0.0;
        double minY = 0.0;

        private AgentTreeNode() {
        }
    }
}
