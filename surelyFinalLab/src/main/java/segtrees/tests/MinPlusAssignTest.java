package segtrees.tests;

import org.junit.jupiter.api.Test;
import segtrees.Combiner;
import segtrees.SegmentTree;
import segtrees.Updater;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinPlusAssignTest {
    @Test
    void smallFixedScenario() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.minLongs(), Updater.assignLongs());

        assertEquals(2, st.query(1, 4));
        st.update(0, 5, 3L);        //Присваиваем всем 3
        assertEquals(3, st.query(0,3));
    }

    @Test
    void preciseRange() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.minLongs(), Updater.assignLongs());

        assertEquals(2, st.query(1, 2));
        st.update(1, 2, 3L);         //2 = 3
        assertEquals(3, st.query(1,2));
    }

    @Test
    void queueEntireArray() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.minLongs(), Updater.assignLongs());

        assertEquals(1, st.query(0, 5));
        st.update(0, 5, 10L);                //Присваиваем всем 10
        assertEquals(10, st.query(0,5));
    }

    @Test
    void notCrossingRanges() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.minLongs(), Updater.assignLongs());

        assertEquals(1, st.query(0, 3));
        assertEquals(4, st.query(3, 5));

        st.update(0, 3, 10L);                //1, 2, 3 = 10
        st.update(4, 5, 2L);                //5 = 2

        assertEquals(2, st.query(0,5));
    }

    @Test
    void CrossingRanges() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.minLongs(), Updater.assignLongs());

        assertEquals(1, st.query(0, 3));
        assertEquals(3, st.query(2, 5));

        st.update(0, 3, 3L);                //1,2,3 = 3
        st.update(1, 5, 2L);                //2,3,4,5 = 2

        assertEquals(3, st.query(0,1));
        assertEquals(2, st.query(0,2));
        assertEquals(2, st.query(4,5));
    }

    @Test
    void twoUpdatesInARow() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.minLongs(), Updater.assignLongs());

        st.update(0, 5, 3L);                //Присваиваем всем 3
        st.update(3, 5, 15L);               //4, 5 = 15
        assertEquals(3, st.query(0,5));
        assertEquals(15, st.query(3,5));
    }

    @Test
    void speedLarge() {
        int n = 1_000_000;
        int ops = 1_000_000;
        SegmentTree<Long, Long> st =
                new SegmentTree<>(n, Combiner.minLongs(), Updater.assignLongs());

        Random rnd = new Random(1);
        long t0 = System.nanoTime();
        for (int i = 0; i < ops; i++) {
            if (rnd.nextBoolean()) {
                int l = rnd.nextInt(n), r = rnd.nextInt(n-1) + 1 + 1;
                st.update(l, r, rnd.nextLong(1000));
            }
            else {
                int l = rnd.nextInt(n), r = rnd.nextInt(n-1) + 1 + 1;
                st.query(l, r);
            }
        }

        long ms = (System.nanoTime()-t0)/1_000_000;
        assertTrue(ms < 2000, "slow:_" + ms + "_ms");
    }

    @Test
    void InitializationByLength() {
        int n = 10;
        SegmentTree<Long, Long> segmentTree =
                new SegmentTree<>(n, Combiner.minLongs(), Updater.assignLongs());

        segmentTree.update(0, 4, 5L); // Присваиваем 5 элементам с 0 по 4
        segmentTree.update(2, 6, 3L); // Присваиваем 3 элементам с 2 по 6

        assertEquals(3, segmentTree.query(1, 3));
        assertEquals(5, segmentTree.query(1, 2));
    }
}
