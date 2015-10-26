package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;


/**
 * Q Learning agent with Linear Approximation function
 */
public class QLearningAgent extends QAgent implements Agent{

    private float step_size;
    private float discount;
    //private HashMap<int[], Float> mapping = new HashMap<int[], Float>();
    private float randomJump;
    private Environment prevEnv;
    private float prevFitScore;
    private int[] prevState;
    private boolean[] prevAction;
    private Random numGenerator = new Random();

    public QLearningAgent()
    {
        super("QLearningAgent");
        randomJump = (float) 0.2;
        step_size = (float) 0.1;
        discount = (float) 1.0;
        learnedParams = new HashMap<StateActionPair,Float>();
        reset();
    }


    public boolean[] getAction() {
        LinkedList<boolean[]> all_actions = getPossibleActions(prevEnv);

        // Take random action with some probability
        if(numGenerator.nextFloat() < randomJump){
            int randIndex = numGenerator.nextInt(all_actions.size());
            return(all_actions.get(randIndex));
        }

        // Calculate score for all possible next actions
        float[] qscores = new float[all_actions.size()];
        for (int i = 0; i < all_actions.size(); i++) {
            boolean[] action = all_actions.get(i);
            StateActionPair sap = new StateActionPair(prevState, action);
            qscores[i] = eval_score(sap);
        }

        // Find ArgMax over all actions using calculated scores
        boolean[] bestAction = all_actions.get(0);
        float bestScore = qscores[0];
        for(int i = 1; i < all_actions.size(); i++){
            if(qscores[i] > bestScore){
                bestScore = qscores[i];
                bestAction = all_actions.get(i);
            }
        }
        return(bestAction);
    }

    private float eval_score(StateActionPair sap){
        float score;
        if (learnedParams.containsKey(sap)) {
            score = (Float) learnedParams.get(sap);
        } else {
            score = 0;
        }
        return(score);
    }

    public void integrateObservation(Environment environment) {

        // Get observed state vector
        int ZLevelScene = 2;
        int ZLevelEnemies = 2;
        int[] currState =  environment.getSerializedFullObservationZZ(ZLevelScene, ZLevelEnemies);
        float currFitScore = (float) environment.getEvaluationInfo().computeBasicFitness();

        // Initial values
        if(prevState == null){
            prevState = currState;
            prevAction = new boolean[Environment.numberOfKeys];
            prevFitScore = currFitScore;
            prevEnv = environment;
        }

        // Update Learning Parameters
        boolean[] currAction = getAction();
        StateActionPair currSAP = new StateActionPair(currState, currAction);
        StateActionPair prevSAP = new StateActionPair(prevState, prevAction);
        float prevReward = currFitScore - prevFitScore;
        Float newScore = (1 - step_size) * eval_score(prevSAP) + step_size * (prevReward + discount * eval_score(currSAP));
        learnedParams.put(prevSAP, newScore);

        // Update Persistent Parameters
        prevState = currState;
        prevAction = currAction;
        prevFitScore = currFitScore;
        prevEnv = environment;
    }

    private int[] boolToInt(boolean[] arr){
        int[] result = new int[arr.length];
        for(int i=0;i<arr.length;i++){
            result[i] = arr[i] ? 1 : 0;
        }
        return(result);
    }
}
