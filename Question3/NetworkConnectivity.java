package Question3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkConnectivity {
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
    
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
    
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
    
        public void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            if (rank[px] < rank[py]) {
                parent[px] = py;
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
            } else {
                parent[py] = px;
                rank[px]++;
            }
        }
    }

    private static class Connection implements Comparable<Connection> {
        int device1, device2, cost;

        public Connection(int device1, int device2, int cost) {
            this.device1 = device1;
            this.device2 = device2;
            this.cost = cost;
        }

        @Override
        public int compareTo(Connection other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    public static int minCost(int n, int[] modules, int[][] connections) {
        List<Connection> allConnections = new ArrayList<>();
        for (int[] conn : connections) {
            allConnections.add(new Connection(conn[0] - 1, conn[1] - 1, conn[2]));
        }
        Collections.sort(allConnections);

        // Try using Kruskal's algorithm first without any modules
        int directConnectionCost = kruskalMST(n, allConnections);
        if (directConnectionCost == -1) {
            return -1; // Network cannot be connected
        }

        // Try combinations with modules
        int minTotalCost = directConnectionCost;
        for (int i = 0; i < n; i++) {
            UnionFind uf = new UnionFind(n);
            int currentCost = modules[i];
            
            // Connect all devices to the device with module
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    uf.union(i, j);
                }
            }

            // Add necessary direct connections
            for (Connection conn : allConnections) {
                if (uf.find(conn.device1) != uf.find(conn.device2)) {
                    currentCost += conn.cost;
                    uf.union(conn.device1, conn.device2);
                }
            }

            minTotalCost = Math.min(minTotalCost, currentCost);
        }

        return minTotalCost;
    }

    private static int kruskalMST(int n, List<Connection> connections) {
        UnionFind uf = new UnionFind(n);
        int totalCost = 0;
        int edges = 0;

        for (Connection conn : connections) {
            if (uf.find(conn.device1) != uf.find(conn.device2)) {
                uf.union(conn.device1, conn.device2);
                totalCost += conn.cost;
                edges++;
            }
        }

        // Check if the network is fully connected
        return edges == n - 1 ? totalCost : -1;
    }

    public static void main(String[] args) {
        // Test case
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {{1, 2, 1}, {2, 3, 1}};
        
        System.out.println("Minimum cost: " + minCost(n, modules, connections));
    }
}
