package com.idgs12.auth.auth.service;

import com.idgs12.auth.auth.dto.*;
import com.idgs12.auth.auth.FeignClient.UsuarioFeignClient;
import com.idgs12.auth.auth.security.JwtUtil;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ===================================
    // REGISTRO DE USUARIO
    // ===================================
    public String register(RegisterRequestDTO request) {
        System.out.println("=== INICIO PROCESO DE REGISTRO ===");
        System.out.println("Correo a registrar: " + request.getCorreo());

        try {
            // 1. Verificar si el usuario ya existe
            UsuarioAuthDTO usuarioExistente = null;

            try {
                System.out.println("Consultando si el usuario ya existe en el sistema...");
                usuarioExistente = usuarioFeignClient.getUsuarioByCorreo(request.getCorreo());
                System.out.println("Usuario encontrado en la base de datos: " + usuarioExistente.getCorreo());

            } catch (FeignException.NotFound e) {
                // 404 = Usuario no existe, esto es CORRECTO para proceder con el registro
                System.out.println("✓ Usuario NO existe en la base de datos (404 esperado)");
                System.out.println("✓ Procediendo con la creación del nuevo usuario...");
                usuarioExistente = null;

            } catch (FeignException e) {
                System.err.println("Error al consultar microservicio Usuarios: " + e.status() + " - " + e.getMessage());
                throw new RuntimeException("Error al comunicarse con el servicio de Usuarios");
            }

            // 2. Si el usuario ya existe, rechazar el registro
            if (usuarioExistente != null) {
                System.err.println("✗ El correo ya está registrado: " + request.getCorreo());
                throw new RuntimeException("El correo ya está registrado");
            }

            // 3. Crear nuevo usuario
            System.out.println("Preparando datos del nuevo usuario...");
            UsuarioAuthDTO nuevoUsuario = new UsuarioAuthDTO();
            nuevoUsuario.setNombre(request.getNombre());
            nuevoUsuario.setApellidoPaterno(request.getApellidoPaterno());
            nuevoUsuario.setApellidoMaterno(request.getApellidoMaterno());
            nuevoUsuario.setCorreo(request.getCorreo());
            nuevoUsuario.setMatricula(request.getMatricula());
            nuevoUsuario.setRol(request.getRol());

            // 4. Encriptar la contraseña antes de enviarla
            System.out.println("Encriptando contraseña con BCrypt...");
            String contrasenaEncriptada = passwordEncoder.encode(request.getContrasena());
            nuevoUsuario.setContrasena(contrasenaEncriptada);
            System.out.println("✓ Contraseña encriptada correctamente");

            // 5. Llamar al microservicio Usuarios para crear el nuevo usuario
            System.out.println("Enviando petición al microservicio Usuarios para crear usuario...");
            UsuarioAuthDTO usuarioCreado = usuarioFeignClient.createUsuarioWithPassword(nuevoUsuario);
            System.out.println("✓ Usuario creado exitosamente con ID: " + usuarioCreado.getId());
            System.out.println("=== REGISTRO COMPLETADO EXITOSAMENTE ===");

            return "Usuario registrado exitosamente";

        } catch (RuntimeException e) {
            System.err.println("=== ERROR EN REGISTRO ===");
            System.err.println("Mensaje de error: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            System.err.println("=== ERROR INESPERADO EN REGISTRO ===");
            System.err.println("Tipo de error: " + e.getClass().getName());
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
        }
    }

    // ===================================
    // LOGIN
    // ===================================
    public AuthResponseDTO login(LoginRequestDTO request) {
        System.out.println("=== INICIO PROCESO DE LOGIN ===");
        System.out.println("Correo: " + request.getCorreo());

        try {
            // 1. Buscar usuario por correo
            System.out.println("Consultando usuario en el sistema...");
            UsuarioAuthDTO usuario = null;

            try {
                usuario = usuarioFeignClient.getUsuarioByCorreo(request.getCorreo());
                System.out.println("✓ Usuario encontrado: " + usuario.getCorreo());

            } catch (FeignException.NotFound e) {
                System.err.println("✗ Usuario no encontrado en la base de datos");
                throw new RuntimeException("Credenciales inválidas");
            }

            if (usuario == null) {
                System.err.println("✗ Usuario no existe");
                throw new RuntimeException("Credenciales inválidas");
            }

            // 2. Verificar contraseña
            System.out.println("Verificando contraseña...");
            if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
                System.err.println("✗ Contraseña incorrecta");
                throw new RuntimeException("Credenciales inválidas");
            }
            System.out.println("✓ Contraseña correcta");

            // 3. Generar token JWT
            System.out.println("Generando token JWT...");
            String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidoPaterno();
            String token = jwtUtil.generateToken(
                    usuario.getId(),
                    usuario.getCorreo(),
                    usuario.getRol(),
                    usuario.getMatricula(),
                    nombreCompleto);
            System.out.println("✓ Token generado exitosamente");

            // 4. Preparar respuesta
            AuthResponseDTO response = new AuthResponseDTO();
            response.setToken(token);
            response.setType("Bearer");

            UsuarioInfoDTO usuarioInfo = new UsuarioInfoDTO();
            usuarioInfo.setId(usuario.getId());
            usuarioInfo.setNombre(usuario.getNombre());
            usuarioInfo.setApellidoPaterno(usuario.getApellidoPaterno());
            usuarioInfo.setApellidoMaterno(usuario.getApellidoMaterno());
            usuarioInfo.setCorreo(usuario.getCorreo());
            usuarioInfo.setMatricula(usuario.getMatricula());
            usuarioInfo.setRol(usuario.getRol());

            response.setUsuario(usuarioInfo);

            System.out.println("=== LOGIN COMPLETADO EXITOSAMENTE ===");
            System.out.println("Usuario ID: " + usuario.getId() + " | Rol: " + usuario.getRol());
            return response;

        } catch (RuntimeException e) {
            System.err.println("=== ERROR EN LOGIN ===");
            System.err.println("Mensaje: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            System.err.println("=== ERROR INESPERADO EN LOGIN ===");
            System.err.println("Tipo de error: " + e.getClass().getName());
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage());
        }
    }

    // ===================================
    // VALIDAR TOKEN
    // ===================================
    public Boolean validateToken(String token) {
        System.out.println("=== VALIDANDO TOKEN ===");
        try {
            Boolean isValid = jwtUtil.validateToken(token);
            System.out.println("Token válido: " + isValid);
            return isValid;
        } catch (Exception e) {
            System.err.println("Error al validar token: " + e.getMessage());
            return false;
        }
    }

    // ===================================
    // EXTRAER INFORMACIÓN DEL TOKEN
    // ===================================
    public UsuarioInfoDTO getUsuarioFromToken(String token) {
        System.out.println("=== EXTRAYENDO INFORMACIÓN DEL TOKEN ===");
        try {
            UsuarioInfoDTO usuario = new UsuarioInfoDTO();
            usuario.setId(jwtUtil.extractUsuarioId(token));
            usuario.setCorreo(jwtUtil.extractCorreo(token));
            usuario.setRol(jwtUtil.extractRol(token));
            usuario.setMatricula(jwtUtil.extractMatricula(token));

            System.out.println(
                    "✓ Información extraída - Usuario ID: " + usuario.getId() + " | Correo: " + usuario.getCorreo());
            return usuario;

        } catch (Exception e) {
            System.err.println("✗ Token inválido: " + e.getMessage());
            throw new RuntimeException("Token inválido");
        }
    }
}
