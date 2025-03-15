public enum SequenceType {
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
