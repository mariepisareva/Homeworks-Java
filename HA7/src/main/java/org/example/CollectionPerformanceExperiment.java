package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Сравнивает производительность {@link ArrayList} и {@link LinkedList}
 * на шести сценариях: добавление в конец, в начало и в середину,
 * последовательный доступ по индексу, удаление из начала и из конца.
 * Запускается через {@link #main(String[])} и печатает таблицу
 * с медианой времени по нескольким замерам.
 */
public class CollectionPerformanceExperiment {

    private static final int N = 10_000;
    private static final int WARMUP_RUNS = 3;
    private static final int MEASURED_RUNS = 7;
    private static long checksum = 0;

    private CollectionPerformanceExperiment() {
    }

    private static List<Integer> newList(boolean linked) {
        return linked ? new LinkedList<>() : new ArrayList<>();
    }

    private static void fill(List<Integer> list) {
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
    }

    private static long runOnce(int scenario, boolean linked) {
        List<Integer> list = newList(linked);
        if (scenario >= 4) {
            fill(list);
        }

        long sum = 0;
        long start = System.nanoTime();
        switch (scenario) {
            case 1:
                for (int i = 0; i < N; i++) {
                    list.add(i);
                }
                break;
            case 2:
                for (int i = 0; i < N; i++) {
                    list.add(0, i);
                }
                break;
            case 3:
                for (int i = 0; i < N; i++) {
                    list.add(list.size() / 2, i);
                }
                break;
            case 4:
                for (int i = 0; i < N; i++) {
                    sum += list.get(i);
                }
                break;
            case 5:
                for (int i = 0; i < N; i++) {
                    sum += list.remove(0);
                }
                break;
            case 6:
                for (int i = 0; i < N; i++) {
                    sum += list.remove(list.size() - 1);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }
        long elapsed = System.nanoTime() - start;

        checksum += sum + list.size();
        return elapsed;
    }

    private static double measure(int scenario, boolean linked) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            runOnce(scenario, linked);
        }
        long[] times = new long[MEASURED_RUNS];
        for (int i = 0; i < MEASURED_RUNS; i++) {
            times[i] = runOnce(scenario, linked);
        }
        Arrays.sort(times);
        return times[MEASURED_RUNS / 2] / 1_000_000.0;
    }

    /**
     * Запускает эксперимент и печатает таблицу результатов.
     *
     * @param args не используются
     */
    public static void main(String[] args) {
        String[] names = {"Add to end", "Add to start", "Insert in middle", "Get by index", "Remove from start", "Remove from end"};

        System.out.printf("%-20s %10s %16s %16s%n", "Operation", "Elements", "ArrayList, ms", "LinkedList, ms");
        System.out.println("-".repeat(65));
        for (int s = 1; s <= 6; s++) {
            double array = measure(s, false);
            double linked = measure(s, true);
            System.out.printf("%-20s %10d %16.3f %16.3f%n", names[s - 1], N, array, linked);
        }
        System.out.println("Checksum: " + checksum);
    }
}