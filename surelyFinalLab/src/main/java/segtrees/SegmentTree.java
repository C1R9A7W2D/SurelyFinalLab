package segtrees;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class SegmentTree<T, U> {
    private T[] nodes;
    private BinaryOperator<T> combiner;
    private BinaryOperator<T> updater;

    public SegmentTree(T[] a, BinaryOperator<T> combiner, BinaryOperator<T> updater) {
        nodes = a;
        this.combiner = combiner;
        this.updater = updater;
    }

    public SegmentTree(int n, BinaryOperator<T> combiner, BinaryOperator<T> updater) {
        nodes = (T[]) new Object[n];
        this.combiner = combiner;
        this.updater = updater;
    }

    public T query(int l, int r) {
        Stream<T> segment = Arrays.stream(nodes).skip(l).limit(r - l);
        return segment.reduce(combiner).get();
    }

    public void update(int l, int r, T update) {
        if (l > r) {
            int t = l;
            l = r;
            r = t;
        }

        Stream<T> segment = Arrays.stream(nodes).skip(l).limit(r - l);
        T[] a = (T[]) segment.map((x) -> updater.apply(x, update)).toArray();
        if (r - l >= 0) System.arraycopy(a, 0, nodes, l, r - l);
    }
}
