package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.Random;

protected String name;

/**
 * Implement common functions for all Learning Agent algorithms
 */
public abstract class QAgent implements Agent{

    boolean[] getAction();


    /**
     * For use in calculating the Q approximation functions
     * @param environment - from game state interface
     * @return vector of all env state variables - order is consistent but not important
     */
    int[] getStateVec(Environment environment){
        int[] vec = new int[15];

        // state variables
        float[] getMarioFloatPos();
        int getMarioMode();
        float[] getEnemiesFloatPos();
        boolean isMarioOnGround();
        boolean isMarioAbleToJump();
        boolean isMarioCarrying();
        boolean isMarioAbleToShoot();

        // ZlevelScene takes values [0,2] - looks like 0 gives the most detail
        byte[][] getMergedObservationZZ(int ZLevelScene, int ZLevelEnemies);
        byte[][] getLevelSceneObservationZ(int ZLevelScene);
        byte[][] getEnemiesObservationZ(int ZLevelEnemies);
        int[] getSerializedFullObservationZZ(int ZLevelScene, int ZLevelEnemies);
        int[] getSerializedEnemiesObservationZ(int ZLevelEnemies);
        int[] getSerializedMergedObservationZZ(int ZLevelScene, int ZLevelEnemies);
        float[] getCreaturesFloatPos();
        int getKillsTotal();
        int getKillsByFire();
        int getKillsByStomp();
        int getKillsByShell();


        boolean isLevelFinished();
        int[] getEvaluationInfoAsInts();
        int getIntermediateReward();
        int[] getMarioEgoPos();
        int getTimeSpent();

        int marioStatus = marioState[0];
        int marioMode = marioState[1];
        int isMarioOnGround = marioState[2];
        int isMarioAbleToJump = marioState[3];
        int isMarioAbleToShoot = marioState[4];
        int isMarioCarrying = marioState[5];
        int getKillsTotal = marioState[6];
        int getKillsByFire = marioState[7];
        int getKillsByStomp = marioState[8];
        int getKillsByShell = marioState[9];
        int getTimeLeft = marioState[10];
        return(vec);
    }

    void giveIntermediateReward(float intermediateReward);

    /**
     * clears all dynamic data, such as hidden layers in recurrent networks
     * just implement an empty method for a reactive controller
     */
    public void reset();

    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol);

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}