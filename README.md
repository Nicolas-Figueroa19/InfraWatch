
# InfraWatch

Plataforma de monitoreo de infraestructura informática desarrollada como proyecto de tesis.

## Descripción

InfraWatch permite recopilar, almacenar y visualizar métricas de rendimiento de servidores y equipos informáticos.

Actualmente, el sistema permite monitorear el uso de CPU, memoria RAM y disco mediante un agente Python que envía mediciones periódicas a una API REST desarrollada con Spring Boot.

Las métricas se almacenan en MySQL y se visualizan mediante un panel web desarrollado con Angular.

## Tecnologías utilizadas

- Java 25
- Spring Boot 4
- Spring Data JPA
- MySQL 8
- Angular 22
- TypeScript
- Python 3
- psutil
- requests
- Maven
- Git y GitHub

## Arquitectura del sistema

InfraWatch está compuesto por tres componentes principales:

### Backend

API REST desarrollada con Spring Boot, encargada de gestionar servidores, recibir métricas y almacenarlas en MySQL.

Ubicación:

`backend/infrawatch-backend`

### Frontend

Aplicación Angular que muestra las métricas del servidor monitoreado y consulta periódicamente la API REST.

Ubicación:

`frontend/infrawatch-frontend`

### Agente Python

Programa que recopila métricas del computador y las envía al backend cada 30 segundos.

Ubicación:

`agent-python`

## Requisitos previos

- JDK 25
- MySQL 8
- Node.js y npm
- Angular CLI
- Python 3
- Maven o Maven Wrapper

## Configuración de la base de datos

Crear una base de datos local llamada:

`infrawatch_db`

La configuración de conexión se encuentra en:

`backend/infrawatch-backend/src/main/resources/application.properties`

El backend admite las siguientes variables de entorno:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Para un entorno distinto al de desarrollo, se recomienda utilizar un usuario MySQL dedicado y no almacenar credenciales en el repositorio.

## Ejecución del backend

Desde la raíz del repositorio, en Windows:

```cmd
cd backend\infrawatch-backend
mvnw.cmd spring-boot:run
```

El backend estará disponible en:

`http://localhost:8080`

## Ejecución del frontend

Abrir otra terminal desde la raíz del repositorio:

```cmd
cd frontend\infrawatch-frontend
npm install
npm start
```

El frontend estará disponible en:

`http://localhost:4200`

## Ejecución del agente Python

Abrir otra terminal desde la raíz del repositorio:

```cmd
cd agent-python
python -m venv venv
venv\Scripts\activate.bat
python -m pip install -r requirements.txt
python agent.py
```

El agente recopila las métricas de CPU, RAM y disco del computador Windows y las envía al backend cada 30 segundos.

Actualmente, el agente utiliza el identificador de servidor `2`, por lo que debe existir previamente un servidor con ese ID en la base de datos.

## Endpoints principales

### Servidores

`GET /api/servidores`

`POST /api/servidores`

`GET /api/servidores/{id}`

`PUT /api/servidores/{id}`

`DELETE /api/servidores/{id}`

### Métricas

`GET /api/metricas`

`GET /api/metricas/{id}`

`POST /api/metricas`

## Estado del proyecto

Proyecto en desarrollo.

Funcionalidades implementadas:

- Registro y administración de servidores.
- API REST para recepción y consulta de métricas.
- Persistencia de datos en MySQL.
- Agente Python para monitoreo de CPU, RAM y disco.
- Panel web Angular.
- Consulta y actualización automática de métricas.

Funcionalidades pendientes de desarrollo:

- Autenticación y autorización.
- Gestión de múltiples servidores desde el frontend.
- Gráficos históricos.
- Alertas y notificaciones.
- Mejoras de seguridad y validación.
- Pruebas automatizadas.

## Consideraciones de seguridad

InfraWatch se encuentra en etapa de desarrollo y está configurado para pruebas locales.

La API todavía no cuenta con autenticación, por lo que no debe exponerse directamente a Internet.

No se deben almacenar contraseñas, tokens ni otras credenciales dentro del repositorio.

## Rama de desarrollo

El desarrollo activo del proyecto se realiza en la rama `develop`.
