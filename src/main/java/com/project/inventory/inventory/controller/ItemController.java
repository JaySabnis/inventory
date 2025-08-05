package com.project.inventory.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inventory.inventory.dto.ItemDTO;
import com.project.inventory.inventory.service.ItemService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ItemController {
    @Autowired
    private ItemService service;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @GetMapping
    public List<ItemDTO> getItems() {
        return service.getAllItems();
    }

    @PostMapping("/update")
    public ItemDTO updateItem(@RequestBody ItemDTO dto) throws Exception {
        return service.updateItem(dto);
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamItemUpdates() {
        return service.getItemStream()
                .map(itemDTO -> {
                    return toJson(Map.of("action", "update", "object", itemDTO));
                })
                .delayElements(Duration.ofMillis(100));
    }

    private String toJson(Object obj) {
        try {
            return "data: " + new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj) + "\n\n";
        } catch (Exception e) {
            return "data: {\"error\":\"serialization error\"}\n\n";
        }
    }
}
