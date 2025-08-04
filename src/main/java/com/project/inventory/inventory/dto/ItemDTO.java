package com.project.inventory.inventory.dto;

import lombok.Data;

@Data
public class ItemDTO {
    private String id;
    private String name;
    private double price;
    private boolean available;
}
