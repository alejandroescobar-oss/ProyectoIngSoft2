package com.taller2;

import database.DatabaseInitializer;
import javafx.application.Application;
import javafx.stage.Stage;
import repository.UsuarioRepository;
import repository.UsuarioRepositorySQLite;
import security.Argon2PasswordHasher;
import security.PasswordHasher;
import service.UsuarioService;
import ui.LoginView;
import validation.PasswordValidator;


public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Inicializar base de datos
        DatabaseInitializer.initialize();

        // Repository
        UsuarioRepository repository =
                new UsuarioRepositorySQLite();

        // Validación de contraseña
        PasswordValidator passwordValidator =
                new PasswordValidator();

        // Hashing
        PasswordHasher passwordHasher =
                new Argon2PasswordHasher();

        // Servicio
        UsuarioService usuarioService =
                new UsuarioService(
                        repository,
                        passwordValidator,
                        passwordHasher
                );

        // Mostrar Login
        LoginView loginView =
                new LoginView(usuarioService);

        loginView.mostrar(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}