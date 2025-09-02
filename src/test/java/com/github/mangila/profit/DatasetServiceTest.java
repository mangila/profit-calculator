package com.github.mangila.profit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DatasetServiceTest {

    @Autowired
    private DatasetService datasetService;

    @Test
    void getData() {
        MaxProfit maxProfit = datasetService.getMaximumProfit(1);
        assertThat(maxProfit.buyDay()).isEqualTo(2);
        assertThat(maxProfit.sellDay()).isEqualTo(4);
    }
}