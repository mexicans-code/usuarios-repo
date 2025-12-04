package com.idgs12.usuarios.usuarios.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioResponseDTO {
    private int id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String rol;
    private String matricula;
    private List<ProgramaDTO> programas;
}