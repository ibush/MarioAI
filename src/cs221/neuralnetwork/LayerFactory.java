package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.HashMap;

/**
 * Factory paradigm to generate various layers in neural networks
 * Supported Layers
 * Fully Connected (sigmoid and RELU nonlinearities)
 * Convulutional Layer
 * Softmax?
 */
public class LayerFactory {

    public static final String TYPE_FULLY_CONNECTED = "fullyConnected";
    public static final String TYPE_CONVOLUTION = "convolution";
    public static final String TYPE_SOFTMAX = "softmax";
    public static final String TYPE_RELU = "relu";

    public static final double RANDOM_WEIGHT_MAX = 0.01;


    public static Layer genLayer(int layerNum, String type, HashMap<String, Double> hparams,
                                 HashMap<Integer, double[][]> weightsMap, int inputSize, int outputSize) {

        if(!type.equals(TYPE_RELU)) {
            double[][] weights = weightsMap.get(layerNum);
            if(weights == null) {
                System.out.println("Creating new weights for layer " + layerNum);
                double[][] rand = Matrix.rand(inputSize, outputSize);
                weights = Matrix.scalarMult(rand, RANDOM_WEIGHT_MAX); //TODO: Could just have rand call above return in this range for efficiency
                weightsMap.put(layerNum, weights);
            }
        }

    public static Layer genLayer(String name, String type, HashMap<String, Double> hparams, int inputSize, int outputSize) {
        if (type.equals(TYPE_FULLY_CONNECTED)) {
            return new FcLayer(name, hparams, inputSize, outputSize);
        } else if (type.equals(TYPE_CONVOLUTION)) {
            System.out.println("NOT YET IMPLEMENTED");
        } else if (type.equals(TYPE_SOFTMAX)) {
            System.out.println("NOT YET IMPLEMENTED");
        } else if (type.equals(TYPE_RELU)) {
            return new ReluLayer(name, hparams, inputSize, outputSize);
        }else {
            System.out.println("Layer not supported");
        }
        return(null);
    }
}
