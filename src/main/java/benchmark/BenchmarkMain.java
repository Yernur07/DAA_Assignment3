package benchmark;
import algorithms.DAGShortestPaths;
import algorithms.KahnTopologicalSort;
import algorithms.KosarajuSCC;
import algorithms.KruskalMST;
import algorithms.PrimMST;
import graph.Graph;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
public class BenchmarkMain {
    private static final int[] SIZES = {
            1_000,
            3_162,
            10_000,
            31_623,
            100_000
    };
    private static final int RUNS = 5;
    private record Measurement(
            long dsuOps,
            long edgeScans,
            long comparisons,
            int maxDepth,
            long result
    ) {
    }
    private static class BenchmarkRow {
        String algorithm;
        String graph;
        int n;
        int m;
        double timeMs;
        Measurement measurement;
        BenchmarkRow(
                String algorithm,
                String graph,
                int n,
                int m,
                double timeMs,
                Measurement measurement
        ) {
            this.algorithm = algorithm;
            this.graph = graph;
            this.n = n;
            this.m = m;
            this.timeMs = timeMs;
            this.measurement = measurement;
        }
    }
    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        Path output = Path.of("results", "results.csv");
        Files.createDirectories(output.getParent());
        StringBuilder csv = new StringBuilder();
        csv.append(
                "algorithm,graph,n,m,time_ms,dsu_ops,"
                        + "edge_scans,comparisons,max_depth,result\n"
        );
        for (int n : SIZES) {
            runDensity(n, 4, "sparse", csv);
            runDensity(n, 20, "dense", csv);
        }
        Files.writeString(output, csv.toString());
        System.out.println("Benchmark completed.");
        System.out.println("CSV file: " + output.toAbsolutePath());
        System.out.println("Data rows: " + (csv.toString().split("\n").length - 1));
    }
    private static void runDensity(
            int n,
            int density,
            String densityName,
            StringBuilder csv
    ) {
        int m = n * density;
        System.out.println(
                "\nGraph size: n=" + n
                        + ", m=" + m
                        + ", density=" + densityName
        );
        Graph undirected = GraphGenerator.generateUndirected(n, m);
        String undirectedName = "undirected_" + densityName;
        BenchmarkRow kruskal = benchmark(
                "Kruskal",
                undirectedName,
                undirected,
                () -> {
                    KruskalMST.Result result =
                            new KruskalMST().findMST(undirected);
                    return new Measurement(
                            result.getDsuOps(),
                            result.getEdgeScans(),
                            result.getComparisons(),
                            0,
                            result.getTotalWeight()
                    );
                }
        );
        BenchmarkRow prim = benchmark(
                "Prim",
                undirectedName,
                undirected,
                () -> {
                    PrimMST.Result result =
                            new PrimMST().findMST(undirected);
                    return new Measurement(
                            result.getDsuOps(),
                            result.getEdgeScans(),
                            result.getComparisons(),
                            0,
                            result.getTotalWeight()
                    );
                }
        );
        if (kruskal.measurement.result != prim.measurement.result) {
            throw new IllegalStateException(
                    "Kruskal and Prim returned different MST weights: n="
                            + n + ", m=" + m
            );
        }
        appendRow(csv, kruskal);
        appendRow(csv, prim);
        Graph directed = GraphGenerator.generateDirected(n, m);
        String directedName = "directed_" + densityName;
        BenchmarkRow kosaraju = benchmark(
                "Kosaraju",
                directedName,
                directed,
                () -> {
                    KosarajuSCC.Result result =
                            new KosarajuSCC().findSCC(directed);
                    return new Measurement(
                            0,
                            result.getEdgeScans(),
                            result.getComparisons(),
                            result.getMaxDepth(),
                            result.getComponentCount()
                    );
                }
        );
        appendRow(csv, kosaraju);
        Graph dag = GraphGenerator.generateDag(n, m);
        String dagName = "dag_" + densityName;
        BenchmarkRow kahn = benchmark(
                "Kahn",
                dagName,
                dag,
                () -> {
                    KahnTopologicalSort.Result result =
                            new KahnTopologicalSort().sort(dag);

                    if (result.hasCycle()) {
                        throw new IllegalStateException(
                                "Generated DAG contains a cycle"
                        );
                    }
                    return new Measurement(
                            0,
                            result.getEdgeScans(),
                            result.getComparisons(),
                            result.getMaxDepth(),
                            result.getOrder().size()
                    );
                }
        );
        BenchmarkRow shortestPaths = benchmark(
                "DagShortestPath",
                dagName,
                dag,
                () -> {
                    DAGShortestPaths.Result result =
                            new DAGShortestPaths().findShortestPaths(dag);
                    long sum = 0;
                    for (long distance : result.getDistances()) {
                        sum += distance;
                    }
                    return new Measurement(
                            0,
                            result.getEdgeScans(),
                            result.getComparisons(),
                            result.getMaxDepth(),
                            sum
                    );
                }
        );
        appendRow(csv, kahn);
        appendRow(csv, shortestPaths);
        System.out.println(
                "Kruskal MST weight: " + kruskal.measurement.result
        );
        System.out.println(
                "Prim MST weight: " + prim.measurement.result
        );
        System.out.println(
                "SCC count: " + kosaraju.measurement.result
        );
        System.out.println(
                "DAG shortest-path sum: "
                        + shortestPaths.measurement.result
        );
    }
    private static BenchmarkRow benchmark(
            String algorithm,
            String graphName,
            Graph graph,
            Supplier<Measurement> task
    ) {
        long[] times = new long[RUNS];
        Measurement measurement = null;
        task.get();
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            measurement = task.get();
            long end = System.nanoTime();
            times[i] = end - start;
        }
        Arrays.sort(times);
        double medianMs = times[RUNS / 2] / 1_000_000.0;
        System.out.printf(
                "%-18s n=%-6d m=%-8d median=%.3f ms%n",
                algorithm,
                graph.getVertexCount(),
                graph.getEdgeCount(),
                medianMs
        );
        return new BenchmarkRow(
                algorithm,
                graphName,
                graph.getVertexCount(),
                graph.getEdgeCount(),
                medianMs,
                measurement
        );
    }
    private static void appendRow(
            StringBuilder csv,
            BenchmarkRow row
    ) {
        Measurement m = row.measurement;
        csv.append(String.format(
                Locale.US,
                "%s,%s,%d,%d,%.6f,%d,%d,%d,%d,%d%n",
                row.algorithm,
                row.graph,
                row.n,
                row.m,
                row.timeMs,
                m.dsuOps(),
                m.edgeScans(),
                m.comparisons(),
                m.maxDepth(),
                m.result()
        ));
    }
}