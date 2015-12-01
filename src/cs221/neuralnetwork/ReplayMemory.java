package cs221.neuralnetwork;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

public class ReplayMemory implements Serializable {

    static final boolean RANDOM_STORAGE = false;

    Random randgen;
    ArrayList<double[]> trainX;
    ArrayList<Double> trainy;
    int index;
    int capacity;

    public ReplayMemory(int memSize){
        trainX = new ArrayList<double[]>(memSize);
        trainy = new ArrayList<Double>(memSize);
        index = 0;
        capacity = memSize;
        randgen = new Random();
    }

    // Adds feature vector/truth pair to memory
    // If memory is at capacity, replaces random example with new one
    public void addMemory(double[] featureVec, double truth){
        if(index < capacity){
            trainX.add(index, featureVec);
            trainy.add(index, truth);
            index++;
        }else{
            int replaceInd = 0;
            if(RANDOM_STORAGE) {
                replaceInd = randgen.nextInt(capacity);
            }else{
                index = 0;
            }
            trainX.add(replaceInd, featureVec);
            trainy.add(replaceInd, truth);
        }
    }

    public List<double[][]> sample(int batchSize){
        double[][] examples = new double[batchSize][];
        double[][] targets = new double[batchSize][1];
        for(int i=0; i<batchSize; i++){
            int randind = randgen.nextInt(index);
            examples[i] = trainX.get(randind);
            targets[i][0] = trainy.get(randind);
        }
        List result = new LinkedList<double[][]>();
        result.add(examples);
        result.add(targets);
        return result;
    }
}
