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

CREATE TABLE Destinos.Itinerario  	
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

CREATE TABLE Pasajero (
    IdPasajero BIGSERIAL NOT NULL,
    NombrePasajero VARCHAR(200) NOT NULL,
    TipoPasajero VARCHAR(50) NOT NULL,
    FechaNacimiento DATE NOT NULL,
	Edad INT,
 
	CONSTRAINT PKPasajero PRIMARY KEY(IdPasajero)
);

CREATE OR REPLACE FUNCTION calcular_edad()
RETURNS TRIGGER AS $$
BEGIN
    NEW.Edad := EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM NEW.FechaNacimiento) -
               CASE WHEN TO_CHAR(NEW.FechaNacimiento, 'MMDD') > TO_CHAR(CURRENT_DATE, 'MMDD') THEN 1 ELSE 0 END;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TGCalcularEdad
BEFORE INSERT OR UPDATE ON Pasajero
FOR EACH ROW
EXECUTE FUNCTION calcular_edad();

INSERT INTO Pasajero(NombrePasajero, TipoPasajero, FechaNacimiento)
VALUES ('Josue Torres', 'Adulto', '11-13-1997')

SELECT * FROM Pasajero

DROP TABLE Pasajero


CREATE TABLE Transporte
(
    IdTransporte BIGSERIAL  NOT NULL,
    Matricula VARCHAR(20) NOT NULL,
    CapacidadLugares INT NOT NULL,
    CapacidadGasolina INT NOT NULL,
    Modelo VARCHAR(50) NOT NULL,
    Marca VARCHAR(50) NOT NULL,
    TipoTransporte VARCHAR(50) NOT NULL,

    CONSTRAINT PKTransporte PRIMARY KEY (IdTransporte)
);

ALTER TABLE Transporte
ADD CONSTRAINT uq_Matricula UNIQUE (Matricula);

ALTER TABLE Transporte
ADD CONSTRAINT chk_tipo CHECK (TipoTransporte IN ('Van','Autobús'));

ALTER TABLE Transporte
ADD CONSTRAINT chk_CapLug CHECK (CapacidadLugares IN (6,10));

INSERT INTO Transporte(Matricula, CapacidadLugares, CapacidadGasolina, Modelo, Marca, TipoTransporte)
VALUES ('UXG-169-D', 6, 40, '2025', 'Mercedes Benz', 'Van')

SELECT * FROM Transporte

CREATE SCHEMA Ventas

CREATE TABLE Ventas.Salida
(
    IdSalida BIGSERIAL NOT NULL,
    IdTransporte BIGINT NOT NULL,
	IdOperador BIGINT NOT NULL, 
	IdItinerario BIGINT NOT NULL,     
    PrecioSalida FLOAT NOT NULL, 

    CONSTRAINT PKSalida PRIMARY KEY (IdSalida),

	CONSTRAINT FKIdTransporteSalida FOREIGN KEY (IdTransporte) 
	REFERENCES Transporte(IdTransporte),

	CONSTRAINT FKOperadorSalida FOREIGN KEY (IdOperador) 
	REFERENCES Operador(IdOperador),

	CONSTRAINT FKItinerarioSalida FOREIGN KEY (IdItinerario) 
	REFERENCES Destinos.Itinerario(IdItinerario)
);

DROP TABLE Ventas.Salida

INSERT INTO Ventas.Salida(IdTransporte, IdOperador, IdItinerario, PrecioSalida)
VALUES ( 1, 1, 2, 10)

SELECT * FROM Ventas.Salida

CREATE TABLE Ventas.Transaccion
(
    IdTransaccion BIGSERIAL NOT NULL,
    IdSalida BIGINT NOT NULL,
	IdTarjeta BIGINT NOT NULL, 
	FechaTransaccion DATE DEFAULT CURRENT_TIMESTAMP,     
    Total FLOAT, 

    CONSTRAINT PKTransaccion PRIMARY KEY (IdTransaccion),

	CONSTRAINT FKIdSalidaTransaccion FOREIGN KEY (IdSalida) 
	REFERENCES Ventas.Salida(IdSalida),

	CONSTRAINT FKIdTarjetaTransaccion FOREIGN KEY (IdTarjeta) 
	REFERENCES InfoCliente.TarjetaCliente(IdTarjeta)
);

INSERT INTO Ventas.Transaccion(IdSalida, IdTarjeta, Total)
VALUES ( 1, 7, 0)

SELECT * FROM Ventas.Transaccion


