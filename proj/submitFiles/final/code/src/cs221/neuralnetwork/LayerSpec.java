package cs221.neuralnetwork;

public class LayerSpec {

    private String name;
    private String type;
    private int inputSize;
    private int outputSize;

    public LayerSpec(String type){
        this.type = type;
    }
    public LayerSpec(String type, int inputSize, int outputSize){
        this.type = type;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public int getInputSize(){return(inputSize);}

    public int getOutputSize(){return(outputSize);}

    public String getType(){
        return(type);
    }

}
