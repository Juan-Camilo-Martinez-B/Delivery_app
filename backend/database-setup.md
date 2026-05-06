# Conexión a base de datos (PostgreSQL) – Local, Render y pgAdmin

Este proyecto usa Spring Data JPA + PostgreSQL. La app se conecta por `application.properties` y, al iniciar, Hibernate puede crear/actualizar las tablas según tus entidades (`@Entity`, `@Table`, `@Column`, etc.).

## 1) Datos que necesitas para `application.properties`

Spring Boot necesita estos 3 valores:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

Con PostgreSQL, el formato típico de la URL JDBC es:

```text
jdbc:postgresql://HOST:PUERTO/NOMBRE_BD
```

Si el servidor exige SSL (muy común en Render), se agrega:

```text
jdbc:postgresql://HOST:PUERTO/NOMBRE_BD?sslmode=require
```

## 2) Cómo obtener los datos en Render

En Render (PostgreSQL):

1. Entra a tu servicio de PostgreSQL.
2. Abre la sección de conexión (por ejemplo “Connections” / “Info”).
3. Ahí vas a ver (con nombres similares):
   - Host (por ejemplo: `dpg-xxxxx.oregon-postgres.render.com`)
   - Port (normalmente `5432`)
   - Database (por ejemplo: `delivery_app_aopr`)
   - User (por ejemplo: `delivery_app_aopr_user`)
   - Password
   - External Database URL / Connection string (con formato `postgresql://user:pass@host/db`)

### Convertir la URL de Render a JDBC

Render suele darte algo como:

```text
postgresql://USUARIO:PASSWORD@HOST/NOMBRE_BD
```

Eso en Spring Boot se transforma en:

```text
jdbc:postgresql://HOST:5432/NOMBRE_BD?sslmode=require
```

El usuario y password van en propiedades separadas (no en la URL).

## 3) Configuración recomendada en `application.properties`

Recomendación: NO dejar contraseñas pegadas en el repo. Usa variables de entorno.

Ejemplo:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://HOST:5432/NOMBRE_BD?sslmode=require}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:USUARIO}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}

spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
```

### Cómo setear las variables de entorno

PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://HOST:5432/NOMBRE_BD?sslmode=require"
$env:SPRING_DATASOURCE_USERNAME="USUARIO"
$env:SPRING_DATASOURCE_PASSWORD="PASSWORD"
.\mvnw.cmd spring-boot:run
```

CMD:

```bat
set SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:5432/NOMBRE_BD?sslmode=require
set SPRING_DATASOURCE_USERNAME=USUARIO
set SPRING_DATASOURCE_PASSWORD=PASSWORD
mvnw.cmd spring-boot:run
```

## 4) Conectar Render PostgreSQL desde pgAdmin (GUI)

En pgAdmin:

1. Click derecho en “Servers” → “Register” → “Server…”
2. Tab “General”
   - Name: el que quieras (por ejemplo `DeliveryApp Render`)
3. Tab “Connection”
   - Host name/address: el **Host** de Render
   - Port: `5432`
   - Maintenance database: el **Database** de Render
   - Username: el **User** de Render
   - Password: el **Password** de Render (puedes marcar “Save password” si quieres)
4. Tab “SSL” (si aparece en tu versión)
   - SSL mode: `require`

Si tu pgAdmin no muestra “SSL mode”, normalmente puedes agregarlo en “Advanced” o usar la opción equivalente para obligar SSL.

## 5) ¿Las tablas se crean solas?

Sí, si se cumplen estas condiciones:

- La app logra conectarse a PostgreSQL.
- `spring.jpa.hibernate.ddl-auto=update` (o `create`, `create-drop`).
- Tus entidades están anotadas con `@Entity` y están dentro del package escaneado por Spring Boot.

Postman NO crea tablas. Postman sirve para llamar endpoints y verificar que al crear/leer/actualizar datos, estos quedan persistidos.

## 6) Validar que realmente está persistiendo en Render

En pgAdmin o en la consola SQL ejecuta:

```sql
select table_name
from information_schema.tables
where table_schema = 'public'
order by table_name;
```

Y para columnas:

```sql
select table_name, column_name, data_type, is_nullable
from information_schema.columns
where table_schema = 'public'
order by table_name, ordinal_position;
```

