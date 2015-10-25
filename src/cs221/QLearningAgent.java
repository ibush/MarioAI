package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import cs221.QAgent;


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
