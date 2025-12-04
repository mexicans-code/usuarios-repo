package com.idgs12.grupos.grupos.dto;

import lombok.Data;
import java.util.List;

@Data
public class GrupoDTO {
    private Integer id;
    private String nombre;
    private String cuatrimestre;
    private Boolean estado;
    private List<Long> alumnoIds;
}
