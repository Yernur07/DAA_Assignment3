
package algorithms;

import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KosarajuSCCTest {

    @Test
    void findsTwoStronglyConnectedComponents() {
        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        KosarajuSCC algorithm = new KosarajuSCC();
        KosarajuSCC.Result result = algorithm.findSCC(graph);

        assertEquals(2, result.getComponentCount());

        List<List<Integer>> components =
                result.getStronglyConnectedComponents();

        assertTrue(components.stream().anyMatch(
                c -> new HashSet<>(c).equals(Set.of(0, 1, 2))
        ));

        assertTrue(components.stream().anyMatch(
                c -> new HashSet<>(c).equals(Set.of(3, 4))
        ));
    }

    @Test
    void isolatedVerticesAreSeparateComponents() {
        Graph graph = new Graph(3, true);

        KosarajuSCC.Result result =
                new KosarajuSCC().findSCC(graph);

        assertEquals(3, result.getComponentCount());
    }

    @Test
    void handlesEmptyGraph() {
        Graph graph = new Graph(0, true);

        KosarajuSCC.Result result =
                new KosarajuSCC().findSCC(graph);

        assertEquals(0, result.getComponentCount());
        assertEquals(0, result.getMaxDepth());
    }

    @Test
    void rejectsUndirectedGraph() {
        Graph graph = new Graph(3, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> new KosarajuSCC().findSCC(graph)
        );
    }

    @Test
    void handlesLongChainWithoutRecursion() {
        int n = 20_000;
        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        KosarajuSCC.Result result =
                new KosarajuSCC().findSCC(graph);

        assertEquals(n, result.getComponentCount());
        assertTrue(result.getMaxDepth() > 0);
    }
}
