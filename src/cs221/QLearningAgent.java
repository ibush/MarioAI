package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.security.KeyPair;
import java.util.HashMap;
import cs221.QAgent;


/**
 * Q Learning agent with Linear Approximation function
 */
public class QLearningAgent extends QAgent implements Agent{

    private float step_size;
    private HashMap<int[], Float> mapping = new HashMap<int[], Float>();
    private float randomJump;
    private int[] prevState;
    private boolean[] prevAction;


    public QLearningAgent()
    {
        super("QLearningAgent");
        randomJump = (float) 0.5;
        reset();
    }


    public boolean[] getAction() {
        boolean[] action = new boolean[0];

        this.prevAction = action;
        return action;
    }

    public void integrateObservation(Environment environment) {

        // Get observed state vector
        int ZLevelScene = 2;
        int ZLevelEnemies = 2;
        int[] vec =  environment.getSerializedFullObservationZZ(ZLevelScene, ZLevelEnemies);


        // Initial values
        if(prevState == null){
            prevState = vec;
            prevAction = new boolean[0];
        }

        // Update parameters
        int r = environment.getEvaluationInfo().computeBasicFitness();
        //int[] stateActionPair = new int[prevState.length + prevAction.length];
        //System.arraycopy(prevState,0,stateActionPair, 0, prevState.length);
        //System.arraycopy(boolToIntnt(prevAction), 0, stateActionPair, prevState.length, prevAction.length);
        //this.learnedParams.put(stateActionPair, r)

    }

    public int[] boolToInt(boolean[] arr){
        int[] result = new int[arr.length];
        for(int i=0;i<arr.length;i++){
            result[i] = arr[i] ? 1 : 0;
        }
        return(result);
    }

    private float evalState(int[] stateVec){
        return 0
    }
}
