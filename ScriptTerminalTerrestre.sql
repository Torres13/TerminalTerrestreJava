-- Database: TerminalTerrestre

-- DROP DATABASE IF EXISTS "TerminalTerrestre";

CREATE DATABASE "TerminalTerrestre"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en-US'
    LC_CTYPE = 'en-US'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE Destinos.Ciudad
(
	IdCiudad BIGSERIAL NOT NULL,
	NonCiudad VARCHAR(200) NOT NULL,
	
	CONSTRAINT PKCiudad PRIMARY KEY (IdCiudad)
)

SELECT * FROM Destinos.Ciudad

CREATE TABLE Destinos.Estacion
(
	IdEstacion BIGSERIAL NOT NULL,
	IdCiudad BIGINT NOT NULL,
	NomEstacion VARCHAR(200) NOT NULL,
	Direccion VARCHAR(500) NOT NULL,
	NumTerminales INT,
	
	CONSTRAINT PKEstacion PRIMARY KEY (IdEstacion),

	CONSTRAINT FKCiudad1 FOREIGN KEY (IdCiudad) 
	REFERENCES Destinos.Ciudad(IdCiudad)
)

INSERT INTO Destinos.Estacion(IdCiudad, NomEstacion, Direccion, NumTerminales)
VALUES (1, 'TTP', 'Av. Benito Juares 500', 0)

SELECT * FROM Destinos.Estacion

CREATE TABLE Destinos.Terminal
(
	IdTerminal BIGSERIAL NOT NULL,
	IdEstacion BIGINT NOT NULL,
	NomTerminal VARCHAR(200) NOT NULL,
	
	CONSTRAINT PKTerminal PRIMARY KEY (IdTerminal),

	CONSTRAINT FKEstacion1 FOREIGN KEY (IdEstacion) 
	REFERENCES Destinos.Estacion(IdEstacion)
)
SELECT * FROM Destinos.Terminal

INSERT INTO Destinos.Terminal(IdEstacion, NomTerminal)
VALUES (1, 'Local')

CREATE OR REPLACE FUNCTION actualizar_num_terminales()
RETURNS TRIGGER AS $$
BEGIN
    -- Actualizar la estación anterior si se modificó IdEstacion
    IF OLD.IdEstacion IS NOT NULL THEN
        UPDATE Destinos.Estacion
        SET NumTerminales = (
            SELECT COUNT(*) FROM Destinos.Terminal
            WHERE IdEstacion = OLD.IdEstacion
        )
        WHERE IdEstacion = OLD.IdEstacion;
    END IF;

    -- Actualizar la nueva estación si se modificó IdEstacion
    IF NEW.IdEstacion IS NOT NULL THEN
        UPDATE Destinos.Estacion
        SET NumTerminales = (
            SELECT COUNT(*) FROM Destinos.Terminal
            WHERE IdEstacion = NEW.IdEstacion
        )
        WHERE IdEstacion = NEW.IdEstacion;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;




DROP TRIGGER IF EXISTS TGCalculaNumTerminales ON Destinos.Terminal;

CREATE TRIGGER TGCalculaNumTerminales
AFTER INSERT OR DELETE OR UPDATE ON Destinos.Terminal
FOR EACH ROW
EXECUTE FUNCTION actualizar_num_terminales();

CREATE TABLE Operador
(
	IdOperador BIGSERIAL NOT NULL,
	NomOpe VARCHAR(200) NOT NULL,
	RFC VARCHAR(13) NOT NULL,
	Salario FLOAT NOT NULL, 

	CONSTRAINT PKOperador PRIMARY KEY (IdOperador)
)
INSERT INTO Operador(NomOpe, RFC, Salario)
VALUES ( 'Josue Torres', 'TORJ971113JS4', 2500)

SELECT * FROM Operador

CREATE SCHEMA InfoCliente

CREATE TABLE InfoCliente.Cliente
(
	IdCliente BIGSERIAL NOT NULL,
	NomCliente VARCHAR(200) NOT NULL,
	Email VARCHAR(100) NOT NULL,
	Telefono BIGINT NOT NULL, 
	ClienteDesde DATE,
	NumTarjetas INT,

	CONSTRAINT PKOperador PRIMARY KEY (IdCliente)
)

INSERT INTO InfoCliente.Cliente(NomCliente, Email, Telefono, ClienteDesde, NumTarjetas)
VALUES ( 'Josue Torres', 'josue@uaslp.com', 4444276945, '2025-05-21',0)

SELECT * FROM InfoCliente.Cliente

CREATE OR REPLACE FUNCTION set_cliente_desde()
RETURNS TRIGGER AS $$
BEGIN
    NEW.ClienteDesde := CURRENT_DATE;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_SetClienteDesde
BEFORE INSERT ON InfoCliente.Cliente
FOR EACH ROW
EXECUTE FUNCTION set_cliente_desde();