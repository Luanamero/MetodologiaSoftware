package com.medapp.use;

import com.medapp.models.User;
import com.medapp.utils.user.*;
import com.medapp.utils.password.*;

public class UsuarioValidador {
    
    /**
     * Valida um usuário completo
     */
    public static void validarUsuario(User usuario) throws UserException, PasswordException {
        if (usuario == null) {
            throw new UserException("User cannot be null");
        }
        
        validarUsername(usuario.getUsername());
        validarPassword(usuario);
    }
    
    /**
     * Valida apenas o username usando o validador existente
     */
    public static void validarUsername(String username) throws UserException {
        UsernameValidator.validate(username);
    }
    
    /**
     * Valida apenas a password usando o validador existente
     */
    public static void validarPassword(User usuario) throws PasswordException {
        PasswordValidator.validate(usuario);
    }
    
    /**
     * Valida credenciais básicas para criação de usuário
     */
    public static void validarCredenciais(String username, String password, String email) 
            throws UserException, PasswordException {
        
        validarUsernameBasico(username);
        validarPasswordBasico(password);
        validarEmail(email);
    }
    
    private static void validarUsernameBasico(String username) throws UserException {
        if (isNullOrEmpty(username)) {
            throw new EmptyUsernameException();
        }
    }
    
    private static void validarPasswordBasico(String password) throws PasswordException {
        if (isNullOrEmpty(password)) {
            throw new EmptyPasswordException();
        }
    }
    
    private static void validarEmail(String email) throws UserException {
        if (isNullOrEmpty(email)) {
            throw new InvalidUsernameException("Email cannot be empty");
        }
        
        if (!isValidEmailFormat(email)) {
            throw new InvalidUsernameException("Email must have valid format");
        }
    }
    
    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    private static boolean isValidEmailFormat(String email) {
        return email.contains("@") && email.contains(".");
    }
}
