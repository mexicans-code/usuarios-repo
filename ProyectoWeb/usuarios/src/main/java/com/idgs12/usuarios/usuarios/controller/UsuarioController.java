package com.idgs12.usuarios.usuarios.controller;

import com.idgs12.usuarios.usuarios.dto.UsuarioAuthDTO;
import com.idgs12.usuarios.usuarios.dto.UsuarioDTO;
import com.idgs12.usuarios.usuarios.dto.UsuarioResponseDTO;
import com.idgs12.usuarios.usuarios.entity.UsuarioEntity;
import com.idgs12.usuarios.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Traer todos los usuarios con programas
    @GetMapping("/all")
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioService.getAllUsuariosDTO();
    }

    // Traer un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable int id) {
        UsuarioResponseDTO usuario = usuarioService.getUsuarioDTOById(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    // Crear usuario con programas
    @PostMapping
    public UsuarioEntity crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.saveUsuarioConProgramas(usuarioDTO);
    }

    // Actualizar usuario con programas
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> actualizarUsuario(
            @PathVariable int id,
            @RequestBody UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(id);
        UsuarioEntity updated = usuarioService.saveUsuarioConProgramas(usuarioDTO);
        return ResponseEntity.ok(updated);
    }

    // Buscar usuario por matrícula
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByMatricula(@PathVariable String matricula) {
        UsuarioResponseDTO usuario = usuarioService.getUsuarioDTOByMatricula(matricula);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioAuthDTO> getUsuarioByCorreo(@PathVariable String correo) {
        System.out.println("Recibida petición para buscar usuario por correo: " + correo);

        UsuarioAuthDTO usuario = usuarioService.findByCorreo(correo);
        if (usuario != null) {
            System.out.println("Usuario encontrado para correo: " + correo);
            return ResponseEntity.ok(usuario);
        }
        System.out.println("No se encontró usuario para correo: " + correo);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> createUsuarioWithPassword(@RequestBody UsuarioAuthDTO dto) {
        UsuarioDTO usuario = usuarioService.createWithPassword(dto);
        return ResponseEntity.ok(usuario);
    }

}
