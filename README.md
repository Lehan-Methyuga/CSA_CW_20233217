# 🏛️ Smart Campus API
**Module:** 5COSC022W Client-Server Architectures (2025/26)  
**Author:** Lehan Methyuga | **ID:** 20233217

![Java](https://img.shields.io/badge/Java-11%2B-2C3E50?style=for-the-badge&logo=java)
![JAX-RS](https://img.shields.io/badge/JAX--RS-Jakarta_EE-E8ECF0?style=for-the-badge&logo=jakartaee&logoColor=black)
![Tomcat](https://img.shields.io/badge/Apache_Tomcat-9.0%2B-2C3E50?style=for-the-badge&logo=apachetomcat)

---

## 📖 1. Project Overview
This project is my coursework submission for the Smart Campus initiative.  
It provides a REST API backend for managing Rooms and IoT Sensors (for example CO2 monitors and occupancy sensors).

To follow the coursework constraints, I built this using **pure JAX-RS (Jersey)**:
- No Spring Boot
- No SQL/NoSQL database
- In-memory storage using thread-safe collections (`ConcurrentHashMap`)

---

## 🚀 2. Quick Start Guide
### Prerequisites
- Java 11+
- Maven 3.8+
- Apache Tomcat 9+

### Build
```bash
mvn clean package
```

### Deploy
1. Maven generates `target/smart-campus-api.war`
2. Copy it into Tomcat `webapps/`
3. Start Tomcat

Base URL:
```text
http://localhost:8080/smart-campus-api/api/v1
```

---

## ⚡ 3. Advanced Implementations (Distinction Features)
### 1. ETag Caching (`304 Not Modified`)
Implemented on `GET /rooms/{roomId}`.  
If client sends matching `If-None-Match`, server returns `304` without sending full payload again.

### 2. Custom Boundary Validation (`400 Bad Request`)
Invalid JSON payload logic (empty fields, invalid values) is blocked before persistence layer and mapped via custom exception.

### 3. Thread-Safe Concurrency (Read/Write Locks)
DAO layer uses `ReentrantReadWriteLock`, allowing concurrent reads and safe exclusive writes.

### 4. Sub-Resource Locator Pattern
Nested resource routing is handled via:
`/sensors/{sensorId}/readings`

---

## 🧪 4. Interactive Testing Guide (cURL)
The following 16-step sequence is the same order used in my video demonstration.

Status coverage:
- Success: `200`, `201`, `204`
- Architectural constraints: `304`, `400`, `403`, `409`, `422`

<details>
<summary><b>Step 1 - Discovery Root (Expected: 200 OK)</b></summary>

```bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1" \
  -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 2 - List Rooms (Expected: 200 OK)</b></summary>

```bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/rooms" \
  -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 3 - Get Room LIB-301 and capture ETag (Expected: 200 OK)</b></summary>

```bash
curl -i -X GET "http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301" \
  -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 4 - Conditional GET using If-None-Match (Expected: 304 Not Modified)</b></summary>

```bash
# Replace <ETAG_FROM_STEP_3> with the exact ETag value returned in Step 3
curl -i -X GET "http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301" \
  -H "Accept: application/json" \
  -H "If-None-Match: <ETAG_FROM_STEP_3>"
```
</details>

<details>
<summary><b>Step 5 - Create Invalid Room (Expected: 400 Bad Request)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/rooms" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"ROOM-BAD-01\",\"name\":\"\",\"capacity\":-1}"
```
</details>

<details>
<summary><b>Step 6 - Create Valid Room ROOM-A100 (Expected: 201 Created)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/rooms" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"ROOM-A100\",\"name\":\"AI Lab\",\"capacity\":60}"
```
</details>

<details>
<summary><b>Step 7 - Delete ROOM-A100 (Expected: 204 No Content)</b></summary>

```bash
curl -i -X DELETE "http://localhost:8080/smart-campus-api/api/v1/rooms/ROOM-A100" \
  -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 8 - Create Invalid Sensor Payload (Expected: 400 Bad Request)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/sensors" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"SENS-BAD-01\",\"type\":\"\",\"status\":\"\",\"roomId\":\"LIB-301\"}"
```
</details>

<details>
<summary><b>Step 9 - Create Sensor with Missing Room Link (Expected: 422 Unprocessable Entity)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/sensors" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"SENS-404-ROOM\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"roomId\":\"ROOM-NOT-EXIST\"}"
```
</details>

<details>
<summary><b>Step 10 - Create Valid Sensor SENS-CO2-01 (Expected: 201 Created)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/sensors" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"SENS-CO2-01\",\"roomId\":\"LIB-301\",\"type\":\"CO2\",\"status\":\"ACTIVE\"}"
```
</details>

<details>
<summary><b>Step 11 - Filter Sensors by Type CO2 (Expected: 200 OK)</b></summary>

```bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2" \
  -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 12 - Get Readings for SENS-CO2-01 (Expected: 200 OK)</b></summary>

```bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors/SENS-CO2-01/readings" \
  -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 13 - Add Reading to SENS-CO2-01 (Expected: 201 Created)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/sensors/SENS-CO2-01/readings" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"READ-01\",\"timestamp\":1713931200000,\"value\":412.5}"
```
</details>

<details>
<summary><b>Step 14 - Create MAINTENANCE Sensor SENS-MAINT-01 (Expected: 201 Created)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/sensors" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"SENS-MAINT-01\",\"roomId\":\"LIB-301\",\"type\":\"Temperature\",\"status\":\"MAINTENANCE\"}"
```
</details>

<details>
<summary><b>Step 15 - Add Reading to MAINTENANCE Sensor (Expected: 403 Forbidden)</b></summary>

```bash
curl -X POST "http://localhost:8080/smart-campus-api/api/v1/sensors/SENS-MAINT-01/readings" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"READ-M-01\",\"timestamp\":1713931300000,\"value\":27.1}"
```
</details>

<details>
<summary><b>Step 16 - Delete LIB-301 while sensors exist (Expected: 409 Conflict)</b></summary>

```bash
curl -i -X DELETE "http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301" \
  -H "Accept: application/json"
```
</details>

> Note: Run in order. Some requests depend on resources created in earlier steps.

---

## 📝 5. Conceptual Report (Architectural Decisions)

### Part 1: Architecture and Discovery
**JAX-RS lifecycle and state management**  
By default, JAX-RS resources are request-scoped. New resource instances are created per request, so keeping shared state inside resource fields is unsafe. I stored shared data in singleton DAO classes and protected it with thread-safe structures and locks.

**Why HATEOAS matters**  
Hypermedia makes APIs more discoverable. Instead of clients hardcoding every endpoint, they can follow links returned in responses. This reduces client breakage when endpoint structures evolve.

### Part 2: Room Management
**IDs vs full objects in list responses**  
IDs-only reduces payload size but introduces more follow-up calls (N+1 style overhead). Full objects increase payload size but reduce round trips and make clients simpler.

**DELETE idempotency**  
My DELETE is idempotent: first delete can return `204`, repeated delete can return `404`, but final server state is unchanged (resource remains deleted).

### Part 3: Sensor Operations and Validation
**Incorrect media types with `@Consumes(MediaType.APPLICATION_JSON)`**  
If client sends `text/plain` or XML, JAX-RS rejects it before method logic and returns `415 Unsupported Media Type`.

**Why query parameters for filtering**  
`/sensors?type=CO2` is better than path-based filtering for search because type is a query constraint on a collection, not a separate resource identity.

### Part 4: Sub-Resource Routing
**Sub-Resource Locator pattern**  
Using `/sensors/{sensorId}/readings` routed to `SensorReadingResource` keeps responsibilities separated and prevents one large controller from becoming difficult to maintain.

**Required side effect**  
When a reading is posted, parent sensor `currentValue` is updated so summary and historical data stay consistent.

### Part 5: Error Handling and Security
**422 vs 404 for missing linked room**  
`404` means route missing. In this case route exists, but payload references invalid data (`roomId` not found). `422` communicates this more accurately.

**Why stack traces should never be exposed**  
Stack traces leak internal class names, package structure, and dependency details. Attackers can use this information for targeted exploit research. A global exception mapper returns safe generic `500` responses.

**Why use filters for logging**  
JAX-RS filters centralize request/response logging and avoid repetitive logging code in every endpoint.

---