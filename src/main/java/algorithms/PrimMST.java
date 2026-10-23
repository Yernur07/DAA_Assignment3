
package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {

    public static class Result {
        private final List<Edge> edges;
        private final long totalWeight;
        private final int components;
        private final long dsuOps;
        private final long edgeScans;
        private final long comparisons;

        public Result(
                List<Edge> edges,
                long totalWeight,
                int components,
                long dsuOps,
                long edgeScans,
                long comparisons
        ) {
            this.edges = new ArrayList<>(edges);
            this.totalWeight = totalWeight;
            this.components = components;
            this.dsuOps = dsuOps;
            this.edgeScans = edgeScans;
            this.comparisons = comparisons;
        }

        public List<Edge> getEdges() {
            return List.copyOf(edges);
        }

        public long getTotalWeight() {
            return totalWeight;
        }

        public int getComponents() {
            return components;
        }

        public long getDsuOps() {
            return dsuOps;
        }

        public long getEdgeScans() {
            return edgeScans;
        }

        public long getComparisons() {
            return comparisons;
        }
    }

    public Result findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph"
            );
        }

        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        long edgeScans = 0;
        long[] comparisons = {0};

        Comparator<Edge> comparator = (a, b) -> {
            comparisons[0]++;
            return Integer.compare(
                    a.getWeight(),
                    b.getWeight()
            );
        };

        PriorityQueue<Edge> queue =
                new PriorityQueue<>(comparator);

        int components = 0;

        // Start a new tree for each disconnected component
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            components++;
            visited[start] = true;

            for (Edge edge : graph.getAdjacentEdges(start)) {
                queue.offer(edge);
            }

            while (!queue.isEmpty()) {
                Edge edge = queue.poll();
                edgeScans++;

                int to = edge.getTo();

                // Ignore edges leading to visited vertices
                if (visited[to]) {
                    continue;
                }

                visited[to] = true;
                mstEdges.add(edge);
                totalWeight += edge.getWeight();

                for (Edge next :
                        graph.getAdjacentEdges(to)) {
                    if (!visited[next.getTo()]) {
                        queue.offer(next);
                    }
                }
            }
        }

        return new Result(
                mstEdges,
                totalWeight,
                components,
                0,
                edgeScans,
                comparisons[0]
        );
    }
}
