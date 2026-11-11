# Proyecto Ingeniería de Software II

Este repositorio conserva la aplicación de escritorio Swing/SQLite del curso y contiene el material y las implementaciones separadas del Taller 10 de JPA.

## Taller 10

- `Taller10_Informe.md`: análisis del demo escolar, respuestas de diseño y diagramas UML/relacional.
- `jpa-clean-persistence-demo\jpa-clean-persistence-demo`: demo Spring Boot del Punto 1 (cursos, estudiantes y departamentos).
- `question-bank-jpa`: aplicación Spring Boot para el Banco Saber Pro, con arquitectura por capas, dominio independiente de JPA, H2 y pruebas.
- `Taller10_JPA_Guia.md`: enunciado completo del laboratorio.

## Requisitos

JDK 17 o superior y Maven 3.8 o superior. Desde PowerShell en esta carpeta:

```powershell
mvn test
mvn -f .\jpa-clean-persistence-demo\jpa-clean-persistence-demo\pom.xml test
mvn -f .\question-bank-jpa\pom.xml test
```

Ejecutar el demo escolar:

```powershell
Set-Location .\jpa-clean-persistence-demo\jpa-clean-persistence-demo
mvn spring-boot:run
```

Ejecutar el Banco Saber Pro en otra terminal:

```powershell
Set-Location .\question-bank-jpa
mvn spring-boot:run
```

Ambas aplicaciones usan el puerto 8080 por defecto; no se deben ejecutar simultáneamente en ese mismo puerto. Para cambiar el puerto del Banco Saber Pro en PowerShell, establece `$env:SERVER_PORT = '18080'` antes de `mvn spring-boot:run`. Cada aplicación tiene su propia base H2 en memoria y consola documentada en su README. La aplicación principal Swing continúa iniciándose desde el IDE con `com.taller2.Main`; su base SQLite no es reemplazada por estos demos.
