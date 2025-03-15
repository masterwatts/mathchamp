import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    static final Scanner input = new Scanner(System.in);
    static User user;

    static long startTimestamp;

    public static void main(String[] args) {
        startTimestamp = System.currentTimeMillis();

        clearConsole();
        System.out.print("Hello wise mathematician! Will you be able to correctly recognize the type of the following mighty mathematical sequences?\n> ");

        // Does the response suggest that the user wants to continue the game?
        if (!Arrays.asList("yes", "yeah", "yea", "y").contains(input.nextLine().toLowerCase())) {
            System.out.println("Oh no! I guess we'll meet later... Until then, wise mathematician!");
            System.exit(1);
        }

        clearConsole();
        System.out.print("Great! Now let's get started...\nWise mathematician, what's your name?\n> ");

        // Initialize a new user with the name input by the user
        user = new User(
                input.nextLine()
        );

        // Continues asking questions until user reaches level 10
        while (user.level <= 9) {
            clearConsole();
            System.out.printf("[LEVEL %d]\n\nAlright, %s. What is the type of the following sequence?\n\n", user.level, user.name);
            Sequence sequence = Sequence.generateSequence(user);

            if (sequence == null) {
                // This statement should not be reached as generateSequence() only returns null upon unrecognizable sequence types
                break;
            }

            final StringBuilder sequenceBuilder = new StringBuilder();
            for (int i = 0; i < sequence.terms.size(); i++) {
                // Visualizing the terms of the sequence in an array format
                sequenceBuilder.append(
                        String.format("%d%s", sequence.terms.get(i), i == sequence.terms.size() - 1 ? "" : ", ")
                );
            }

            System.out.printf("Your sequence is: %s\n", sequenceBuilder);

            // Map of letter against sequence type
            final Map<String, SequenceType> answers = new HashMap<>();

            answers.put("A", SequenceType.ARITHMETIC);
            answers.put("B", SequenceType.QUADRATIC);
            answers.put("C", SequenceType.CUBIC);
            answers.put("D", SequenceType.PRIME);

            StringBuilder answerBuilder = new StringBuilder();
            for (Map.Entry<String, SequenceType> entry : answers.entrySet()) {
                answerBuilder.append(String.format("%s. %s\n", entry.getKey(), entry.getValue().typeName));
            }

            System.out.printf("%s\n> ", answerBuilder);

            final String answer = input.nextLine().trim().toUpperCase();
            String vowelIdentify = sequence.type.typeName.equals("Arithmetic") ? "an" : "a"; // prints "an arithmetic sequence" instead of "a arithmetic sequence"
            if (answers.get(answer) == sequence.type) {
                user.level++;

                System.out.printf(
                        "\nCongratulations! That is correct.\nThis was %s %s sequence%s\nYour level has increased to LEVEL %d!\nPress [ENTER] to continue",
                        vowelIdentify, sequence.type.typeName.toUpperCase(), sequence.type == SequenceType.PRIME ? "." : String.format(": %s\n", Sequence.visualiseNthTerm(sequence)), user.level
                );
                input.nextLine();
            } else {
                if (user.level > 1) {
                    user.level--;
                }

                System.out.printf(
                        "\nClose! This was actually %s %s sequence%s\nYour level has been decreased to LEVEL %d!\nPress [ENTER] to continue",
                        vowelIdentify, sequence.type.typeName.toUpperCase(), sequence.type == SequenceType.PRIME ? "." : String.format(": %s\n", Sequence.visualiseNthTerm(sequence)), user.level
                );
                input.nextLine();
            }
        }

        clearConsole();

        final long timeToBeat = System.currentTimeMillis() - startTimestamp;
        System.out.printf("Ho ho ho! Wise mathematician, you are truly a master! You have beat the game in %s minutes and %s seconds. Wow!\nUntil next time!", TimeUnit.MILLISECONDS.toMinutes(timeToBeat), TimeUnit.MILLISECONDS.toSeconds(timeToBeat));
    }

    static void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.print("\n");
        }
    }
}