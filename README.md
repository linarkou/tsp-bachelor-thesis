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
  - Tour created by Nearest Neighbour algorithm
  - Tour of the points, sorted by distance from initial point
  - Tour around the start point clockwise
  - Random tour
- Selection
  - Rank selection
  - Elitism
- Crossover
  - Partialy mapped crossover
  - Cyce crossover
  - Edge recombination
- Mutation
  - Move 
  - Swap
  - Scramble
  - Inversion
## Technical info
