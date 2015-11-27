package cs221;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.agents.Agent;
import cs221.neuralnetwork.Layer;
import cs221.neuralnetwork.LayerFactory;
import cs221.neuralnetwork.LayerSpec;
import cs221.neuralnetwork.NeuralNet;

import java.util.*;

/**
 * Created by matt on 11/8/15.
 */
public class NNAgent extends QAgent implements Agent{

    private final static boolean INDICATOR_REWARDS = true;
    private final static double RANDOM_ACTION_EPSILON = 0.2;
    private final static double STEP_SIZE = 0.01;
    private final static double DISCOUNT = 0.8;

    private final static int Z_LEVEL_SCENE = 2;
    private final static int Z_LEVEL_ENEMIES = 2;

    private NeuralNet net;
    private int numFeatures;

    private Environment environment;
    private float prevFitScore;
    private int[] state;
    private boolean[] action;
    private double bestScore;
    private Random numGenerator = new Random();
    private ArrayList<boolean[]> possibleActions;


    public NNAgent(){
        super("NNAgent");
        learnedParams = new HashMap<String,Double>(); //hparams

        reset();
    }

    public void integrateObservation(Environment environment) {
        // Get observed state vector
        int[] succState = environment.getSerializedFullObservationZZ(Z_LEVEL_SCENE, Z_LEVEL_ENEMIES);
        float currFitScore = (float) environment.getEvaluationInfo().computeBasicFitness();

        integrateObservation(succState, currFitScore);
        this.environment = environment;
    }

    public void integrateObservation(int[] succState, float currFitScore) {
        // Initial values
        if(state == null){
            state = succState;
            action = new boolean[Environment.numberOfKeys];
            prevFitScore = 0;
            bestScore = 0;
            possibleActions = getPossibleActions(environment);

            if(net == null) {
                numFeatures = state.length + possibleActions.size() + 1;
                int numActions = possibleActions.size();
                List<LayerSpec> layerSpecs = new ArrayList<LayerSpec>();
                //Layer 1:
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_FULLY_CONNECTED, numFeatures, numActions));
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_RELU, numActions, 1));
                //Layer 2:
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_FULLY_CONNECTED, numActions, numActions));
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_RELU, numActions, 1));
                //Layer 3:
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_FULLY_CONNECTED, numActions, 1));

                learnedParams.put(Layer.STEP_SIZE, STEP_SIZE);

                net = new NeuralNet(layerSpecs, learnedParams);
            }
        }

        StateActionPair SAP = new StateActionPair(state, action);
        boolean[] succAction = findBestAction(environment, succState);
        float reward = currFitScore - prevFitScore;
        if(INDICATOR_REWARDS) {
            if(reward != 0) reward = reward > 0 ? 1.0f : -1.0f;
        }

        double update = (evalScore(SAP) - reward - DISCOUNT * bestScore); //TODO: Rethink this
        net.backprop(update);

        // Update Persistent Parameters
        state = succState;
        action = succAction;
        prevFitScore = currFitScore;
    }

    public boolean[] getAction() {

        // Take random action with some probability
        if(numGenerator.nextFloat() < RANDOM_ACTION_EPSILON){
            int randIndex = numGenerator.nextInt(possibleActions.size());
            action = possibleActions.get(randIndex);
        }
        // Otherwise return best action (calculated in integrateObservation)
        return action;
    }

    private double[][] extractFeatures(StateActionPair sap){
        // Feature extractor
        int[] state = sap.getState();
        double[][] features = new double[1][numFeatures]; //TODO: Just make all the NN stuff vectors?
        int ind = 0;
        for (int i = 0; i < state.length; i++) {
            features[0][ind] = (state[i] == 0) ? 0.0 : 1.0;
            ind++;
        }
        for(int i = 0; i < possibleActions.size(); i ++) {
            if(Arrays.equals(possibleActions.get(i), sap.getAction())) {
                features[0][ind] = 1.0;
                break;
            }
            ind++;
        }
        // Bias term
        features[0][numFeatures - 1] = 1.0;
        return(features);
    }

    //TODO: extractFeatures once in integrateObservation and store rather than doing it twice per round?
    private double evalScore(StateActionPair sap){
        double[][] features = extractFeatures(sap);
        return net.forward(features);
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

}
