# Tienda Monolito

Aplicación de e-commerce desarrollada como arquitectura monolítica en Java Spring Boot. Permite comprar, vender y distribuir productos en línea con autenticación delegada a Firebase Authentication.

---

## Estructura del proyecto

```
tienda-monolito/
├── docs/
│   ├── c4/                       Archivos .drawio (contexto, contenedores, componentes)
│   ├── postman/                  Colección Postman con los escenarios
│   └── uml/                      Diagramas UML adicionales
├── src/
│   ├── main/
│   │   ├── java/com/tienda/monolito/
│   │   │   ├── config/           Configuración global (Security, Firebase, OpenAPI)
│   │   │   ├── security/         Filtro JWT de Firebase
│   │   │   ├── common/
│   │   │   │   ├── exception/    Handler global de excepciones
│   │   │   │   └── dto/          Respuestas genéricas (ApiResponse, ErrorResponse)
│   │   │   ├── person/           Módulo de personas
│   │   │   ├── user/             Módulo de usuarios
│   │   │   ├── role/             Módulo de roles
│   │   │   ├── address/          Módulo de direcciones
│   │   │   ├── product/          Módulo de productos
│   │   │   ├── category/         Módulo de categorías
│   │   │   ├── wallet/           Módulo de billetera virtual
│   │   │   ├── cart/             Módulo de carrito de compras
│   │   │   └── delivery/         Módulo de delivery y deliveryOrders
│   │   └── resources/
│   │       ├── application.yml   Configuración general
│   │       └── db/migration/     Migraciones Flyway
│   └── test/                     Tests unitarios e integración
├── docker/                       Dockerfile y docker-compose
├── scripts/                      Scripts utilitarios
├── .github/workflows/            Pipeline de CI/CD (GitHub Actions)
├── pom.xml
└── README.md
```

Cada módulo de dominio sigue la misma estructura interna:

```
<modulo>/
├── controller/       Endpoints REST
├── service/
│   └── impl/         Implementación de la lógica de negocio
├── repository/       Repositorio Spring Data JPA
├── entity/           Entidad JPA
└── dto/
    ├── request/      DTOs de entrada
    └── response/     DTOs de salida
```

---

## Módulos del sistema

| Módulo | Descripción | Sección enunciado |
|---|---|---|
| **person** | ABM de personas (nombre, DNI, teléfono, estado) | 3.1.1 |
| **user** | Usuarios asociados a personas, vinculados con Firebase UID | 3.1.1 |
| **role** | Roles del sistema (admin, vendedor, comprador, entregador) | 3.1.1 |
| **address** | Direcciones de personas, usadas como puntos de venta | 3.2.1 |
| **product** | Catálogo de productos con stock, precio y punto de venta | 3.2.1 |
| **category** | Categorías predefinidas de productos | 3.2.1 |
| **wallet** | Billetera virtual: saldo e historial de transacciones | 3.4 |
| **cart** | Carrito persistente y proceso de checkout | 3.5 |
| **delivery** | DeliveryOrders generadas en checkout, gestión de entregas | 3.6 |

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3 |
| Autenticación | Firebase Authentication (JWT) |
| Base de datos | PostgreSQL |
| Migraciones | Flyway |
| Documentación API | OpenAPI / Swagger UI |
| Contenedores | Docker + Docker Compose |
| CI/CD | GitHub Actions |

---

## Requisitos previos

- Java 17+
- Maven 3.8+
- Docker y Docker Compose
- Credenciales de Firebase (archivo `serviceAccountKey.json`)

---

## Levantar el proyecto

### Con Docker Compose

```bash
docker-compose -f docker/docker-compose.yml up --build
```

La aplicación estará disponible en `http://localhost:8080`.

### Sin Docker (desarrollo local)

1. Configurar base de datos PostgreSQL y actualizar `src/main/resources/application.yml`.
2. Colocar el archivo de credenciales de Firebase en la ruta configurada.
3. Ejecutar:

```bash
mvn spring-boot:run
```

---

## Documentación de la API

Con la aplicación corriendo, acceder a:

```
http://localhost:8080/swagger-ui.html
```

La colección de Postman con todos los escenarios se encuentra en `docs/postman/`.

---

## Flujo de compra

```
Buscar producto → Agregar al carrito → Checkout →
  ├── Validar stock y saldo
  ├── Generar deliveryOrder por ítem
  ├── Descontar saldo de billetera
  ├── Actualizar stock
  └── Vaciar carrito
```

## Flujo de entrega

```
DeliveryOrder generada → Entregador visualiza pendientes →
Toma pedido → Marca como entregado → Actualiza estado
```

---

## Tests

```bash
mvn test
```

---

## Diagramas de arquitectura

Los diagramas C4 (contexto, contenedores y componentes) se encuentran en `docs/c4/`. Los archivos `.drawio` editables están en la misma carpeta.

---

## CI/CD

El pipeline de GitHub Actions (`.github/workflows/`) ejecuta build, tests y construcción de imagen Docker en cada push a la rama principal.
