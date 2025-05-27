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
ALTER TABLE InfoCliente.Cliente
ADD CONSTRAINT uq_email UNIQUE (Email),
ADD CONSTRAINT uq_telefono UNIQUE (Telefono);

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

CREATE TABLE InfoCliente.TarjetaCliente
(
	IdTarjeta BIGSERIAL NOT NULL,
	IdCliente BIGINT NOT NULL,
	Banco VARCHAR(100) NOT NULL,
	Tipo VARCHAR(100) NOT NULL,
	NumTarjeta BIGINT NOT NULL, 
	FechaVenci VARCHAR(100) NOT NULL,
	ConSeg INT NOT NULL,

	CONSTRAINT PKTarjetaCliente PRIMARY KEY (IdTarjeta),

	CONSTRAINT FKCliente FOREIGN KEY (IdCliente) 
	REFERENCES InfoCliente.Cliente(IdCliente)
)

ALTER TABLE InfoCliente.TarjetaCliente DROP COLUMN ConSeg;
ALTER TABLE InfoCliente.TarjetaCliente ADD COLUMN ConSeg VARCHAR(3);


ALTER TABLE InfoCliente.TarjetaCliente
ADD CONSTRAINT uq_numTarjeta UNIQUE (NumTarjeta);

ALTER TABLE InfoCliente.TarjetaCliente
ADD CONSTRAINT chk_banco CHECK (Banco IN ('Santander','Banamex','BBVA','HSBC','BanBajio','Scotiabank','Banorte','Azteca','Inbursa','Afirme'));

ALTER TABLE InfoCliente.TarjetaCliente
ADD CONSTRAINT chk_tipo CHECK (Tipo IN ('Crédito','Débito'));

CREATE OR REPLACE FUNCTION actualizar_num_tarjetas()
RETURNS TRIGGER AS $$
BEGIN
    -- Actualizar el número de tarjetas para el nuevo cliente si hay un cambio de cliente
    IF NEW.IdCliente IS DISTINCT FROM OLD.IdCliente THEN
        UPDATE InfoCliente.Cliente
        SET NumTarjetas = (
            SELECT COUNT(*) 
            FROM InfoCliente.TarjetaCliente 
            WHERE IdCliente = NEW.IdCliente
        )
        WHERE IdCliente = NEW.IdCliente;

        UPDATE InfoCliente.Cliente
        SET NumTarjetas = (
            SELECT COUNT(*) 
            FROM InfoCliente.TarjetaCliente 
            WHERE IdCliente = OLD.IdCliente
        )
        WHERE IdCliente = OLD.IdCliente;
    ELSE
        -- Si no hay cambio de cliente, actualizar normalmente
        UPDATE InfoCliente.Cliente
        SET NumTarjetas = (
            SELECT COUNT(*) 
            FROM InfoCliente.TarjetaCliente 
            WHERE IdCliente = COALESCE(NEW.IdCliente, OLD.IdCliente)
        )
        WHERE IdCliente = COALESCE(NEW.IdCliente, OLD.IdCliente);
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;




CREATE TRIGGER TGActualizaNumTarjetas
AFTER INSERT OR UPDATE OR DELETE ON InfoCliente.TarjetaCliente
FOR EACH ROW
EXECUTE FUNCTION actualizar_num_tarjetas();

SELECT * FROM InfoCliente.TarjetaCliente

CREATE TABLE  	
(
    IdItinerario BIGSERIAL NOT NULL,
    IdSalida BIGINT NOT NULL,
	IdLlegada BIGINT NOT NULL,   
    Dias VARCHAR(50) NOT NULL,
    HoraSalida VARCHAR(50) NOT NULL,
	HoraLlegada VARCHAR(50) NOT NULL,
    KMs INT NOT NULL, 

    CONSTRAINT PKItinerario PRIMARY KEY (IdItinerario),

	CONSTRAINT FKITerSalida FOREIGN KEY (IdSalida) 
	REFERENCES Destinos.Terminal(IdTerminal),

	CONSTRAINT FKITerLleg FOREIGN KEY (IdLlegada) 
	REFERENCES Destinos.Terminal(IdTerminal)
);

INSERT INTO Destinos.Itinerario(IdSalida, IdLlegada, Dias, HoraSalida, HoraLlegada, KMs)
VALUES ( 1, 5, 'L, M, V,', '7:00', '15:00', 800)

SELECT * FROM Destinos.Itinerario
















































