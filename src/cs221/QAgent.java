package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

//import java.util.Random;



/**
 * Implement common functions for all Learning Agent algorithms
 */
public abstract class QAgent implements Agent{

    private final static boolean DECREASING_EPSILON_GREEDY = false;
    private final static float STATIC_EPSILON_GREEDY = 0.2f;
    private final static int UPDATE_FREQUENCY = 50; //Number of iterations per epsilon-greedy update
    private final static double MIN_EPSILON_GREEDY = 0.1;

    protected String name;
    //protected boolean[] action = new boolean[Environment.numberOfKeys];// Empty action
    protected int receptiveFieldWidth;
    protected int receptiveFieldHeight;
    protected int marioEgoRow;
    protected int marioEgoCol;

    protected double randomJump;
    private int numUpdates;

    protected HashMap learnedParams;

    public QAgent(String name){
        this.name = name;

        if(DECREASING_EPSILON_GREEDY){
            randomJump = 1;
            numUpdates = 0;
        } else {
            randomJump = STATIC_EPSILON_GREEDY;
        }
    }

    /**
     *
     * @param env
     * @return List of all possible actions from a given state
     */
    public ArrayList<boolean[]> getPossibleActions(Environment env){
        ArrayList<boolean[]> result = new ArrayList<boolean[]>();
        // Currently enumerates all action combinations without filter
        int num = Environment.numberOfKeys;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                for(int k = 0; k < 2; k++){
                    for(int l = 0; l < 2; l++){
                        for(int m = 0; m < 2; m++){
                            for(int n = 0; n < 2; n++){
                                result.add(new boolean[]{i!=0, j!=0, k!=0, l!=0, m!=0, n!=0});
                            }
                        }
                    }
                }
            }
        }
        return(result);
    }

    public void giveIntermediateReward(float intermediateReward){
        // Not implemented
    }

    /**
     * clears all dynamic data, such as hidden layers in recurrent networks
     * just implement an empty method for a reactive controller
     */
    public void reset(){
        //action = new boolean[Environment.numberOfKeys];// Empty action
    }

    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol){
        receptiveFieldWidth = rfWidth;
        receptiveFieldHeight = rfHeight;

        marioEgoRow = egoRow;
        marioEgoCol = egoCol;
    }

    public double getEpsilonGreedy() {
        if(DECREASING_EPSILON_GREEDY) {
            numUpdates++;
            if (numUpdates % UPDATE_FREQUENCY == 0 && randomJump > MIN_EPSILON_GREEDY) {
                randomJump = 1.0 / (Math.sqrt(numUpdates / UPDATE_FREQUENCY));
                //System.out.println("epsilon: " + randomJump);
            }
        }
        return randomJump;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public HashMap getLearnedParams(){
        return this.learnedParams;
    }

    public void setLearnedParams(HashMap learnedParams){
        this.learnedParams = learnedParams;
    }
}