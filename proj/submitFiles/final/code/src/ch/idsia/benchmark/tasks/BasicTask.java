/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.statistics.StatisticalSummary;
import cs221.QAgent;
import cs221.QLearningAgent;
import cs221.QLinearAgent;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy,
 * sergey@idsia.ch
 * Date: Mar 14, 2010 Time: 4:47:33 PM
 */

public class BasicTask implements Task
{
protected final static Environment environment = MarioEnvironment.getInstance();
private HashMap learnedParams;
private Agent agent;
protected MarioAIOptions options;
private long COMPUTATION_TIME_BOUND = 42; // stands for prescribed  FPS 24.
private String name = getClass().getSimpleName();
private EvaluationInfo evaluationInfo;

private Vector<StatisticalSummary> statistics = new Vector<StatisticalSummary>();

public BasicTask(MarioAIOptions marioAIOptions)
{
    this.setOptionsAndReset(marioAIOptions);
}

/**
 * @param repetitionsOfSingleEpisode
 * @return boolean flag whether controller is disqualified or not
 */
public boolean runSingleEpisode(final int repetitionsOfSingleEpisode) {
    return runSingleEpisode(repetitionsOfSingleEpisode, true);
}

//Output to file will print  weighted fitness and distance scores for all runs to text files
public boolean runSingleEpisode(final int repetitionsOfSingleEpisode, boolean outputToFile)
{
    PrintWriter fitnessScores = null;
    PrintWriter distance = null;
    PrintWriter compTime = null;
    if(outputToFile) {
        try {
            fitnessScores = new PrintWriter("stats/fitnessScores_" + agent.getName(), "UTF-8");
            distance = new PrintWriter("stats/distance_" + agent.getName(), "UTF-8");
            compTime = new PrintWriter("stats/time_" + agent.getName(), "UTF-8");
        } catch (Exception e) {
            System.out.println("Could not open output files");
        }
    }
    if(agent instanceof QAgent ) ((QAgent)agent).setLearnedParams(learnedParams);

    long c;
    long startTime = 0;
    long endTime;
    long runtime;
    for (int r = 0; r < repetitionsOfSingleEpisode; ++r)
    {
        if(agent instanceof QLearningAgent && r > QLearningAgent.MAX_LEARNING_RUNS)
            ((QLearningAgent)agent).learning = false;

        endTime = System.currentTimeMillis();
        runtime = (endTime - startTime) / 1000;
        System.out.println("Iteration : " + Integer.toString(r) + " runtime : " + Long.toString(runtime));
        startTime = System.currentTimeMillis();

        this.reset();
        while (!environment.isLevelFinished())
        {
            environment.tick();
            if (!GlobalOptions.isGameplayStopped)
            {
                c = System.nanoTime();

                agent.integrateObservation(environment);
                agent.giveIntermediateReward(environment.getIntermediateReward());

                boolean[] action = agent.getAction();

                if(compTime != null){
                    compTime.println(System.nanoTime() - c);
                    compTime.flush();
                }

                environment.performAction(action);
            }
        }
        environment.closeRecorder(); //recorder initialized in environment.reset
        environment.getEvaluationInfo().setTaskName(name);
        this.evaluationInfo = environment.getEvaluationInfo().clone();

        if(fitnessScores != null){
            fitnessScores.println(environment.getEvaluationInfo().computeWeightedFitness());
            fitnessScores.flush();
        }
        if(distance!= null){
            distance.println(environment.getEvaluationInfo().computeDistancePassed());
            distance.flush();
        }

        if (agent instanceof QAgent && !(agent instanceof QLearningAgent) && (r % GlobalOptions.snapshot_steps == 0)) {
            learnedParams = ((QAgent)agent).getLearnedParams();
            //Save params to file
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("params/params_" + agent.getName()));
                out.writeObject(learnedParams);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not write params to file");
            }
        }
        //System.out.println(Arrays.toString((double[])learnedParams.get("weights")));
    }

    if(fitnessScores != null) fitnessScores.close();
    if(distance != null) distance.close();
    return true;
}

public static double avg(double[] m) {
    double sum = 0;
    for (int i = 0; i < m.length; i++) {
        sum += m[i];
    }
    return sum / m.length;
}

public Environment getEnvironment()
{
    return environment;
}

public int evaluate(Agent controller)
{
    return 0;
}

public void setOptionsAndReset(MarioAIOptions options)
{
    this.options = options;
    reset();
}

public void setOptionsAndReset(final String options)
{
    this.options.setArgs(options);
    reset();
}

public void doEpisodes(int amount, boolean verbose, final int repetitionsOfSingleEpisode) {
    learnedParams = new HashMap();

    if(agent instanceof QAgent ) {
        try{
            //read params from file
            File file = new File("params/params_" + agent.getName());
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            learnedParams = (HashMap)ois.readObject();
            ois.close();
        }catch(Exception e){
            //Starting learning from scratch
            System.out.println("Starting learning from scratch");
            learnedParams = new HashMap();
        }
    }

    for (int j = 0; j < EvaluationInfo.numberOfElements; j++) {
        statistics.addElement(new StatisticalSummary());
    }
    for (int i = 0; i < amount; ++i) {
        this.reset();
        this.runSingleEpisode(repetitionsOfSingleEpisode);
        if (verbose)
            System.out.println(environment.getEvaluationInfoAsString());

        for (int j = 0; j < EvaluationInfo.numberOfElements; j++) {
            statistics.get(j).add(environment.getEvaluationInfoAsInts()[j]);
        }
    }

    System.out.println(statistics.get(3).toString());

    if (agent instanceof QAgent) {
    //Save params to file
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("params/params_" + agent.getName()));
            out.writeObject(learnedParams);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not write params to file");
        }
    }
}

public boolean isFinished()
{
    return false;
}

public void reset()
{
    agent = options.getAgent();
    environment.reset(options);
    agent.reset();
    agent.setObservationDetails(environment.getReceptiveFieldWidth(),
            environment.getReceptiveFieldHeight(),
            environment.getMarioEgoPos()[0],
            environment.getMarioEgoPos()[1]);
}

public String getName()
{
    return name;
}

public void printStatistics()
{
    System.out.println(evaluationInfo.toString());
}

public EvaluationInfo getEvaluationInfo()
{
//    System.out.println("evaluationInfo = " + evaluationInfo);
    return evaluationInfo;
}

}

//            start timer
//            long tm = System.currentTimeMillis();

//            System.out.println("System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND = " + (System.currentTimeMillis() - tm ));
//            if (System.currentTimeMillis() - tm > COMPUTATION_TIME_BOUND)
//            {
////                # controller disqualified on this level
//                System.out.println("Agent is disqualified on this level");
//                return false;
//            }