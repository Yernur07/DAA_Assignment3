import graph.Edge;
import graph.Graph;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void shouldStoreUndirectedEdges() {
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 8);

        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());
        assertEquals(1, graph.getAdjacentEdges(0).size());
        assertEquals(2, graph.getAdjacentEdges(1).size());
    }

    @Test
    void shouldStoreDirectedEdges() {
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 5);

        assertEquals(1, graph.getAdjacentEdges(0).size());
        assertEquals(0, graph.getAdjacentEdges(1).size());
    }

    @Test
    void shouldSupportSelfLoopsAndParallelEdges() {
        Graph graph = new Graph(2, false);

        graph.addEdge(0, 0, 3);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 1, 7);

        assertEquals(3, graph.getEdgeCount());
        assertEquals(3, graph.getAdjacentEdges(0).size());
    }

    @Test
    void shouldReverseDirectedGraph() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 2, 10);

        Graph reversed = graph.reverse();

        Edge edge = reversed.getAdjacentEdges(2).get(0);

        assertEquals(2, edge.getFrom());
        assertEquals(0, edge.getTo());
        assertEquals(10, edge.getWeight());
    }

    @Test
    void shouldReadGraphFromFile() throws Exception {
        Graph graph = Graph.readFromFile(
                Path.of("data", "dag.txt")
        );

        assertEquals(5, graph.getVertexCount());
        assertEquals(5, graph.getEdgeCount());
        assertTrue(graph.isDirected());
    }

    @Test
    void shouldRejectInvalidVertex() {
        Graph graph = new Graph(3, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addEdge(0, 3, 5)
        );
    }
}

