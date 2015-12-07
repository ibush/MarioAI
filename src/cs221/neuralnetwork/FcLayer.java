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
    private double[][] biases;
    private double[][] input; //Stored for use in backprop
    private String name;

    public FcLayer(int layerNum, HashMap<String, double[][]> weightsMap, int inputSize, int outputSize){
        this.layerNum = layerNum;
        this.name = "fc" + Integer.toString(layerNum);
        this.weightsMap = weightsMap;

        if(!weightsMap.containsKey(name + "_w")){
            weightsMap.put(name + "_w", initWeights(inputSize, outputSize));
            weightsMap.put(name + "_b", initBiases(outputSize));
        }

        weights = weightsMap.get(name + "_w");
        biases = weightsMap.get(name + "_b");
    }

    // Normalized weight initialization
    private double[][] initWeights(int inputSize, int outputSize){
        System.out.println("Generating new weights for : " + name);
        double stddev =Math.sqrt(2.0/inputSize);
        return(Matrix.norm(inputSize, outputSize, stddev));
    }

    private double[][] initBiases(int outputSize){
        System.out.println("Generating new biases for : " + name);
        return(Matrix.zeros(outputSize, 1));
    }


    public double[][] forward(double[][] input){
        this.input = input; //Store for use in backprop
        double[][] result = Matrix.multiply(input, weights);
        for(int i = 0; i < result.length; i++){
            for(int j = 0; j < result[0].length; j++){
                result[i][j] = result[i][j] + biases[j][0];
            }
        }
        return result;
    }

    public double[][] backprop(double[][] doutput, double lr, double reg){
        double[][] dWeights = Matrix.multiply(Matrix.transpose(input), doutput);
        double[][] dreg = Matrix.scalarMult(weights, reg);
        dWeights = Matrix.add(dWeights, dreg);
        double[][] dInput = Matrix.multiply(doutput,Matrix.transpose(weights));


        // Update Weights
        double[][] change = Matrix.scalarMult(dWeights, -lr);
        double[][] dbiases = Matrix.scalarMult(Matrix.transpose(doutput), -lr);
        weights = Matrix.add(weights, change);
        biases = Matrix.add(biases, dbiases);
        weightsMap.put(name, weights);

        return dInput;
    }

    public String getName() {
        return name;
    }

    public String getType(){return "fc";}

    public double[][] getWeights() {
        return(weights);
    }

}
