package com.idgs12.idgs12.dto;

import java.util.List;

import lombok.Data;

@Data
public class DivisionToViewListDto {
    private Long divisionId;
    private String nombre;
    private List<String> programasEducativos;
}
