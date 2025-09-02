package com.github.mangila.profit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static final ConcurrentHashMap<Integer, Dataset> CACHE = new ConcurrentHashMap<>();

    private final ResourceLoader resourceLoader;

    public Application(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        for (int i = 1; i <= 4; i++) {
            Resource resource = resourceLoader.getResource("classpath:data/data-%d.csv".formatted(i));
            if (!resource.exists()) {
                throw new IllegalStateException("Data file not found: " + resource.getFilename());
            }
            CACHE.put(i, parseFile(resource));
        }
    }

    private Dataset parseFile(Resource resource) throws IOException {
        try (var lines = Files.lines(resource.getFile().toPath())) {
            List<ClosingDay> closingDays = lines
                    .map(s -> s.split(":"))
                    .map(strings -> new ClosingDay(new Day(Integer.parseInt(strings[0])), new Price(Integer.parseInt(strings[1]))))
                    .toList();
            return new Dataset(closingDays);
        }
    }
}
