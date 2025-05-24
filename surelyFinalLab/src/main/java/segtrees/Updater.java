package segtrees;

public interface Updater<T> {
    class AddUpdater implements Updater<Long> {
        @Override
        public Long update(Long current, Long value, int length) {
            if (current == null)
                current = 0L;
            return current + value * length;
        }
    }

    class AssignUpdater implements Updater<Long> {
        @Override
        public Long update(Long current, Long value, int length) {
            return value;
        }
    }

    T update(T a, T b, int length);

    static Updater<Long> addLongs() {
        return new AddUpdater();
    }

    static Updater<Long> assignLongs() {
        return new AssignUpdater();
    }
}
