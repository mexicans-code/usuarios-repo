package com.idgs12.idgs12.dto;

import lombok.Data;
import java.util.List;

@Data
public class DivisionToCreateDto {
    private String nombre;
    private String image;
    private String activo;
    private List<String> programasEducativos;
}
