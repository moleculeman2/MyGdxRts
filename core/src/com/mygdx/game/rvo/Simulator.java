//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mygdx.game.rvo;

import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    public static final Simulator instance = new Simulator();
    final List<Agent> agents = new ArrayList();
    final List<Obstacle> obstacles = new ArrayList();
    final KdTree kdTree = new KdTree();
    double timeStep = 0.0;
    private Agent defaultAgent = null;
    private double globalTime = 0.0;

    public int addAgent(Vector2D position) {
        if (this.defaultAgent == null) {
            return -1;
        } else {
            Agent agent = new Agent();
            agent.id = this.agents.size();
            agent.maxNeighbors = this.defaultAgent.maxNeighbors;
            agent.maxSpeed = this.defaultAgent.maxSpeed;
            agent.neighborDistance = this.defaultAgent.neighborDistance;
            agent.position = position;
            agent.radius = this.defaultAgent.radius;
            agent.timeHorizonAgents = this.defaultAgent.timeHorizonAgents;
            agent.timeHorizonObstacles = this.defaultAgent.timeHorizonObstacles;
            agent.velocity = this.defaultAgent.velocity;
            this.agents.add(agent);
            return agent.id;
        }
    }

    public int addAgent(Vector2D position, double neighborDistance, int maxNeighbors, double timeHorizonAgents, double timeHorizonObstacles, double radius, double maxSpeed, Vector2D velocity) {
        Agent agent = new Agent();
        agent.id = this.agents.size();
        agent.maxNeighbors = maxNeighbors;
        agent.maxSpeed = maxSpeed;
        agent.neighborDistance = neighborDistance;
        agent.position = position;
        agent.radius = radius;
        agent.timeHorizonAgents = timeHorizonAgents;
        agent.timeHorizonObstacles = timeHorizonObstacles;
        agent.velocity = velocity;
        this.agents.add(agent);
        return agent.id;
    }

    public int addObstacle(List<Vector2D> vertices) {
        if (vertices.size() < 2) {
            return -1;
        } else {
            int obstacleNo = this.obstacles.size();

            for(int vertexNo = 0; vertexNo < vertices.size(); ++vertexNo) {
                Obstacle obstacle = new Obstacle();
                obstacle.point = (Vector2D)vertices.get(vertexNo);
                if (vertexNo != 0) {
                    obstacle.previous = (Obstacle)this.obstacles.get(this.obstacles.size() - 1);
                    obstacle.previous.next = obstacle;
                }

                if (vertexNo == vertices.size() - 1) {
                    obstacle.next = (Obstacle)this.obstacles.get(obstacleNo);
                    obstacle.next.previous = obstacle;
                }

                obstacle.direction = ((Vector2D)vertices.get(vertexNo == vertices.size() - 1 ? 0 : vertexNo + 1)).subtract((Vector)vertices.get(vertexNo)).normalize();
                obstacle.convex = vertices.size() == 2 || RVOMath.leftOf((Vector2D)vertices.get(vertexNo == 0 ? vertices.size() - 1 : vertexNo - 1), (Vector2D)vertices.get(vertexNo), (Vector2D)vertices.get(vertexNo == vertices.size() - 1 ? 0 : vertexNo + 1)) >= 0.0;
                obstacle.id = this.obstacles.size();
                this.obstacles.add(obstacle);
            }

            return obstacleNo;
        }
    }

    public double doStep() {
        this.kdTree.buildAgentTree();
        this.agents.parallelStream().forEach((agent) -> {
            agent.computeNeighbors();
            agent.computeNewVelocity();
        });
        this.agents.parallelStream().forEach((agent) -> {
            agent.update();
        });
        this.globalTime += this.timeStep;
        return this.globalTime;
    }

    public int getAgentAgentNeighbor(int agentNo, int neighborNo) {
        return ((Agent)((Pair)((Agent)this.agents.get(agentNo)).agentNeighbors.get(neighborNo)).getSecond()).id;
    }

    public int getAgentMaxNeighbors(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).maxNeighbors;
    }

    public double getAgentMaxSpeed(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).maxSpeed;
    }

    public double getAgentNeighborDistance(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).neighborDistance;
    }

    public int getAgentNumAgentNeighbors(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).agentNeighbors.size();
    }

    public int getAgentNumObstacleNeighbors(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).obstacleNeighbors.size();
    }

    public int getAgentObstacleNeighbor(int agentNo, int neighborNo) {
        return ((Obstacle)((Pair)((Agent)this.agents.get(agentNo)).obstacleNeighbors.get(neighborNo)).getSecond()).id;
    }

    public List<Line> getAgentLines(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).lines;
    }

    public Vector2D getAgentPosition(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).position;
    }

    public Vector2D getAgentPreferredVelocity(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).preferredVelocity;
    }

    public double getAgentRadius(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).radius;
    }

    public double getAgentTimeHorizonAgents(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).timeHorizonAgents;
    }

    public double getAgentTimeHorizonObstacles(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).timeHorizonObstacles;
    }

    public Vector2D getAgentVelocity(int agentNo) {
        return ((Agent)this.agents.get(agentNo)).velocity;
    }

    public double getGlobalTime() {
        return this.globalTime;
    }

    public int getNumAgents() {
        return this.agents.size();
    }

    public int getNumObstacleVertices() {
        return this.obstacles.size();
    }

    public Vector2D getObstacleVertex(int vertexNo) {
        return ((Obstacle)this.obstacles.get(vertexNo)).point;
    }

    public int getNextObstacleVertexNo(int vertexNo) {
        return ((Obstacle)this.obstacles.get(vertexNo)).next.id;
    }

    public int getPreviousObstacleVertexNo(int vertexNo) {
        return ((Obstacle)this.obstacles.get(vertexNo)).previous.id;
    }

    public double getTimeStep() {
        return this.timeStep;
    }

    public void processObstacles() {
        this.kdTree.buildObstacleTree();
    }

    public boolean queryVisibility(Vector2D point1, Vector2D point2, double radius) {
        return this.kdTree.queryVisibility(point1, point2, radius);
    }

    public void setAgentDefaults(double neighborDistance, int maxNeighbors, double timeHorizonAgents, double timeHorizonObstacles, double radius, double maxSpeed, Vector2D velocity) {
        if (this.defaultAgent == null) {
            this.defaultAgent = new Agent();
        }

        this.defaultAgent.maxNeighbors = maxNeighbors;
        this.defaultAgent.maxSpeed = maxSpeed;
        this.defaultAgent.neighborDistance = neighborDistance;
        this.defaultAgent.radius = radius;
        this.defaultAgent.timeHorizonAgents = timeHorizonAgents;
        this.defaultAgent.timeHorizonObstacles = timeHorizonObstacles;
        this.defaultAgent.velocity = velocity;
    }

    public void setAgentMaxNeighbors(int agentNo, int maxNeighbors) {
        ((Agent)this.agents.get(agentNo)).maxNeighbors = maxNeighbors;
    }

    public void setAgentMaxSpeed(int agentNo, double maxSpeed) {
        ((Agent)this.agents.get(agentNo)).maxSpeed = maxSpeed;
    }

    public void setAgentNeighborDistance(int agentNo, double neighborDistance) {
        ((Agent)this.agents.get(agentNo)).neighborDistance = neighborDistance;
    }

    public void setAgentPosition(int agentNo, Vector2D position) {
        ((Agent)this.agents.get(agentNo)).position = position;
    }

    public void setAgentPreferredVelocity(int agentNo, Vector2D preferredVelocity) {
        ((Agent)this.agents.get(agentNo)).preferredVelocity = preferredVelocity;
    }

    public void setAgentRadius(int agentNo, double radius) {
        ((Agent)this.agents.get(agentNo)).radius = radius;
    }

    public void setAgentTimeHorizonAgents(int agentNo, double timeHorizonAgents) {
        ((Agent)this.agents.get(agentNo)).timeHorizonAgents = timeHorizonAgents;
    }

    public void setAgentTimeHorizonObstacles(int agentNo, double timeHorizonObstacles) {
        ((Agent)this.agents.get(agentNo)).timeHorizonObstacles = timeHorizonObstacles;
    }

    public void setAgentVelocity(int agentNo, Vector2D velocity) {
        ((Agent)this.agents.get(agentNo)).velocity = velocity;
    }

    public void setGlobalTime(double globalTime) {
        this.globalTime = globalTime;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    private Simulator() {
    }
}
