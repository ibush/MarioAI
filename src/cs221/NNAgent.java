package cs221;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.agents.Agent;
import cs221.neuralnetwork.LayerSpec;
import cs221.neuralnetwork.NeuralNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matt on 11/8/15.
 */
public class NNAgent extends QAgent implements Agent{

    private NeuralNet net;

    public NNAgent(){
        super("NNAgent");
    }

    public boolean[] getAction() {

        return(null);
    }

    public void integrateObservation(Environment environment) {

    }


}
