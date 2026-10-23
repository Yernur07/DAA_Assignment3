package algorithms;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class DAGShortestPaths {
    private static final long INF = Long.MAX_VALUE / 4;
    public static class Result {
        private final long[] distances;
        private final int[] parents;
        private final long edgeScans;
        private final long comparisons;
        private final int maxDepth;
        public Result(long[] distances,
                      int[] parents,
                      long edgeScans,
                      long comparisons,
                      int maxDepth) {
            this.distances = distances.clone();
            this.parents = parents.clone();
            this.edgeScans = edgeScans;
            this.comparisons = comparisons;
            this.maxDepth = maxDepth;
        }
        public long[] getDistances() {
            return distances.clone();
        }
        public long getDistanceTo(int vertex) {
            checkVertex(vertex);
            return distances[vertex];
        }
        public boolean isReachable(int vertex) {
            checkVertex(vertex);
            return distances[vertex] != INF;
        }
        public List<Integer> getPathTo(int vertex) {
            checkVertex(vertex);
            if (distances[vertex] == INF) {
                return Collections.emptyList();
            }
            List<Integer> path = new ArrayList<>();
            int current = vertex;
            while (current != -1) {
                path.add(current);
                current = parents[current];
            }
            Collections.reverse(path);
            return path;
        }
        public long getEdgeScans() {
            return edgeScans;
        }
        public long getComparisons() {
            return comparisons;
        }
        public int getMaxDepth() {
            return maxDepth;
        }
        private void checkVertex(int vertex) {
            if (vertex < 0 || vertex >= distances.length) {
                throw new IllegalArgumentException(
                        "Invalid vertex: " + vertex
                );
            }
        }
    }
    public Result findShortestPaths(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "DAG shortest paths requires a directed graph"
            );
        }
        KahnTopologicalSort.Result topologicalResult =
                new KahnTopologicalSort().sort(graph);
        if (topologicalResult.hasCycle()) {
            throw new IllegalArgumentException(
                    "Shortest paths require a DAG without cycles"
            );
        }
        int n = graph.getVertexCount();
        long[] distances = new long[n];
        int[] parents = new int[n];
        int[] indegree = new int[n];
        Arrays.fill(distances, INF);
        Arrays.fill(parents, -1);
        long edgeScans = 0;
        long comparisons = 0;
        for (int vertex = 0; vertex < n; vertex++) {
            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                indegree[edge.getTo()]++;
            }
        }
        for (int vertex = 0; vertex < n; vertex++) {
            comparisons++;
            if (indegree[vertex] == 0) {
                distances[vertex] = 0;
            }
        }
        for (int vertex : topologicalResult.getOrder()) {
            if (distances[vertex] == INF) {
                continue;
            }
            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                int to = edge.getTo();
                long candidate = distances[vertex] + edge.getWeight();
                edgeScans++;
                comparisons++;
                if (candidate < distances[to]) {
                    distances[to] = candidate;
                    parents[to] = vertex;
                }
            }
        }
        return new Result(
                distances,
                parents,
                edgeScans,
                comparisons,
                topologicalResult.getMaxDepth()
        );
    }
}