
package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KosarajuSCC {

    public static class Result {
        private final List<List<Integer>> components;
        private final long edgeScans;
        private final long comparisons;
        private final int maxDepth;

        public Result(List<List<Integer>> components,
                      long edgeScans,
                      long comparisons,
                      int maxDepth) {
            this.components = new ArrayList<>();

            for (List<Integer> component : components) {
                this.components.add(new ArrayList<>(component));
            }

            this.edgeScans = edgeScans;
            this.comparisons = comparisons;
            this.maxDepth = maxDepth;
        }

        public List<List<Integer>> getStronglyConnectedComponents() {
            List<List<Integer>> copy = new ArrayList<>();

            for (List<Integer> component : components) {
                copy.add(Collections.unmodifiableList(component));
            }

            return Collections.unmodifiableList(copy);
        }

        public int getComponentCount() {
            return components.size();
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
    }

    public Result findSCC(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kosaraju requires a directed graph"
            );
        }

        int n = graph.getVertexCount();

        boolean[] visited = new boolean[n];
        int[] nextIndex = new int[n];
        int[] stack = new int[n];
        int[] order = new int[n];

        int orderSize = 0;
        int maxDepth = 0;

        long edgeScans = 0;
        long comparisons = 0;

        // First DFS pass: record vertices by finishing order.
        for (int start = 0; start < n; start++) {
            comparisons++;

            if (visited[start]) {
                continue;
            }

            int top = 0;
            stack[top] = start;
            visited[start] = true;
            nextIndex[start] = 0;

            maxDepth = Math.max(maxDepth, top + 1);

            while (top >= 0) {
                int vertex = stack[top];
                List<Edge> adjacent = graph.getAdjacentEdges(vertex);

                if (nextIndex[vertex] < adjacent.size()) {
                    Edge edge = adjacent.get(nextIndex[vertex]++);
                    int to = edge.getTo();

                    edgeScans++;
                    comparisons++;

                    if (!visited[to]) {
                        visited[to] = true;
                        nextIndex[to] = 0;

                        stack[++top] = to;
                        maxDepth = Math.max(maxDepth, top + 1);
                    }
                } else {
                    order[orderSize++] = vertex;
                    top--;
                }
            }
        }

        // Reverse every edge in the graph.
        Graph reversedGraph = graph.reverse();

        // Reset visited for the second DFS pass.
        visited = new boolean[n];

        List<List<Integer>> components = new ArrayList<>();

        // Process vertices in reverse finishing order.
        for (int i = orderSize - 1; i >= 0; i--) {
            int start = order[i];

            comparisons++;

            if (visited[start]) {
                continue;
            }

            List<Integer> component = new ArrayList<>();

            int top = 0;
            stack[top] = start;
            visited[start] = true;

            maxDepth = Math.max(maxDepth, top + 1);

            while (top >= 0) {
                int vertex = stack[top--];

                component.add(vertex);

                for (Edge edge : reversedGraph.getAdjacentEdges(vertex)) {
                    int to = edge.getTo();

                    edgeScans++;
                    comparisons++;

                    if (!visited[to]) {
                        visited[to] = true;
                        stack[++top] = to;

                        maxDepth = Math.max(maxDepth, top + 1);
                    }
                }
            }

            components.add(component);
        }

        return new Result(
                components,
                edgeScans,
                comparisons,
                maxDepth
        );
    }
}
