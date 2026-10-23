# Assignment 3: Graph Algorithms

## 1. Introduction
The goal of this assignment is to implement and evaluate graph algorithms and data structures. The project includes a graph representation based on adjacency lists, a Disjoint Set Union (DSU) data structure, minimum spanning tree algorithms, strongly connected components, topological sorting, and shortest paths in directed acyclic graphs.
The algorithms were tested on graphs of different sizes and densities. The benchmark results were saved in `results/results.csv`, and plots were generated to compare operation counts and edge scans.

## 2. Implemented Algorithms
| Algorithm | Purpose | Time Complexity |
|---|---|---|
| Disjoint Set Union | Maintains disjoint sets | Nearly O(1) amortized per operation with path compression and union by size |
| Kruskal | Finds a minimum spanning forest | O(E log E) |
| Prim | Finds a minimum spanning forest | O((V + E) log V) with a priority queue |
| Kosaraju | Finds strongly connected components | O(V + E) |
| Kahn | Produces a topological ordering and detects cycles | O(V + E) |
| DAG Shortest Paths | Finds shortest paths in a directed acyclic graph | O(V + E) |
Here, V represents the number of vertices and E represents the number of edges.

## 3. Graph Representation
The graph is represented using adjacency lists. Each vertex stores its outgoing edges and their weights. The implementation supports directed and undirected graphs, disconnected graphs, isolated vertices, self-loops, and parallel edges.
Graph input files are stored in the `data` directory. They include examples of disconnected graphs, cyclic graphs, and directed acyclic graphs.

## 4. Correctness

### 4.1 Disjoint Set Union
The DSU structure maintains separate components. The `find` operation uses path compression, while `union` uses union by size. A successful union merges two different components. If both vertices already belong to the same component, the number of components does not change.

### 4.2 Kruskal and Prim
Kruskal sorts edges by weight and adds an edge only when it connects two different components. This prevents cycles. By the cut property of minimum spanning trees, repeatedly selecting the minimum eligible edge produces a minimum spanning forest.
Prim grows a tree from each unvisited component by selecting the minimum-weight available edge. Both algorithms should produce forests with the same total weight for the same undirected graph.

### 4.3 Kosaraju
Kosaraju performs two depth-first search passes. The first pass calculates finishing order, and the second pass explores the reversed graph in decreasing finishing order. Each traversal identifies a strongly connected component.
The DFS implementation is iterative to avoid stack overflow on large graphs.

### 4.4 Kahn Topological Sort
Kahn's algorithm starts with vertices whose indegree is zero. It repeatedly removes one such vertex and decreases the indegrees of its outgoing neighbors. If all vertices are processed, the graph is acyclic. If some vertices remain, the graph contains a directed cycle.

### 4.5 DAG Shortest Paths
The shortest-path algorithm processes vertices in topological order. When a vertex is processed, its outgoing edges are relaxed. Because every edge points forward in a topological ordering, all incoming paths to a vertex are considered before that vertex is processed. This allows the algorithm to support negative edge weights without requiring negative-cycle handling.
All vertices with indegree zero are initialized with distance zero, following the assignment's virtual-source convention.

## 5. Experimental Setup
The benchmark uses the following vertex counts:
`1000, 3162, 10000, 31623, 100000`
Two graph densities are tested:
- Sparse graphs: E = 4V
- Dense graphs: E = 20V
A fixed random seed of 42 is used for reproducibility. Each benchmark includes a warm-up run and five measured runs. The median execution time is recorded.
The CSV file contains the algorithm name, graph type, vertex count, edge count, execution time, operation counters, maximum depth, and result.

## 6. Results and Discussion
The generated plots are stored in `results/plots/`.

### 6.1 Operations vs Number of Vertices
The `operations_vs_n.png` plot compares the counted operations for different algorithms and graph densities. The total operation count in this plot is calculated as:
`dsu_ops + edge_scans + comparisons`
The graph helps illustrate how the measured work changes as the number of vertices increases. Dense graphs generally contain more edges, so algorithms that process edges can perform substantially more work on dense inputs.

### 6.2 Edge Scans per Edge
The `scans_per_edge.png` plot compares the number of edge scans relative to the number of input edges. Kosaraju scans each directed edge in both DFS passes, giving approximately 2E scans. Kahn's algorithm and DAG shortest-path relaxation scan approximately E edges under the assignment's counting convention.

### 6.3 Kruskal Edge Scan Ratio
The `kruskal_scans_ratio.png` plot compares Kruskal's measured edge scans divided by the number of input edges for sparse and dense graphs. Kruskal can stop once a spanning forest has been completed, so its scan count does not necessarily equal the total number of edges.

### 6.4 Execution Time
Execution time depends on graph size, density, sorting, priority-queue operations, and runtime conditions. Median measurements reduce the influence of unusually fast or slow individual runs. The operation counters are useful for comparing algorithmic work independently of timing noise.
The final conclusions should be updated using the actual benchmark CSV and generated plots.

## 7. Conclusion
This project implements several fundamental graph algorithms and evaluates their behavior on sparse and dense graphs. The experiments demonstrate the importance of graph representation, algorithmic complexity, and operation counting.
Kruskal and Prim provide alternative approaches to minimum spanning forests. Kosaraju identifies strongly connected components, Kahn detects directed cycles while producing a topological order, and DAG shortest paths efficiently calculate distances even when some edge weights are negative.
The benchmark data and plots provide a basis for comparing theoretical complexity with observed performance.

