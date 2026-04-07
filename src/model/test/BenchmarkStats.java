package model.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BenchmarkStats {
    
    private final Long sum;
    private final Long mean;
    private final Long min;
    private final Long max;
    private final Double var;
    private final Double sd;
    
    private BenchmarkStats(Long sum, Long mean, Long min, Long max, Double var, Double sd) {
        this.sum = sum;
        this.mean = mean;
        this.min = min;
        this.max = max;
        this.var = var;
        this.sd = sd;
    }
    
    public static BenchmarkStats of(List<Long> runtime) {
        return new BenchmarkStats(sum(runtime),
                                  mean(runtime),
                                  min(runtime),
                                  max(runtime),
                                  var(runtime),
                                  sd(runtime));
    }
    
    private static Long sum(List<Long> runtime) {
        return runtime.stream()
                      .reduce(0L, Long::sum);
    }
    
    private static Long mean(List<Long> runtime) {
        return Long.divideUnsigned(sum(runtime), runtime.size());
    }
    
    private static Long min(List<Long> runtime) {
        return runtime.stream()
                      .min(Long::compareTo)
                      .orElse(runtime.getFirst());
    }
    
    private static Long max(List<Long> runtime) {
        return runtime.stream()
                      .max(Long::compareTo)
                      .orElse(runtime.getFirst());
    }
    
    private static Double var(List<Long> runtime) {
        Long mean = mean(runtime);
        double sum = sum(runtime.stream()
                                .map(r -> (r - mean) ^ 2)
                                .toList());
        double size = (runtime.size() - 1);
        return sum / size;
    }
    
    private static Double sd(List<Long> runtime) {
        return Math.sqrt(var(runtime));
    }
    
    @Override
    public String toString() {
        return String.format("Sum: %13d | Mean: %13d | Min: %13d | Max: %13d \nVar: %13f | SD: %13f",
                             sum,
                             mean,
                             min,
                             max,
                             var,
                             sd);
    }
}
