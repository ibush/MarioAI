# MarioAI

Example Commands:

java -cp .:../lib/* ch.idsia.scenarios.Main

java -cp .:../lib/* ch.idsia.scenarios.Main -ag ch.idsia.agents.controllers.ForwardJumpingAgent -ld 1 -ls 42 -ll 256 -lt 2

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
