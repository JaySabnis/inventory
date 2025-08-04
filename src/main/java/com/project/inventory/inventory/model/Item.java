package com.project.inventory.inventory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Inventory")
public class Item {

    @Id
    private String id;
    private String name;
    private double price;
    private boolean available;

}
