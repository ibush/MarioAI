package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.HashMap;

/**
 * Created by ibush on 11/21/15.
 */
public class ReluLayer implements Layer {

    private double[][] dRelu; //Partial derivative of ReLU


    public ReluLayer(HashMap<String, Double> hparams, int inputSize, int outputSize){
        dRelu = new double[outputSize][inputSize];
    }

    public double[][] forward(double[][] input){
        return Matrix.relu(input, dRelu);
    }

    public double[][] backprop(double[][] doutput){

        //TODO: Rethink this (dimensions correct?)
        double[][] result = new double[dRelu.length][dRelu[0].length];
        for(int i = 0; i < dRelu.length; i++ ) {
            for(int j = 0; j < dRelu[i].length; j++) {
                result[i][j] = doutput[j][i] * dRelu[i][j];
            }
        }

        return result;
    }

}
