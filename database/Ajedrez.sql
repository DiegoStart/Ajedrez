CREATE DATABASE Ajedrez;

CREATE SCHEMA ajedrez;

SET search_path TO ajedrez;

CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    foto_perfil TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'ACTIVO'
);

CREATE TABLE jugador (
    id_jugador SERIAL PRIMARY KEY,
    id_usuario INT,
    nombre_jugador VARCHAR(50) NOT NULL,
    tipo VARCHAR(20) NOT NULL DEFAULT 'HUMANO',
    elo INT DEFAULT 1200,
    victorias INT DEFAULT 0,
    derrotas INT DEFAULT 0,
    tablas INT DEFAULT 0,
    rendiciones INT DEFAULT 0,
    abandonos INT DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
);

CREATE TABLE partida (
    id_partida SERIAL PRIMARY KEY,
    estado VARCHAR(20) DEFAULT 'ESPERANDO',
    resultado VARCHAR(10),
    causa_finalizacion VARCHAR(50),
    tipo_partida VARCHAR(20) DEFAULT 'LOCAL',
    tiempo_control VARCHAR(20) DEFAULT '600',
    duracion INT,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP
);

CREATE TABLE participacion (
    id_participacion SERIAL PRIMARY KEY,
    id_partida INT NOT NULL,
    id_jugador INT NOT NULL,
    color BOOLEAN NOT NULL,
    resultado_individual VARCHAR(20),
    tiempo_restante INT,

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,

    FOREIGN KEY (id_jugador)
        REFERENCES jugador(id_jugador)
        ON DELETE CASCADE
);

CREATE TABLE movimiento (
    id_movimiento SERIAL PRIMARY KEY,
    id_partida INT NOT NULL,
    numero_movimiento INT NOT NULL,
    color BOOLEAN NOT NULL,
    pieza VARCHAR(20) NOT NULL,
    origen TEXT NOT NULL,
	destino TEXT NOT NULL,
    pieza_capturada VARCHAR(20),
    tipo_movimiento VARCHAR(30) DEFAULT 'NORMAL',
    jaque BOOLEAN DEFAULT FALSE,
    jaque_mate BOOLEAN DEFAULT FALSE,
    causa_tablas VARCHAR(50HAR(20),
    pieza_promocion VARCHAR(10),
    fen TEXT,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
),
    notacion_algebraica VARC
    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE
);

CREATE TABLE instantanea (
    id_instantanea SERIAL PRIMARY KEY,
    id_partida INT UNIQUE NOT NULL,
    estado_actual TEXT NOT NULL,
    turno_actual BOOLEAN NOT NULL,
    tiempo_blancas INT,
    tiempo_negras INT,
    ultimo_movimiento TEXT,
    contador_movimientos INT DEFAULT 1,
    cincuenta_movimientos INT DEFAULT 0,

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE
);

-- Consulta equivalente:
SELECT * FROM ajedrez.instantanea;
SELECT * FROM ajedrez.usuario;
SELECT * FROM ajedrez.jugador;
SELECT * FROM ajedrez.movimiento;
SELECT * FROM ajedrez.partida;
SELECT * FROM ajedrez.participacion;

TRUNCATE TABLE ajedrez.movimiento, ajedrez.instantanea, ajedrez.participacion, ajedrez.partida, ajedrez.jugador, ajedrez.usuario RESTART IDENTITY CASCADE;