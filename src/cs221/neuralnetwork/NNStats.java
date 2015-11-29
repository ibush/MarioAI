package cs221.neuralnetwork;

import cs221.Matrix;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class NNStats {

    private String AgentName;
    private PrintWriter weightWriter;
    private PrintWriter metricWriter;

    //Stats store variable
    private HashMap<String, double[]> weightStore;
    private HashMap<String, Double> metricStore;

    public NNStats(String AgentName){
        this.AgentName = AgentName;

        weightStore = new HashMap<String, double[]>();
        metricStore = new HashMap<String, Double>();

        try {
            metricWriter = new PrintWriter("stats/NNmetrics_" + this.AgentName, "UTF-8");
            weightWriter = new PrintWriter("stats/NNweights_" + this.AgentName, "UTF-8");
        } catch (Exception e) {
            System.out.println("File Could not be opened");
        }
    }

    public void addLearningRate(double lr){
        metricStore.put("lr",lr);
    }

    public void addError(double error){
        metricStore.put("error", error);
    }

    public void addWeights(NeuralNet net){
        ArrayList<Layer> layers = net.getLayers();
        for(Layer layer : layers) {
            double[][] weights = layer.getWeights();
            weightStore.put(layer.getName(), calcMatStats(weights));
        }
    }

    public void flush(){
        for(String key : metricStore.keySet()){
            metricWriter.println(key + "," + metricStore.get(key));
        }
        for(String key : weightStore.keySet()){
            double[] weightStats = weightStore.get(key);
            StringBuilder sb = new StringBuilder();
            sb.append(key + ",");
            for(int i=0; i<weightStats.length; i++){
                sb.append(weightStats[i]);
                sb.append(",");
            }
            weightWriter.println(sb.toString());
        }
        metricWriter.flush();
        weightWriter.flush();

        metricStore.clear();
        weightStore.clear();
    }

    private double[] calcMatStats(double[][] mat) {
        double sum = 0;
        double variance = 0;
        double vmin = mat[0][0];
        double vmax = mat[0][0];
        double numElems = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                sum = sum + mat[i][j];
                numElems = numElems + 1;
                vmin = Math.min(vmin, mat[i][j]);
                vmax = Math.max(vmax, mat[i][j]);
            }
        }
        double mean = sum / numElems;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                variance = variance + Math.pow(mat[i][j] - mean, 2)/numElems;
            }
        }
        double[] results = new double[]{mean, variance, vmin, vmax};
        return(results);
    }
}
