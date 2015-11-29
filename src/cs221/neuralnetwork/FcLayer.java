package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.HashMap;

/**
 * Created by ibush on 11/15/15.
 */
public class FcLayer implements Layer {

    private static final double RANDOM_WEIGHT_MAX = 0.01;
    private HashMap<String, Double> hparams;
    private double[][] weights;
    private double[][] input; //Stored for use in backprop
    private String name;

    public FcLayer(String name, HashMap<String, Double> hparams, int inputSize, int outputSize){
        this.name = name;
        this.hparams = hparams;
        double[][] rand = Matrix.rand(inputSize, outputSize);
        //TODO: Need to write the weights out to file for future runs
        weights = Matrix.scalarMult(rand, RANDOM_WEIGHT_MAX); //TODO: Could just have rand call above return in this range for efficiency
    }

    public double[][] forward(double[][] input){
        this.input = input; //Store for use in backprop
        return Matrix.multiply(input, weights); //TODO: Just dot product? (input vector?)
    }

    public double[][] backprop(double[][] doutput){
        double stepSize = hparams.get(STEP_SIZE);
        double[][] dWeights = Matrix.multiply(Matrix.transpose(input), doutput);
        double[][] change = Matrix.scalarMult(dWeights, -stepSize);
        
        weights = Matrix.add(weights, change);
        return dWeights;
    }

    public String getName() {
        return name;
    }

    public double[][] getWeights() {
        return(weights);
    }

}
