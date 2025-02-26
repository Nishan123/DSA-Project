import java.util.*;

public class Solution {

    // Main method to test the solution with sample inputs.
    public static void main(String[] args) {
        // Example 1
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {
            {0, 1},
            {1, 2},
            {2, 3},
            {3, 4},
            {4, 5}
        };
        System.out.println("Output (Example 1): " + minRoads(packages1, roads1)); // Expected 2

        // Example 2
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {
            {0, 1},
            {0, 2},
            {1, 3},
            {1, 4},
            {2, 5},
            {5, 6},
            {5, 7}
        };
        System.out.println("Output (Example 2): " + minRoads(packages2, roads2)); // Expected 2
    }

    /**
     * Computes the minimum number of roads to traverse in order to collect all packages.
     *
     * @param packages an array where packages[i]==1 means there is a package at location i.
     * @param roads a 2D array where each element is an edge [a, b] indicating a road between a and b.
     * @return the minimum number of road traversals required.
     */
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        
        // If there are no packages, no roads need to be traversed.
        boolean anyPackage = false;
        for (int p : packages) {
            if (p == 1) {
                anyPackage = true;
                break;
            }
        }
        if (!anyPackage) return 0;
        
        // Build the undirected graph.
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : roads) {
            int u = edge[0], v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // Compute all-pairs shortest distances.
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            bfs(i, graph, dist[i]);
        }
        
        int minAns = Integer.MAX_VALUE;
        // We'll try every nonempty subset S ⊆ {0,1,...,n-1} as candidate "stops"
        // where if you stand on S, you automatically collect any package located
        // at a node that is within distance 2 of at least one stop in S.
        // We represent S as a bitmask of length n.
        int totalSubsets = 1 << n;
        for (int mask = 1; mask < totalSubsets; mask++) {
            // Check if S (the set of stops given by 'mask') covers every package.
            if (!coversPackages(mask, packages, dist)) {
                continue;
            }
            // Build a list of the nodes (stops) in this subset.
            List<Integer> stops = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (((mask >> j) & 1) == 1) {
                    stops.add(j);
                }
            }
            // Compute the minimum tour that visits all stops (and returns to the start)
            // using TSP DP over the stops. We use the complete graph with edge weights
            // given by dist[u][v].
            int tourCost = tspTour(stops, dist);
            minAns = Math.min(minAns, tourCost);
        }
        return minAns == Integer.MAX_VALUE ? -1 : minAns;
    }
    
    /**
     * Standard BFS from source 's' to compute shortest distances in an unweighted graph.
     * Fills the distances array 'd' (d[i] = distance from s to i).
     */
    private static void bfs(int s, List<Integer>[] graph, int[] d) {
        Queue<Integer> q = new LinkedList<>();
        d[s] = 0;
        q.offer(s);
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int nei : graph[cur]) {
                if (d[nei] == Integer.MAX_VALUE) {
                    d[nei] = d[cur] + 1;
                    q.offer(nei);
                }
            }
        }
    }
    
    /**
     * Checks whether the set of stops (given as bitmask over 0..n-1) covers all package locations.
     * A package at node i is covered if there exists a stop v (i.e. a bit set in 'mask')
     * with dist[i][v] <= 2.
     */
    private static boolean coversPackages(int mask, int[] packages, int[][] dist) {
        int n = packages.length;
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                boolean covered = false;
                for (int j = 0; j < n; j++) {
                    if (((mask >> j) & 1) == 1) {
                        if (dist[i][j] <= 2) {
                            covered = true;
                            break;
                        }
                    }
                }
                if (!covered) return false;
            }
        }
        return true;
    }
    
    /**
     * Solves a TSP (minimum cycle) problem on the given list of stops.
     * stops: list of node indices that are included in the tour.
     * dist: the full distance matrix (where dist[u][v] is the shortest distance from u to v).
     *
     * We fix the first stop as the starting/ending point.
     */
    private static int tspTour(List<Integer> stops, int[][] dist) {
        int k = stops.size();
        int fullMask = (1 << k) - 1;
        // dp[mask][i] = minimum cost to have visited the stops in 'mask' (subset of indices in stops)
        // and ending at stops.get(i). We fix index 0 as the start.
        int[][] dp = new int[1 << k][k];
        for (int i = 0; i < (1 << k); i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[1 << 0][0] = 0;
        
        // Iterate over all masks.
        for (int mask = 0; mask < (1 << k); mask++) {
            for (int i = 0; i < k; i++) {
                if (((mask >> i) & 1) == 1 && dp[mask][i] != Integer.MAX_VALUE) {
                    for (int j = 0; j < k; j++) {
                        if (((mask >> j) & 1) == 0) { // j not yet visited
                            int nextMask = mask | (1 << j);
                            int cost = dp[mask][i] + dist[stops.get(i)][stops.get(j)];
                            dp[nextMask][j] = Math.min(dp[nextMask][j], cost);
                        }
                    }
                }
            }
        }
        // Complete the cycle by returning to the starting stop.
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < k; i++) {
            if (dp[fullMask][i] != Integer.MAX_VALUE) {
                best = Math.min(best, dp[fullMask][i] + dist[stops.get(i)][stops.get(0)]);
            }
        }
        return best;
    }
}
