package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by matt on 11/1/15.
 */
public class QLinearAgent extends QAgent implements Agent {

    private final static boolean INDICATOR_REWARDS = true;

    private final static float REGULARIZATION_LAMDA = 0.01f;
    private final static float RANDOM_ACTION_EPSILON = (float) 0.2;
    private final static float STEP_SIZE = (float) 0.1;
    private final static float DISCOUNT = (float) 1.0;
    private final static String WEIGHTS_KEY = "weights";

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
    private double bestScore;
    private Random numGenerator = new Random();
    private ArrayList<boolean[]> possibleActions;


    public QLinearAgent()
    {
        super("QLinearAgent");
        randomJump = RANDOM_ACTION_EPSILON;
        stepSize = STEP_SIZE;
        discount = DISCOUNT;
        learnedParams = new HashMap<String,Float[]>();

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
            bestScore = 0;
            possibleActions = getPossibleActions(environment);
            double[] weights = new double[state.length * possibleActions.size() + 1];
            learnedParams.put(WEIGHTS_KEY, weights);
        }

        StateActionPair SAP = new StateActionPair(state, action);
        boolean[] succAction = findBestAction(environment, succState);
        float reward = currFitScore - prevFitScore;
        if(INDICATOR_REWARDS) {
            reward = reward > 0 ? 1.0f : -1.0f;
        }

        // Update Weights
        double[] weights = (double[])learnedParams.get(WEIGHTS_KEY);
        double update = stepSize * (evalScore(SAP) - reward - discount * bestScore);
        double[] chg = Matrix.scalarMult(extractFeatures(SAP), update);
        double[] newWeights = Matrix.subtract(weights, chg);
        double[] regularization = Matrix.scalarMult(weights, REGULARIZATION_LAMDA);
        newWeights = Matrix.subtract(newWeights,regularization);
        learnedParams.put(WEIGHTS_KEY, newWeights);
        //System.out.println(Arrays.toString(newWeights));


        // Update Persistent Parameters
        state = succState;
        action = succAction;
        prevFitScore = currFitScore;
        this.environment = environment;
    }

    public boolean[] getAction() {

        // Take random action with some probability
        if(numGenerator.nextFloat() < randomJump){
            int randIndex = numGenerator.nextInt(possibleActions.size());
            action = possibleActions.get(randIndex);
        }
        // Otherwise return best action (calculated in integrateObservation)
        return action;
    }

    private double[] extractFeatures(StateActionPair sap){
        // Feature extractor
        int[] state = sap.getState();
        double[] weights = (double[]) learnedParams.get(WEIGHTS_KEY);
        double[] features = new double[weights.length];
        int ind = 0;
        for(int i = 0; i < possibleActions.size(); i ++) {
            for (int j = 0; j < state.length; j++) {
                if(possibleActions.get(i) == sap.getAction()) {
                        // State action interaction terms
                        features[ind] = (state[j] == 0) ? 0.0 : 1.0;
                        ind++;
                }
            }
        }
        // Bias term
        features[ind] = 1;
        assert ind == features.length - 1;
        return(features);
    }

    //TODO: extractFeatures once in integrateObservation and store rather than doing it twice per round?
    private double evalScore(StateActionPair sap){
        double[] features = extractFeatures(sap);
        double[] weights = (double[]) learnedParams.get(WEIGHTS_KEY);
        double score = Matrix.dotProduct(weights, features);

        if(score == Double.POSITIVE_INFINITY) System.out.println("ERROR");
        return(score);
    }

    private boolean[] findBestAction(Environment env, int[] state) {
        ArrayList<boolean[]> allActions = getPossibleActions(env);

        // Calculate score for all possible next actions
        double[] qscores = new double[allActions.size()];
        for (int i = 0; i < allActions.size(); i++) {
            boolean[] action = allActions.get(i);
            StateActionPair sap = new StateActionPair(state, action);
            qscores[i] = evalScore(sap);
        }

        // Find ArgMax over all actions using calculated scores
        int ind = numGenerator.nextInt(allActions.size());
        boolean[] bestAction = allActions.get(ind);
        bestScore = qscores[ind];
        for(int i = 1; i < allActions.size(); i++){
            if(qscores[i] > bestScore){
                bestScore = qscores[i];
                bestAction = allActions.get(i);
            }
        }
        //System.out.println(bestScore);
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
