package com.github.mangila.profit;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatasetService {

    private final ThreadLocal<Integer> sellDay = ThreadLocal.withInitial(() -> -1);

    public Price getPriceOnDay(int level, int day) {
        return Application.CACHE.get(level).data().stream()
                .filter(p -> p.day().equals(new Day(day)))
                .findFirst()
                .map(ClosingDay::price)
                .orElseThrow(() -> new IllegalArgumentException("No data found for level %d and day %d".formatted(level, day)));
    }

    public int getDays(int level) {
        return Application.CACHE.get(level)
                .data()
                .size();
    }

    public MaxProfit getMaximumProfit(int level) {
        return new MaxProfit(getBuyDay(level), getSellDay());
    }

    /**
     * Determines the optimal buying day to achieve the maximum profit based on the given level's dataset.
     * <br>
     * The algorithm works as follows:
     * First it builds a map of closing days for each day, then it iterates over the map and tries to find the highest
     * profit for each closing day.
     * <br>
     *
     * @param level the dataset level used to determine the buying day
     * @return the day index to buy to achieve maximum profit, or -1 if no profitable day is found
     */
    public int getBuyDay(int level) {
        int days = getDays(level);
        Map<ClosingDay, List<ClosingDay>> closingDays = new HashMap<>();
        for (int day = 0; day < days; day++) {
            var closingDay = new ClosingDay(new Day(day), getPriceOnDay(level, day));
            closingDays.computeIfAbsent(closingDay, k -> new ArrayList<>());
            for (int j = day + 1; j < days; j++) {
                ClosingDay futureClosingDay = new ClosingDay(new Day(j), getPriceOnDay(level, j));
                closingDays.get(closingDay)
                        .add(futureClosingDay);
            }
        }
        int maximumProfit = 0;
        int buyDay = -1;
        for (var entry : closingDays.entrySet()) {
            ClosingDay currentDay = entry.getKey();
            var sequence = entry.getValue();
            for (ClosingDay closingDay : sequence) {
                int priceOnDay = closingDay.price().value();
                int profit = priceOnDay - currentDay.price().value();
                if (profit > maximumProfit) {
                    maximumProfit = profit;
                    buyDay = currentDay.day().value();
                    sellDay.set(closingDay.day().value());
                }
            }
        }
        return buyDay;
    }

    /**
     *
     * Retrieves the optimal sell day for maximum profit.
     * This value is determined based on the calculation performed
     * during the execution of {@link #getBuyDay(int)}.
     * If the sell day is not set, an {@code IllegalStateException} is thrown.
     *
     * @return the calculated sell day as an integer, representing the index of the day
     * to sell to maximize profit
     * @throws IllegalStateException if no sell day has been determined
     */
    public int getSellDay() {
        if (sellDay.get() == -1) {
            throw new IllegalStateException("No sell day found");
        }
        return sellDay.get();
    }
}