CREATE TABLE Ventas.Asiento
(
	IdAsiento BIGSERIAL NOT NULL,
    IdSalida BIGINT NOT NULL,
	Disponibilidad INT NOT NULL,
	NumAsiento INT NOT NULL,

	CONSTRAINT PKAsiento PRIMARY KEY (IdAsiento),

	CONSTRAINT FKIdSalidaAsiento FOREIGN KEY (IdSalida) 
	REFERENCES Ventas.Salida(IdSalida)
)

SELECT * FROM Ventas.Asiento
SELECT * FROM Ventas.Boleto


CREATE TABLE Ventas.Boleto
(
    IdBoleto BIGSERIAL NOT NULL,
    IdAsiento BIGINT NOT NULL,
	IdTransaccion BIGINT NOT NULL, 
	IdPasajero BIGINT NOT NULL,     
    SubTotal INT, 

    CONSTRAINT PKBoleto PRIMARY KEY (IdBoleto),

	CONSTRAINT FKIdAsientoBoleto FOREIGN KEY (IdAsiento) 
	REFERENCES Ventas.Asiento(IdAsiento),

	CONSTRAINT FKTransaccionBoleto FOREIGN KEY (IdTransaccion) 
	REFERENCES Ventas.Transaccion(IdTransaccion),

	CONSTRAINT FKPasajeroBoleto FOREIGN KEY (IdPasajero) 
	REFERENCES Pasajero(IdPasajero)
);



CREATE OR REPLACE FUNCTION calcular_subtotal()
RETURNS TRIGGER AS $$
DECLARE
    precioSalida FLOAT;
    kms INT;
BEGIN
    -- Obtener PrecioSalida desde la tabla Salida
    SELECT s.PrecioSalida INTO precioSalida
    FROM Ventas.Salida s
    JOIN Ventas.Transaccion t ON s.IdSalida = t.IdSalida
    WHERE t.IdTransaccion = NEW.IdTransaccion;

    -- Obtener KMs desde la tabla Itinerario
    SELECT i.KMs INTO kms
    FROM Destinos.Itinerario i
    JOIN Ventas.Salida s ON i.IdItinerario = s.IdItinerario
    JOIN Ventas.Transaccion t ON s.IdSalida = t.IdSalida
    WHERE t.IdTransaccion = NEW.IdTransaccion;

    -- Evitar valores NULL en cálculos
    NEW.SubTotal := COALESCE(precioSalida, 0) * COALESCE(kms, 0);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear el trigger con BEFORE para evitar recursión infinita
CREATE TRIGGER trigger_calcular_subtotal
BEFORE INSERT OR UPDATE ON Ventas.Boleto
FOR EACH ROW
EXECUTE FUNCTION calcular_subtotal();




CREATE OR REPLACE FUNCTION actualizar_total_transaccion()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el boleto se está eliminando, ajustar el Total de la transacción anterior
    IF TG_OP = 'DELETE' THEN
        UPDATE Ventas.Transaccion
        SET Total = COALESCE(Total, 0) - COALESCE(OLD.SubTotal, 0)
        WHERE IdTransaccion = OLD.IdTransaccion;
    END IF;

    -- Si el boleto se actualiza y cambia de transacción, restamos el SubTotal de la anterior y recalculamos la nueva
    IF TG_OP = 'UPDATE' AND OLD.IdTransaccion <> NEW.IdTransaccion THEN
        -- Restar el subtotal de la transacción anterior
        UPDATE Ventas.Transaccion
        SET Total = COALESCE(Total, 0) - COALESCE(OLD.SubTotal, 0)
        WHERE IdTransaccion = OLD.IdTransaccion;
    END IF;

    -- Recalcular el total para la transacción nueva o afectada
    UPDATE Ventas.Transaccion
    SET Total = (
        SELECT COALESCE(SUM(SubTotal), 0)
        FROM Ventas.Boleto
        WHERE IdTransaccion = COALESCE(NEW.IdTransaccion, OLD.IdTransaccion)
    )
    WHERE IdTransaccion = COALESCE(NEW.IdTransaccion, OLD.IdTransaccion);

    RETURN NULL; -- No es necesario devolver NEW ya que es un AFTER trigger
END;
$$ LANGUAGE plpgsql;

-- Crear el trigger para INSERT, UPDATE y DELETE en Boleto
CREATE TRIGGER trigger_actualizar_total_transaccion
AFTER INSERT OR UPDATE OR DELETE ON Ventas.Boleto
FOR EACH ROW
EXECUTE FUNCTION actualizar_total_transaccion();


