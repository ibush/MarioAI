package cs221.neuralnetwork;

import cs221.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matt on 11/8/15.
 */
public class NeuralNet {

    private double lr;
    private double reg;

    private ArrayList<Layer> layers;
    private HashMap<String, Double> hparams;

    public NeuralNet(List<LayerSpec> layerSpecs, HashMap<String,Double> hparams, HashMap<Integer, double[][]> weightsMap){
        layers = new ArrayList<Layer>();
        for(LayerSpec layerSpec : layerSpecs) {
            layers.add(LayerFactory.genLayer(layerSpec.getName(),
                    layerSpec.getType(), hparams,
                    (int)Math.floor(layerSpec.getArg(Layer.INPUT_SIZE)),
                    (int)Math.floor(layerSpec.getArg(Layer.OUTPUT_SIZE))));
        }
        this.hparams = hparams;
    }

    // Returns the score of the input feature matrix
    public double forward(double[][] features){
        double[][] result = features;
        for(Layer layer : layers) {
            result = layer.forward(result);
        }
        return result[0][0];
    }

    // Integrates that the previous forward call produced the given actual reward
    public void backprop(double error){
        double[][] doutput = {{error}}; //TODO: Rethink this
        for(int i = layers.size() - 1; i >= 0; i--) {
            doutput = layers.get(i).backprop(doutput);
        }
    }

    // For printing stats
    public ArrayList<Layer> getLayers(){
        return(layers);
    }
}
