import java.util.ArrayList;

/**
 * The SpamProcessing class determines the similarity of a tokenized message to spam
 * by comparing token frequencies in spam and human messages.
 */
public class SpamProcessing {
    private double threshold;

    /**
     * Constructs a SpamProcessing object with a default threshold.
     * 
     * Preconditions: None.
     * Postconditions: threshold is set to an experimentally determined value.
     */
    public SpamProcessing() {
        this.threshold = 0.7787889031513116;
    }

    /**
     * Constructs a SpamProcessing object with a custom threshold.
     * 
     * Preconditions: threshold is a valid double.
     * Postconditions: threshold is set to the provided value.
     * @param threshold Score above which a message is classified as spam.
     */
    public SpamProcessing(double threshold) {
        this.threshold = threshold;
    }

    /**
     * Counts the occurrences of a token in a tokenized message.
     * 
     * Preconditions: tokenizedText is not null.
     * Postconditions: Returns the number of times the token appears in the list.
     * @param token Token to count.
     * @param tokenizedText ArrayList of tokens to search.
     * @return Frequency count of the token.
     */
    private int countFrequency(String token, ArrayList<String> tokenizedText) {
        int count = 0;
        for (String t : tokenizedText) {
            if (token.equals(t)) count++;
        }
        return count;
    }

    /**
     * Computes a spam score for a tokenized message.
     * 
     * Preconditions: tokenizedText is preprocessed. SpamPreprocessing lists are loaded.
     * Postconditions: Returns a double representing similarity to spam (positive = more spam-like).
     * @param tokenizedText Preprocessed ArrayList of tokens.
     * @return Spam similarity score.
     */
    public double computeScore(ArrayList<String> tokenizedText) {
        double score = 0.0;
        ArrayList<ArrayList<String>> spamMessages = SpamPreprocessing.getSpamMessages();
        ArrayList<ArrayList<String>> humanMessages = SpamPreprocessing.getHumanMessages();
        for (String word : tokenizedText) {
            double tokenTextRatio = (double) countFrequency(word, tokenizedText) / tokenizedText.size();
            for (ArrayList<String> humanMessage : humanMessages) {
                double humanTextRatio = -1 * (double) countFrequency(word, humanMessage) / humanMessage.size();
                score += tokenTextRatio * humanTextRatio;
            }
            for (ArrayList<String> spamMessage : spamMessages) {
                double spamTextRatio = (double) countFrequency(word, spamMessage) / spamMessage.size();
                score += tokenTextRatio * spamTextRatio;
            }
        }
        return score;
    }

    /**
     * Determines whether a spam score classifies the message as spam.
     * 
     * Preconditions: threshold is set, score is computed from computeScore().
     * Postconditions: Returns true if score exceeds threshold, false otherwise.
     * @param score Computed spam score.
     * @return True if spam, false if human.
     */
    public boolean isSpamText(double score) {
        return score > threshold;
    }
}
