package segtrees;

public class SegmentTree<T, U> {
    public SegmentTree(T[] a, Combiner<T> combiner, Updater<T> updater) {
        initialize(a.length, combiner, updater);
        buildTree(a, 0, size-1, 0);
    }

    public SegmentTree(int n, Combiner<T> combiner, Updater<T> updater) {
        initialize(n, combiner, updater);
    }

    private void initialize(int n, Combiner<T> combiner, Updater<T> updater) {
        this.size = n;
        this.combiner = combiner;
        this.updater = updater;
        tree = (T[]) new Object[4 * n];
        lazy = (T[]) new Object[4 * n];
    }

    private void buildTree(T[] a, int segStart, int segEnd, int pos) {
        if (segStart == segEnd) {
            tree[pos] = a[segStart]; // Листовой узел
        } else {
            int mid = (segStart + segEnd) / 2;
            buildTree(a, segStart, mid, 2 * pos + 1); // Левое поддерево
            buildTree(a, mid + 1, segEnd, 2 * pos + 2); // Правое поддерево
            tree[pos] = combiner.combine(tree[2 * pos + 1], tree[2 * pos + 2]);
        }
    }

    private T[] tree;
    private T[] lazy;
    private Combiner<T> combiner;
    private Updater<T> updater;
    private int size;

    public void update(int start, int end, T value) {
        updateRange(0, size - 1, start, end - 1, value, 0);
    }

    private void updateRange(int segStart, int segEnd, int rangeStart, int rangeEnd, T value, int pos) {
        // Проверка на наличие отложенных обновлений
        if (lazy[pos] != null) {
            updateWithChildren(segStart, segEnd, lazy[pos], pos);
            lazy[pos] = null; // Сбрасываем отложенное значение
        }

        if (segmentOutsideOfRange(segStart, segEnd, rangeStart, rangeEnd)) {
            return;
        }

        if (segmentFullyInRange(segStart, segEnd, rangeStart, rangeEnd)) {
            updateWithChildren(segStart, segEnd, value, pos);
            return;
        }

        updateSegmentCrossesRange(segStart, segEnd, rangeStart, rangeEnd, value, pos);
    }

    private void updateWithChildren(int segStart, int segEnd, T value, int pos) {
        tree[pos] = updater.update(tree[pos], value, segEnd - segStart + 1);

        // Если не листовой узел, передаем значение потомкам
        if (segStart != segEnd) {
            lazy[2 * pos + 1] = updater.update(lazy[2 * pos + 1], value, 1);
            lazy[2 * pos + 2] = updater.update(lazy[2 * pos + 2], value, 1);
        }
    }

    private static boolean segmentOutsideOfRange(int segStart, int segEnd, int rangeStart, int rangeEnd) {
        return segStart > segEnd || segStart > rangeEnd || segEnd < rangeStart;
    }

    private static boolean segmentFullyInRange(int segStart, int segEnd, int rangeStart, int rangeEnd) {
        return segStart >= rangeStart && segEnd <= rangeEnd;
    }

    private void updateSegmentCrossesRange(int segStart, int segEnd, int rangeStart, int rangeEnd, T value, int pos) {
        int mid = (segStart + segEnd) / 2;
        updateRange(segStart, mid, rangeStart, rangeEnd, value, 2 * pos + 1);
        updateRange(mid + 1, segEnd, rangeStart, rangeEnd, value, 2 * pos + 2);
        tree[pos] = combiner.combine(tree[2 * pos + 1], tree[2 * pos + 2]); // Обновляем текущий сегмент
    }

    public T query(int start, int end) {
        return queryRange(0, size - 1, start, end - 1, 0);
    }

    private T queryRange(int segStart, int segEnd, int rangeStart, int rangeEnd, int pos) {
        // Проверка на наличие отложенных обновлений
        if (lazy[pos] != null) {
            updateWithChildren(segStart, segEnd, lazy[pos], pos);
            lazy[pos] = null;
        }

        if (segmentOutsideOfRange(segStart, segEnd, rangeStart, rangeEnd)) {
            return null;
        }

        if (segmentFullyInRange(segStart, segEnd, rangeStart, rangeEnd)) {
            return tree[pos];
        }

        return querySegmentCrossesRange(segStart, segEnd, rangeStart, rangeEnd, pos);
    }

    private T querySegmentCrossesRange(int segStart, int segEnd, int rangeStart, int rangeEnd, int pos) {
        int mid = (segStart + segEnd) / 2;
        T leftResult = queryRange(segStart, mid, rangeStart, rangeEnd, 2 * pos + 1);
        T rightResult = queryRange(mid + 1, segEnd, rangeStart, rangeEnd, 2 * pos + 2);

        if (leftResult == null)
            return rightResult;
        if (rightResult == null)
            return leftResult;

        return combiner.combine(leftResult, rightResult);
    }
}
