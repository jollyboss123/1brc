package org.jolly.onebrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * @author jolly
 *
 * O(n) time
 * 1. read file line by line and store in List variable
 * 2. aggregate into a Map variable but postpone average calculation once loop all lines
 * </p>
 * Findings:
 * 1. using a {@link Class} in place of a {@link Record} for objects that expect to be mutated over time will serve
 * better performance since it reduces object creation (i.e. {@link Record} variables are immutable and have to
 * create new {@link Record} with updated values).
 * 2. using {@link Map#compute(Object, BiFunction)} may incur some performance overhead because of lambda invocation.
 * Since in our case our update logic is simple, {@link Map#get(Object)} and {@link Map#put(Object, Object)} is more
 * efficient. However, if the update logic is complex and involves significant computation, {@link Map#compute(Object, BiFunction)}
 * is more efficient because the update is performed in a single operation.
 */
public class CalculateAverage_baseline_loop {
    private static final String MEASUREMENTS_FILE = "./measurements.txt";

    static class Result {
        double min;
        double max;
        double total;
        int count;

        Result(double min, double max, double total, int count) {
            this.min = min;
            this.max = max;
            this.total = total;
            this.count = count;
        }

        Result(double temp) {
            this(temp, temp, temp, 1);
        }

        void update(double temp) {
            min = Math.min(min, temp);
            max = Math.max(max, temp);
            total += temp;
            count++;
        }

        double getAverage() {
            return round(total) / count;
        }

        private double round(double val) {
            return Math.round(val * 10.0) / 10.0;
        }

        @Override
        public String toString() {
            return round(min) + "/" + round(getAverage()) + "/" + round(max);
        }
    }

    public static void main(String[] args) {
        TreeMap<String, Result> resultMap = new TreeMap<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(MEASUREMENTS_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String line : lines) {
            String[] parts = line.split(";");
            String name = parts[0];
            double temp = Double.parseDouble(parts[1]);

            Result result = resultMap.get(name);
            if (result == null) {
                resultMap.put(name, new Result(temp));
            } else {
                result.update(temp);
            }
        }

        resultMap.forEach((name, result) -> System.out.println(name + ";" + result));
    }
}
