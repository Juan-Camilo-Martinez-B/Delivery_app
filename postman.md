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

### 1) Crear cliente

**POST** `{{baseUrl}}/api/usuarios/clientes`

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

**POST** `{{baseUrl}}/api/usuarios/tiendas`

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

**POST** `{{baseUrl}}/api/usuarios/tiendas/{{tiendaId}}/productos`

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

**GET** `{{baseUrl}}/api/usuarios/clientes/productos`

### 5) Crear pedido (desde clientes)

**POST** `{{baseUrl}}/api/usuarios/clientes/{{usuarioId}}/pedidos`

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

**PUT** `{{baseUrl}}/api/usuarios/tiendas/pedidos/{{pedidoId}}/listo`

### 10) Repartidor: ver pedidos disponibles

**GET** `{{baseUrl}}/api/usuarios/repartidores/pedidos/disponibles`

### 11) Repartidor: aceptar pedido

**PUT** `{{baseUrl}}/api/usuarios/repartidores/pedidos/{{pedidoId}}/aceptar`

### 12) Repartidor: marcar como entregado

**PUT** `{{baseUrl}}/api/usuarios/repartidores/pedidos/{{pedidoId}}/entregado`

### 13) Crear repartidor (Extra para completar flujo)

**POST** `{{baseUrl}}/api/usuarios/repartidores`

Body (raw / JSON):
```json
{
  "nombre": "Carlos López",
  "telefono": "987654321",
  "direccion": "Avenida Central 456",
  "vehiculo": "Moto"
}
```

Guarda `id` como `repartidorId`.

### 14) Reembolsar pago

**POST** `{{baseUrl}}/api/pagos/{{pagoId}}/reembolsar`

## Gestión de Estados del Pedido

El estado de un pedido cambia a través de acciones específicas realizadas por los diferentes actores (Tienda y Repartidor) mediante métodos **PUT** (siguiendo las convenciones REST para actualizaciones parciales):

| Acción | Actor | Endpoint | Nuevo Estado |
| :--- | :--- | :--- | :--- |
| **Pagar** | Cliente | `POST /api/pagos` | `PENDIENTE` (con pago asociado) |
| **Marcar Listo** | Tienda | `PUT /api/usuarios/tiendas/pedidos/{id}/listo` | `LISTO` |
| **Aceptar** | Repartidor | `PUT /api/usuarios/repartidores/pedidos/{id}/aceptar` | `EN_CAMINO` |
| **Entregar** | Repartidor | `PUT /api/usuarios/repartidores/pedidos/{id}/entregado` | `ENTREGADO` |
| **Genérico** | Admin | `PUT /api/pedidos/{id}/estado/{NUEVO_ESTADO}` | *(Cualquiera)* |

## Cómo modificar el DataLoader

El [DataLoader.java](file:///c:/Users/XTHZC7/Desktop/UCC/Sexto_Semestre/Programacion_orientada_a_objetos/delivery_app/src/main/java/com/delivery/delivery_app/config/DataLoader.java) es el encargado de precargar datos al iniciar la aplicación.

### Cuándo modificarlo:
1. **Nuevos registros**: Si quieres que la app inicie con más tiendas o productos por defecto.
2. **Cambios en el Modelo**: Si agregas un nuevo campo obligatorio a `Usuario` o `Producto`, debes actualizar los constructores o setters en el `run`.
3. **Pruebas de flujo**: Si quieres cambiar el flujo de simulación que imprime logs al final.

### Pasos para hacer cambios:
1. **Localiza el método `run`**: Aquí se crean las instancias de los objetos.
2. **Usa los repositorios o servicios**: Siempre usa `tiendaService.crearTienda(tienda)` o `clienteRepository.save(cliente)` para persistir.
3. **Condición de parada**: El DataLoader tiene un `if` al inicio que verifica si ya existen datos (ej: `if (clienteRepository.findByTelefono("111").isPresent())`). Si quieres que tus cambios se apliquen en una BD ya poblada, debes borrar los datos primero con el script SQL de `TRUNCATE`.

### Ejemplo para agregar una nueva tienda por defecto:
```java
Tienda nueva = new Tienda();
nueva.setId(UUID.randomUUID().toString());
nueva.setNombre("Nueva Tienda");
// ... set otros campos
tiendaService.crearTienda(nueva);
```

