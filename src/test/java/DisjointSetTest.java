
import algorithms.DisjointSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisjointSetTest {

    @Test
    void shouldStartWithSeparateComponents() {
        DisjointSet dsu = new DisjointSet(5);

        assertEquals(5, dsu.getComponents());
    }

    @Test
    void shouldMergeComponents() {
        DisjointSet dsu = new DisjointSet(5);

        assertTrue(dsu.union(0, 1));
        assertTrue(dsu.union(1, 2));

        assertEquals(3, dsu.getComponents());
        assertEquals(dsu.find(0), dsu.find(2));
    }

    @Test
    void shouldNotCountRepeatedUnion() {
        DisjointSet dsu = new DisjointSet(3);

        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(0, 1));

        assertEquals(2, dsu.getComponents());
        assertEquals(1, dsu.getSuccessfulUnions());
    }

    @Test
    void shouldTrackFindCalls() {
        DisjointSet dsu = new DisjointSet(3);

        dsu.find(0);
        dsu.union(0, 1);

        assertEquals(3, dsu.getFindCalls());
    }

    @Test
    void shouldRejectInvalidElement() {
        DisjointSet dsu = new DisjointSet(3);

        assertThrows(
                IllegalArgumentException.class,
                () -> dsu.find(3)
        );
    }
}

