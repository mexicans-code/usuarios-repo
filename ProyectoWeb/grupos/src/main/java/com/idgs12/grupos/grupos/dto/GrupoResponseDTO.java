package com.idgs12.grupos.grupos.dto;

import lombok.Data;
import java.util.List;

@Data
public class GrupoResponseDTO {
    private int id;
    private String nombre;
    private String cuatrimestre;
    private Boolean estado;
    private List<UsuarioDTO> alumnos;
    private List<ProfesorDTO> profesores;
}