package algorithms;
import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
public class KahnTopologicalSort {
    public static class Result {
        private final List<Integer> order;
        private final boolean hasCycle;
        private final long edgeScans;
        private final long comparisons;
        private final int maxDepth;
        public Result(List<Integer> order,
                      boolean hasCycle,
                      long edgeScans,
                      long comparisons,
                      int maxDepth) {
            this.order = new ArrayList<>(order);
            this.hasCycle = hasCycle;
            this.edgeScans = edgeScans;
            this.comparisons = comparisons;
            this.maxDepth = maxDepth;
        }
        public List<Integer> getOrder() {
            return Collections.unmodifiableList(order);
        }
        public boolean hasCycle() {
            return hasCycle;
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
    public Result sort(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Topological sort requires a directed graph"
            );
        }
        int n = graph.getVertexCount();
        int[] indegree = new int[n];
        long edgeScans = 0;
        long comparisons = 0;
        for (int vertex = 0; vertex < n; vertex++) {
            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                indegree[edge.getTo()]++;
            }
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int vertex = 0; vertex < n; vertex++) {
            comparisons++;
            if (indegree[vertex] == 0) {
                queue.offer(vertex);
            }
        }
        int maxDepth = queue.size();
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            order.add(vertex);
            for (Edge edge : graph.getAdjacentEdges(vertex)) {
                int to = edge.getTo();
                edgeScans++;
                indegree[to]--;
                comparisons++;
                if (indegree[to] == 0) {
                    queue.offer(to);
                    maxDepth = Math.max(maxDepth, queue.size());
                }
            }
        }
        boolean hasCycle = order.size() != n;
        return new Result(
                order,
                hasCycle,
                edgeScans,
                comparisons,
                maxDepth
        );
    }
}