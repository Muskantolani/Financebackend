package org.example.apitestingproject.DTO;


import java.math.BigDecimal;

public record ProductDTO(int id, String name, BigDecimal price, String category) {}
