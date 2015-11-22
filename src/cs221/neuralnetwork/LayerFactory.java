package cs221.neuralnetwork;

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


    public static Layer genLayer(String type, HashMap<String, Double> hparams, int inputSize, int outputSize) {
        if (type.equals(TYPE_FULLY_CONNECTED)) {
            return new FcLayer(hparams, inputSize, outputSize);
        } else if (type.equals(TYPE_CONVOLUTION)) {
            System.out.println("NOT YET IMPLEMENTED");
        } else if (type.equals(TYPE_SOFTMAX)) {
            System.out.println("NOT YET IMPLEMENTED");
        } else if (type.equals(TYPE_RELU)) {
            return new ReluLayer(hparams, inputSize, outputSize);
        }else {
            System.out.println("Layer not supported");
        }
        return(null);
    }
}
