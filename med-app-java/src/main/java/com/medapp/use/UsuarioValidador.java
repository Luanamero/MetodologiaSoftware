package com.medapp.use;

import com.medapp.models.User;
import com.medapp.models.Paciente;
import com.medapp.models.ProfissionalSaude;
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

        // Validações específicas por tipo de usuário
        if (usuario instanceof Paciente) {
            Paciente paciente = (Paciente) usuario;
            validarCPF(paciente.getCpf());
            validarTelefoneCelular(paciente.getTelefone());
        } else if (usuario instanceof ProfissionalSaude) {
            ProfissionalSaude prof = (ProfissionalSaude) usuario;
            validarCRM(prof.getCrm());
        }
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

    // ===== Validações específicas =====
    public static void validarCPF(String cpf) throws InvalidCPFException {
        if (isNullOrEmpty(cpf)) {
            throw new InvalidCPFException("CPF não pode ser vazio");
        }
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) {
            throw new InvalidCPFException("CPF deve conter 11 dígitos");
        }
        // Rejeita CPFs com todos os dígitos iguais
        if (digits.chars().distinct().count() == 1) {
            throw new InvalidCPFException("CPF inválido");
        }
        if (!isValidCPFCheckDigits(digits)) {
            throw new InvalidCPFException("CPF inválido");
        }
    }

    private static boolean isValidCPFCheckDigits(String digits) {
        try {
            int d1 = calculateCPFCheckDigit(digits.substring(0, 9), 10);
            int d2 = calculateCPFCheckDigit(digits.substring(0, 9) + d1, 11);
            return digits.equals(digits.substring(0, 9) + d1 + d2);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateCPFCheckDigit(String base, int weightStart) {
        int sum = 0;
        for (int i = 0; i < base.length(); i++) {
            sum += (base.charAt(i) - '0') * (weightStart - i);
        }
        int mod = sum % 11;
        return (mod < 2) ? 0 : 11 - mod;
    }

    public static void validarTelefoneCelular(String telefone) throws InvalidPhoneException {
        if (isNullOrEmpty(telefone)) {
            throw new InvalidPhoneException("Telefone celular não pode ser vazio");
        }
        String digits = telefone.replaceAll("\\D", "");
        // Celular brasileiro: 11 dígitos (DDI não incluído), com 9 na posição após DDD
        if (digits.length() != 11 || digits.charAt(2) != '9') {
            throw new InvalidPhoneException("Telefone celular inválido (formato esperado: DDD + 9 + 8 dígitos)");
        }
    }

    public static void validarCRM(String crm) throws InvalidCRMException {
        if (isNullOrEmpty(crm)) {
            throw new InvalidCRMException("CRM não pode ser vazio");
        }
        // 4-6 dígitos e opcional UF (ex: 12345-SP, 123456SP, 1234)
        if (!crm.toUpperCase().matches("\\d{4,6}([-/]?[A-Z]{2})?")) {
            throw new InvalidCRMException("CRM inválido (ex: 12345-SP)");
        }
    }
}
