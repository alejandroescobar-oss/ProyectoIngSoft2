# Builder para el Banco de Preguntas

Ejemplo en Java 17 del patrón creacional Builder para construir preguntas de
selección múltiple, verdadero/falso y abiertas.

## Modelo

Todas las preguntas requieren enunciado, dificultad, categoría y autor. La
justificación y la fuente son opcionales. Además:

- Selección múltiple: requiere al menos dos opciones y una respuesta correcta
  que coincida exactamente con una de ellas.
- Verdadero/falso: requiere una respuesta booleana.
- Abierta: requiere una respuesta esperada de referencia.

`Question` es inmutable. El `QuestionDirector` establece los campos comunes en
un orden único y delega la configuración y validación específica del tipo al
Builder recibido. Las opciones se representan con objetos inmutables `Option`.
El código de producción está organizado en los paquetes `builder`, `director`
y `domain`.

## Ejecutar la demostración y las pruebas JUnit

Desde la raíz del proyecto, en PowerShell:

```powershell
$buildDir = Join-Path $env:TEMP ("Taller-7-Mage-" + [guid]::NewGuid().ToString("N"))
New-Item -ItemType Directory -Path $buildDir | Out-Null
$junitJar = "test-lib\junit-platform-console-standalone-6.1.3.jar"
try {
    $sources = Get-ChildItem src\main\java, src\test\java -Recurse -Filter *.java
    javac -encoding UTF-8 -cp $junitJar -d $buildDir $sources.FullName
    if ($LASTEXITCODE -ne 0) { throw "Falló la compilación." }
    java -cp $buildDir gestionbancodepreguntas.ApplicationMain
    if ($LASTEXITCODE -ne 0) { throw "Falló la demostración." }
    java -jar $junitJar execute --class-path $buildDir --scan-class-path
    if ($LASTEXITCODE -ne 0) { throw "Fallaron las pruebas." }
} finally {
    Remove-Item -LiteralPath $buildDir -Recurse -Force
}
```

Los archivos `.class` se guardan temporalmente fuera del proyecto y se eliminan
al finalizar. El proyecto incluye el ejecutable JUnit Platform Console Standalone
en `test-lib`, por lo que no es necesario descargar dependencias.
