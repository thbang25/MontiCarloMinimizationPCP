# MontiCarloMinimizationPCP

Please access the primary file named "MonteCarloMinimizationParallel" and specify your preferred range of grid sizes for setting the pool size. Select the ranges for your x and y values, and set the desired search density.
![Screenshot (195)](https://github.com/thbang25/MontiCarloMinimizationPCP/assets/83241507/fa00d12f-76a3-4551-87d4-f9df11b3f345)

Using a Recursive task I declared a method called minimizationParallel, declaring the start, middle, and end value, the threshold, and the searchParallel array to keep track of the searches performed.
![Screenshot (198)](https://github.com/thbang25/MontiCarloMinimizationPCP/assets/83241507/b020e993-3fab-4d75-8207-f0c81f74b486)

The global minimum is computed by iterating through the start, middle, and end of the array, and the threshold is used to adjust the workload required, if the value is too small or too large it will affect the performance, it can be adjusted in the main section and you can toggle through it until a sufficient value is found. This section of the code returns the minimum value.
![Screenshot (197)](https://github.com/thbang25/MontiCarloMinimizationPCP/assets/83241507/0c23a34a-1298-4735-9ca3-080d912464e6)

