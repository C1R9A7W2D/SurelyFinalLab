package segtrees;

import java.util.function.BinaryOperator;

public class Combiner {
    public static BinaryOperator<Long> sumLongs() {
        return Long::sum;
    }
}
