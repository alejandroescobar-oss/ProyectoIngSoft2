package com.taller2;

import database.DatabaseInitializer;
import core.Kernel;
import repository.UsuarioRepositorySQLite;
import repository.QuestionRepositorySQLite;
import security.Argon2PasswordHasher;
import service.QuestionService;
import service.UsuarioService;
import ui.swing.LoginView;
import validation.PasswordValidator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializar base de datos (tablas de usuarios y preguntas)
        DatabaseInitializer.initialize();
        Kernel kernel = new Kernel();
        kernel.arrancar();

        // 2. Crear dependencias del servicio de usuarios
        UsuarioRepositorySQLite usuarioRepository = new UsuarioRepositorySQLite();
        PasswordValidator passwordValidator = new PasswordValidator();
        Argon2PasswordHasher passwordHasher = new Argon2PasswordHasher();
        UsuarioService usuarioService = new UsuarioService(
                usuarioRepository,
                passwordValidator,
                passwordHasher
        );
        QuestionService questionService = new QuestionService(new QuestionRepositorySQLite());

        // 3. Lanzar la vista de Login en Swing (asegura que se ejecute en el hilo de eventos)
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(usuarioService, questionService, kernel);
            loginView.setVisible(true);   // Método de JFrame
        });
    }
}