SELECT IdTransporte FROM Ventas.Salida WHERE IdSalida IN (SELECT IdTransaccion FROM Ventas.Boleto);

SELECT * FROM Ventas.Boleto;



SELECT s.IdTransporte, t.IdSalida, b.IdTransaccion
FROM Ventas.Salida s
JOIN Ventas.Transaccion t ON s.IdSalida = t.IdSalida
JOIN Ventas.Boleto b ON t.IdTransaccion = b.IdTransaccion;


CREATE OR REPLACE FUNCTION actualizar_capacidad_transporte()
RETURNS TRIGGER AS $$
DECLARE 
    transporteActual BIGINT;
    transporteAnterior BIGINT;
BEGIN
    -- Obtener el transporte actual basado en la nueva transacción
    SELECT s.IdTransporte INTO transporteActual
    FROM Ventas.Salida s
    JOIN Ventas.Transaccion t ON s.IdSalida = t.IdSalida
    WHERE t.IdTransaccion = NEW.IdTransaccion;

    -- Si la acción es DELETE, incrementar CapacidadLugares en el transporte correspondiente
    IF TG_OP = 'DELETE' THEN
        SELECT s.IdTransporte INTO transporteAnterior
        FROM Ventas.Salida s
        JOIN Ventas.Transaccion t ON s.IdSalida = t.IdSalida
        WHERE t.IdTransaccion = OLD.IdTransaccion;

        UPDATE Transporte
        SET CapacidadLugares = CapacidadLugares + 1
        WHERE IdTransporte = transporteAnterior;
    END IF;

    -- Si la acción es UPDATE y cambia de transporte, ajustar ambos
    IF TG_OP = 'UPDATE' AND OLD.IdTransaccion <> NEW.IdTransaccion THEN
        SELECT s.IdTransporte INTO transporteAnterior
        FROM Ventas.Salida s
        JOIN Ventas.Transaccion t ON s.IdSalida = t.IdSalida
        WHERE t.IdTransaccion = OLD.IdTransaccion;

        UPDATE Transporte SET CapacidadLugares = CapacidadLugares + 1 WHERE IdTransporte = transporteAnterior;
        UPDATE Transporte SET CapacidadLugares = CapacidadLugares - 1 WHERE IdTransporte = transporteActual;
    END IF;

    -- Si la acción es INSERT, disminuir CapacidadLugares en el transporte correspondiente
    IF TG_OP = 'INSERT' THEN
        UPDATE Transporte
        SET CapacidadLugares = CapacidadLugares - 1
        WHERE IdTransporte = transporteActual;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Crear el trigger con AFTER para reflejar cambios luego de la inserción/modificación
CREATE TRIGGER trigger_actualizar_capacidad_transporte
AFTER INSERT OR UPDATE OR DELETE ON Ventas.Boleto
FOR EACH ROW
EXECUTE FUNCTION actualizar_capacidad_transporte();

SELECT 
    p.TipoPasajero,
    COUNT(b.IdBoleto) AS TotalBoletos
FROM 
    Pasajero p
INNER JOIN 
    Ventas.Boleto b ON p.IdPasajero = b.IdPasajero
GROUP BY 
    p.TipoPasajero;


SELECT 
    p.TipoPasajero,
    COUNT(b.IdBoleto) AS TotalBoletos
FROM 
    Pasajero p
INNER JOIN 
    Ventas.Boleto b ON p.IdPasajero = b.IdPasajero
WHERE 
    p.TipoPasajero = 'Estudiante'
GROUP BY 
    p.TipoPasajero;


--Consulta de tipo de pasajeeros segun su tipo la transaccion 
SELECT 
    p.TipoPasajero,
    COUNT(b.IdBoleto) AS TotalBoletos
FROM 
    Ventas.Boleto b
INNER JOIN 
    Pasajero p ON b.IdPasajero = p.IdPasajero
WHERE 
    b.IdTransaccion = 8 
    AND p.TipoPasajero = 'Adulto' 
GROUP BY 
    p.TipoPasajero;


--Consulta de Boletos segun tipo de pasajero
SELECT 
    b.IdBoleto,
    b.SubTotal
FROM 
    Ventas.Boleto b
JOIN 
    Pasajero p ON b.IdPasajero = p.IdPasajero
WHERE 
    p.TipoPasajero = 'Adulto'; 


