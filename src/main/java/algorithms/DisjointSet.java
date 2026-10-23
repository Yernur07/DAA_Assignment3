
package algorithms;

public class DisjointSet {

    private final int[] parent;
    private final int[] size;

    private int components;
    private long findCalls;
    private long successfulUnions;

    public DisjointSet(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Number of elements cannot be negative"
            );
        }

        parent = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        checkElement(x);
        findCalls++;

        // Path compression
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }

        return x;
    }

    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        // Already in the same set
        if (rootA == rootB) {
            return false;
        }

        // Union by size
        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];

        components--;
        successfulUnions++;

        return true;
    }

    public int getComponents() {
        return components;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getSuccessfulUnions() {
        return successfulUnions;
    }

    public void resetCounters() {
        findCalls = 0;
        successfulUnions = 0;
    }

    private void checkElement(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IllegalArgumentException(
                    "Invalid element: " + x
            );
        }
    }
}

