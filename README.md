# MarioAI

Example Commands:

java -cp .:../lib/* ch.idsia.scenarios.Main

java -cp .:../lib/* ch.idsia.scenarios.Main -ag ch.idsia.agents.controllers.ForwardJumpingAgent -ld 1 -ls 42 -ll 256 -lt 2

java -cp .:../lib/* ch.idsia.scenarios.Main -ag cs221.NNAgent -vis off

Example command-line options (if not specified, uses default in ch/idsia/benchmark/mario/engine/GlobalOptions.java)
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

To modify the number of episodes, change 3rd parameter in line 58 of Main.java.
To start learning from scratch, delete the params_<Agent> file in the bin directory.

Algorithms to Implement - All Q-Learning
	Just state vector
	Linear Approximation
	Hand engineered features (action combos?)
	Neural Network
	Polynomial Features
	Intermediate Rewards
        Tweak parameters (step size, epsilon, etc.)
