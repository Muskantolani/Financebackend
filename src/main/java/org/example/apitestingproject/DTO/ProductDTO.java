package org.example.apitestingproject.dto;


import java.math.BigDecimal;

public record ProductDTO(int id, String name, BigDecimal price, String category) {}
