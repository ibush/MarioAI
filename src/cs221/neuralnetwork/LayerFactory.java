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

    public static Layer genLayer(String type, HashMap<String, Double> params){
        if(type == "fullyConnected") {
            System.out.println("NOT YET IMPLEMENTED");
        }else if(type == "convulution"){
            System.out.println("NOT YET IMPLEMENTED");
        }else if(type == "softmax"){
            System.out.println("NOT YET IMPLEMENTED");
        }else{
            System.out.println("Layer not supported");
        }
        return(null);
    }
}
