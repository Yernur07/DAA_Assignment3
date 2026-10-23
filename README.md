# DAA Assignment 3 — Graph Algorithms

## Overview
This project implements and benchmarks graph algorithms in Java. It focuses on graph representation, disjoint sets, minimum spanning forests, strongly connected components, topological sorting, and shortest paths in directed acyclic graphs (DAGs).

## Implemented Algorithms
- **Disjoint Set Union (DSU):** path compression and union by size.
- **Kruskal's Algorithm:** minimum spanning forest.
- **Prim's Algorithm:** minimum spanning forest using a priority queue.
- **Kosaraju's Algorithm:** strongly connected components using iterative DFS.
- **Kahn's Algorithm:** topological sorting and cycle detection.
- **DAG Shortest Paths:** shortest paths using topological order, including negative edge weights.

## Project Structure
```text
DAA_Assignment3/
├── data/
│   ├── disconnected.txt
│   ├── cyclic.txt
│   └── dag.txt
├── results/
│   ├── results.csv
│   └── plots/
├── src/
│   ├── main/java/
│   │   ├── graph/
│   │   ├── algorithms/
│   │   ├── metrics/
│   │   └── benchmark/
│   └── test/java/
├── pom.xml
├── plot_results.py
├── REPORT.md
└── README.md
```

## Requirements
- Java 21 or compatible JDK configured for the project.
- Apache Maven 3.10.0 or compatible Maven installation.
- Python 3 and Matplotlib for generating plots.

## Build and Test
Run these commands from the project root.
Compile the project and run the tests:

```bash
mvn clean test
```

Package the project:
```bash
mvn clean package
```

## Benchmark
The benchmark evaluates graphs with the following numbers of vertices:
`1000, 3162, 10000, 31623, 100000`
It uses two graph densities:
- Sparse graphs: `m = 4n`
- Dense graphs: `m = 20n`
A fixed random seed of `42` is used for reproducibility. Each benchmark performs a warm-up run followed by five measured runs. The median execution time is recorded.
The results are written to:
```text
results/results.csv
```
The CSV contains algorithm names, graph types, vertex and edge counts, execution times, operation counters, maximum depth, and result values.

## Generate Plots
Install Matplotlib:
```bash
py -m pip install matplotlib
```
Generate the plots:
```bash
py plot_results.py
```
The generated plots are saved in `results/plots/`:
- `operations_vs_n.png`
- `scans_per_edge.png`
- `kruskal_scans_ratio.png`

## Input Data Format
Graph input files use the following format:

```text
n m directed|undirected
from to weight
from to weight
...
```

- `n` is the number of vertices.
- `m` is the number of edges.
- Vertices are numbered from `0` to `n - 1`.
- Each edge contains its source, destination, and integer weight.
- The graph type is either `directed` or `undirected`.

Example:
```text
5 5 directed
0 1 4
0 2 2
1 3 3
2 3 6
3 4 1
```
Sample input files are available in the `data/` directory.

## Documentation
See [REPORT.md](REPORT.md) for the algorithm complexity analysis, correctness arguments, experimental results, and conclusions.

