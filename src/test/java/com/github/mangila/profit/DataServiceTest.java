package com.github.mangila.profit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DataServiceTest {

    @Autowired
    private DataService dataService;

    @Test
    void getData() {
        MaxProfit maxProfit = dataService.getMaximumProfit(1);
        assertThat(maxProfit.buyDay()).isEqualTo(0);
        assertThat(maxProfit.sellDay()).isEqualTo(2);
    }
}