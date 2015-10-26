package cs221.test;

import java.util.LinkedList;
import cs221.QAgent;
import cs221.QLearningAgent;

/**
 * Created by matt on 10/25/15.
 */
public class TestQAgent {
    public static void main(String[] args) {

        QLearningAgent agent = new QLearningAgent();
        LinkedList<boolean[]> all_actions = agent.getPossibleActions(null);
        System.out.println("Printing all possible actions...");
        for(int i=0; i<all_actions.size(); i++){
            print_bool_arr(all_actions.get(i));
        }
    }

    private static void  print_bool_arr(boolean[] arr){
        for(int i=1; i<arr.length; i++){
            System.out.printf("%b, ",arr[i]);
        }
        System.out.println("");
    }
}
