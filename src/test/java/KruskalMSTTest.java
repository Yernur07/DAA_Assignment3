import algorithms.KruskalMST;
import graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KruskalMSTTest {

    @Test
    void shouldFindMinimumSpanningTree() {
        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(0, 2, 5);
        graph.addEdge(2, 3, 1);

        KruskalMST.Result result =
                new KruskalMST().findMST(graph);

        assertEquals(6, result.getTotalWeight());
        assertEquals(3, result.getEdges().size());
        assertEquals(1, result.getComponents());
    }

    @Test
    void shouldHandleDisconnectedGraph() {
        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(3, 4, 7);

        KruskalMST.Result result =
                new KruskalMST().findMST(graph);

        assertEquals(13, result.getTotalWeight());
        assertEquals(3, result.getEdges().size());
        assertEquals(2, result.getComponents());
    }

    @Test
    void shouldHandleEmptyGraph() {
        Graph graph = new Graph(0, false);

        KruskalMST.Result result =
                new KruskalMST().findMST(graph);

        assertEquals(0, result.getTotalWeight());
        assertEquals(0, result.getComponents());
        assertTrue(result.getEdges().isEmpty());
    }

    @Test
    void shouldRejectDirectedGraph() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 5);

        assertThrows(
                IllegalArgumentException.class,
                () -> new KruskalMST().findMST(graph)
        );
    }
}
