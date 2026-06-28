CREATE DATABASE AjedrezPrueba;

CREATE TABLE jugador (
    id_jugador SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE partida (
    id_partida SERIAL PRIMARY KEY,
    id_jugador_blanco INT NOT NULL,
    id_jugador_negro INT NOT NULL,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    resultado VARCHAR(30),
    causa_finalizacion VARCHAR(50),
    estado VARCHAR(20) DEFAULT 'En curso',

    FOREIGN KEY (id_jugador_blanco) REFERENCES jugador(id_jugador),
    FOREIGN KEY (id_jugador_negro) REFERENCES jugador(id_jugador)
);

CREATE TABLE movimiento (
    id_movimiento SERIAL PRIMARY KEY,
    id_partida INT NOT NULL,
    numero_movimiento INT NOT NULL,
    color VARCHAR(10) NOT NULL,
    origen VARCHAR(2) NOT NULL,
    destino VARCHAR(2) NOT NULL,
    pieza VARCHAR(20) NOT NULL,
    pieza_capturada VARCHAR(20),
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE
);

CREATE TABLE tablero (
    id_tablero SERIAL PRIMARY KEY,
    id_partida INT UNIQUE NOT NULL,
    tablero_actual TEXT NOT NULL,
    turno_actual VARCHAR(10) NOT NULL,
    tiempo_blancas INT,
    tiempo_negras INT,
    ultimo_movimiento TEXT,
    contador_movimientos INT DEFAULT 1,

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE
);

SELECT * FROM jugador;
SELECT * FROM partida;
SELECT * FROM movimiento;
SELECT * FROM tablero;
SELECT * FROM movimiento WHERE id_partida = 1;
UPDATE tablero
SET tablero_actual = 'nnnnnnnnnnnnnnnnnnkmnnPmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnTmnnnnnnnnnnnnKmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnqmnnnnnn',
    turno_actual = 'Blanco',
    ultimo_movimiento = '-1,-1,-1,-1',
    contador_movimientos = 87
WHERE id_partida = 7;