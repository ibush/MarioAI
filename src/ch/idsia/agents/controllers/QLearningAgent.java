package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.Random;


/**
 * Q Learning agent with Linear Approximation function
 */
public class QLearningAgent extends QAgent implements Agent{

    public QLearningAgent()
    {
        super("QLearningAgent");
        reset();
    }


    @java.lang.Override
    public boolean[] getAction() {
        return new boolean[0];
    }

    @java.lang.Override
    public void integrateObservation(Environment environment) {
        int ZLevelScene = 2;
        int ZLevelEnemies = 2;

        int[] vec =  environment.getSerializedFullObservationZZ(ZLevelScene, ZLevelEnemies);
    }
}
