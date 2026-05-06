# Delivery App (Spring Boot)

Aplicación backend tipo delivery para gestionar usuarios (clientes/repartidores), tiendas, productos, pedidos y pagos. La persistencia se hace con Spring Data JPA y PostgreSQL (por ejemplo Render).

## Tecnologías

- Java 11+ (proyecto basado en Spring Boot 2.7.0)
- Spring Boot Web (API REST)
- Spring Data JPA (entidades, repositorios y persistencia)
- PostgreSQL (runtime)
- H2 (solo para tests)

## Estructura del proyecto

- `src/main/java/com/delivery/delivery_app/model`: entidades JPA (`@Entity`, `@Table`, `@Column`, relaciones)
- `src/main/java/com/delivery/delivery_app/repository`: repositorios `JpaRepository`
- `src/main/java/com/delivery/delivery_app/service`: reglas de negocio y transacciones
- `src/main/java/com/delivery/delivery_app/controller`: endpoints REST
- `src/main/java/com/delivery/delivery_app/config/DataLoader.java`: carga datos iniciales al arrancar (seed/demo)
- `src/main/resources/application.properties`: configuración de datasource/JPA

## Base de datos y tablas

La aplicación crea/actualiza el esquema automáticamente al iniciar si `spring.jpa.hibernate.ddl-auto=update` está configurado y la conexión a PostgreSQL es válida. No necesitas Postman para “crear tablas”; Postman se usa para crear/consultar datos (INSERT/SELECT) vía la API.

## Cómo ejecutar

### 1) Configurar variables de entorno (recomendado)

Configura el datasource con variables de entorno (por ejemplo usando Render o una BD local):

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### 2) Levantar la aplicación

```bash
./mvnw spring-boot:run
```

En Windows (cmd):

```bat
mvnw.cmd spring-boot:run
```

## Endpoints principales

Base URL: `http://localhost:8080`

- Clientes: `/api/clientes`
  - POST `/api/clientes/usuarios`
  - GET `/api/clientes/usuarios/{usuarioId}`
  - POST `/api/clientes/{usuarioId}/pedidos`
  - GET `/api/clientes/{usuarioId}/historial`
  - GET `/api/clientes/productos`
  - GET `/api/clientes/productos/disponibles`
  - GET `/api/clientes/productos/categoria/{categoria}`
- Tienda: `/api/tienda`
  - POST `/api/tienda`
  - PUT `/api/tienda/{tiendaId}`
  - GET `/api/tienda/{tiendaId}/productos`
  - POST `/api/tienda/{tiendaId}/productos`
  - PUT `/api/tienda/productos/{productoId}`
  - DELETE `/api/tienda/productos/{productoId}`
  - GET `/api/tienda/pedidos/pendientes`
  - PUT `/api/tienda/pedidos/{pedidoId}/listo`
- Pedidos: `/api/pedidos`
  - POST `/api/pedidos/{usuarioId}`
  - GET `/api/pedidos/{pedidoId}`
  - GET `/api/pedidos/{pedidoId}/factura`
  - GET `/api/pedidos/estado/{estado}`
  - GET `/api/pedidos/rango-fechas?inicio=...&fin=...`
  - PUT `/api/pedidos/{pedidoId}/estado/{nuevoEstado}`
- Pagos: `/api/pagos`
  - POST `/api/pagos`
  - GET `/api/pagos/{pagoId}`
  - POST `/api/pagos/{pagoId}/reembolsar`
- Repartidores: `/api/repartidores`
  - GET `/api/repartidores/pedidos/disponibles`
  - PUT `/api/repartidores/pedidos/{pedidoId}/aceptar`
  - PUT `/api/repartidores/pedidos/{pedidoId}/entregado`
  - GET `/api/repartidores/{repartidorId}/pedidos-asignados`
  - PUT `/api/repartidores/{repartidorId}/disponibilidad/{disponible}`

## Postman

Para probar el flujo completo y validar persistencia en BD (crear tienda/producto/pedido/pago, etc.) usa:

- `postman.md`

## Tests

```bash
./mvnw test
```

