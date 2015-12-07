package cs221;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import cs221.neuralnetwork.Iteration;

import java.util.ArrayList;
import java.util.HashMap;

//import java.util.Random;



/**
 * Implement common functions for all Learning Agent algorithms
 */
public abstract class QAgent implements Agent{

    protected String name;
    //protected boolean[] action = new boolean[Environment.numberOfKeys];// Empty action
    protected int receptiveFieldWidth;
    protected int receptiveFieldHeight;
    protected int marioEgoRow;
    protected int marioEgoCol;

    protected double randomJump;
    private Iteration iter;

    protected HashMap learnedParams;

    public QAgent(String name){
        this.name = name;
    }

    /**
     *
     * @param env
     * @return List of all possible actions from a given state
     */
    public ArrayList<boolean[]> getPossibleActions(Environment env){
        ArrayList<boolean[]> result = new ArrayList<boolean[]>();

/* Comment in for hand-picked actions
        addAction(result, new int[]{Mario.KEY_LEFT});
        addAction(result, new int[]{Mario.KEY_RIGHT});
        addAction(result, new int[]{Mario.KEY_JUMP});
        addAction(result, new int[]{Mario.KEY_SPEED});

        addAction(result, new int[]{Mario.KEY_RIGHT, Mario.KEY_JUMP});
        addAction(result, new int[]{Mario.KEY_RIGHT, Mario.KEY_SPEED});
        addAction(result, new int[]{Mario.KEY_RIGHT, Mario.KEY_JUMP, Mario.KEY_SPEED});
*/

        //All combinations of Left, Right, Jump, Speed (except simultaneous left & right)
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                if(i != 0 && j!= 0) continue; //Can't go left & right
                for(int l = 0; l < 2; l++){
                    for(int m = 0; m < 2; m++){
                        result.add(new boolean[]{i != 0, j != 0, false, l != 0, m != 0, false}); //No up or down keys
                    }
                }
            }
        }

        return(result);
    }

    private void addAction(ArrayList<boolean[]> actions, int[] indices) {
        boolean[] action = new boolean[6];
        for(int index : indices) {
            action[index] = true;
        }
        actions.add(action);
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
        if(GlobalOptions.decreasingEpsilonGreedy) {
            iter.value++;
            if (iter.value % GlobalOptions.iterationsPerEpsUpdate == 0 && randomJump > GlobalOptions.minEpsilonGreedy) {
                randomJump = 1.0 / (Math.sqrt(iter.value / GlobalOptions.iterationsPerEpsUpdate));
                if(randomJump < GlobalOptions.minEpsilonGreedy) randomJump = GlobalOptions.minEpsilonGreedy;
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

        if(GlobalOptions.decreasingEpsilonGreedy){
            if(learnedParams.containsKey("iter")) {
                iter = (Iteration) learnedParams.get("iter");
                randomJump = 1.0 / (Math.sqrt(iter.value / GlobalOptions.iterationsPerEpsUpdate));
                if(randomJump < GlobalOptions.minEpsilonGreedy) randomJump = GlobalOptions.minEpsilonGreedy;
            } else {
                iter = new Iteration(0);
                learnedParams.put("iter", iter);
                randomJump = 1.0;
            }
        } else {
            randomJump = GlobalOptions.staticEpsilonGreedy;
        }
    }
}