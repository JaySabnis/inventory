package com.project.inventory.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inventory.inventory.dto.ItemDTO;
import com.project.inventory.inventory.service.ItemService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public void streamItemUpdates(HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*"); // ✅ CORS header
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Content-Type", "text/event-stream");
        response.setCharacterEncoding("UTF-8");

        service.subscribe(itemDTO -> {
            try {
                String json = objectMapper.writeValueAsString(Map.of("action", "update", "object", itemDTO));
                response.getWriter().write("data: " + json + "\n\n");
                response.getWriter().flush();
            } catch (IOException ignored) {
            }
        });
    }
}
