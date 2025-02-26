import java.util.Arrays;

public class LowestInvestment {

    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {

        // Compute initial candidates for low and high
        int[] candidates = {
            returns1[0] * returns2[0],
            returns1[0] * returns2[returns2.length - 1],
            returns1[returns1.length - 1] * returns2[0],
            returns1[returns1.length - 1] * returns2[returns2.length - 1]
        };
        long low = Arrays.stream(candidates).min().getAsInt();
        long high = Arrays.stream(candidates).max().getAsInt();

        // Binary search for the k-th smallest product
        while (low < high) {
            long mid = low + (high - low) / 2;
            long count = 0;
            for (int a : returns1) {
                if (a == 0) {
                    if (mid >= 0) {
                        count += returns2.length;
                    }
                } else if (a > 0) {
                    double target = (double) mid / a;
                    int cnt = bisectRight(returns2, target);
                    count += cnt;
                } else {
                    double target = (double) mid / a;
                    int cnt = returns2.length - bisectLeft(returns2, target);
                    count += cnt;
                }
            }
            if (count >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        // Return high as integer
        return (int) high;
    }

    // Helper function to find the rightmost index where the element is <= target
    private static int bisectRight(int[] arr, double target) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // Helper function to find the leftmost index where the element is >= target
    private static int bisectLeft(int[] arr, double target) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println(kthSmallestProduct(returns1, returns2, k)); // Output: 8

        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        int k2 = 6;
        System.out.println(kthSmallestProduct(returns3, returns4, k2)); // Output: 0
    }
}