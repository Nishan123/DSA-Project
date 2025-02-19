public class ClosestPair{
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2];
        
        // Compare all pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    // Calculate Manhattan distance
                    int distance = Math.abs(x_coords[i] - x_coords[j]) + 
                                 Math.abs(y_coords[i] - y_coords[j]);
                    
                    // Update if we found a smaller distance
                    // or if distance is equal but current pair is lexicographically smaller
                    if (distance < minDistance || 
                        (distance == minDistance && (i < result[0] || 
                        (i == result[0] && j < result[1])))) {
                        minDistance = distance;
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // Test case
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};
        
        int[] result = findClosestPair(x_coords, y_coords);
        System.out.println("Closest pair indices: [" + result[0] + ", " + result[1] + "]");
    }
}