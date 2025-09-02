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


    @GetMapping("{dataset}")
    public Dataset getData(@PathVariable int dataset) {
        return Application.CACHE.get(dataset);
    }

    @GetMapping("profit/{dataset}")
    public MaxProfit getMaxProfit(@PathVariable int dataset) {
        return datasetService.getMaximumProfit(dataset);
    }
}
