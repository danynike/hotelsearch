# Hotel Availability Search Service  
Arquitectura Hexagonal · Spring Boot · Kafka · MongoDB · Testcontainers

---

## Descripción general

Este proyecto implementa un microservicio en **Spring Boot** para procesar búsquedas de disponibilidad hotelera.  
Expone dos endpoints REST versionados (`/api/1/search` y `/api/1/count`), publica eventos en **Kafka** y persiste los mensajes consumidos en **MongoDB** para poder calcular cuántas búsquedas “similares” existen.

La solución está diseñada con **arquitectura hexagonal (Ports & Adapters)**, sin Lombok, con validaciones en el borde del sistema y con tests unitarios y de integración end-to-end (E2E).

---

## Objetivo funcional

### POST `/api/1/search`
Recibe:
```json
{
  "hotelId": "1234aBc",
  "checkIn": "29/12/2023",
  "checkOut": "31/12/2023",
  "ages": [30, 29, 1, 3]
}

response:
{
  "searchId": "xxxxx"
}

Acciones:

Valida el payload.
Usa DTO inmutable (record) en el controlador.
Publica el evento en Kafka en el topic hotel_availability_searches.

### GET /api/1/count?searchId=xxxxx
Response:
{
  "searchId": "xxxxx",
  "search": {
    "hotelId": "1234aBc",
    "checkIn": "29/12/2023",
    "checkOut": "31/12/2023",
    "ages": [1, 3, 29, 30]
  },
  "count": 100
}

Acciones:

Busca el searchId en Mongo.
Normaliza edades (ordenamiento) para determinar similitud.
Devuelve el número de búsquedas similares (mismo hotelId + fechas + edades ordenadas).

### Tecnologías

Java 21
Spring Boot 3.x
Spring Web / Validation
Spring Kafka
Spring Data MongoDB
Kafka (Confluent images para Docker local)
MongoDB (Docker)
JUnit 5, MockMvc
Testcontainers (Kafka + Mongo para integración)
Awaitility (espera asíncrona por consumo Kafka)

### Arquitectura Hexagonal (Ports & Adapters)

La arquitectura se organiza en 3 capas principales:
#1) Dominio (domain)

Contiene la lógica pura del negocio, sin dependencias de Spring ni frameworks:
Value Objects: SearchId, HotelId, StayDates, Ages, NormalizedSearchKey
Entidad: Search
Servicio de dominio: SearchKeyNormalizer
Excepciones de dominio: SearchNotFoundException
Motivo: el dominio debe ser independiente y testeable sin infraestructura.

#2) Aplicación (application)

Contiene los casos de uso y los puertos (interfaces) para hablar con el mundo externo:
Use cases (ports in):
CreateSearchUseCase
GetSearchCountUseCase
PersistSearchUseCase
Ports out:
PublishSearchPort (Kafka producer)
SaveSearchPort (Mongo persist)
LoadSearchPort (Mongo read)
CountSimilarSearchesPort (Mongo count)
Implementaciones:
CreateSearchService
GetSearchCountService
PersistSearchService
Motivo: los casos de uso orquestan el flujo. La aplicación depende de abstracciones (puertos), no de implementaciones (SOLID: Dependency Inversion).

#3) Adaptadores (adapter)

Implementan la infraestructura.
Inbound adapters (entrada):
REST Controller SearchController
Kafka Consumer SearchKafkaConsumer
Outbound adapters (salida):
Kafka Producer SearchKafkaProducerAdapter
Mongo Adapter SearchMongoRepositoryAdapter
Motivo: infraestructura intercambiable. Puedes cambiar Mongo por PostgreSQL o Kafka por otra cola sin romper dominio/casos de uso.
