package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Q Learning agent with Linear Approximation function
 */
public class QLearningAgent extends QAgent implements Agent{

    private final static float RANDOM_ACTION_EPSILON = (float) 0.2;
    private final static float STEP_SIZE = (float) 0.1;
    private final static float DISCOUNT = (float) 1.0;

    private final static int Z_LEVEL_SCENE = 2;
    private final static int Z_LEVEL_ENEMIES = 2;

    private float stepSize;
    private float discount;
    //private HashMap<int[], Float> mapping = new HashMap<int[], Float>();
    private float randomJump;
    private Environment environment;
    private float prevFitScore;
    private int[] state;
    private boolean[] action;
    private Random numGenerator = new Random();

    public QLearningAgent()
    {
        super("QLearningAgent");
        randomJump = RANDOM_ACTION_EPSILON;
        stepSize = STEP_SIZE;
        discount = DISCOUNT;
        learnedParams = new HashMap<StateActionPair,Float>();
        reset();
    }

    public void integrateObservation(Environment environment) {

        // Get observed state vector
        int[] succState =  environment.getSerializedFullObservationZZ(Z_LEVEL_SCENE, Z_LEVEL_ENEMIES);
        float currFitScore = (float) environment.getEvaluationInfo().computeBasicFitness();

        // Initial values
        if(state == null){
            state = succState;
            action = new boolean[Environment.numberOfKeys];
            prevFitScore = 0;
        }

        // Update Learning Parameters
        StateActionPair SAP = new StateActionPair(state, action);
        boolean[] succAction = findBestAction(environment, succState);
        StateActionPair succSAP = new StateActionPair(succState, succAction);
        float reward = currFitScore - prevFitScore;

        Float newScore = (1 - stepSize) * evalScore(SAP) + stepSize * (reward + discount * evalScore(succSAP));
        learnedParams.put(SAP, newScore);

        // Update Persistent Parameters
        state = succState;
        action = succAction;
        prevFitScore = currFitScore;
        this.environment = environment;
    }

    public boolean[] getAction() {

        // Take random action with some probability
        if(numGenerator.nextFloat() < randomJump){
            ArrayList<boolean[]> allActions = getPossibleActions(environment);
            int randIndex = numGenerator.nextInt(allActions.size());
            action = allActions.get(randIndex);
        }
        // Otherwise return best action (calculated in integrateObservation)
        return action;
    }

    private float evalScore(StateActionPair sap){
        float score;
        if (learnedParams.containsKey(sap)) {
            score = (Float) learnedParams.get(sap);
        } else {
            score = 0;
        }
        return(score);
    }

    private boolean[] findBestAction(Environment env, int[] state) {
        ArrayList<boolean[]> allActions = getPossibleActions(env);

        // Calculate score for all possible next actions
        float[] qscores = new float[allActions.size()];
        for (int i = 0; i < allActions.size(); i++) {
            boolean[] action = allActions.get(i);
            StateActionPair sap = new StateActionPair(state, action);
            qscores[i] = evalScore(sap);
        }

        // Find ArgMax over all actions using calculated scores
        boolean[] bestAction = allActions.get(0);
        float bestScore = qscores[0];
        for(int i = 1; i < allActions.size(); i++){
            if(qscores[i] > bestScore){
                bestScore = qscores[i];
                bestAction = allActions.get(i);
            }
        }
        return(bestAction);
    }

    private int[] boolToInt(boolean[] arr){
        int[] result = new int[arr.length];
        for(int i=0;i<arr.length;i++){
            result[i] = arr[i] ? 1 : 0;
        }
        return(result);
    }
}
