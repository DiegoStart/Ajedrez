# ♟️ Ajedrez en Java

Proyecto desarrollado como parte de mi formación en Ingeniería en Sistemas Computacionales.

## Características

- Juego de ajedrez para consola.
- Implementación de las reglas oficiales.
- Enroque corto y largo.
- Captura al paso.
- Promoción de peón.
- Jaque y jaque mate.
- Ahogado.
- Tablas por material insuficiente.
- Tablas por triple repetición.
- Regla de los 50 movimientos.
- Temporizador por jugador.
- Guardado y carga de partidas con PostgreSQL.

## Tecnologías

- Java
- PostgreSQL
- JDBC
- Visual Studio Code

## Estructura del proyecto

```
src/
├── core/
├── consola/
├── persistencia/
└── gui/

assets/
database/
```

## Base de datos

1. Crear una base de datos PostgreSQL.
2. Ejecutar el script ubicado en:

```
database/
```

3. Crear un archivo `config.properties` tomando como base `config.example.properties`.

## Estado del proyecto

### Versión actual
- ✔ Consola funcional
- ✔ Persistencia en PostgreSQL

### Próximamente
- Interfaz gráfica 2D
- Interfaz gráfica 3D
- IA
- Multijugador

## Autor

Diego Venegas Chávez