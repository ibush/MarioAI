package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.HashMap;

/**
 * Created by ibush on 11/21/15.
 */
public class ReluLayer implements Layer {

    private double[][] dRelu; //Partial derivative of ReLU
    private String name;

    public ReluLayer(int layerNum, int inputSize, int outputSize){
        this.name = "relu" + Integer.toString(layerNum);
        dRelu = new double[inputSize][outputSize];
    }

    public double[][] forward(double[][] input){
        return Matrix.relu(input, dRelu);
    }

    public double[][] backprop(double[][] doutput, double lr, double reg){

        double[][] result = new double[doutput.length][doutput[0].length];
        for(int i = 0; i < doutput.length; i++ ) {
            for(int j = 0; j < doutput[i].length; j++) {
                result[i][j] = doutput[i][j] * dRelu[i][j];
            }
        }

        return result;
    }

    public String getName(){return(name);}

    public String getType(){return "relu";}

    public double[][] getWeights(){return(new double[][]{{0}});}
}
