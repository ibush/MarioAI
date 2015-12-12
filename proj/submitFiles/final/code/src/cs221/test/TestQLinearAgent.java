package cs221.test;

import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import cs221.QAgent;
import cs221.QLinearAgent;
import sun.management.resources.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ibush on 11/8/15.
 */
public class TestQLinearAgent {

    public static void main(String[] args) {
        HashMap learnedParams = new HashMap();
        QLinearAgent agent = new QLinearAgent();
        agent.setLearnedParams(learnedParams);

        int[] state = {0, 0};
        float score = 1;
        agent.integrateObservation(state, score);
        learnedParams = agent.getLearnedParams();
        double[] weights = (double[])learnedParams.get("weights");
        System.out.println(Arrays.toString(weights));

        boolean[] action = agent.getAction();
        System.out.println(Arrays.toString(action));
        int ind = 0;
        ArrayList<boolean[]> possibleActions = agent.getPossibleActions(null);
        for(int i = 0; i < possibleActions.size(); i++) {
            if(Arrays.equals(action, possibleActions.get(i))) {
                ind = i;
                break;
            }
        }

        state[0] = 1;
        score = -1;
        agent.integrateObservation(state, score);
        learnedParams = agent.getLearnedParams();
        weights = (double[])learnedParams.get("weights");
        System.out.println(Arrays.toString(weights));
        System.out.println(weights[ind * 2]);
    }
}
