package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matt on 11/8/15.
 */
public class NeuralNet {


    private ArrayList<Layer> layers;
    private HashMap<String, Double> hparams;

    public NeuralNet(List<LayerSpec> layerSpecs, HashMap<String, double[][]> weightsMap){
        layers = new ArrayList<Layer>();
        for(int i=0; i< layerSpecs.size(); i++) {
            layers.add(LayerFactory.genLayer(i, layerSpecs.get(i).getType(), weightsMap,
                    layerSpecs.get(i).getInputSize(), layerSpecs.get(i).getOutputSize()));
        }
    }

    // Returns the score of the input feature matrix
    public double[][] forward(double[][] features){
        double[][] result = features;
        for(Layer layer : layers) {
            result = layer.forward(result);
        }
        return result;
    }

    // Integrates that the previous forward call produced the given actual reward
    public void backprop(double[][] doutput, double lr, double reg){
        for(int i = layers.size() - 1; i >= 0; i--) {
            doutput = layers.get(i).backprop(doutput, lr, reg);
        }
    }

    // Use for regularization
    public double getWeightSq(){
        double total = 0;
        for(Layer l : layers){
            if(l.getType() == "fc"){
                double[][] weights = l.getWeights();
                for(int i = 0; i < weights.length; i++){
                    for(int j = 0; j < weights[0].length; j++){
                        total = total +  Math.pow(weights[i][j],2);
                    }
                }
            }
        }
        return(total);
    }

    // For printing stats
    public ArrayList<Layer> getLayers(){
        return(layers);
    }
}
