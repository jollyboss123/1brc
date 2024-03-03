package org.jolly.onebrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author jolly
 *
 * O(n) time
 * 1. read file line by line and store in List variable
 * 2. aggregate into a Map variable but postpone average calculation once loop all lines
 * </p>
 * Findings:
 *
 */
public class CalculateAverage_baseline_loop {
    private static final String MEASUREMENTS_FILE = "./measurements.txt";
    record Result(double min, double max, double total, int count) {
        private Result (double temp) {
            this(temp, temp, temp, 1);
        }

        @Override
        public String toString() {
            double avg = round(total) / count;
            return round(min) + "/" + round(avg) + "/" + round(max);
        }

        private double round(double val) {
            return Math.round(val * 10.0) / 10.0;
        }
    }
    record Measurement(String name, double temp) {
        private Measurement(String[] arr) {
            this(arr[0], Double.parseDouble(arr[1]));
        }
    }

    public static void main(String[] args) {
        Map<String, Result> resultMap = new TreeMap<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(MEASUREMENTS_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var l : lines) {
            final Measurement m = new Measurement(l.split(";", 2));
            resultMap.compute(m.name, (k, v) -> {
                if (v == null) {
                    return new Result(m.temp);
                } else {
                    return new Result(
                            Math.min(m.temp, v.min),
                            Math.max(m.temp, v.max),
                            v.total + m.temp,
                            v.count + 1
                    );
                }
            });
        }

        System.out.println(resultMap);
    }
}
