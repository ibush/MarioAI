package cs221.test;

import java.util.ArrayList;

import cs221.QLearningAgent;

/**
 * Created by matt on 10/25/15.
 */
public class TestQAgent {
    public static void main(String[] args) {

        QLearningAgent agent = new QLearningAgent();
        ArrayList<boolean[]> allActions = agent.getPossibleActions(null);
        System.out.println("Printing all possible actions...");
        for(int i=0; i<allActions.size(); i++){
            printBoolArr(allActions.get(i));
        }
    }

    private static void printBoolArr(boolean[] arr){
        for(int i=1; i<arr.length; i++){
            System.out.printf("%b, ",arr[i]);
        }
        System.out.println("");
    }
}
