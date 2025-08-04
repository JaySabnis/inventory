package com.project.inventory.inventory.service;

import com.project.inventory.inventory.dto.ItemDTO;
import com.project.inventory.inventory.model.Item;
import com.project.inventory.inventory.respository.ItemRepository;
import com.project.inventory.inventory.socket.ItemWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository repository;

    @Autowired
    private ItemWebSocketHandler socketHandler;

    private final List<Consumer<ItemDTO>> subscribers = new CopyOnWriteArrayList<>();

    public List<ItemDTO> getAllItems() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ItemDTO updateItem(ItemDTO dto) throws Exception {
        Optional<Item> optionalItem = repository.findById(dto.getId());
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setAvailable(dto.isAvailable());
            Item updated = repository.save(item);
            ItemDTO updatedDTO = toDTO(updated);
            socketHandler.broadcastUpdate("update",updatedDTO);
            notifySubscribers(updatedDTO);
            return updatedDTO;
        } else {
            throw new RuntimeException("Item not found");
        }
    }

    private ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    public void subscribe(Consumer<ItemDTO> consumer) {
        subscribers.add(consumer);
    }

    private void notifySubscribers(ItemDTO dto) {
        for (Consumer<ItemDTO> subscriber : subscribers) {
            subscriber.accept(dto);
        }
    }
}
