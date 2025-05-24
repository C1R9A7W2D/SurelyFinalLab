package segtrees;

import java.util.function.BinaryOperator;

public interface Combiner<T> {
    class SumCombiner implements Combiner<Long>{
        @Override
        public Long combine(Long a, Long b) {
            if (a == null)
                a = 0L;
            if (b == null)
                b = 0L;

            return a + b;
        }
    }

    T combine(T a, T b);

    static Combiner<Long> sumLongs() {
        return new SumCombiner();
    }
}
