package segtrees;

public interface Combiner<T> {
    class SumCombiner implements Combiner<Long>{
        @Override
        public Long combine(Long a, Long b) {
            if (a == null)
                return b;
            if (b == null)
                return a;

            return a + b;
        }
    }

    class MinCombiner implements Combiner<Long> {
        @Override
        public Long combine(Long a, Long b) {
            if (a == null)
                return b;
            if (b == null)
                return a;

            return Math.min(a, b);
        }
    }

    T combine(T a, T b);

    static Combiner<Long> sumLongs() {
        return new SumCombiner();
    }

    static Combiner<Long> minLongs() {
        return new MinCombiner();
    }
}
