# Sistema de Gestión de RRHH

Aplicación de escritorio en Java (JavaFX + MySQL) para el curso de Diseño de Patrones — Grupo 12.

## Requisitos

- JDK 17 o superior
- Maven 3.9+
- MySQL 8+

## Configuración

1. Copia `.env.example` a `.env` y completa tus credenciales de MySQL:

```
DB_URL=jdbc:mysql://localhost:3306/rrhh?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=changeme
DB_POOL_SIZE=5
FERIADOS_PAIS_DEFAULT=PE
```

2. Crea la base de datos vacía `rrhh` en tu servidor MySQL.

3. Aplica el esquema y los datos semilla:

```bash
mvn compile
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes;src/main/resources;$(cat cp.txt)" com.rrhh.util.SchemaInitializer
rm cp.txt
```

Esto crea las tablas y un usuario administrador semilla: `admin` / `admin123`.

## Ejecutar

```bash
mvn javafx:run
```

## Probar

```bash
mvn test
```

Incluye pruebas unitarias (patrones sin BD) y de integración (contra MySQL real y la API pública de feriados Nager.Date).

## Documentación

Ver [`docs/DOCUMENTACION.md`](docs/DOCUMENTACION.md) para la justificación de patrones, arquitectura y principios SOLID/GRASP, y [`docs/diagrama-clases.puml`](docs/diagrama-clases.puml) para el diagrama de clases (PlantUML).
