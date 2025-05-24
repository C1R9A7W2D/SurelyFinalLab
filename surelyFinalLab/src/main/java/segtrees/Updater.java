package segtrees;

import java.util.function.BinaryOperator;

public class Updater {
    public static BinaryOperator<Long> addLongs() {
        return Long::sum;
    }
}
