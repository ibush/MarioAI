package cs221;

import cs221.neuralnetwork.Layer;
import cs221.neuralnetwork.NeuralNet;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class Stats {

    private String AgentName;
    private PrintWriter weightWriter;
    private PrintWriter metricWriter;

    //Stats store variable
    private HashMap<String, double[]> weightStore;
    private HashMap<String, Double> metricStore;

    private HashMap<String,double[][]> prevWeights;

    public Stats(String AgentName){
        this.AgentName = AgentName;

        prevWeights = new HashMap<String, double[][]>();
        weightStore = new HashMap<String, double[]>();
        metricStore = new HashMap<String, Double>();


        try {
            metricWriter = new PrintWriter("stats/metrics_" + this.AgentName, "UTF-8");
            weightWriter = new PrintWriter("stats/weights_" + this.AgentName, "UTF-8");
        } catch (Exception e) {
            System.out.println("File Could not be opened");
        }
    }

    public void addLearningRate(double lr){
        metricStore.put("lr",lr);
    }

    public void addEpsilonGreedy(double epsilon) {
        metricStore.put("epsilon", epsilon);
    }

    public void addError(double error){
        metricStore.put("error", error);
    }

    public void addWeights(double[] weights) {
        double[][] weightsMatrix = {weights};
        storeWeights(AgentName, weightsMatrix);
    }

    public void addWeights(NeuralNet net){
        ArrayList<Layer> layers = net.getLayers();
        for(Layer layer : layers) {
            double[][] weights = layer.getWeights();
            storeWeights(layer.getName(), weights);
        }
    }

    private void storeWeights(String name, double[][] weights) {
        weightStore.put(name, calcMatStats(weights));
        if(prevWeights.containsKey(name)){
            double[][] prev = prevWeights.get(name);
            metricStore.put(name + "_weightDiff", calcDiff(prev, weights));
        }
        prevWeights.put(name, weights);
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

    // Calculate the change in two weight matrices
    private double calcDiff(double[][] prev, double[][] curr){
        double diff = 0;
        for(int i = 0; i < prev.length; i++){
            for(int j = 0; j < prev[0].length; j++){
                diff += Math.abs(prev[i][j] - curr[i][j]);
            }
        }
        return diff;
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
