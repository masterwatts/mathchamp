public class Prime {
    /**
     *
     * @param n A natural integer
     * @return True if [n] is a prime number
     */
    static boolean isPrime(int n) {
        if (n <= 1) return false; // Lowest prime number is 1

        for (int i = 2; i < n; i++) {
            if (n % i == 0) return false; // If the number is divisible by any other number except for 1 and itself, it is not a prime number
        }

        return true;
    }

    /**
     *
     * @param n A natural number
     * @return The n-th element in the prime number set
     */
    static int generateNthPrime(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be a natural number");

        int counter = 1, value = 2; // 2 is the 1-indexed prime number in the prime number set (the first one is 1, index 0)

        while (counter != n) { // Keep repeating until we reach n
            value++;
            if (isPrime(value)) counter++; // Update counter (counter is now the index of [value] in the prime number set
        }

        return value;
    }
}
