package segtrees.tests;

import org.junit.jupiter.api.Test;
import segtrees.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SumAndPlusTest {
    @Test
    void smallFixedScenario() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.sumLongs(), Updater.addLongs());

        assertEquals(9, st.query(1, 4));    //2+3+4
        st.update(0, 5, 3L);                //+3 ко всем
        assertEquals(15, st.query(0,3));    //4+5+6
    }

    @Test
    void preciseRange() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.sumLongs(), Updater.addLongs());

        assertEquals(2, st.query(1, 2));    //2
        st.update(1, 2, 3L);                //+3 к 2
        assertEquals(5, st.query(1,2));
    }

    @Test
    void queueEntireArray() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.sumLongs(), Updater.addLongs());

        assertEquals(15, st.query(0, 5));    //1+2+3+4+5
        st.update(0, 5, 10L);                //+10 ко всем
        assertEquals(65, st.query(0,5));     //11+12+13+14+15
    }

    @Test
    void notCrossingRanges() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.sumLongs(), Updater.addLongs());

        assertEquals(6, st.query(0, 3));    //1+2+3
        assertEquals(9, st.query(3, 5));    //4+5

        st.update(0, 1, 10L);                //+10 к 1
        st.update(4, 5, 6L);                //+6 к 5

        assertEquals(31, st.query(0,5));     //11+2+3+4+11
    }

    @Test
    void CrossingRanges() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.sumLongs(), Updater.addLongs());

        assertEquals(6, st.query(0, 3));    //1+2+3
        assertEquals(12, st.query(2, 5));    //3+4+5

        st.update(0, 3, 3L);                //+3 к 1,2,3
        st.update(1, 5, 2L);                //+2 к 2,3,4,5

        assertEquals(32, st.query(0,5));     //4+7+8+6+7
    }

    @Test
    void twoUpdatesInARow() {
        // 1 2 3 4 5
        Long[] a = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st =
                new SegmentTree<>(a, Combiner.sumLongs(), Updater.addLongs());

        st.update(0, 5, 3L);                //+3 ко всем
        st.update(3, 5, 15L);               //+15 к 4 и 5
        assertEquals(60, st.query(0,5));    //4+5+6+22+23
    }

    @Test
    void speedLarge() {
        int n = 1_000_000;
        int ops = 1_000_000;
        SegmentTree<Long, Long> st =
                new SegmentTree<>(n, Combiner.sumLongs(), Updater.addLongs());

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
        int n = 10; // Размер массива
        SegmentTree<Long, Long> segmentTree = new SegmentTree<>(n, Combiner.sumLongs(), Updater.addLongs());

        // Пример обновления диапазона
        segmentTree.update(0, 4, 5L); // Добавляем 5 к элементам с 0 по 4
        segmentTree.update(2, 6, 3L); // Добавляем 3 к элементам с 2 по 6

        // Пример запроса суммы в диапазоне
        long sum = segmentTree.query(1, 3); // Запрашиваем сумму элементов с 1 по 3
        assertEquals(13, sum);
    }
}
