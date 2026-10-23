import csv
import os
from collections import defaultdict
import matplotlib.pyplot as plt
CSV_FILE = "results/results.csv"
OUTPUT_DIR = "results/plots"
os.makedirs(OUTPUT_DIR, exist_ok=True)
rows = []
with open(CSV_FILE, "r", newline="", encoding="utf-8") as file:
    reader = csv.DictReader(file)
    for row in reader:
        row["n"] = int(row["n"])
        row["m"] = int(row["m"])
        row["time_ms"] = float(row["time_ms"])
        row["dsu_ops"] = int(row["dsu_ops"])
        row["edge_scans"] = int(row["edge_scans"])
        row["comparisons"] = int(row["comparisons"])
        row["max_depth"] = int(row["max_depth"])
        row["result"] = int(row["result"])
        rows.append(row)
def density_of(graph_name):
    if graph_name.endswith("sparse"):
        return "sparse"
    return "dense"
operations = defaultdict(dict)
for row in rows:
    algorithm = row["algorithm"]
    density = density_of(row["graph"])
    n = row["n"]
    total = row["dsu_ops"] + row["edge_scans"] + row["comparisons"]
    operations[(algorithm, density)][n] = total
plt.figure(figsize=(11, 7))
for (algorithm, density), values in sorted(operations.items()):
    sizes = sorted(values.keys())
    counts = [values[n] for n in sizes]
    plt.plot(
        sizes,
        counts,
        marker="o",
        label=f"{algorithm} - {density}"
    )
plt.xscale("log")
plt.yscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Total counted operations")
plt.title("Operation Growth vs Graph Size")
plt.grid(True, which="both")
plt.legend(fontsize=8)
plt.tight_layout()
plt.savefig(os.path.join(OUTPUT_DIR, "operations_vs_n.png"), dpi=200)
plt.close()
scan_ratios = defaultdict(dict)
for row in rows:
    algorithm = row["algorithm"]
    density = density_of(row["graph"])
    n = row["n"]
    m = row["m"]
    ratio = row["edge_scans"] / m if m else 0
    scan_ratios[(algorithm, density)][n] = ratio
plt.figure(figsize=(11, 7))
for (algorithm, density), values in sorted(scan_ratios.items()):
    sizes = sorted(values.keys())
    ratios = [values[n] for n in sizes]
    plt.plot(
        sizes,
        ratios,
        marker="o",
        label=f"{algorithm} - {density}"
    )
plt.xscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Edge scans / m")
plt.title("Edge Scans per Input Edge")
plt.grid(True, which="both")
plt.legend(fontsize=8)
plt.tight_layout()
plt.savefig(os.path.join(OUTPUT_DIR, "scans_per_edge.png"), dpi=200)
plt.close()
kruskal_ratios = defaultdict(dict)
for row in rows:
    if row["algorithm"] != "Kruskal":
        continue
    density = density_of(row["graph"])
    n = row["n"]
    m = row["m"]
    kruskal_ratios[density][n] = row["edge_scans"] / m if m else 0
plt.figure(figsize=(10, 6))
for density, values in sorted(kruskal_ratios.items()):
    sizes = sorted(values.keys())
    ratios = [values[n] for n in sizes]
    plt.plot(
        sizes,
        ratios,
        marker="o",
        label=density
    )
plt.xscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Kruskal edge scans / m")
plt.title("Kruskal: Fraction of Edges Scanned")
plt.grid(True, which="both")
plt.legend()
plt.tight_layout()
plt.savefig(os.path.join(OUTPUT_DIR, "kruskal_scans_ratio.png"), dpi=200)
plt.close()
print("All plots saved to:", OUTPUT_DIR)
print("1. operations_vs_n.png")
print("2. scans_per_edge.png")
print("3. kruskal_scans_ratio.png")
