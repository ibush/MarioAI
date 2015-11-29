package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.HashMap;

/**
 * Created by ibush on 11/15/15.
 */
public class FcLayer implements Layer {

    int layerNum;
    private HashMap<String, Double> hparams;
    HashMap<Integer, double[][]> weightsMap;
    private double[][] weights;
    private double[][] input; //Stored for use in backprop
    private String name;

    public FcLayer(int layerNum, HashMap<String, Double> hparams, HashMap<Integer, double[][]> weightsMap){
        this.layerNum = layerNum;
        this.name = "fc" + Integer.toString(layerNum);
        this.hparams = hparams;
        this.weightsMap = weightsMap;

        weights = weightsMap.get(layerNum);
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
        weightsMap.put(layerNum, weights);
        return dWeights;
    }

    public String getName() {
        return name;
    }

    public double[][] getWeights() {
        return(weights);
    }

}
