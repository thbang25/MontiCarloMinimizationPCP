# MontiCarloMinimizationPCP

Please access the primary file named "MonteCarloMinimizationParallel" and specify your preferred range of grid sizes for setting the pool size. Select the x and y values range, and set the desired search density.

![Screenshot (195)](https://github.com/thbang25/MontiCarloMinimizationPCP/assets/83241507/e8181c01-e69a-4893-be4c-33985803e95e)


The MonteCarloMinimizationParallel class has some similarities to the original montecarlominimization program but I have removed the loop iteration and incorporated the fork-join framework to improve the program's speed.



Using recursive task, we compute the global minimum by iterating through the start, middle, and end of the array, the threshold is used to adjust the workload required if the value is too small or too large it will affect the performance, it can be adjusted in the main section and you can toggle through it until a sufficient value is found.
