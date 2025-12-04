package com.idgs12.grupos.grupos.dto;

import lombok.Data;

@Data
public class MateriaDTO {
    private Long id;
    private String nombre;
    private Long programaId;
    private String nombrePrograma;
    private Boolean activo;
}
