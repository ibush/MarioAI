package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

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

    public QAgent(String name){
        this.name = name;
    }

    /**
     * For use in calculating the Q approximation functions
     * @param environment - from game state interface
     * @return vector of all env state variables - order is consistent but not important
     */
    int[] getStateVec(Environment environment){

        int ZLevelScene = 2;
        int ZLevelEnemies = 2;

        // ZlevelScene takes values [0,2] - from most to least detail
        // More info found at http://www.marioai.org/gameplay-track/marioai-benchmark
        int[] vec =  environment.getSerializedFullObservationZZ(ZLevelScene, ZLevelEnemies);

    return(vec);
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

}