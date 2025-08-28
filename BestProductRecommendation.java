import java.io.*;
import java.util.*;

public class BestProductRecommendation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product category (e.g., Laptop, Tablet, Smartwatch): ");
        String inputCategory = scanner.nextLine();// Read the category entered by user

        // Path to the CSV file containing product ratings
        String csvFile = "src/ratings.csv";
        String line;
        Map<String, List<Double>> productRatings = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            // Read each line of the CSV file
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String category = parts[1];
                String brand = parts[2];
                String model = parts[3];
                double rating = Double.parseDouble(parts[4]);

                if (!category.equalsIgnoreCase(inputCategory)) continue;

                String productKey = brand + " " + model;
                productRatings.putIfAbsent(productKey, new ArrayList<>());
                productRatings.get(productKey).add(rating);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If no products were found for the entered category, exit program
        if (productRatings.isEmpty()) {
            System.out.println("No data found for this category.");
            return;
        }

        Map<String, Double> avgRatings = new HashMap<>();
        for (String product : productRatings.keySet()) {
            List<Double> ratings = productRatings.get(product);
            double avg = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgRatings.put(product, avg);
        }

        String bestProduct = Collections.max(avgRatings.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Best product for " + inputCategory + " based on ratings: " + bestProduct);

        System.out.println("All products with average ratings:");
        avgRatings.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
                scanner.close();
    }
}
