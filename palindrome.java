import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

// Custom Exception for invalid input
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

// Runnable task for palindrome check using recursion
class PalindromeTask implements Runnable {
    private final String input;

    public PalindromeTask(String input) {
        this.input = input;
    }

    // Recursive function to check palindrome
    private boolean isPalindromeRecursive(String str, int left, int right) {
        if (left >= right) return true;
        return (str.charAt(left) == str.charAt(right)) && isPalindromeRecursive(str, left + 1, right - 1);
    }

    @Override
    public void run() {
        boolean result = isPalindromeRecursive(input, 0, input.length() - 1);
        System.out.println("Thread: " + Thread.currentThread().getName() +
                " | Input: " + input + " | Palindrome: " + result);
    }
}

public class ComplexPalindromeChecker {
    // Function to clean input (remove spaces, special characters, convert to lowercase)
    public static String cleanInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    // Check using Java Streams
    public static boolean isPalindromeStream(String input) {
        return IntStream.range(0, input.length() / 2)
                .allMatch(i -> input.charAt(i) == input.charAt(input.length() - i - 1));
    }

    public static void main(String[] args) {
        String[] testCases = {"Racecar", "Hello", "A man, a plan, a canal: Panama", "123321", "Not a Palindrome"};

        ExecutorService executor = Executors.newFixedThreadPool(3); // Multithreading for efficiency

        for (String test : testCases) {
            try {
                if (test.isEmpty()) throw new InvalidInputException("Input cannot be empty!");

                String cleaned = cleanInput(test);
                System.out.println("Checking: " + test + " (Processed: " + cleaned + ")");

                // Check palindrome using Streams
                boolean isStreamPalindrome = isPalindromeStream(cleaned);
                System.out.println("Stream Method: " + isStreamPalindrome);

                // Multithreading: Execute recursive palindrome check
                executor.execute(new PalindromeTask(cleaned));

            } catch (InvalidInputException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        executor.shutdown();
    }
}
