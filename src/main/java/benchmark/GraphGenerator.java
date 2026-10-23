package benchmark;
import graph.Graph;
import java.util.Random;
public class GraphGenerator {
    private static final int SEED = 42;
    private GraphGenerator() {
    }
    private static void shuffle(int[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    public static Graph generateUndirected(int n, int m) {
        if (n < 1 || m < n - 1 || (long) m > (long) n * (n - 1) / 2) {
            throw new IllegalArgumentException("Invalid undirected graph size");
        }
        Random random = new Random(SEED);
        Graph graph = new Graph(n, false);
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        shuffle(perm, random);
        for (int i = 1; i < n; i++) {
            int parent = perm[random.nextInt(i)];
            int child = perm[i];
            int weight = 1 + random.nextInt(1000);
            graph.addEdge(parent, child, weight);
        }
        int addedEdges = n - 1;
        while (addedEdges < m) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);
            if (a == b) {
                continue;
            }
            int weight = 1 + random.nextInt(1000);
            graph.addEdge(a, b, weight);
            addedEdges++;
        }
        return graph;
    }
    public static Graph generateDirected(int n, int m) {
        if (n < 1 || m < 0 || (long) m > (long) n * (n - 1)) {
            throw new IllegalArgumentException("Invalid directed graph size");
        }
        Random random = new Random(SEED);
        Graph graph = new Graph(n, true);
        int addedEdges = 0;
        while (addedEdges < m) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);
            if (a == b) {
                continue;
            }
            int weight = 1 + random.nextInt(1000);
            graph.addEdge(a, b, weight);
            addedEdges++;
        }
        return graph;
    }
    public static Graph generateDag(int n, int m) {
        if (n < 1 || m < 0 || (long) m > (long) n * (n - 1) / 2) {
            throw new IllegalArgumentException("Invalid DAG size");
        }
        Random random = new Random(SEED);
        Graph graph = new Graph(n, true);
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[i] = i;
        }
        shuffle(rank, random);
        int addedEdges = 0;
        while (addedEdges < m) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);
            if (a == b) {
                continue;
            }
            if (rank[a] > rank[b]) {
                int temp = a;
                a = b;
                b = temp;
            }
            int weight = 1 + random.nextInt(1000);
            graph.addEdge(a, b, weight);
            addedEdges++;
        }
        return graph;
    }
}
