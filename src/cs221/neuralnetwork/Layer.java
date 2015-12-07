package cs221.neuralnetwork;

import java.util.HashMap;

/**
 * Created by matt on 11/8/15.
 */
public interface Layer {
    public static final String INPUT_SIZE = "inputSize";
    public static final String OUTPUT_SIZE = "outputSize";
    public static final String STEP_SIZE = "stepSize";

    // Forward pass of NN
    public double[][] forward(double[][] input);

    // Back pass of NN
    public double[][] backprop(double[][] doutput, double lr, double reg);

    public String getName();

    public String getType();

    public double[][] getWeights();
}
