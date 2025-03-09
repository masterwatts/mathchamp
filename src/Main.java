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
        if (!Arrays.asList("yes", "yeah", "yea", "y").contains(input.nextLine().toLowerCase())) {
            System.out.println("Oh no! I guess we'll meet later... Until then, wise mathematician!");
            System.exit(1);
        }

        clearConsole();
        System.out.print("Great! Now let's get started...\nWise mathematician, what's your name?\n> ");

        user = new User(
                input.nextLine()
        );

        clearConsole();
        while (user.level <= 9) {
            clearConsole();
            System.out.printf("[LEVEL %d]\n\nAlright, %s. What is the type of the following sequence?\n\n", user.level, user.name);
            Sequence sequence = generateSequence(user);

            if (sequence == null) break;

            final StringBuilder sequenceBuilder = new StringBuilder();
            for (int i = 0; i < sequence.terms.size(); i++) {
                sequenceBuilder.append(
                        String.format("%d%s", sequence.terms.get(i), i == sequence.terms.size() - 1 ? "" : ", ")
                );
            }

            System.out.printf("Your sequence is: %s\n", sequenceBuilder);

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
            String vowelIdentify = sequence.type.typeName.equals("Arithmetic") ? "an" : "a";
            if (answers.get(answer) == sequence.type) {
                user.level++;

                System.out.printf(
                        "\nCongratulations! That is correct.\nThis was %s %s sequence%s\nYour level has increased to LEVEL %d!\nPress [ENTER] to continue",
                        vowelIdentify, sequence.type.typeName.toUpperCase(), sequence.type == SequenceType.PRIME ? "." : String.format(": %s\n", visualiseNthTerm(sequence)), user.level
                );
                input.nextLine();
            } else {
                if (user.level > 1) {
                    user.level--;
                }

                System.out.printf(
                        "\nClose! This was actually %s %s sequence%s\nYour level has been decreased to LEVEL %d!\nPress [ENTER] to continue",
                        vowelIdentify, sequence.type.typeName.toUpperCase(), sequence.type == SequenceType.PRIME ? "." : String.format(": %s\n", visualiseNthTerm(sequence)), user.level
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

    static class User {
        final String name;
        int level;

        User(String name) {
            this.name = name;
            this.level = 1;
        }
    }

    static class Sequence {
        final SequenceType type;
        ArrayList<Integer> terms;
        int a, b, c, d;

        Sequence(SequenceType type) {
            this.type = type;
        }
    }

    /**
     *
     * @param sequence A sequence that is NOT a prime sequence
     * @return The sequence in proper mathematical formatting and notation
     */
    static String visualiseNthTerm(Sequence sequence) {
        switch (sequence.type) {
            case ARITHMETIC -> {
                final StringBuilder sequenceBuilder = new StringBuilder(sequence.a + "n");

                if (sequence.b != 0) {
                    sequenceBuilder.append(String.format(" %s %d", sequence.b < 0 ? "-" : "+", Math.abs(sequence.b)));
                }

                return sequenceBuilder.toString();
            }

            case QUADRATIC -> { // an^2 + bn + c
                final StringBuilder sequenceBuilder = new StringBuilder(sequence.a + "n²");

                if (sequence.b != 0) {
                    sequenceBuilder.append(String.format(" %s %dn", sequence.b < 0 ? "-" : "+", Math.abs(sequence.b)));
                }

                if (sequence.c != 0) {
                    sequenceBuilder.append(String.format(" %s %dn", sequence.c < 0 ? "-" : "+", Math.abs(sequence.c)));
                }

                return sequenceBuilder.toString();
            }

            case CUBIC -> {
                final StringBuilder sequenceBuilder = new StringBuilder(sequence.a + "n³");

                if (sequence.b != 0) {
                    sequenceBuilder.append(String.format(" %s %dn²", sequence.b < 0 ? "-" : "+", Math.abs(sequence.b)));
                }

                if (sequence.c != 0) {
                    sequenceBuilder.append(String.format(" %s %dn", sequence.c < 0 ? "-" : "+", Math.abs(sequence.c)));
                }

                if (sequence.d != 0) {
                    sequenceBuilder.append(String.format(" %s %d", sequence.d < 0 ? "-" : "+", Math.abs(sequence.d)));
                }

                return sequenceBuilder.toString();
            }
        }

        return null;
    }

    /**
     *
     * @param user The user data
     * @return An appropriate mathematical sequence based on the user's level
     * @see SequenceType for mathematical sequence types and their appropriate levels
     * @see User for user data
     */
    static Sequence generateSequence(User user) {
        if (user == null || user.name == null) {
            throw new IllegalArgumentException("User has not been completely initialized");
        }

        ArrayList<SequenceType> types = Arrays.stream(SequenceType.values()).filter(t -> user.level >= t.startingLevel).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(types);

        SequenceType type = types.get(0);

        final Sequence sequence = new Sequence(type);
        sequence.a = 0;

        // Since we must ensure that a ≠ 0:
        while (sequence.a == 0) {
            sequence.a = ThreadLocalRandom.current().nextInt(-5, 5);
        }

        switch (type) {
            case ARITHMETIC -> {
                sequence.b = ThreadLocalRandom.current().nextInt(-5, 5);

                ArrayList<Integer> terms = new ArrayList<>();
                for (int n = 1; n <= 5; n++) {
                    // an + b
                    terms.add(sequence.a * n + sequence.b);
                }

                sequence.terms = terms;

                return sequence;
            }

            case QUADRATIC -> {
                sequence.b = ThreadLocalRandom.current().nextInt(-5, 5);
                sequence.c = ThreadLocalRandom.current().nextInt(-5, 5);

                ArrayList<Integer> terms = new ArrayList<>();
                for (int n = 1; n <= 5; n++) {
                    // an^2 + bn + c
                    terms.add(sequence.a * (int) Math.pow(n, 2) + sequence.b * n + sequence.c);
                }

                sequence.terms = terms;

                return sequence;
            }

            case CUBIC -> {
                sequence.b = ThreadLocalRandom.current().nextInt(-5, 5);
                sequence.c = ThreadLocalRandom.current().nextInt(-5, 5);
                sequence.d = ThreadLocalRandom.current().nextInt(-5, 5);

                ArrayList<Integer> terms = new ArrayList<>();
                for (int n = 1; n <= 5; n++) {
                    // an^3 + bn^2 + cn + d
                    terms.add(sequence.a * (int) Math.pow(n, 3) + sequence.b * (int) Math.pow(n, 2) + sequence.c * n + sequence.d);
                }

                sequence.terms = terms;

                return sequence;
            }

            case PRIME -> {
                int startingPrimeIndex = ThreadLocalRandom.current().nextInt(5, 20);

                ArrayList<Integer> terms = new ArrayList<>();
                for (int n = 0; n < 5; n++) {
                    terms.add(generateNthPrime(startingPrimeIndex + n));
                }

                sequence.terms = terms;

                return sequence;
            }
        }

        return null;
    }

    /**
     *
     * @param n A natural integer
     * @return True if [n] is a prime number
     */
    static boolean isPrime(int n) {
        if (n <= 1) return false;

        for (int i = 2; i < n; i++) {
            if (n % i == 0) return false;
        }

        return true;
    }

    /**
     *
     * @param n A natural number
     * @return The n-th element in the prime number set
     */
    static int generateNthPrime(int n) {
        int counter = 1, value = 2;

        while (counter != n) {
            value++;
            if (isPrime(value)) counter++;
        }

        return value;
    }

    enum SequenceType {
        ARITHMETIC("Arithmetic", 1), // an + b
        QUADRATIC("Quadratic", 3), // an^2 + bn + c
        CUBIC("Cubic", 5), // an^3 + bn^2 + cn + d
        PRIME("Prime", 7); // sequence of prime numbers

        final String typeName;

        /**
         * Denotes the level of the user that will trigger the appearance of the sequence
         */
        final int startingLevel;

        SequenceType(String typeName, int startingLevel) {
            this.typeName = typeName;
            this.startingLevel = startingLevel;
        }
    }
}