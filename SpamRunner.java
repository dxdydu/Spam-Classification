import java.util.ArrayList;
import java.util.Scanner;

public class SpamRunner {
    public static void main(String[] args) {
        // Preparing for computation and user input
        SpamPreprocessing.loadStopWords("STOPWORDS.txt");
        SpamPreprocessing.loadMessageLists("spam.csv", "spam", "ham");
        SpamProcessing processor = new SpamProcessing();
        System.out.println("""
                ===========================================================================================
                NLP Spam Classifier
                ===========================================================================================
                Instructions:
                - Enter a message to classify it as spam or human.
                - Type 0 and press Enter to exit.
                - The higher the score, the more similar the text is to spam.
                - Score ranges from negative infinity to infinity.
                - Stopwords from: https://gist.github.com/sebleier/554280
                - Spam/Ham dataset from: https://www.kaggle.com/datasets/uciml/sms-spam-collection-dataset
                ===========================================================================================
                """);
        // Iteratively asking user for input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Input your text or 0: ");
            String input = scanner.nextLine().trim();
            // Terminates program when user inputs 0
            if (input.equals("0")) break;
            // Checks if user inputted a message
            if (input.isEmpty()) {
                System.out.println("Please enter a non-empty message.");
                continue;
            }
            // User's message is processed, computed for spam similarity, and classified
            ArrayList<String> tokenizedInput = SpamPreprocessing.preprocess(input);
            double score = processor.computeScore(tokenizedInput);
            System.out.println("Score: " + score);
            if (processor.isSpamText(score)) {
                System.out.println("[SPAM] This text is classified as spam.");
            } else {
                System.out.println("[HUMAN] This text is classified as human.");
            }
            System.out.println();
        }
        scanner.close();
        // Display end message
        System.out.println();
        System.out.println("Program has ended. Thank you for using the Spam Classifier.");
    }
}
