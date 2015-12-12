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


    public static Layer genLayer(int layerNum, String type, HashMap<String, double[][]> weightsMap,
                                 int inputSize, int outputSize) {

        if (type.equals(TYPE_FULLY_CONNECTED)) {
            return new FcLayer(layerNum, weightsMap, inputSize, outputSize);
        } else if (type.equals(TYPE_RELU)) {
            return new ReluLayer(layerNum, inputSize, outputSize);
        } else if (type.equals(TYPE_CONVOLUTION)) {
            System.out.println("NOT YET IMPLEMENTED");
        } else if (type.equals(TYPE_SOFTMAX)) {
            System.out.println("NOT YET IMPLEMENTED");
        }else {
            System.out.println("Layer not supported");
        }
        return(null);
    }
}
