package com.github.mangila.profit;

import java.util.Collections;
import java.util.List;

public record Dataset(List<ClosingDay> data) {
    public static final Dataset EMPTY = new Dataset(Collections.emptyList());
}
