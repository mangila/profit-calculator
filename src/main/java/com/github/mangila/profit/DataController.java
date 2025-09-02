package com.github.mangila.profit;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/data")
@CrossOrigin(origins = "*")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }


    @GetMapping("{level}")
    public Data getData(@PathVariable int level) {
        return Application.CACHE.get(level);
    }

    @GetMapping("profit/{level}")
    public MaxProfit getMaxProfit(@PathVariable int level) {
        return dataService.getMaximumProfit(level);
    }
}
