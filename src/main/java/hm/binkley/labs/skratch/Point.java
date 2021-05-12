package hm.binkley.labs.skratch;

import static java.lang.System.out;

record Point(int x, int y) {
    public static void main(final String... args) {
        out.printf("POINT -> %s%n", new Point(1, 2));
    }
}
