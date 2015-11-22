package cs221.neuralnetwork;

import java.util.HashMap;

/**
 * Created by matt on 11/8/15.
 */
public class LayerSpec {

    private String type;
    private HashMap<String, Double> args;

    public LayerSpec(String type){
        this.type = type;
        args = new HashMap<String, Double>();
    }
    public LayerSpec(String type, int inputSize, int outputSize){
        this.type = type;
        args = new HashMap<String, Double>();
        addArg(Layer.INPUT_SIZE, new Double(inputSize));
        addArg(Layer.OUTPUT_SIZE, new Double(outputSize));
    }

    public void addArg(String arg, Double val){
        args.put(arg,val);
    }

    public Double getArg(String arg){
        return(args.get(arg));
    }

    public String getType(){
        return(type);
    }
}
