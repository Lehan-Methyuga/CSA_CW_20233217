# 🎓 Smart Campus API - Coursework Portfolio

![Java Version](https://img.shields.io/badge/Java-11%2B-blue?logo=java)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-JAX--RS-orange?logo=jakartaee)
![Apache Tomcat](https://img.shields.io/badge/Apache_Tomcat-9.0%2B-F8DC75?logo=apachetomcat)

> **Module:** 5COSC022W Client-Server Architectures  
> **Author:** Lehan Methyuga (Student ID: 20233217)  
> **Topic:** REST API design, development and implementation  

---

## 📖 1. Project Overview
Welcome to my submission for the **Client-Server Architectures Coursework (2025/26)**. This project serves as the robust backend for the university's "Smart Campus" initiative. 

My architectural goal was to build a highly scalable RESTful API to manage physical Rooms and various Sensors (like CO2 monitors and occupancy trackers) using purely **Jakarta RESTful Web Services (JAX-RS)**. Adhering strictly to the specifications, I avoided Spring Boot and SQL Databases, relying instead on high-performance, strictly synchronized in-memory data structures.

---

## 🚀 2. Build & Launch Instructions

This project conforms perfectly to the standard Java EE Web Profile via Maven. 

**1. Compiling the Project**  
Open your terminal, navigate to the project root directory, and execute:
```bash
mvn clean package
```

**2. Server Deployment**  
Maven will compile the system and instantly generate a `smart-campus-api.war` artifact located inside your `/target` folder. Simply copy this file directly into the `webapps` folder of your **Apache Tomcat (v9+)** installation and launch the Tomcat server.

---

## 🌟 3. Advanced Implementations (Going Above & Beyond)

To ensure my API scales resiliently into an industry-grade format, I challenged myself to natively integrate advanced handling patterns:

> [!TIP]
> **304 Not Modified (ETags & Server-Side Caching)**  
> To critically save operational bandwidth, I integrated intelligent ETags inside the `GET /rooms/{roomId}` endpoint. The server calculates a deterministic `hashCode()` based on the exact variable state of the requested Room. If a client queries the room with an identical `If-None-Match` header tag, the API automatically suspends compilation and gracefully bounces back an empty `304 Not Modified` payload.

> [!WARNING]
> **400 Bad Request (Custom Payload Boundary Validation)**  
> To protect the internal application states from garbage JSON data inputs, my POST requests strictly intercept formatting locally before interacting with the database. Triggering malformed inputs (like empty strings or negative room capacities) immediately summons my custom `MalformedPayloadException`, actively evaluated by a native mapper returning a safe `/errors/400` entity.

> [!IMPORTANT]
> **Elite Concurrency Controls (ReentrantReadWriteLocks)**  
> Instead of arbitrarily locking the entire DAO layer with heavy `synchronized` statements, I implemented refined `ReentrantReadWriteLock` variables bridging the DAO structures. This explicitly permits limitless `GET` connections to read instances simultaneously, while perfectly guaranteeing singleton operations specifically locked during `POST` and `DELETE` events.

---

## 💻 4. API Testing Directory (Video Demo cURL Scripts)

The following local execution strings correspond sequentially to the functionality highlighted during my Postman Video Demonstration test track:

<details>
<summary><b>Step 1: Test API Metadata (Root Discovery)</b></summary>

```bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1 -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 2: Room Compilation (POST Integration)</b></summary>

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d "{\"name\": \"Main Auditorium\", \"capacity\": 250}"
```
</details>

<details>
<summary><b>Step 3: Bi-Directional Sensor Assignment (POST Validation)</b></summary>

*(Make sure to accurately replace `ROOM-XXXX` with the generated Room ID!)*
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d "{\"roomId\": \"ROOM-XXXX\", \"type\": \"CO2\", \"status\": \"ACTIVE\"}"
```
</details>

<details>
<summary><b>Step 4: Horizontal Sensor Filtering by Criteria (GET)</b></summary>

```bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2" -H "Accept: application/json"
```
</details>

<details>
<summary><b>Step 5: Updating Historical Readings (Nested Sub-Resource Side-Effects)</b></summary>

*(Make sure to accurately replace `SENS-XXXX` with your generated Sensor ID!)*
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/SENS-XXXX/readings \
     -H "Content-Type: application/json" \
     -d "{\"value\": 412.5}"
```
</details>

---

## 📝 5. Conceptual Report (Coursework Q&A Section)

### Part 1: Service Architecture & Setup

> **Q1: Explain the default lifecycle of a JAX-RS Resource class and how it impacts in-memory data synchronization.**

**My Defense:** By default, JAX-RS strictly executes its mapping interfaces (such as my `SensorRoomResource`) via a "per-request" lifecycle design. Every single time a user queries an HTTP endpoint, the Tomcat containment rapidly instantiates a brand new resource component, handles the execution flow, and then violently rips it down under Java's Garbage Collection. Because this architecture actively strips statefulness, storing simple ArrayLists identically inside the controller file logically self-deletes all variables dynamically. To architect around this, I detached my data stores explicitly into abstracted `RoomDAO` and `SensorDAO` classes functioning globally as pure Singletons.

> **Q2: Why is Hypermedia (HATEOAS) a hallmark of advanced RESTful design and how does it benefit clients?**

**My Defense:** Integrating HATEOAS effectively implies that standard objects are directly served attached to contextual hyperlink state operations targeting what a user can functionally do physically next. This functions amazingly structurally because mobile engineers are safely blocked from hard-coding vulnerable endpoints into front-end logic directly. We establish a massive level of abstraction correctly; if I ever redesign the literal URI structure under `/api/v2`, client devices theoretically won’t break dynamically because they map specifically off the URI payloads handed out by HATEOAS engines instead of static strings.

### Part 2: Room Management

> **Q1: Returning only IDs vs full objects in lists - what are the implications for network bandwidth vs client processing?**

**My Defense:** Packaging complete object components into expansive lists (like `GET /rooms`) aggressively offsets processing pressure locally onto front-end arrays. Unfortunately, executing this expands the basic JSON payload into immense network-clogging dimensions directly constrained by network latency limitations severely tracking massive systems natively. If I counter it by just dropping arrays loaded singularly with PK strings (`IDs`), the transmission is radically compressed physically. However, client processors dynamically inherit the massive burden inherently spinning up hundreds of sequential subsequent `GET /rooms/{id}` requests exactly tracing the "N+1" problem framework sequentially just to populate screen labels dynamically.

> **Q2: Is the DELETE operation idempotent in your implementation? What happens if a client sends the exact same DELETE request multiple times?**

**My Defense:** Without question, my `DELETE` structural operations define perfect idempotency. Executing mathematically idempotent functions fundamentally demands that invoking identical operations indefinitely always reliably concludes mimicking an identical singular iteration state identically natively. If you accidentally throw 10 simultaneous `DELETE /rooms/LIB-301` attacks right at my API, my logic drops the physical Room actively immediately capturing `204 No Content` properly on cycle one. Every subsequent duplicate request gracefully recognizes the empty key state executing an identical empty payload structured softly inside `404 Not Found` responses identically structurally.

### Part 3: Sensor Operations & Linking

> **Q1: What are the technical consequences if a client attempts to send data in a different format when you've defined `@Consumes(MediaType.APPLICATION_JSON)`? How does JAX-RS handle it?**

**My Defense:** Pushing `@Consumes(MediaType.APPLICATION_JSON)` aggressively onto my `POST` mapping definitions executes a native firewall. When arbitrary systems theoretically blast malicious XML structures or raw Text variables, my application prevents code-level activation completely natively. Functionally, JAX-RS natively monitors the arbitrary HTTP Headers catching payload formatting deviations locally inside the container level returning standard `415 Unsupported Media Type` protections globally before triggering deserialization vulnerability errors cleanly.

> **Q2: Why is the query parameter approach (`?type=CO2`) considered superior for filtering compared to URL paths (`/type/CO2`)?**

**My Defense:** Query parameters correctly establish an explicit metadata subset functionally modifying an existing relational matrix logic cleanly (`/sensors`). Because they operate natively mapped as filtering arrays systematically expanding horizontally structurally (`?type=CO2&status=ACTIVE`), scaling endpoints operates elegantly properly statically linked to the exact same list domain locally. If I arbitrarily baked those variables natively into absolute URL routing structures inherently matching `/sensors/type/CO2`, my framework structurally claims that `type` identically acts natively representing distinct entity layers incorrectly breaking basic collection definitions logically structurally.

### Part 4: Deep Nesting with Sub-Resources

> **Q1: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does it help manage complexity in large APIs?**

**My Defense:** Instead of cramming all advanced relational constraints dynamically interacting alongside nested configurations functionally inside a singular massive `SensorResource` file, my architecture perfectly bridges logical domains smoothly functionally. By simply binding my `@Path("/{sensorId}/readings")` directly outputting distinct `SensorReadingResource.java` class contexts seamlessly inherently, my API effectively executes flawless Single-Responsibility modularity cleanly natively preventing logic conflicts efficiently dynamically.

### Part 5: Advanced Error Handling, Exception Mapping & Logging

> **Q1: Why is HTTP 422 often considered more semantically accurate than a standard 404 when dealing with a missing reference inside a JSON payload?**

**My Defense:** Standard `404 Not Found` messages correctly reflect conditions whenever clients incorrectly map their request structures onto unwritten mapping bindings sequentially improperly (`e.g /api/v1/fake-url`). So if I map out a perfect payload structurally onto `POST /sensors`, yet explicitly define broken `roomId` bindings natively inside my framework syntax properties sequentially, deploying `404 Not Found` radically confuses engineers fundamentally incorrectly. Raising perfectly mapped `422 Unprocessable Entity` structures (executed by the ExceptionMapper globally mapped onto my `LinkedResourceNotFoundException`) appropriately explains exactly theoretically: "The Endpoint works properly natively structurally; however, your JSON referential variables violate structural constraints functionally logically natively."

> **Q2: From a cybersecurity standpoint, what are the risks associated with exposing internal Java stack traces to external API consumers?**

**My Defense:** Exposing stack traces structurally unredacted openly grants external parties effectively a detailed topological map logically identifying backend layers structurally effortlessly. Uncaught variables natively explicitly log root path arrays structurally detailing precisely utilized package dependencies correctly functionally mapping exact Java/Jakarta operational patches identically implicitly. Threat actors efficiently scrape these operational vulnerabilities logically cross-referencing precise syntax frameworks cleanly across open-source CVE (Common Vulnerabilities) databanks natively logically exposing surgical entry point exploitations fundamentally effectively structurally natively reliably rapidly accurately!

> **Q3: Why is it advantageous to use JAX-RS filters for logging rather than manually inserting `Logger.info()` inside every single resource method?**

**My Defense:** Binding operational logging capabilities strictly alongside standard variables uniformly executing `Logger.info()` statically onto all 20 individual functions actively logically inherently creates heavy code redundancies and eventually mathematically risks accidental omission oversights locally natively logically properly gracefully. Operating JAX-RS `ContainerRequestFilter` integrations mathematically implements seamless Aspect-Oriented operational frameworks globally effortlessly flawlessly securely tracking entire application sequences automatically right as logic operations commence cleanly properly!
