# Postman (Delivery App) – Requests para probar BD

## Variables de entorno (Postman)

Crea un Environment en Postman con:

- `baseUrl` = `http://localhost:8080`
- `usuarioId` = *(vacío al inicio)*
- `tiendaId` = *(vacío al inicio)*
- `productoId` = *(vacío al inicio)*
- `pedidoId` = *(vacío al inicio)*
- `pagoId` = *(vacío al inicio)*
- `repartidorId` = *(vacío al inicio)*

Headers recomendados en todas:

- `Content-Type: application/json`

## Flujo recomendado (end-to-end)

### 1) Crear usuario (cliente)

**POST** `{{baseUrl}}/api/clientes/usuarios`

Body (raw / JSON):
```json
{
  "nombre": "Ana",
  "telefono": "111",
  "direccion": "Calle 1"
}
```

Guarda `id` de la respuesta como `usuarioId`.

### 2) Crear tienda

**POST** `{{baseUrl}}/api/tienda`

Body (raw / JSON):
```json
{
  "nombre": "Super Tienda",
  "telefono": "5551234",
  "direccion": "Calle Comercio 789",
  "horarioApertura": "08:00:00",
  "horarioCierre": "22:00:00"
}
```

Guarda `id` como `tiendaId`.

### 3) Agregar producto a una tienda

**POST** `{{baseUrl}}/api/tienda/{{tiendaId}}/productos`

Body (raw / JSON):
```json
{
  "nombre": "Hamburguesa Clásica",
  "precio": 12.99,
  "disponible": true,
  "categoria": "Comida Rápida",
  "descripcion": "Hamburguesa con queso"
}
```

Guarda `id` como `productoId`.

### 4) Ver todos los productos (cliente)

**GET** `{{baseUrl}}/api/clientes/productos`

### 5) Crear pedido (desde clientes)

**POST** `{{baseUrl}}/api/clientes/{{usuarioId}}/pedidos`

Body (raw / JSON):
```json
[
  {
    "productoId": "{{productoId}}",
    "cantidad": 2,
    "precioUnitario": 12.99
  }
]
```

Guarda `id` como `pedidoId`.

### 6) Obtener pedido

**GET** `{{baseUrl}}/api/pedidos/{{pedidoId}}`

### 7) Generar factura del pedido

**GET** `{{baseUrl}}/api/pedidos/{{pedidoId}}/factura`

### 8) Procesar pago del pedido

**POST** `{{baseUrl}}/api/pagos`

Body (raw / JSON):
```json
{
  "pedidoId": "{{pedidoId}}",
  "monto": 100.0,
  "metodo": "EFECTIVO",
  "referencia": "POSTMAN-REF-1"
}
```

Guarda `id` como `pagoId`.

### 9) Marcar pedido como listo (tienda)

**PUT** `{{baseUrl}}/api/tienda/pedidos/{{pedidoId}}/listo`

### 10) Repartidor: ver pedidos disponibles

**GET** `{{baseUrl}}/api/repartidores/pedidos/disponibles`

### 11) Repartidor: aceptar pedido

**PUT** `{{baseUrl}}/api/repartidores/pedidos/{{pedidoId}}/aceptar`

### 12) Repartidor: marcar como entregado

**PUT** `{{baseUrl}}/api/repartidores/pedidos/{{pedidoId}}/entregado`

### 13) Reembolsar pago

**POST** `{{baseUrl}}/api/pagos/{{pagoId}}/reembolsar`

## Endpoints adicionales (útiles para validar persistencia)

### Clientes

**Obtener usuario por ID**

**GET** `{{baseUrl}}/api/clientes/usuarios/{{usuarioId}}`

**Ver historial de pedidos de un usuario**

**GET** `{{baseUrl}}/api/clientes/{{usuarioId}}/historial`

**Ver productos disponibles**

**GET** `{{baseUrl}}/api/clientes/productos/disponibles`

**Buscar productos por categoría**

**GET** `{{baseUrl}}/api/clientes/productos/categoria/Comida%20R%C3%A1pida`

### Tienda

**Actualizar datos de tienda**

**PUT** `{{baseUrl}}/api/tienda/{{tiendaId}}`

Body (raw / JSON):
```json
{
  "nombre": "Super Tienda (Editada)",
  "telefono": "5559999",
  "direccion": "Nueva dirección 123",
  "horarioApertura": "09:00:00",
  "horarioCierre": "21:00:00"
}
```

**Ver productos de una tienda**

**GET** `{{baseUrl}}/api/tienda/{{tiendaId}}/productos`

**Actualizar un producto**

**PUT** `{{baseUrl}}/api/tienda/productos/{{productoId}}`

Body (raw / JSON):
```json
{
  "nombre": "Hamburguesa Clásica XL",
  "precio": 14.5,
  "disponible": true,
  "categoria": "Comida Rápida",
  "descripcion": "Versión más grande"
}
```

**Eliminar un producto**

**DELETE** `{{baseUrl}}/api/tienda/productos/{{productoId}}`

**Ver pedidos pendientes**

**GET** `{{baseUrl}}/api/tienda/pedidos/pendientes`

### Pedidos

**Crear pedido (endpoint alternativo)**

**POST** `{{baseUrl}}/api/pedidos/{{usuarioId}}`

Body (raw / JSON):
```json
[
  {
    "productoId": "{{productoId}}",
    "cantidad": 1,
    "precioUnitario": 12.99
  }
]
```

**Obtener pedidos por estado**

**GET** `{{baseUrl}}/api/pedidos/estado/PENDIENTE`

**Obtener pedidos por rango de fechas**

**GET** `{{baseUrl}}/api/pedidos/rango-fechas?inicio=2026-01-01T00:00:00&fin=2026-12-31T23:59:59`

**Actualizar estado del pedido**

**PUT** `{{baseUrl}}/api/pedidos/{{pedidoId}}/estado/PAGADO`

### Pagos

**Obtener pago por ID**

**GET** `{{baseUrl}}/api/pagos/{{pagoId}}`

### Repartidores

**Ver pedidos asignados (según implementación actual filtra por estado EN_CAMINO)**

**GET** `{{baseUrl}}/api/repartidores/{{repartidorId}}/pedidos-asignados`

**Actualizar disponibilidad**

**PUT** `{{baseUrl}}/api/repartidores/{{repartidorId}}/disponibilidad/true`

