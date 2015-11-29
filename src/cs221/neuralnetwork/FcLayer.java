package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.HashMap;

/**
 * Created by ibush on 11/15/15.
 */
public class FcLayer implements Layer {

    int layerNum;
    HashMap<String, double[][]> weightsMap;
    private double[][] weights;
    private double[][] input; //Stored for use in backprop
    private String name;

    public FcLayer(int layerNum, HashMap<String, double[][]> weightsMap, int inputSize, int outputSize){
        this.layerNum = layerNum;
        this.name = "fc" + Integer.toString(layerNum);
        this.weightsMap = weightsMap;

        if(!weightsMap.containsKey(name)){
            weightsMap.put(name, initWeights(inputSize, outputSize));
        }

        weights = weightsMap.get(name);
    }

    // Normalized weight initialization
    private double[][] initWeights(int inputSize, int outputSize){
        System.out.println("Generating new weights for : " + name);
        double stddev =Math.sqrt(2.0/inputSize);
        return(Matrix.norm(inputSize, outputSize, stddev));
    }


    public double[][] forward(double[][] input){
        this.input = input; //Store for use in backprop
        return Matrix.multiply(input, weights);
    }

    public double[][] backprop(double[][] doutput, double lr){
        double[][] dWeights = Matrix.multiply(Matrix.transpose(input), doutput);
        double[][] dInput = Matrix.multiply(doutput,Matrix.transpose(weights));

        // Update Weights
        double[][] change = Matrix.scalarMult(dWeights, -lr);
        weights = Matrix.add(weights, change);
        weightsMap.put(name, weights);

        return dInput;
    }

    public String getName() {
        return name;
    }

    public double[][] getWeights() {
        return(weights);
    }

}
