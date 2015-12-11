# MarioAI

Code Structure:

src/cs221 contains code for our Q-Learning agents. All agents extend the base 
QAgent. QLearningAgent, QLinearAgent, and NNAgent are the identity, linear, 
and neural network implementations, respectively. The neuralnetwork folder 
contains code to implement the neural network (fully-connected and rectified 
linear layers and the replay memory).

Example Commands:

Play Mario manually:
java -cp .:../lib/* ch.idsia.scenarios.Main

Watch an agent play:
java -cp .:../lib/* ch.idsia.scenarios.Main -ag cs221.QLinearAgent

Run an agent with the visual simulation off:
java -cp .:../lib/* ch.idsia.scenarios.Main -ag cs221.NNAgent -vis off

Examples of added command-line options (if not specified, will use default in 
ch/idsia/benchmark/mario/engine/GlobalOptions.java):

-deps true //true = use decreasing epsilon greedy
-eps 0.2 //static epsilon greedy value (if deps false)
-meps 0.1 //min epsilon greedy (if deps true)
-iteps 1000 //iterations per epsilon greedy update
-reg 0.01 //regularization lamdba
-step 0.01 //step size
-dis 0.95 //discount
-ind false //true = use indicator rewards
-test true //true = testing (not learning)
-batch 50 //NN batch size
-rep 5000 //NN replay size
-h1 100 //NN hidden 1 size
-h2 100 //NN hidden 2 size
-lrdecay 0.5 //learning rate decay factor 

To modify the number of episodes to run, change 3rd parameter in line 58 of 
ch/idsia/scenarios/Main.java.
To start learning from scratch, delete the params_<Agent> file in the 
bin/params directory.


