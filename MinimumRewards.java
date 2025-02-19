public class MinimumRewards {

    public static int minRewards(int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        
        int n = ratings.length;
        int[] rewards = new int[n];
        
        // Step 1: Every employee gets at least one reward
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }
        
        // Step 2: Left-to-right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Step 3: Right-to-left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        // Step 4: Sum all rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }

    public static void main(String[] args) {
        // Example 1
        int[] ratings1 = {1, 0, 2};
        int result1 = minRewards(ratings1);
        System.out.println("Minimum rewards for ratings1: " + result1); // Expected output: 5

        // Example 2
        int[] ratings2 = {1, 2, 2};
        int result2 = minRewards(ratings2);
        System.out.println("Minimum rewards for ratings2: " + result2); // Expected output: 4
    }
}
