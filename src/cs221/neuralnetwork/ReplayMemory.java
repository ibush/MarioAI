package cs221.neuralnetwork;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

public class ReplayMemory implements Serializable {

    static final boolean RANDOM_STORAGE = false;

    Random randgen;
    double[][] trainX;
    double[] trainy;
    int index;
    int capacity;
    boolean full;

    public ReplayMemory(int memSize){
        System.out.println("Initializing Replay Memory");
        trainX = new double[memSize][];
        trainy = new double[memSize];
        index = 0;
        capacity = memSize;
        randgen = new Random();
        full = false;
    }

    // Adds feature vector/truth pair to memory
    // If memory is at capacity, replaces random example with new one
    public void addMemory(double[] featureVec, double truth){
        if(index < capacity){
            trainX[index] = featureVec;
            trainy[index] = truth;
            index++;
        }else{
            full = true;
            int replaceInd = 0;
            if(RANDOM_STORAGE) {
                replaceInd = randgen.nextInt(capacity);
            }else{
                index = 0;
            }
            trainX[replaceInd] = featureVec;
            trainy[replaceInd] = truth;
        }
    }

    public List<double[][]> sample(int batchSize){
        double[][] examples = new double[batchSize][];
        double[][] targets = new double[batchSize][1];
        for(int i=0; i<batchSize; i++){
            int randind = full ? randgen.nextInt(capacity) : randgen.nextInt(index);
            examples[i] = trainX[randind];
            targets[i][0] = trainy[randind];
        }
        List result = new LinkedList<double[][]>();
        result.add(examples);
        result.add(targets);
        return result;
    }
}
