package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.HashMap;
import java.util.LinkedList;

//import java.util.Random;



/**
 * Implement common functions for all Learning Agent algorithms
 */
public abstract class QAgent implements Agent{

    protected String name;
    protected boolean[] action = new boolean[Environment.numberOfKeys];// Empty action
    protected int receptiveFieldWidth;
    protected int receptiveFieldHeight;
    protected int marioEgoRow;
    protected int marioEgoCol;

    protected HashMap learnedParams;

    public QAgent(String name){
        this.name = name;
    }

    /**
     *
     * @param env
     * @return List of all possible actions from a given state
     */
    public LinkedList<boolean[]> getPossibleActions(Environment env){
        LinkedList<boolean[]> result = new LinkedList<boolean[]>();
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
        action = new boolean[Environment.numberOfKeys];// Empty action
    }

    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol){
        receptiveFieldWidth = rfWidth;
        receptiveFieldHeight = rfHeight;

        marioEgoRow = egoRow;
        marioEgoCol = egoCol;
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