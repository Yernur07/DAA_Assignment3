
package graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {

    private final int vertexCount;
    private final boolean directed;

    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int vertexCount, boolean directed) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException(
                    "Number of vertices cannot be negative"
            );
        }

        this.vertexCount = vertexCount;
        this.directed = directed;

        adjacencyList = new ArrayList<>();
        edges = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public void addEdge(int from, int to, int weight) {
        checkVertex(from);
        checkVertex(to);

        Edge edge = new Edge(from, to, weight);

        edges.add(edge);
        adjacencyList.get(from).add(edge);

        if (!directed && from != to) {
            Edge reverseEdge = new Edge(to, from, weight);
            adjacencyList.get(to).add(reverseEdge);
        }
    }

    public List<Edge> getAdjacentEdges(int vertex) {
        checkVertex(vertex);

        return Collections.unmodifiableList(
                adjacencyList.get(vertex)
        );
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public Graph reverse() {
        Graph reversed = new Graph(vertexCount, directed);

        for (Edge edge : edges) {
            if (directed) {
                reversed.addEdge(
                        edge.getTo(),
                        edge.getFrom(),
                        edge.getWeight()
                );
            } else {
                reversed.addEdge(
                        edge.getFrom(),
                        edge.getTo(),
                        edge.getWeight()
                );
            }
        }

        return reversed;
    }

    public static Graph readFromFile(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String firstLine = reader.readLine();

            if (firstLine == null) {
                throw new IOException("Graph file is empty");
            }

            String[] header = firstLine.trim().split("\\s+");

            if (header.length != 3) {
                throw new IOException(
                        "Header must be: n m directed|undirected"
                );
            }

            int n = Integer.parseInt(header[0]);
            int m = Integer.parseInt(header[1]);

            boolean directed;

            if (header[2].equalsIgnoreCase("directed")) {
                directed = true;
            } else if (header[2].equalsIgnoreCase("undirected")) {
                directed = false;
            } else {
                throw new IOException(
                        "Graph type must be directed or undirected"
                );
            }

            if (n < 0 || m < 0) {
                throw new IOException(
                        "Number of vertices and edges cannot be negative"
                );
            }

            Graph graph = new Graph(n, directed);

            for (int i = 0; i < m; i++) {
                String line = reader.readLine();

                if (line == null) {
                    throw new IOException(
                            "Expected " + m + " edges, found " + i
                    );
                }

                String[] parts = line.trim().split("\\s+");

                if (parts.length != 3) {
                    throw new IOException(
                            "Invalid edge on line " + (i + 2)
                    );
                }

                try {
                    int from = Integer.parseInt(parts[0]);
                    int to = Integer.parseInt(parts[1]);
                    int weight = Integer.parseInt(parts[2]);

                    graph.addEdge(from, to, weight);

                } catch (NumberFormatException e) {
                    throw new IOException(
                            "Invalid number on line " + (i + 2), e
                    );
                } catch (IllegalArgumentException e) {
                    throw new IOException(
                            "Invalid vertex on line " + (i + 2), e
                    );
                }
            }
            return graph;
        } catch (NumberFormatException e) {
            throw new IOException("Invalid graph header", e);
        }
    }
    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= vertexCount) {
            throw new IllegalArgumentException(
                    "Invalid vertex: " + vertex
            );
        }
    }
}

