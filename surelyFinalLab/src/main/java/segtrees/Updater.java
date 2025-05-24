package segtrees;

import java.util.function.BinaryOperator;

public interface Updater<T> {
    class AddUpdater implements Updater<Long>{
        @Override
        public Long update(Long current, Long value, int length) {
            if (current == null)
                current = 0L;
            return current + value * length; // Обновляем текущее значение, добавляя новое
        }
    }

    T update(T a, T b, int length);

    static Updater<Long> addLongs() {
        return new AddUpdater();
    }
}
