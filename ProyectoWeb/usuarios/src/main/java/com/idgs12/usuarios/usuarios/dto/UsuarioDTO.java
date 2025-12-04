package com.idgs12.usuarios.usuarios.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioDTO {
    private int id;
    private String nombre;
    private String matricula;
    
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String contrasena;
    private String rol;
    private List<Long> programaIds;
}
