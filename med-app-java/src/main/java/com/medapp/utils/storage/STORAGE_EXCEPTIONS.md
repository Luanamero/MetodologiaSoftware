# Exceções de Armazenamento - MedApp

## 📋 Visão Geral

Este documento descreve as exceções de armazenamento implementadas no sistema MedApp, seguindo as boas práticas já estabelecidas no projeto para tratamento de exceções.

## 🏗️ Estrutura das Exceções

### Hierarquia de Exceções

```
StorageException (classe base)
├── UserAlreadyExistsException
├── UserNotFoundException
├── StoragePermissionException
├── StorageCorruptedException
├── InsufficientStorageSpaceException
└── FileStorageException
```

## 📚 Exceções Implementadas

### 1. `StorageException`
**Localização:** `com.medapp.utils.storage.StorageException`

**Descrição:** Exceção base para todos os problemas relacionados ao armazenamento de dados.

**Uso:**
```java
public class StorageException extends RuntimeException {
    public StorageException(String message);
    public StorageException(String message, Throwable cause);
}
```

### 2. `UserAlreadyExistsException`
**Localização:** `com.medapp.utils.storage.UserAlreadyExistsException`

**Descrição:** Lançada quando se tenta criar um usuário que já existe no sistema.

**Exemplo:**
```java
// Lançada quando tentar salvar um usuário já existente
throw new UserAlreadyExistsException("username");
// Mensagem: "User 'username' already exists in the storage system."
```

### 3. `UserNotFoundException`
**Localização:** `com.medapp.utils.storage.UserNotFoundException`

**Descrição:** Lançada quando se tenta acessar um usuário que não existe no sistema.

**Exemplo:**
```java
// Lançada ao tentar carregar um usuário inexistente
throw new UserNotFoundException("username");
// Mensagem: "User 'username' not found in the storage system."
```

### 4. `StoragePermissionException`
**Localização:** `com.medapp.utils.storage.StoragePermissionException`

**Descrição:** Lançada quando há problemas de permissão no sistema de arquivos.

**Exemplo:**
```java
// Problema de permissão genérico
throw new StoragePermissionException("write file");

// Problema de permissão específico
throw new StoragePermissionException("write file", "/path/to/file");
```

### 5. `StorageCorruptedException`
**Localização:** `com.medapp.utils.storage.StorageCorruptedException`

**Descrição:** Lançada quando os dados estão corrompidos ou em formato inválido.

**Exemplo:**
```java
// Arquivo corrompido
throw new StorageCorruptedException("user.json");

// Com causa raiz
throw new StorageCorruptedException("user.json", ioException);
```

### 6. `InsufficientStorageSpaceException`
**Localização:** `com.medapp.utils.storage.InsufficientStorageSpaceException`

**Descrição:** Lançada quando não há espaço suficiente no sistema de armazenamento.

**Exemplo:**
```java
// Espaço insuficiente genérico
throw new InsufficientStorageSpaceException();

// Para operação específica
throw new InsufficientStorageSpaceException("save user");
```

### 7. `FileStorageException`
**Localização:** `com.medapp.utils.storage.FileStorageException`

**Descrição:** Lançada para problemas específicos de I/O em arquivos.

**Exemplo:**
```java
// Erro de operação em arquivo
throw new FileStorageException("write", "/path/to/file");

// Com causa raiz
throw new FileStorageException("read", "/path/to/file", ioException);
```

## 🔧 Implementação nos Repositórios

### FileRepository

O `FileRepository` foi atualizado para usar todas as exceções de armazenamento:

```java
@Override
public void saveUser(User user) {
    File file = new File(USER_FOLDER, user.getUsername() + ".json");
    
    // Verificar se o usuário já existe
    if (file.exists()) {
        throw new UserAlreadyExistsException(user.getUsername());
    }
    
    try {
        mapper.writeValue(file, user);
    } catch (IOException e) {
        // Tratamento específico para diferentes tipos de erro
        if (e.getMessage().toLowerCase().contains("no space left")) {
            throw new InsufficientStorageSpaceException("save user");
        }
        if (e.getMessage().toLowerCase().contains("permission denied")) {
            throw new StoragePermissionException("write file", file.getAbsolutePath());
        }
        throw new FileStorageException("write", file.getAbsolutePath(), e);
    }
}
```

### UserController

O `UserController` foi atualizado para capturar e tratar as exceções de armazenamento:

```java
public String registerUser(User user) {
    try {
        UsernameValidator.validate(user.getUsername());
        PasswordValidator.validate(user);
        userRepository.saveUser(user);
        return String.format("User '%s' successfully registered.", user.getUsername());
    } catch (UserAlreadyExistsException e) {
        return String.format("Registration failed: %s", e.getMessage());
    } catch (StorageException e) {
        return String.format("Storage error during registration: %s", e.getMessage());
    }
}
```