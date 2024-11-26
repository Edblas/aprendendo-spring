package com.javanauta.aprendendo_spring.business;

import com.javanauta.aprendendo_spring.infrastructure.entity.Usuario;
import com.javanauta.aprendendo_spring.infrastructure.exceptions.ConflictExpection;
import com.javanauta.aprendendo_spring.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.aprendendo_spring.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario salvaUsuario(Usuario usuario) {
        try {
            emailExiste(usuario.getEmail());
            usuario.setsenha(passwordEncoder.encode(usuario.getSenha()));
            return usuarioRepository.save(usuario);
        } catch (ConflictExpection e) {
            throw new ConflictExpection("Email já cadastrado", e.getCause());
        }
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictExpection("Email já cadastrado" + email);
            }
        } catch (ConflictExpection e) {
            throw new ConflictExpection("Email já cadastrado", e.getCause());
        }
    }
    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    public Usuario buscarUsuarioPorEmail(String email) {
    return usuarioRepository.findByEmail(email).orElseThrow(
            () -> new ResourceNotFoundException("Email não encontrado" + email));
    }
    public void deletaUsuarioPoremail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}