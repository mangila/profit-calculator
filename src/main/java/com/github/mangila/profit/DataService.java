package com.github.mangila.profit;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {

    private final ThreadLocal<Integer> sellDay = ThreadLocal.withInitial(() -> -1);

    public Price getPriceOnDay(int level, int day) {
        return Application.CACHE.get(level).data().stream()
                .filter(p -> p.day().equals(new Day(day)))
                .findFirst()
                .map(Pair::price)
                .orElseThrow(() -> new IllegalArgumentException("No data found for level %d and day %d".formatted(level, day)));
    }

    public int getDays(int level) {
        return Application.CACHE.get(level)
                .data()
                .size();
    }

    public MaxProfit getMaximumProfit(int level) {
        return new MaxProfit(level,
                getBuyDay(level),
                getSellDay());
    }

    public int getBuyDay(int level) {
        int days = getDays(level);
        Map<Pair, List<Pair>> map = new HashMap<>();
        for (int day = 0; day < days; day++) {
            var buyPair = new Pair(new Day(day), getPriceOnDay(level, day));
            map.computeIfAbsent(buyPair, k -> new ArrayList<>());
            for (int j = day + 1; j < days; j++) {
                var pair = new Pair(new Day(j), getPriceOnDay(level, j));
                map.get(buyPair).add(pair);
            }
        }
        int maximumProfit = 0;
        int buyDay = -1;
        for (var entry : map.entrySet()) {
            Pair currentDay = entry.getKey();
            var sequence = entry.getValue();
            for (Pair pair : sequence) {
                int priceOnDay = pair.price().price();
                int profit = priceOnDay - currentDay.price().price();
                if (profit > maximumProfit) {
                    maximumProfit = profit;
                    buyDay = currentDay.day().value();
                    sellDay.set(pair.day().value());
                }
            }
        }
        return buyDay;
    }

    public int getSellDay() {
        if (sellDay.get() == -1) {
            throw new IllegalStateException("No sell day found");
        }
        return sellDay.get();
    }
}
