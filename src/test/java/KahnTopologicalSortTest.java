
package algorithms;

import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KahnTopologicalSortTest {

    @Test
    void sortsDagCorrectly() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        KahnTopologicalSort.Result result =
                new KahnTopologicalSort().sort(graph);

        assertFalse(result.hasCycle());
        assertEquals(4, result.getOrder().size());

        List<Integer> order = result.getOrder();

        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    void detectsCycle() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        KahnTopologicalSort.Result result =
                new KahnTopologicalSort().sort(graph);

        assertTrue(result.hasCycle());
        assertTrue(result.getOrder().size() < 3);
    }

    @Test
    void handlesDisconnectedDagAndIsolatedVertices() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        KahnTopologicalSort.Result result =
                new KahnTopologicalSort().sort(graph);

        assertFalse(result.hasCycle());
        assertEquals(5, result.getOrder().size());
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        KahnTopologicalSort.Result result =
                new KahnTopologicalSort().sort(graph);

        assertFalse(result.hasCycle());
        assertTrue(result.getOrder().isEmpty());
    }

    @Test
    void rejectsUndirectedGraph() {
        Graph graph = new Graph(3, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> new KahnTopologicalSort().sort(graph)
        );
    }

    @Test
    void detectsSelfLoop() {
        Graph graph = new Graph(2, true);

        graph.addEdge(0, 0, 1);
        graph.addEdge(0, 1, 1);

        KahnTopologicalSort.Result result =
                new KahnTopologicalSort().sort(graph);

        assertTrue(result.hasCycle());
    }
}
