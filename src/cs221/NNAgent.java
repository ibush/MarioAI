package cs221;

import ch.idsia.benchmark.mario.environments.Environment;
import cs221.neuralnetwork.LayerSpec;
import cs221.neuralnetwork.NeuralNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matt on 11/8/15.
 */
public class NNAgent extends QAgent implements QLearningAgent{

    private NeuralNet net;

    public NNAgent(){
        super("QLinearAgent");
        randomJump = RANDOM_ACTION_EPSILON;
        stepSize = STEP_SIZE;
        discount = DISCOUNT;
        learnedParams = new HashMap<String,Float[]>();

        // Network hyperparameters
        HashMap<String,Double> hparams = new HashMap<String, Double>();
        List<LayerSpec> layers= new List<LayerSpec>();

        hparams.put("lr", 0.01);
        hparams.put("reg", 0.005);
        hparams.put("numOut", 1.0);

        LayerSpec l1 = new LayerSpec("fullyConnected");
        LayerSpec l2 = new LayerSpec("fullyConnected");

        layers.add(l1);
        layers.add(l2);

        net = new NeuralNet(layers, hparams);

        reset();
    }

    public void integrateObservation(Environment environment) {
        // Get observed state vector
        int[] succState = environment.getSerializedFullObservationZZ(Z_LEVEL_SCENE, Z_LEVEL_ENEMIES);
        float currFitScore = (float) environment.getEvaluationInfo().computeBasicFitness();

        integrateObservation(succState, currFitScore);
    }

    public void integrateObservation(int[] succState, float currFitScore) {
        // Initial values
        if(state == null){
            state = succState;
            action = new boolean[Environment.numberOfKeys];
            prevFitScore = 0;
            bestScore = 0;
            possibleActions = getPossibleActions(environment);
            //double[] weights = new double[state.length * possibleActions.size() + 1];
            //learnedParams.put(WEIGHTS_KEY, weights);
        }

        StateActionPair SAP = new StateActionPair(state, action);
        boolean[] succAction = findBestAction(environment, succState);
        float reward = currFitScore - prevFitScore;
        if(INDICATOR_REWARDS) {
            if(reward != 0) reward = reward > 0 ? 1.0f : -1.0f;
        }

        // Update Weights
        double[] weights = (double[])learnedParams.get(WEIGHTS_KEY);

        double update = stepSize * (evalScore(SAP) - reward - discount * bestScore);
        double[] chg = Matrix.scalarMult(extractFeatures(SAP), update);
        double[] newWeights = Matrix.subtract(weights, chg);
        if(REGULARIZATION_LAMDA != 0) {
            double[] regularization = Matrix.scalarMult(weights, REGULARIZATION_LAMDA);
            newWeights = Matrix.subtract(newWeights,regularization);
        }


        if(VERBOSE) {
            StringBuilder sb = new StringBuilder();
            sb.append(" Q Pred : ").append(evalScore(SAP));
            sb.append(" reward : ").append(reward);
            sb.append(" Vopt : ").append(bestScore);
            sb.append(" DVopt : ").append(discount * bestScore);
            sb.append(" Update : ").append(update);
            double temp = 0;
            for (int i = 0; i < weights.length; i++) {
                temp = temp + weights[i];
            }
            sb.append(" SumWeight : ").append(temp);
            System.out.println(sb.toString());
        }


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
                if(Arrays.equals(possibleActions.get(i), sap.getAction())) {
                    // State action interaction terms
                    features[ind] = (state[j] == 0) ? 0.0 : 1.0;
                }
                ind++;
            }
        }
        // Bias term
        features[ind] = 1;
        return(features);
    }

    //TODO: extractFeatures once in integrateObservation and store rather than doing it twice per round?
    private double evalScore(StateActionPair sap){
        double[] features = extractFeatures(sap);
        double[] weights = (double[]) learnedParams.get(WEIGHTS_KEY);
        double score = Matrix.dotProduct(weights, features);

        //System.out.println("score: " + score);
        if(score == Double.POSITIVE_INFINITY || score == Double.NEGATIVE_INFINITY) System.out.println("ERROR: Score overflow");
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



}
