# Preguntas de reflexión para la sustentación

Las respuestas distinguen el demo de cursos del Banco de Preguntas. El demo (`jpa-clean-persistence-demo`) mantiene objetos de dominio separados de los registros JPA y muestra relaciones y herencia con JPA. La aplicación principal ya usa Spring Boot, Spring Data JPA y H2 en memoria para guardar usuarios y preguntas; sus entidades JPA son distintas de las clases de dominio y los servicios dependen de contratos de repositorio.

## 1. ¿Qué ventajas y desventajas tiene mantener dos modelos, uno de dominio y otro de persistencia?

Separarlos evita que las reglas del negocio dependan de JPA. El modelo de dominio expresa conceptos y comportamientos del Banco de Preguntas —por ejemplo, una pregunta y las transiciones válidas de su ciclo de vida— sin anotaciones ni detalles de tablas. El modelo de persistencia puede representar claves foráneas, relaciones, restricciones y estrategias de herencia según las necesidades de la base de datos. Así se puede modificar el mapeo o cambiar la tecnología de persistencia con menos impacto en las reglas y los casos de uso.

El costo es mantener ambos modelos sincronizados y escribir y probar mapeadores entre ellos. También puede haber conversiones adicionales y riesgo de errores si los modelos evolucionan de forma distinta. En el demo y en el Banco de Preguntas, `DomainMapper` realiza esa conversión entre el dominio y los registros JPA.

## 2. ¿Qué cambios serían necesarios si el sistema dejara de utilizar JPA?

Con los contratos de repositorio separados de la tecnología, los servicios de aplicación podrían seguir dependiendo de esos contratos. Se reemplazaría la implementación JPA por otra —por ejemplo, JDBC, SQLite u otro mecanismo— y se ajustarían la configuración, las consultas, el acceso a datos y las pruebas de infraestructura. También habría que revisar o reemplazar los registros JPA y el mapeador, pues son específicos de esa tecnología. Las reglas de negocio y las interfaces de los contratos deberían mantenerse siempre que la nueva implementación respete sus operaciones y resultados.

En el Banco de Preguntas, la interfaz `QuestionRepository` y `UsuarioRepository` continúan siendo contratos de dominio; `QuestionRepositoryJpa` y `UsuarioRepositoryJpa` son las implementaciones concretas que usan Spring Data JPA. Para reemplazar JPA se crearían nuevas implementaciones y se cambiaría su configuración sin modificar los servicios. La configuración de esta entrega usa H2 en memoria como pide el taller.

## 3. ¿Cuándo conviene una relación muchos a muchos directa y cuándo es mejor crear una entidad asociativa?

Una relación muchos a muchos directa conviene si la asociación solo expresa que dos elementos están relacionados y no necesita datos ni reglas propias. En el demo, la matrícula entre estudiantes y cursos se representa con `@ManyToMany` y la tabla intermedia `student_courses`.

Si la asociación tiene atributos, identidad o ciclo de vida propios, conviene modelarla como una entidad asociativa. Para las preguntas y competencias, `QuestionCompetency` permite guardar, por ejemplo, el porcentaje con que una competencia contribuye a una pregunta. También facilitaría guardar fechas, orden o estado de la asociación. En ese diseño, la entidad asociativa tendría una relación muchos a uno con `Question` y otra con `Competency`, y la base de datos conservaría las dos claves foráneas.

## 4. ¿Qué problemas pueden aparecer al recuperar asociaciones perezosas (`LAZY`) fuera de una transacción?

Si se accede a una asociación perezosa después de cerrar la sesión o el contexto de persistencia, JPA puede lanzar `LazyInitializationException`, porque ya no puede cargar los datos relacionados. Intentar solucionar el problema cargando asociaciones indiscriminadamente puede producir consultas N+1, más tráfico y degradación del rendimiento. Además, serializar directamente entidades con relaciones bidireccionales puede provocar ciclos o exponer datos que no deberían formar parte de la respuesta.

Para evitarlo, los casos de uso deben recuperar los datos que necesitan dentro de una transacción y devolver una representación adecuada, como un DTO. Según la consulta, se pueden usar cargas explícitas (`JOIN FETCH` o `EntityGraph`) y probar tanto el resultado como la cantidad de consultas. En el Banco, las opciones se mapean como una relación JPA y el adaptador las convierte en objetos del dominio dentro de la transacción.

## 5. ¿Qué estrategia de herencia elegirían si el sistema incorpora muchos tipos de preguntas con atributos y reglas diferentes?

Como punto de partida elegiríamos `JOINED`: los atributos comunes quedarían en una tabla base de preguntas y los específicos en tablas por tipo. Esto evita una tabla única llena de columnas nulas y reduce la duplicación de los atributos comunes, lo que resulta razonable si cada tipo tiene datos y restricciones diferentes. La decisión debe validarse con las consultas previstas, porque recuperar preguntas de distintos tipos puede requerir uniones y tener un costo mayor.

Si las consultas necesitan combinar muchos tipos con frecuencia y prima la simplicidad de lectura, `SINGLE_TABLE` podría ser más adecuada, aceptando columnas nulas y restricciones más complejas. `TABLE_PER_CLASS` puede servir cuando los tipos concretos son muy independientes, pero duplica atributos comunes y dificulta algunas consultas polimórficas. El demo elige `JOINED` para `PersonRecord`, `StudentRecord` e `InstructorRecord`; esa elección ilustra la estrategia, pero no significa que la jerarquía de preguntas ya esté implementada con JPA.

## 6. ¿Por qué la persistencia de una pregunta y la validación de su ciclo de vida son responsabilidades distintas?

La persistencia se ocupa de guardar, consultar y actualizar datos. El ciclo de vida es una regla del negocio: determina qué cambios de estado son válidos y bajo qué condiciones una pregunta puede avanzar, ser rechazada, publicarse o archivarse. Mezclar ambas responsabilidades en el repositorio permitiría que una actualización de base de datos cambiara el estado sin validar la transición.

En el Banco de Preguntas, `QuestionService` valida las transiciones permitidas antes de actualizar la pregunta y luego utiliza el contrato del repositorio para guardar el cambio. Así la regla no depende de JPA, mientras que la implementación concreta se ocupa de persistirla. En operaciones con varios cambios relacionados, una transacción puede asegurar que se confirmen juntos o se reviertan ante un error.

## Criterio final

La arquitectura busca mantener las reglas del Banco de Preguntas independientes de los detalles de persistencia. El dominio y los contratos expresan los conceptos y operaciones del negocio; la infraestructura se encarga de traducirlos al almacenamiento elegido. La aplicación Spring Boot y la interfaz Swing comparten los servicios y la persistencia H2/JPA. H2 se configura en memoria, por lo que reiniciar la aplicación elimina sus registros. La migración de los antiguos archivos SQLite y la implementación del resto de entidades/casos de uso del dominio no están incluidas.
