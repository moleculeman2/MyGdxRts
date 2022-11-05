package com.mygdx.game.rvo;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Example showing a demo with 100 agents split in four groups initially
// positioned in four corners of the environment. Each agent attempts to move to
// other side of the environment through a narrow passage generated by four
// obstacles. There is no road map to guide the agents around the obstacles.
public class Blocks {
    // Store the goals of the agents.
    private final List<Vector2D> goals = new ArrayList<>();

    // Random number generator.
    private final Random random = new Random();

    public void setupScenario() {
        // Specify the global time step of the simulation.
        Simulator.instance.setTimeStep(0.25);

        // Specify the default parameters for agents that are subsequently
        // added.
        Simulator.instance.setAgentDefaults(15.0, 10, 5.0, 5.0, 2.0, 2.0, Vector2D.ZERO);

        // Add agents, specifying their start position, and store their goals on
        // the opposite side of the environment.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Simulator.instance.addAgent(new Vector2D(1000.0 + i * 10.0, 50.0 + j * 10.0));
                goals.add(new Vector2D(50.0, 1000.0));

                Simulator.instance.addAgent(new Vector2D(50.0 + i * 10.0, 50.0 + j * 10.0));
                goals.add(new Vector2D(1000.0, 1000.0));

                Simulator.instance.addAgent(new Vector2D(1000.0 + i * 10.0, 1000.0 + j * 10.0));
                goals.add(new Vector2D(50.0, 50.0));

                Simulator.instance.addAgent(new Vector2D(50.0 + i * 10.0, 1000.0 + j * 10.0));
                goals.add(new Vector2D(1000.0, 50.0));
            }
        }

        // Add (polygonal) obstacles, specifying their vertices in
        // counterclockwise order.
        final List<Vector2D> obstacle1 = new ArrayList<>();
        obstacle1.add(new Vector2D(200.0, 200.0));
        obstacle1.add(new Vector2D(200.0, 300.0));
        obstacle1.add(new Vector2D(300.0, 300.0));
        obstacle1.add(new Vector2D(300.0, 200.0));
        Simulator.instance.addObstacle(obstacle1);

        final List<Vector2D> obstacle2 = new ArrayList<>();
        obstacle2.add(new Vector2D(700.0, 200.0));
        obstacle2.add(new Vector2D(700.0, 300.0));
        obstacle2.add(new Vector2D(800.0, 300.0));
        obstacle2.add(new Vector2D(800.0, 200.0));
        Simulator.instance.addObstacle(obstacle2);

        final List<Vector2D> obstacle3 = new ArrayList<>();
        obstacle3.add(new Vector2D(200.0, 700.0));
        obstacle3.add(new Vector2D(200.0, 800.0));
        obstacle3.add(new Vector2D(300.0, 800.0));
        obstacle3.add(new Vector2D(300.0, 700.0));
        Simulator.instance.addObstacle(obstacle3);

        final List<Vector2D> obstacle4 = new ArrayList<>();
        obstacle4.add(new Vector2D(700.0, 700.0));
        obstacle4.add(new Vector2D(700.0, 800.0));
        obstacle4.add(new Vector2D(800.0, 800.0));
        obstacle4.add(new Vector2D(800.0, 700.0));
        Simulator.instance.addObstacle(obstacle4);

        // Process the obstacles so that they are accounted for in the
        // simulation.
        Simulator.instance.processObstacles();
    }

    @SuppressWarnings("SystemOut")
    public static void updateVisualization() {
        // Output the current global time.
        System.out.print(Simulator.instance.getGlobalTime());

        // Output the current position of all the agents.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            System.out.print(" " + Simulator.instance.getAgentPosition(agentNo));
        }

        System.out.println();
    }

    public void setPreferredVelocities() {
        // Set the preferred velocity to be a vector of unit magnitude (speed)
        // in the direction of the goal.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            Vector2D goalVector =
                    goals.get(agentNo).subtract(Simulator.instance.getAgentPosition(agentNo));
            final double lengthSq = goalVector.getNormSq();

            if (lengthSq > 1.0) {
                goalVector = goalVector.scalarMultiply(1.0 / FastMath.sqrt(lengthSq));
            }

            Simulator.instance.setAgentPreferredVelocity(agentNo, goalVector);

            // Perturb a little to avoid deadlocks due to perfect symmetry.
            final double angle = random.nextDouble() * 2.0 * FastMath.PI;
            final double distance = random.nextDouble() * 0.0001;

            Simulator.instance.setAgentPreferredVelocity(
                    agentNo,
                    Simulator.instance
                            .getAgentPreferredVelocity(agentNo)
                            .add(
                                    new Vector2D(FastMath.cos(angle), FastMath.sin(angle)).scalarMultiply(distance)));
        }
    }

    public boolean reachedGoal() {
        // Check if all agents have reached their goals.
        for (int agentNo = 0; agentNo < Simulator.instance.getNumAgents(); agentNo++) {
            if (Simulator.instance.getAgentPosition(agentNo).distanceSq(goals.get(agentNo)) > 400.0) {
                return false;
            }
        }

        return true;
    }
    /**
    public static void main(String[] args) {
        final Blocks blocks = new Blocks();

        // Set up the scenario.
        blocks.setupScenario();

        // Perform (and manipulate) the simulation.
        do {

            Blocks.updateVisualization();
            blocks.setPreferredVelocities();
            Simulator.instance.doStep();
        } while (!blocks.reachedGoal());
    }
     **/
}