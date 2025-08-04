package com.project.inventory.inventory.respository;

import com.project.inventory.inventory.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
}
