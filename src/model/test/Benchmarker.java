package model.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public final class Benchmarker {
    public static final TestCase test1 = TestCase.ofSafe(0, 10, 100, 100, 999999, 1, 1, 2020, 1, 1, 2026);
    public static final TestCase test2 = TestCase.ofSafe(0, 1000, 100, 100, 999999, 1, 1, 2020, 1, 1, 2026);
    public static final TestCase test3 = TestCase.ofSafe(10, 10000, 10, 10, 10, 1, 1, 2024, 1, 1, 2026);
    public static final TestCase test4 = TestCase.ofSafe(10, 100, 10, 10, 10, 1, 1, 2024, 1, 1, 2026);
    
    private final int trials;
    private final TestCase test;
    private final BenchmarkStats stats;
    
    private Benchmarker(int trials, TestCase test, BenchmarkStats stats) {
        this.trials = trials;
        this.test = test;
        this.stats = stats;
    }
    
    public static Benchmarker of(TestCase test, int trials) {
        List<Long> rt = Collections.synchronizedList(new ArrayList<>(trials));
        AtomicInteger progress = new AtomicInteger(0);
        int increment = trials / 100;
        
        for (int i = 0; i < 10; i++) {
            LedgerGenerator.of(test);
        }
        
        IntStream.range(0, trials)
                 .parallel()
                 .forEach(i -> {
                     long startTime = System.nanoTime();
                     LedgerGenerator.of(test);
                     long endTime = System.nanoTime();
                     rt.add(endTime - startTime);
                     progress(progress.incrementAndGet(), trials, increment);
                 });
        
        return new Benchmarker(trials, test, BenchmarkStats.of(rt));
    }
    
    private static void progress(int progress, int trials, int increment) {
        if (progress % increment == 0)
            System.out.print("\r[" + progress + "\\" + trials + "]");
    }
    
    @Override
    public String toString() {
        return String.format("\nTrials: %d\nTest Case:\n%s\nStats (nanoseconds):\n%s", trials, test, stats);
    }
}
