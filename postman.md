# Postman (Delivery App) – Guía de Pruebas Completa

## Variables de entorno (Postman)

Crea un Environment en Postman con estas variables para facilitar las pruebas:

- `baseUrl` = `http://localhost:8080`
- `tiendaId` = *(ID obtenido al crear la tienda)*
- `productoId` = *(ID obtenido al crear el producto)*
- `clienteId` = *(ID obtenido al crear el cliente)*
- `repartidorId` = *(ID obtenido al crear el repartidor)*
- `pedidoId` = *(ID obtenido al crear el pedido)*
- `pagoId` = *(ID obtenido al procesar el pago)*

---

## Flujo del Ciclo de Vida del Pedido (Paso a Paso)

### 1) Crear la Tienda
**POST** `{{baseUrl}}/api/usuarios/tiendas`
```json
{
  "nombre": "Pizzería La Estación",
  "telefono": "555-9876",
  "direccion": "Calle Falsa 123",
  "horarioApertura": "11:00:00",
  "horarioCierre": "23:00:00"
}
```
> Guarda el `id` recibido como `tiendaId`.

### 2) Agregar Producto a la Tienda
**POST** `{{baseUrl}}/api/usuarios/tiendas/{{tiendaId}}/productos`
```json
{
  "nombre": "Pizza Pepperoni Grande",
  "precio": 15.50,
  "disponible": true,
  "categoria": "Pizzas",
  "descripcion": "Pizza clásica con extra pepperoni"
}
```
> Guarda el `id` recibido como `productoId`.

### 3) Crear el Cliente
**POST** `{{baseUrl}}/api/usuarios/clientes`
```json
{
  "nombre": "Juan Pérez",
  "telefono": "3001234567",
  "direccion": "Avenida Siempreviva 742"
}
```
> Guarda el `id` recibido como `clienteId`.

### 4) Crear el Repartidor
**POST** `{{baseUrl}}/api/usuarios/repartidores`
```json
{
  "nombre": "Carlos Moto",
  "telefono": "3119876543",
  "direccion": "Carrera 10 #20-30",
  "vehiculo": "Moto"
}
```
> Guarda el `id` recibido como `repartidorId`.

### 5) Realizar el Pedido (Orden)
**POST** `{{baseUrl}}/api/usuarios/clientes/{{clienteId}}/pedidos`
```json
[
  {
    "productoId": "{{productoId}}",
    "cantidad": 2,
    "precioUnitario": 15.50
  }
]
```
> Guarda el `id` recibido como `pedidoId`. El estado inicial será `PENDIENTE`.

### 6) Procesar el Pago (Obligatorio para avanzar)
**POST** `{{baseUrl}}/api/pagos`
```json
{
  "pedidoId": "{{pedidoId}}",
  "monto": 31.00,
  "metodo": "TARJETA",
  "referencia": "TRANS-001"
}
```
> Esto cambia el estado del pedido a `PAGADO` (aunque internamente se mantiene el flujo de negocio).

### 7) Tienda: Marcar Pedido como LISTO
**PUT** `{{baseUrl}}/api/usuarios/tiendas/pedidos/{{pedidoId}}/listo`
> **Requisito**: El pedido debe estar pagado. Cambia el estado a `LISTO`.

### 8) Repartidor: Aceptar Pedido (Asociación)
**PUT** `{{baseUrl}}/api/usuarios/repartidores/{{repartidorId}}/pedidos/{{pedidoId}}/aceptar`
> Vincula al repartidor con el pedido y cambia el estado a `EN_CAMINO`.

### 9) Repartidor: Marcar como ENTREGADO
**PUT** `{{baseUrl}}/api/usuarios/repartidores/pedidos/{{pedidoId}}/entregado`
> Cambia el estado final a `ENTREGADO`.

---

## Gestión de Estados y Actores

| Acción | Actor | Método | Endpoint | Nuevo Estado |
| :--- | :--- | :--- | :--- | :--- |
| **Crear Pedido** | Cliente | `POST` | `/api/usuarios/clientes/{id}/pedidos` | `PENDIENTE` |
| **Pagar** | Cliente | `POST` | `/api/pagos` | `PAGADO` (Estado lógico) |
| **Preparar** | Tienda | `PUT` | `/api/usuarios/tiendas/pedidos/{id}/listo` | `LISTO` |
| **Despachar** | Repartidor | `PUT` | `/api/usuarios/repartidores/{repId}/pedidos/{id}/aceptar` | `EN_CAMINO` |
| **Entregar** | Repartidor | `PUT` | `/api/usuarios/repartidores/pedidos/{id}/entregado` | `ENTREGADO` |

---

## Consultas de Seguimiento

### Ver Pedidos Disponibles para Repartidores
**GET** `{{baseUrl}}/api/usuarios/repartidores/pedidos/disponibles`
> Muestra todos los pedidos que están en estado `LISTO` esperando ser aceptados.

### Ver Historial de un Cliente
**GET** `{{baseUrl}}/api/usuarios/clientes/{{clienteId}}/historial`

### Generar Factura
**GET** `{{baseUrl}}/api/pedidos/{{pedidoId}}/factura`

---

## Cómo modificar el DataLoader

El archivo [DataLoader.java](file:///c:/Users/XTHZC7/Desktop/UCC/Sexto_Semestre/Programacion_orientada_a_objetos/delivery_app/src/main/java/com/delivery/delivery_app/config/DataLoader.java) carga datos automáticamente al iniciar.

1. **Para agregar datos**: Edita el método `run` y crea nuevas instancias de `Tienda`, `Cliente`, etc.
2. **Para aplicar cambios**: Si la base de datos ya tiene datos, el DataLoader no se ejecutará (por seguridad). Debes limpiar las tablas primero:
   ```sql
   TRUNCATE TABLE pagos, item_pedidos, pedidos, productos, tiendas, clientes, repartidores, usuarios CASCADE;
   ```
3. **Ajustes de esquema**: Si cambias la estructura de las tablas, usa el método `ajustarEsquemaTiendasSiEsNecesario` para ejecutar SQL nativo de corrección.
