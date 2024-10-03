package com.example.demo.request;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.product.ProductService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class AddProductRequest {
    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;

}
