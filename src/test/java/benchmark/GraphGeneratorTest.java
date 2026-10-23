
package benchmark;

import graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphGeneratorTest {

    @Test
    void generatesConnectedUndirectedGraph() {
        Graph graph = GraphGenerator.generateUndirected(100, 400);

        assertEquals(100, graph.getVertexCount());
        assertEquals(400, graph.getEdgeCount());
        assertFalse(graph.isDirected());
    }

    @Test
    void generatesDirectedGraph() {
        Graph graph = GraphGenerator.generateDirected(100, 400);

        assertEquals(100, graph.getVertexCount());
        assertEquals(400, graph.getEdgeCount());
        assertTrue(graph.isDirected());
    }

    @Test
    void generatesDag() {
        Graph graph = GraphGenerator.generateDag(100, 400);

        assertEquals(100, graph.getVertexCount());
        assertEquals(400, graph.getEdgeCount());
        assertTrue(graph.isDirected());

        assertFalse(
                new algorithms.KahnTopologicalSort()
                        .sort(graph)
                        .hasCycle()
        );
    }

    @Test
    void generatorIsReproducible() {
        Graph first = GraphGenerator.generateUndirected(20, 80);
        Graph second = GraphGenerator.generateUndirected(20, 80);

        assertEquals(first.getEdgeCount(), second.getEdgeCount());

        for (int i = 0; i < first.getEdgeCount(); i++) {
            assertEquals(
                    first.getEdges().get(i).getFrom(),
                    second.getEdges().get(i).getFrom()
            );
            assertEquals(
                    first.getEdges().get(i).getTo(),
                    second.getEdges().get(i).getTo()
            );
            assertEquals(
                    first.getEdges().get(i).getWeight(),
                    second.getEdges().get(i).getWeight()
            );
        }
    }
}
