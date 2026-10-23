
package algorithms;

import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathsTest {

    @Test
    void findsShortestPathsInDag() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 6);
        graph.addEdge(3, 4, 1);

        DAGShortestPaths.Result result =
                new DAGShortestPaths().findShortestPaths(graph);

        assertEquals(0, result.getDistanceTo(0));
        assertEquals(4, result.getDistanceTo(1));
        assertEquals(2, result.getDistanceTo(2));
        assertEquals(7, result.getDistanceTo(3));
        assertEquals(8, result.getDistanceTo(4));

        assertEquals(
                List.of(0, 1, 3, 4),
                result.getPathTo(4)
        );
    }

    @Test
    void supportsNegativeEdgeWeights() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(2, 1, -4);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 10);

        DAGShortestPaths.Result result =
                new DAGShortestPaths().findShortestPaths(graph);

        assertEquals(0, result.getDistanceTo(0));
        assertEquals(-2, result.getDistanceTo(1));
        assertEquals(2, result.getDistanceTo(2));
        assertEquals(1, result.getDistanceTo(3));

        assertEquals(
                List.of(0, 2, 1, 3),
                result.getPathTo(3)
        );
    }

    @Test
    void everySourceStartsAtZero() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 7);
        graph.addEdge(2, 3, -2);

        DAGShortestPaths.Result result =
                new DAGShortestPaths().findShortestPaths(graph);

        assertEquals(0, result.getDistanceTo(0));
        assertEquals(7, result.getDistanceTo(1));
        assertEquals(0, result.getDistanceTo(2));
        assertEquals(-2, result.getDistanceTo(3));
        assertEquals(0, result.getDistanceTo(4));
    }

    @Test
    void rejectsGraphWithCycle() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> new DAGShortestPaths().findShortestPaths(graph)
        );
    }

    @Test
    void rejectsUndirectedGraph() {
        Graph graph = new Graph(3, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> new DAGShortestPaths().findShortestPaths(graph)
        );
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        DAGShortestPaths.Result result =
                new DAGShortestPaths().findShortestPaths(graph);

        assertEquals(0, result.getDistances().length);
    }

    @Test
    void returnsEmptyPathForUnreachableVertex() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 5);

        DAGShortestPaths.Result result =
                new DAGShortestPaths().findShortestPaths(graph);

        assertTrue(result.isReachable(1));
        assertEquals(List.of(0, 1), result.getPathTo(1));
        assertTrue(result.isReachable(2));
        assertEquals(List.of(2), result.getPathTo(2));
    }
}
