import java.io.File;
import java.util.*;

/**
 * The SpamPreprocessing class loads a list of stop words and the dataset of spam/ham messages.
 * The class processes a string, user input or from spam/ham dataset, into tokenized text, with stopwords removed.
 */
public class SpamPreprocessing {
    private static ArrayList<String> stopWords;
    private static ArrayList<ArrayList<String>> spamMessages;
    private static ArrayList<ArrayList<String>> humanMessages;
    private static final int SEED = 67;

    /**
     * Loads stop words from a text file.
     * Preconditions: fileName points to a readable text file, with one stop word per line.
     * Postconditions: stopWords is initialized with all words in lowercase.
     * @param fileName Path to the stop words text file.
     */
    public static void loadStopWords(String fileName) {
        stopWords = new ArrayList<>();
        try (Scanner input = new Scanner(new File(fileName))) {
            while (input.hasNextLine()) {
                stopWords.add(input.nextLine().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("Error reading or parsing " + fileName);
        }
    }

    /**
     * Loads and preprocesses messages from a CSV file, balancing spam and human messages.
     * Preconditions: fileName points to a CSV file with each row containing a label and a message. spamLabel and hamLabel match the CSV labels.
     * Postconditions: spamMessages and humanMessages are initialized, equal in size, and contain preprocessed messages.
     * @param fileName Path to the CSV file.
     * @param spamLabel Label for spam messages.
     * @param hamLabel Label for human messages.
     */
    public static void loadMessageLists(String fileName, String spamLabel, String hamLabel) {
        spamMessages = new ArrayList<>();
        humanMessages = new ArrayList<>();
        try (Scanner input = new Scanner(new File(fileName))) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                String label = parts[0].trim();
                String message = parts[1].trim();
                if (message.startsWith("\"") && message.endsWith("\"")) {
                    message = message.substring(1, message.length() - 1);
                }
                ArrayList<String> preprocessedText = preprocess(message);
                if (label.equals(spamLabel) && !preprocessedText.isEmpty()) spamMessages.add(preprocessedText);
                if (label.equals(hamLabel) && !preprocessedText.isEmpty()) humanMessages.add(preprocessedText);
            }
            int minSize = Math.min(spamMessages.size(), humanMessages.size());
            Collections.shuffle(spamMessages, new Random(SEED));
            Collections.shuffle(humanMessages, new Random(SEED));
            while (spamMessages.size() > minSize) spamMessages.remove(spamMessages.size() - 1);
            while (humanMessages.size() > minSize) humanMessages.remove(humanMessages.size() - 1);
        } catch (Exception e) {
            System.out.println("Error reading or parsing " + fileName);
        }
    }

    /**
     * Tokenizes a string into lowercase words, excluding numbers and symbols.
     * 
     * Preconditions: text is non-null.
     * Postconditions: Returns a list of lowercase word tokens with no empty or numeric tokens.
     * @param text Input text.
     * @return ArrayList of word tokens.
     */
    private static ArrayList<String> tokenize(String text) {
        String[] temp = text.split("\\W+");
        ArrayList<String> tokens = new ArrayList<>();
        for (String token : temp) {
            if (!token.isEmpty() && !token.matches("\\d+")) {
                tokens.add(token.toLowerCase());
            }
        }
        return tokens;
    }

    /**
     * Removes stop words from a tokenized message.
     * 
     * Preconditions: stopWords have been loaded.
     * Postconditions: tokenizedText contains no stop words.
     * @param tokenizedText ArrayList of tokens to filter.
     */
    private static void removeStopWords(ArrayList<String> tokenizedText) {
        if (stopWords == null) {
            System.out.println("Stop words not loaded.");
        }
        for (int i = 0; i < tokenizedText.size(); i++) {
            if (stopWords.contains(tokenizedText.get(i))) {
                tokenizedText.remove(i);
                i--;
            }
        }
    }

    /**
     * Preprocesses a message by tokenizing and removing stop words.
     * 
     * Preconditions: text is not null. Stop words have been loaded.
     * Postconditions: Returns a list of lowercase tokens with stop words removed.
     * @param text Input message.
     * @return ArrayList of processed tokens.
     */
    public static ArrayList<String> preprocess(String text) {
        if (text == null || text.trim().isEmpty()) {
            System.out.println("null or empty string");
        }
        ArrayList<String> processedText = tokenize(text);
        removeStopWords(processedText);
        return processedText;
    }

    /**
     * Returns the list of preprocessed human messages.
     * 
     * Preconditions: loadMessageLists has been called.
     * Postconditions: Returns the list of human messages.
     * @return ArrayList of preprocessed human messages.
     */
    public static ArrayList<ArrayList<String>> getHumanMessages() {
        return humanMessages;
    }

    /**
     * Returns the list of preprocessed spam messages.
     * 
     * Preconditions: loadMessageLists has been called.
     * Postconditions: Returns the list of spam messages.
     * @return ArrayList of preprocessed spam messages.
     */
    public static ArrayList<ArrayList<String>> getSpamMessages() {
        return spamMessages;
    }
}
