BEGIN WORK;
SET TRANSACTION READ WRITE;

SET datestyle = YMD;

-- Esborra taules si existien
DROP TABLE IF EXISTS Juego;
DROP TABLE IF EXISTS Mod;
DROP TABLE IF EXISTS Categoria;

-- Crear la tabla Juego
CREATE TABLE Juego (
    JuegoID SERIAL PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Descripcion TEXT
);

-- Crear la tabla Mod
CREATE TABLE Mod (
    ModID SERIAL PRIMARY KEY,
    JuegoID INT,
    Nombre VARCHAR(255) NOT NULL,
    Autor VARCHAR(255) NOT NULL,
    Descripcion TEXT,
    Version DECIMAL(5, 2),
    FechaSubida DATE,
    FechaActualizacion DATE,
    Tamano VARCHAR(20),
    VersionMinecraft VARCHAR(10),
    RequisitoForge VARCHAR(20),
    Downloads INT,
    FOREIGN KEY (JuegoID) REFERENCES Juego(JuegoID)
);

-- Crear la tabla Categoria
CREATE TABLE Categoria (
    CategoriaID SERIAL PRIMARY KEY,
    ModID INT,
    Nombre VARCHAR(255) NOT NULL,
    FOREIGN KEY (ModID) REFERENCES Mod(ModID)
);

COMMIT;