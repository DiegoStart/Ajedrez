<div align="center">

# ♟️ AJEDREZ ♟️

> **🔴🟡 Sistema de Ajedrez Táctico, Multiversión y Persistente ⚪⚫**  
> *El ajedrez es como la vida: si tienes miedo de perder, nunca ganarás.*

<p align="center">
  <img src="https://img.shields.io/badge/JAVA-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/POSTGRESQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/STATUS-ALPHA_EDITION-yellow?style=for-the-badge&logo=chess&logoColor=black" alt="Status">
  <img src="https://img.shields.io/badge/INTERFACE-2D_%2F_CLI-red?style=for-the-badge" alt="Interface">
</p>

</div>

---

## 🕹️ SOBRE EL JUEGO

**Ajedrez en Java** es un motor y sistema completo de ajedrez diseñado bajo una arquitectura modular robusta. Implementa rigurosamente las reglas oficiales de la FIDE, gestión de usuarios con seguridad integrada, persistencia avanzada de partidas en **PostgreSQL** y múltiples interfaces (tanto en consola con estilo clásico como interfaz gráfica 2D).

---

## ⚡ CARACTERÍSTICAS PRINCIPALES

- ⚔️ **Reglas Oficiales Completas:** Enroque corto y largo, captura al paso y promoción de peón.
- 🚨 **Estados de Partida:** Detección automática de Jaque, Jaque Mate, Ahogado y Tablas (por material insuficiente, triple repetición y regla de los 50 movimientos).
- ⏱️ **Sistema de Tiempo:** Temporizadores sincronizados por jugador.
- 📜 **Historial Táctico:** Registro de movimientos en notación algebraica y soporte de estado mediante **FEN**.
- 🛡️ **Seguridad y Base de Datos:** Control de usuarios con encriptación, gestión de jugadores y guardado/carga de partidas mediante **PostgreSQL** y patrones DAO.
- 🖥️ **Interfaces Dinámicas:** Ejecución modular evolutiva desde consola hasta interfaz gráfica 2D con recursos visuales personalizados.

---

## 🚀 FASES EVOLUTIVAS DEL SISTEMA

El desarrollo ha evolucionado a través de distintas iteraciones de arquitectura:
- **`Alpha` ➔ `Beta` ➔ `Gamma` ➔ `Delta` ➔ `Omega`** *(Versiones de evolución y refinamiento del motor ubicadas en `src/app/`)*.

---

## 📂 ESTRUCTURA DEL PROYECTO

```text
.
├── .github/                 # Registros de flujos y control de evolución
├── .vscode/                 # Configuración de entorno de desarrollo
├── assets/                  # Recursos visuales del juego
│   └── Imagenes/
│       ├── Botones/         # Elementos interactivos de la UI
│       ├── Logos/           # Identidad visual del proyecto
│       └── Piezas/          # Sprites gráficos de piezas de ajedrez
├── database/
│   └── Ajedrez.sql          # Script SQL maestro para la base de datos
├── src/
│   ├── app/                 # Puntos de entrada evolutivos (Alpha a Omega)
│   ├── consola/             # Interfaz de comandos y gestión de texto
│   ├── controlador/         # Enlace entre la vista y las reglas de juego
│   ├── core/                # Núcleo lógico del tablero, reglas y tiempo
│   │   └── piezas/          # Lógica orientada a objetos de cada pieza
│   ├── gui/                 # Interfaz gráfica de usuario 2D
│   ├── modelo/              # Entidades POJO del sistema
│   ├── persistencia/        # Conexión JDBC y patrones DAO
│   └── util/                # Herramientas transversales (encriptación, excepciones)
├── .gitignore
├── config.example.properties # Plantilla de configuración de conexión
├── config.properties        # Configuración local activa
└── README.md
```

### 🧩 Arquitectura por Capas (`src/`)

| Módulo / Paquete | Responsabilidad Táctica |
| :--- | :--- |
| `app` | Lanzadores y versiones del sistema (`AjedrezAlpha` a `AjedrezOmega`). |
| `consola` | Interfaz interactiva alternativa basada en texto. |
| `controlador` | Orquestador de eventos entre las interfaces y el núcleo. |
| `core` | Cerebro del juego: validaciones, tableros y temporizadores. |
| `core/piezas` | Comportamientos específicos de Reyes, Reinas, Torres, Alfiles, Caballos y Peones. |
| `gui` | Renderizado gráfico 2D de las partidas. |
| `modelo` | Estructuras de datos y entidades de negocio. |
| `persistencia` | Conexión a base de datos y operaciones CRUD mediante DAO. |
| `util` | Seguridad, encriptación y utilidades generales. |

---

## 🗄️ BASE DE DATOS Y PERSISTENCIA

El motor se conecta a un servidor relacional en **PostgreSQL** para almacenar perfiles, jugadores e historiales de combate.

### 🔗 Relaciones del Sistema

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
   ├── 1 : N ──► MOVIMIENTO
   │
   └── 1 : 0..1 ──► INSTANTANEA
```

### ⚙️ Configuración Inicial

1. Crea una base de datos limpia en **PostgreSQL**.
2. Ejecuta el script estructural:
   ```text
   database/Ajedrez.sql
   ```
3. Duplica el archivo `config.example.properties`, nombra la copia como `config.properties` y configura tus accesos:
   ```properties
   db.url=jdbc:postgresql://localhost:5432/nombre_tu_base
   db.user=tu_usuario
   db.password=tu_contraseña
   ```

---

## 👤 CREDITOS & AUTOR

<div align="center">

**Diego Venegas Chávez**  
*Ingeniería en Sistemas Computacionales*  

</div>