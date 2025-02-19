public class MinMeasurements{
    public static int minMeasurements(int k, int n) {
        long target = (long) n + 1;
        if (target == 0) {
            return 0;
        }
        int low = 0;
        int high = 1;
        while (!isPossible(k, target, high)) {
            high *= 2;
        }
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (isPossible(k, target, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private static boolean isPossible(int k, long target, int m) {
        long sum = 0;
        long currentTerm = 1;
        sum += currentTerm;
        if (sum >= target) {
            return true;
        }

        int maxI = Math.min(k, m);
        for (int i = 1; i <= maxI; i++) {
            if (currentTerm > Long.MAX_VALUE / (m - i + 1)) {
                sum = target;
                break;
            }
            currentTerm *= (m - i + 1);
            currentTerm /= i;
            sum += currentTerm;
            if (sum >= target) {
                return true;
            }
            if (sum < 0) {
                sum = target;
                break;
            }
        }
        return sum >= target;
    }

    public static void main(String[] args) {
        System.out.println(minMeasurements(3, 100)); 
    }
}