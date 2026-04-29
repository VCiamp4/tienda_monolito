-- ============================================================
-- V1 - Schema inicial del sistema de compra y venta
-- ============================================================

-- Persona: individuo real, puede tener múltiples usuarios
CREATE TABLE persona (
    id               BIGSERIAL PRIMARY KEY,
    nombre_completo  VARCHAR(255) NOT NULL,
    documento        VARCHAR(50)  NOT NULL UNIQUE,
    telefono         VARCHAR(50),
    fecha_nacimiento DATE,
    fecha_registro   DATE         NOT NULL,
    activo           BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Rol: define permisos y funcionalidades disponibles
CREATE TABLE rol (
    id     BIGSERIAL    PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Usuario: credencial Firebase asociada a una persona
CREATE TABLE usuario (
    id            BIGSERIAL    PRIMARY KEY,
    persona_id    BIGINT       NOT NULL REFERENCES persona(id),
    email         VARCHAR(255) NOT NULL UNIQUE,
    firebase_uid  VARCHAR(255) NOT NULL UNIQUE,
    ultimo_acceso TIMESTAMP,
    activo        BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Relación N:M entre usuario y rol
CREATE TABLE usuario_rol (
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    rol_id     BIGINT NOT NULL REFERENCES rol(id),
    PRIMARY KEY (usuario_id, rol_id)
);

-- Dirección: domicilios asociados a una persona (punto de venta o entrega)
CREATE TABLE direccion (
    id         BIGSERIAL    PRIMARY KEY,
    persona_id BIGINT       NOT NULL REFERENCES persona(id),
    calle      VARCHAR(255) NOT NULL,
    numero     VARCHAR(50)  NOT NULL,
    ciudad     VARCHAR(100) NOT NULL,
    provincia  VARCHAR(100) NOT NULL,
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Categoría: catálogo predefinido de tipos de producto
CREATE TABLE categoria (
    id     BIGSERIAL    PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Producto: artículo publicado por un vendedor
CREATE TABLE producto (
    id                  BIGSERIAL      PRIMARY KEY,
    usuario_vendedor_id BIGINT         NOT NULL REFERENCES usuario(id),
    categoria_id        BIGINT         NOT NULL REFERENCES categoria(id),
    direccion_id        BIGINT         NOT NULL REFERENCES direccion(id),
    nombre              VARCHAR(255)   NOT NULL,
    descripcion         TEXT,
    precio              DECIMAL(15, 2) NOT NULL,
    stock               INTEGER        NOT NULL DEFAULT 0,
    image_url           VARCHAR(500),
    activo              BOOLEAN        NOT NULL DEFAULT TRUE
);

-- Carrito: persistente por usuario (sin fecha de vencimiento)
CREATE TABLE carrito (
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    usuario_id BIGINT    NOT NULL UNIQUE REFERENCES usuario(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Item del carrito: producto + cantidad + precio capturado al momento de agregar
CREATE TABLE carrito_item (
    id              BIGSERIAL      PRIMARY KEY,
    carrito_id      BIGINT         NOT NULL REFERENCES carrito(id),
    producto_id     BIGINT         NOT NULL REFERENCES producto(id),
    cantidad        INTEGER        NOT NULL,
    precio_unitario DECIMAL(15, 2) NOT NULL
);

-- Billetera virtual: saldo por usuario
CREATE TABLE billetera (
    id         BIGSERIAL      PRIMARY KEY,
    usuario_id BIGINT         NOT NULL UNIQUE REFERENCES usuario(id),
    saldo      DECIMAL(15, 2) NOT NULL DEFAULT 0.00
);

-- Transacción: historial de cargas y compras de la billetera
CREATE TABLE transaccion (
    id           BIGSERIAL      PRIMARY KEY,
    billetera_id BIGINT         NOT NULL REFERENCES billetera(id),
    monto        DECIMAL(15, 2) NOT NULL,
    tipo         VARCHAR(20)    NOT NULL CHECK (tipo IN ('CARGA', 'COMPRA')),
    fecha        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Delivery order: generado por cada item al hacer checkout
CREATE TABLE delivery_order (
    id                    BIGSERIAL   PRIMARY KEY,
    carrito_item_id       BIGINT      NOT NULL UNIQUE REFERENCES carrito_item(id),
    comprador_id          BIGINT      NOT NULL REFERENCES usuario(id),
    usuario_entregador_id BIGINT      REFERENCES usuario(id),
    direccion_entrega_id  BIGINT      NOT NULL REFERENCES direccion(id),
    estado                VARCHAR(20) NOT NULL CHECK (estado IN ('PENDIENTE', 'ASIGNADO', 'ENTREGADO')),
    fecha_entrega         TIMESTAMP,
    created_at            TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Datos iniciales
-- ============================================================

INSERT INTO rol (nombre) VALUES
    ('ADMINISTRADOR'),
    ('VENDEDOR'),
    ('COMPRADOR'),
    ('ENTREGADOR');

INSERT INTO categoria (nombre) VALUES
    ('Electrónica'),
    ('Ropa y Accesorios'),
    ('Hogar y Jardín'),
    ('Deportes'),
    ('Libros'),
    ('Juguetes'),
    ('Alimentos'),
    ('Otros');
