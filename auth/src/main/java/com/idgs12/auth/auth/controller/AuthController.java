package com.idgs12.auth.auth.controller;

import com.idgs12.auth.auth.dto.*;
import com.idgs12.auth.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ===================================
    // ENDPOINT: REGISTRO
    // ===================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  ENDPOINT: POST /auth/register         ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Datos recibidos:");
        System.out.println("  - Nombre: " + request.getNombre());
        System.out.println("  - Apellido Paterno: " + request.getApellidoPaterno());
        System.out.println("  - Apellido Materno: " + request.getApellidoMaterno());
        System.out.println("  - Correo: " + request.getCorreo());
        System.out.println("  - Matrícula: " + request.getMatricula());
        System.out.println("  - Rol: " + request.getRol());

        try {
            String message = authService.register(request);
            System.out.println("\n✓ REGISTRO EXITOSO");
            System.out.println("Correo registrado: " + request.getCorreo());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);

            System.out.println("Respuesta enviada: " + response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("\n✗ ERROR EN REGISTRO");
            System.err.println("Correo: " + request.getCorreo());
            System.err.println("Error: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ===================================
    // ENDPOINT: LOGIN
    // ===================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  ENDPOINT: POST /auth/login            ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Intento de login:");
        System.out.println("  - Correo: " + request.getCorreo());

        try {
            AuthResponseDTO response = authService.login(request);

            System.out.println("\n✓ LOGIN EXITOSO");
            System.out.println(
                    "Usuario: " + response.getUsuario().getNombre() + " " + response.getUsuario().getApellidoPaterno());
            System.out.println("Rol: " + response.getUsuario().getRol());
            System.out.println("Token generado (primeros 20 chars): "
                    + response.getToken().substring(0, Math.min(20, response.getToken().length())) + "...");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("\n✗ ERROR EN LOGIN");
            System.err.println("Correo: " + request.getCorreo());
            System.err.println("Error: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // ===================================
    // ENDPOINT: VALIDAR TOKEN
    // ===================================
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  ENDPOINT: POST /auth/validate         ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            String token = authHeader.replace("Bearer ", "");
            System.out.println(
                    "Token recibido (primeros 20 chars): " + token.substring(0, Math.min(20, token.length())) + "...");

            Boolean isValid = authService.validateToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            if (isValid) {
                UsuarioInfoDTO usuario = authService.getUsuarioFromToken(token);
                response.put("usuario", usuario);
                System.out.println("✓ Token válido para usuario: " + usuario.getCorreo());
            } else {
                System.out.println("✗ Token inválido");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("✗ ERROR AL VALIDAR TOKEN: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("valid", false);
            error.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // ===================================
    // ENDPOINT: OBTENER INFO DEL TOKEN
    // ===================================
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  ENDPOINT: GET /auth/me                ║");
        System.out.println("╚════════════════════════════════════════╝");

        try {
            String token = authHeader.replace("Bearer ", "");
            System.out.println("Extrayendo información del usuario del token...");

            UsuarioInfoDTO usuario = authService.getUsuarioFromToken(token);

            System.out.println("✓ Información del usuario obtenida:");
            System.out.println("  - ID: " + usuario.getId());
            System.out.println("  - Correo: " + usuario.getCorreo());
            System.out.println("  - Rol: " + usuario.getRol());

            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // ===================================
    // ENDPOINT: PING (HEALTH CHECK)
    // ===================================
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  ENDPOINT: GET /auth/ping              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("✓ Auth service está funcionando correctamente");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Auth service is running");
        response.put("status", "online");

        return ResponseEntity.ok(response);
    }
}
