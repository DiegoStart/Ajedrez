# ♟️ Ajedrez en Java

El presente proyecto consiste en el desarrollo de un sistema de ajedrez en **Java** como parte de mi formación en Ingeniería en Sistemas Computacionales.

u objetivo es implementar las principales reglas del ajedrez, permitir el desarrollo de partidas mediante una interfaz gráfica y almacenar la información de las partidas utilizando **PostgreSQL**.

## 🎮 Características

* Juego de ajedrez.
* Implementación de las reglas oficiales.
* Enroque corto y largo.
* Captura al paso.
* Promoción de peón.
* Jaque y jaque mate.
* Ahogado.
* Tablas por material insuficiente.
* Tablas por triple repetición.
* Regla de los 50 movimientos.
* Temporizador por jugador.
* Registro de movimientos y notación algebraica.
* Almacenamiento del estado de la partida mediante FEN.
* Guardado y carga de partidas con PostgreSQL.
* Gestión de usuarios y jugadores.
* Sistema de participaciones por partida.
* Interfaz gráfica 2D.

## 🛠️ Tecnologías

* **Java**
* **PostgreSQL**
* **JDBC**
* **Visual Studio Code**

## 📂 Estructura del proyecto

```text
.
├── assets/
│   └── Imagenes/
├── database/
│   └── Ajedrez.sql
├── src/
│   ├── app/
│   ├── consola/
│   ├── controlador/
│   ├── core/
│   │   └── piezas/
│   ├── gui/
│   ├── modelo/
│   └── persistencia/
│       └── dao/
├── .gitignore
├── README.md
├── config.example.properties
```

### Organización principal

| Paquete            | Responsabilidad                                          |
| ------------------ | -------------------------------------------------------- |
| `app`              | Puntos de entrada y versiones del proyecto               |
| `consola`          | Funcionalidad relacionada con la consola y excepciones   |
| `controlador`      | Coordinación entre la interfaz y la lógica del sistema   |
| `core`             | Lógica principal del juego y reglas del ajedrez          |
| `core/piezas`      | Implementación de las piezas de ajedrez                  |
| `gui`              | Interfaz gráfica                                         |
| `modelo`           | Entidades y datos del sistema                            |
| `persistencia`     | Conexión y acceso a PostgreSQL                           |
| `persistencia/dao` | Operaciones de acceso a cada entidad de la base de datos |

## 🗄️ Base de datos

El proyecto utiliza **PostgreSQL** para almacenar la información de usuarios, jugadores y partidas.

### Entidades

* `usuario`
* `jugador`
* `partida`
* `participacion`
* `movimiento`
* `instantanea`

### Relaciones

```text
USUARIO
   │
   │ 1 : N
   ▼
JUGADOR
   │
   │ 1 : N
   ▼
PARTICIPACION
   ▲
   │ N : 1
   │
PARTIDA
   │
   ├── 1 : N ──► MOVIMIENTO
   │
   └── 1 : 0..1 ──► INSTANTANEA
```

### Configuración

1. Crear la base de datos PostgreSQL.
2. Ejecutar el script:

```text
database/Ajedrez.sql
```

3. Crear el archivo `config.properties` tomando como referencia:

```text
config.example.properties
```

## 🚀 Estado del proyecto

### Versión actual

* ✔ Lógica de ajedrez funcional.
* ✔ Reglas oficiales principales implementadas.
* ✔ Temporizadores.
* ✔ Historial de movimientos.
* ✔ Notación algebraica.
* ✔ Detección de condiciones de tablas.
* ✔ Persistencia en PostgreSQL.
* ✔ Modelo de datos estructurado.
* ✔ Sistema de usuarios y jugadores.
* ✔ Interfaz gráfica 2D.
* ✔ Arquitectura organizada por responsabilidades.

### Próximamente

* ⏳ Mejoras y ampliación de la interfaz gráfica.
* ⏳ Inteligencia artificial.
* ⏳ Multijugador.
* ⏳ Interfaz gráfica 3D.
* ⏳ Nuevas versiones y ampliaciones del sistema.

## 👤 Autor

**Diego Venegas Chávez**

Estudiante de Ingeniería en Sistemas Computacionales.