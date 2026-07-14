CREATE TABLE IF NOT EXISTS departamentos (
    id_departamento INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    presupuesto      DECIMAL(12,2) NOT NULL DEFAULT 0,
    id_gerente       INT NULL
);

CREATE TABLE IF NOT EXISTS cargos (
    id_cargo    INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    salario_min DECIMAL(10,2) NOT NULL,
    salario_max DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS empleados (
    id_empleado         INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(80) NOT NULL,
    apellido            VARCHAR(80) NOT NULL,
    dni                 VARCHAR(15) NOT NULL UNIQUE,
    fecha_nacimiento    DATE NULL,
    email               VARCHAR(120) NOT NULL UNIQUE,
    fecha_contratacion  DATE NOT NULL,
    salario_base        DECIMAL(10,2) NOT NULL,
    estado              VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    id_departamento     INT NULL,
    id_cargo            INT NULL,
    CONSTRAINT fk_empleado_departamento FOREIGN KEY (id_departamento) REFERENCES departamentos(id_departamento) ON DELETE SET NULL,
    CONSTRAINT fk_empleado_cargo FOREIGN KEY (id_cargo) REFERENCES cargos(id_cargo) ON DELETE SET NULL
);

ALTER TABLE departamentos
    ADD CONSTRAINT fk_departamento_gerente FOREIGN KEY (id_gerente) REFERENCES empleados(id_empleado) ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario     INT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(50) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    tipo_usuario   VARCHAR(30) NOT NULL,
    id_empleado    INT NOT NULL UNIQUE,
    activo         BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuario_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS contratos (
    id_contrato      INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado      INT NOT NULL,
    tipo_contrato    VARCHAR(30) NOT NULL,
    fecha_inicio     DATE NOT NULL,
    fecha_fin        DATE NULL,
    salario_pactado  DECIMAL(10,2) NOT NULL,
    estado           VARCHAR(20) NOT NULL DEFAULT 'VIGENTE',
    CONSTRAINT fk_contrato_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS nominas (
    id_nomina              INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado            INT NOT NULL,
    mes                    TINYINT NOT NULL,
    anio                   SMALLINT NOT NULL,
    salario_bruto          DECIMAL(10,2) NOT NULL,
    total_bonificaciones   DECIMAL(10,2) NOT NULL DEFAULT 0,
    total_deducciones      DECIMAL(10,2) NOT NULL DEFAULT 0,
    salario_neto           DECIMAL(10,2) NOT NULL,
    fecha_generacion       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_nomina_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE CASCADE,
    UNIQUE KEY uk_nomina_periodo (id_empleado, mes, anio)
);

CREATE TABLE IF NOT EXISTS detalle_nomina (
    id_detalle  INT AUTO_INCREMENT PRIMARY KEY,
    id_nomina   INT NOT NULL,
    tipo        VARCHAR(10) NOT NULL,
    concepto    VARCHAR(100) NOT NULL,
    monto       DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_nomina FOREIGN KEY (id_nomina) REFERENCES nominas(id_nomina) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS solicitudes (
    id_solicitud             INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado              INT NOT NULL,
    fecha_inicio             DATE NOT NULL,
    fecha_fin                DATE NOT NULL,
    dias_habiles_calculados  INT NOT NULL,
    estado                   VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    id_aprobador             INT NULL,
    fecha_solicitud          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion         DATETIME NULL,
    comentario               VARCHAR(255) NULL,
    CONSTRAINT fk_solicitud_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE CASCADE,
    CONSTRAINT fk_solicitud_aprobador FOREIGN KEY (id_aprobador) REFERENCES empleados(id_empleado) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS feriados_cache (
    id_feriado  INT AUTO_INCREMENT PRIMARY KEY,
    fecha       DATE NOT NULL,
    nombre      VARCHAR(150) NOT NULL,
    pais        VARCHAR(5) NOT NULL,
    anio        SMALLINT NOT NULL,
    UNIQUE KEY uk_feriado_fecha_pais (fecha, pais)
);

CREATE TABLE IF NOT EXISTS logs_auditoria (
    id_log             INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario         INT NULL,
    accion             VARCHAR(50) NOT NULL,
    entidad_afectada   VARCHAR(100) NULL,
    fecha_hora         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    detalle            VARCHAR(255) NULL,
    CONSTRAINT fk_log_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

INSERT INTO departamentos (nombre, presupuesto) VALUES ('Recursos Humanos', 50000.00);
INSERT INTO cargos (nombre, salario_min, salario_max) VALUES ('Administrador de Sistema', 3000.00, 6000.00);

INSERT INTO empleados (nombre, apellido, dni, email, fecha_contratacion, salario_base, estado, id_departamento, id_cargo)
VALUES ('Admin', 'Sistema', '00000000', 'admin@rrhh.local', CURDATE(), 4000.00, 'ACTIVO', 1, 1);

INSERT INTO usuarios (username, password_hash, tipo_usuario, id_empleado, activo)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMINISTRADOR', 1, TRUE);
