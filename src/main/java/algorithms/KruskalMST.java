
package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KruskalMST {

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
                    "Kruskal requires an undirected graph"
            );
        }

        DisjointSet dsu =
                new DisjointSet(graph.getVertexCount());

        List<Edge> sortedEdges =
                new ArrayList<>(graph.getEdges());

        sortedEdges.sort(
                Comparator.comparingInt(Edge::getWeight)
        );

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        long edgeScans = 0;
        long comparisons = 0;

        for (Edge edge : sortedEdges) {
            edgeScans++;

            int fromRoot = dsu.find(edge.getFrom());
            int toRoot = dsu.find(edge.getTo());

            comparisons++;

            if (fromRoot != toRoot) {
                dsu.union(edge.getFrom(), edge.getTo());

                mstEdges.add(edge);
                totalWeight += edge.getWeight();
            }

            if (dsu.getComponents() <= 1) {
                break;
            }
        }

        long dsuOps = dsu.getFindCalls()
                + dsu.getSuccessfulUnions();

        return new Result(
                mstEdges,
                totalWeight,
                dsu.getComponents(),
                dsuOps,
                edgeScans,
                comparisons
        );
    }
}

