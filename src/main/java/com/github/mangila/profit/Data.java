package com.github.mangila.profit;

import java.util.Collections;
import java.util.List;

public record Data(List<Pair> data) {
    public static final Data EMPTY = new Data(Collections.emptyList());
}
