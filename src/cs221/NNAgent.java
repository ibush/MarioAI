package cs221;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.agents.Agent;
import cs221.neuralnetwork.*;

import java.util.*;

/**
 * Created by matt on 11/8/15.
 */
public class NNAgent extends QAgent implements Agent{

    // Simulation Parameters
    private final static int Z_LEVEL_SCENE = 2;
    private final static int Z_LEVEL_ENEMIES = 2;

    // Neural Network Parameters
    private final static int STAT_INTERVAL = 20;
    private final static int UPDATE_INTERVAL = 20;
    private static double LR = GlobalOptions.stepSize / GlobalOptions.batchSize;
    private final static int DECAY_STEP = 20000;

    private final static boolean TEST_TIME = false; // no learning at test time

    private int numFeatures;
    protected HashMap hparams;

    private Environment environment;
    private float prevFitScore;
    private int[] state;
    private boolean[] action;
    private double bestScore;
    private Random numGenerator = new Random();
    private ArrayList<boolean[]> possibleActions;

    private NeuralNet net;
    private Stats stats;

    // Persistent Objects
    private ReplayMemory rm;
    private Iteration iter;
    private HashMap<String, double[][]> weights;

    public NNAgent(){
        super("NNAgent");
        hparams = new HashMap<String,Double>();
        learnedParams = new HashMap<Integer,Object>();
        stats = new Stats(name);

        reset();
    }

    // Unpack persistent objects
    public void setLearnedParams(HashMap learnedParams){
        this.learnedParams = learnedParams;
    }

    public void integrateObservation(Environment environment) {
        // Get observed state vector
        int[] succState = environment.getSerializedFullObservationZZ(Z_LEVEL_SCENE, Z_LEVEL_ENEMIES);
        float currFitScore = (float) environment.getEvaluationInfo().computeBasicFitness();

        integrateObservation(succState, currFitScore);
        this.environment = environment;
    }

    public void integrateObservation(int[] succState, float currFitScore) {
        // If this is the first observation of the round
        if(state == null){
            state = succState;
            action = new boolean[Environment.numberOfKeys];
            prevFitScore = 0;
            bestScore = 0;
            possibleActions = getPossibleActions(environment);


            // Unpack Values
            if(learnedParams.containsKey("weights")){
                iter = (Iteration)learnedParams.get("iter");
                rm = (ReplayMemory)learnedParams.get("rm");
                weights = (HashMap<String,double[][]>)learnedParams.get("weights");
                System.out.println("Starting Simulation at iteration : " + Integer.toString(iter.value));
            }else{
                // If this is the first observation of the simulation/trials
                rm = new ReplayMemory(GlobalOptions.replaySize);
                weights = new HashMap<String, double[][]>();
                iter = new Iteration(1);
                learnedParams.put("weights", weights);
                learnedParams.put("rm",rm);
                learnedParams.put("iter",iter);
            }

            if(net == null) {
                numFeatures = state.length + possibleActions.size() + 1;
                int numActions = possibleActions.size();


                // Network Architecture
                List<LayerSpec> layerSpecs = new ArrayList<LayerSpec>();
                //Layer 1:
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_FULLY_CONNECTED, numFeatures, GlobalOptions.h1Size));
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_RELU, GlobalOptions.batchSize, GlobalOptions.h1Size));
                //Layer 2:
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_FULLY_CONNECTED, GlobalOptions.h1Size, GlobalOptions.h2Size));
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_RELU, GlobalOptions.batchSize, GlobalOptions.h2Size));
                //Layer 3:
                layerSpecs.add(new LayerSpec(LayerFactory.TYPE_FULLY_CONNECTED, GlobalOptions.h2Size, 1));

                net = new NeuralNet(layerSpecs, weights);

            }
        }

        // state and action denote (s,a) while succState and succAction denote (s'a')
        // Reward denotes r
        StateActionPair SAP = new StateActionPair(state, action);
        boolean[] succAction = findBestAction(environment, succState);
        StateActionPair succSAP = new StateActionPair(succState, succAction);
        double succBestScore = evalScore(succSAP);

        float reward = currFitScore - prevFitScore;
        if(GlobalOptions.useIndicatorRewards) {
            if(reward != 0) reward = reward > 0 ? 1.0f : -1.0f;
        }


        double trueScore = reward + GlobalOptions.dicount * succBestScore;
        rm.addMemory(extractFeatures(SAP)[0], trueScore);

        // Annealed learning rate and epsilon greedy
        if(iter.value % DECAY_STEP == 0 && !TEST_TIME){
            LR = LR * GlobalOptions.decayFactor;
            //RANDOM_ACTION_EPSILON = RANDOM_ACTION_EPSILON * DECAY_FACTOR;
            System.out.println("Decay Step - LR : " + Double.toString(LR)
                    + " Epsilon : " + Double.toString(randomJump));
        }


            // only do this update on every n-th iteration
        if(iter.value % UPDATE_INTERVAL == 0 && !TEST_TIME){
            List<double[][]> batch = rm.sample(GlobalOptions.batchSize);
            double[][] trainX = batch.get(0);
            double[][] trainy = batch.get(1);
            double[][] pred = net.forward(trainX);
            double[][] trainError = Matrix.subtract(pred, trainy);
            double regError = 0.5 * GlobalOptions.regularizationLamda * net.getWeightSq();
            trainError = Matrix.scalarAdd(trainError, regError);
            net.backprop(trainError, LR, GlobalOptions.regularizationLamda);
        }

        if(iter.value % STAT_INTERVAL == 0 && !TEST_TIME){
            // Print learning statistics - on every nth iteration
            double error = (evalScore(SAP) - trueScore);
            stats.addError(error);
            stats.addWeights(net);
            stats.addLearningRate(LR);
            stats.addEpsilonGreedy(randomJump);
            stats.flush();
        }

        // Update Persistent Parameters
        iter.value++;
        state = succState;
        action = succAction;
        prevFitScore = currFitScore;
    }

    public boolean[] getAction() {

        // Take random action with some probability
        if(numGenerator.nextFloat() < getEpsilonGreedy()){
            int randIndex = numGenerator.nextInt(possibleActions.size());
            action = possibleActions.get(randIndex);
        }
        // Otherwise return best action (calculated in integrateObservation)
        return action;
    }

    private double[][] extractFeatures(StateActionPair sap){
        // Feature extractor
        int[] state = sap.getState();
        double[][] features = new double[1][numFeatures];
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
        return net.forward(features)[0][0];
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
