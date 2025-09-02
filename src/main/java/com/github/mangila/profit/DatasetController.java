package com.github.mangila.profit;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/data")
@CrossOrigin(origins = "*")
public class DatasetController {

    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }


    @GetMapping("{level}")
    public Dataset getData(@PathVariable int level) {
        return Application.CACHE.get(level);
    }

    @GetMapping("profit/{level}")
    public MaxProfit getMaxProfit(@PathVariable int level) {
        return datasetService.getMaximumProfit(level);
    }
}
