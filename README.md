# TravellingSalesmanProblem
This is my Bachelor thesis in computer science at Ufa SATU on genetic algorithms for solving [Travelling Salesman Problem](https://en.wikipedia.org/wiki/Travelling_salesman_problem).
## Problem description
There are:
- Point of production of goods (address)
- List of requests for delivery of goods (address, date, amount)

Need to find a route of minimum cost on a certain date to all addresses requests for delivery, that begins and ends at the point of production.
## Research info
I compared 18 modifications of genetic algorithms, builded by combinations of next parameters:
- Initial population
  - **Tour created by Nearest Neighbour algorithm**
  - **Tour of the points, sorted by distance from initial point**
  - **Tour around the start point clockwise**
  - **Random tour**
- Selection operators
  - **Rank selection**
  - **Elitism**
- Crossover operator
  - **Edge recombination**
- Mutation operators
  - Move 
  - **Inversion**
- Parents in crossover
  - 2
  - **3**
- Mutation probability
  - 0.1
  - **0.3**
  - 0.5

Test dataset taken from [TSPLIB](https://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/). I tested on datasets, that contains up to 100 nodes. 

Finally, I choosed a modification of algorithm with **bold** parameters.
## Technical info
This is Web project with back-end on Java and front-end on JSP (HTML+CSS).
