import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Sequence {
    final SequenceType type;
    ArrayList<Integer> terms;
    int a, b, c, d;

    Sequence(SequenceType type) {
        this.type = type;
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

        // We only want the types that are appropriate for our user's current level
        ArrayList<SequenceType> types = Arrays.stream(SequenceType.values())
                .filter(t -> user.level >= t.startingLevel)
                .collect(Collectors.toCollection(ArrayList::new));
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
                    terms.add(Prime.generateNthPrime(startingPrimeIndex + n));
                }

                sequence.terms = terms;

                return sequence;
            }
        }

        return null;
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
}
