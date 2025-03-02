import java.util.*;
import java.util.regex.*;

public class TrendingHashtags {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Integer> hashtagCount = new HashMap<>();
        Pattern hashtagPattern = Pattern.compile("#\\w+");

        System.out.println("Enter 7 tweets (Each field will be entered separately):");

        // Loop to take 7 tweet inputs
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nTweet " + i + ":");

            System.out.print("Enter User ID: ");
            int userId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Tweet ID: ");
            int tweetId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Tweet Text: ");
            String tweetText = scanner.nextLine().trim();

            System.out.print("Enter Tweet Date (YYYY-MM-DD): ");
            String tweetDate = scanner.nextLine().trim();

            // Extract hashtags from tweet text
            Matcher matcher = hashtagPattern.matcher(tweetText);
            while (matcher.find()) {
                String hashtag = matcher.group().toLowerCase(); // Convert to lowercase
                hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
            }
        }
        scanner.close();

        // Sort hashtags by count (descending) and then by name (descending)
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
        sortedHashtags.sort((a, b) -> {
            int countCompare = b.getValue().compareTo(a.getValue()); // Descending count
            return countCompare != 0 ? countCompare : b.getKey().compareTo(a.getKey()); // Descending name
        });

        // Display top 3 trending hashtags
        System.out.println("\n+------------+-------+");
        System.out.println("| Hashtag    | Count |");
        System.out.println("+------------+-------+");
        for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
            System.out.printf("| %-10s | %5d |\n", sortedHashtags.get(i).getKey(), sortedHashtags.get(i).getValue());
        }
        System.out.println("+------------+-------+");
    }
}
