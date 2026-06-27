# 🛒 ShopNest — Spring Boot E-Commerce REST API

A full-featured e-commerce REST API built with **Spring Boot 3**, designed as a learning project covering all core Spring Boot concepts — IoC, DI, JPA, Security, Transactions, Validation, and more.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Spring Boot Concepts Covered](#spring-boot-concepts-covered)
- [Project Structure](#project-structure)
- [Entity Relationship Diagram](#entity-relationship-diagram)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Authentication](#authentication)
- [Running Tests](#running-tests)
- [Build Order for Learners](#build-order-for-learners)

---

## Overview

ShopNest is a backend REST API for an e-commerce platform. It supports:

- User registration and login with JWT authentication
- Product and category management (admin only)
- Shopping cart per user
- Order placement with stock validation (fully transactional)
- Payment tracking
- Role-based access control (USER / ADMIN)
- Global exception handling with clean error responses
- Pagination and dynamic filtering on product listing
- Swagger UI for interactive API documentation

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Security | Spring Security 6 + JWT (jjwt 0.12) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8 (PostgreSQL compatible) |
| Validation | Jakarta Bean Validation (`@Valid`) |
| API Docs | SpringDoc OpenAPI 3 (Swagger UI) |
| Build Tool | Maven |
| Testing | JUnit 5 + Mockito + Spring Boot Test |

---

## Spring Boot Concepts Covered

| Concept | Where it appears |
|---|---|
| `@RestController`, `@RequestMapping` | All controllers |
| `@Service`, `@Repository` | Service and repository layers |
| Constructor injection / `@Autowired` | All services |
| `@Entity`, `@Table`, `@Column` | All entity classes |
| `@OneToMany`, `@ManyToOne`, `@OneToOne` | User, Order, Cart, Product |
| `@Embeddable` | Address inside Order |
| Spring Data JPA (`JpaRepository`) | All repositories |
| Custom `@Query` (JPQL) | ProductRepository, OrderRepository |
| `JpaSpecificationExecutor` | Dynamic product filtering |
| `Pageable` + `Page<T>` | Product listing, order history |
| `@Transactional` | OrderService.placeOrder() |
| Spring Security filter chain | SecurityConfig |
| `OncePerRequestFilter` | JwtAuthFilter |
| `UserDetails` / `UserDetailsService` | User entity, UserDetailsServiceImpl |
| `BCryptPasswordEncoder` | AuthService |
| `@PreAuthorize` | Admin-only endpoints |
| `@Valid` + `@NotBlank` / `@Email` | All request DTOs |
| `@ControllerAdvice` + `@ExceptionHandler` | GlobalExceptionHandler |
| `@Value` | JWT secret, expiry from application.yml |
| `@CreatedDate`, `@LastModifiedDate` | User, Cart (JPA Auditing) |
| `@EnableWebSecurity` | SecurityConfig |
| `application.yml` profiles | dev / prod separation |

---

## Project Structure

```
src/
└── main/
    ├── java/com/shopnest/
    │   ├── ShopNestApplication.java
    │   │
    │   ├── config/
    │   │   ├── SecurityConfig.java          # Filter chain, CORS, BCrypt bean
    │   │   └── OpenApiConfig.java           # Swagger JWT header config
    │   │
    │   ├── entity/
    │   │   ├── User.java
    │   │   ├── Product.java
    │   │   ├── Category.java
    │   │   ├── Cart.java
    │   │   ├── CartItem.java
    │   │   ├── Order.java
    │   │   ├── OrderItem.java
    │   │   ├── Payment.java
    │   │   ├── Address.java                 # @Embeddable
    │   │   └── enums/
    │   │       ├── Role.java                # USER, ADMIN
    │   │       ├── OrderStatus.java         # PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    │   │       └── PaymentStatus.java       # PENDING, SUCCESS, FAILED
    │   │
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── ProductRepository.java       # + JpaSpecificationExecutor
    │   │   ├── CategoryRepository.java
    │   │   ├── CartRepository.java
    │   │   ├── CartItemRepository.java
    │   │   ├── OrderRepository.java
    │   │   └── PaymentRepository.java
    │   │
    │   ├── service/
    │   │   ├── AuthService.java
    │   │   ├── ProductService.java
    │   │   ├── CategoryService.java
    │   │   ├── CartService.java
    │   │   ├── OrderService.java            # @Transactional here
    │   │   ├── PaymentService.java
    │   │   └── UserDetailsServiceImpl.java  # implements UserDetailsService
    │   │
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   ├── ProductController.java
    │   │   ├── CategoryController.java
    │   │   ├── CartController.java
    │   │   ├── OrderController.java
    │   │   └── PaymentController.java
    │   │
    │   ├── dto/
    │   │   ├── request/
    │   │   │   ├── RegisterRequest.java
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── ProductRequest.java
    │   │   │   ├── AddToCartRequest.java
    │   │   │   ├── UpdateQtyRequest.java
    │   │   │   └── PlaceOrderRequest.java
    │   │   └── response/
    │   │       ├── AuthResponse.java
    │   │       ├── ProductDTO.java
    │   │       ├── CartDTO.java
    │   │       ├── CartItemDTO.java
    │   │       ├── OrderDTO.java
    │   │       ├── OrderItemDTO.java
    │   │       ├── PaymentDTO.java
    │   │       ├── ErrorResponse.java
    │   │       └── ValidationErrorResponse.java
    │   │
    │   ├── security/
    │   │   ├── JwtUtil.java                 # generate, extract, validate
    │   │   └── JwtAuthFilter.java           # extends OncePerRequestFilter
    │   │
    │   ├── exception/
    │   │   ├── GlobalExceptionHandler.java  # @ControllerAdvice
    │   │   ├── ResourceNotFoundException.java
    │   │   ├── InsufficientStockException.java
    │   │   └── CartEmptyException.java
    │   │
    │   └── specification/
    │       └── ProductSpecification.java    # dynamic filtering with JPA Criteria
    │
    └── resources/
        ├── application.yml
        ├── application-dev.yml
        └── application-prod.yml
```

---

## Entity Relationship Diagram

```
User ──────────── Cart
 │   (1:1)          │
 │                  │ (1:N)
 │ (1:N)          CartItem ──── (N:1) ──── Product ──── (N:1) ──── Category
 │
Order ──── (1:1) ──── Payment
  │
  │ (1:N)
OrderItem ──── (N:1) ──── Product
```

---

## API Endpoints

### Auth — `/api/auth`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/register` | Public | Register new user, returns JWT |
| POST | `/login` | Public | Login, returns JWT |
| GET | `/me` | USER | Get current user profile |

### Products — `/api/products`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/` | Public | List all products (paginated, filterable) |
| GET | `/{id}` | Public | Get product by ID |
| POST | `/` | ADMIN | Create product |
| PUT | `/{id}` | ADMIN | Update product |
| DELETE | `/{id}` | ADMIN | Soft delete product |

Query params on GET `/`: `?page=0&size=10&sort=price,asc&category=electronics&search=laptop&minPrice=500&maxPrice=2000`

### Categories — `/api/categories`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/` | Public | List all categories |
| POST | `/` | ADMIN | Create category |
| DELETE | `/{id}` | ADMIN | Delete category |

### Cart — `/api/cart`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/` | USER | View cart with totals |
| POST | `/items` | USER | Add item (or increment qty) |
| PUT | `/items/{itemId}` | USER | Update item quantity |
| DELETE | `/items/{itemId}` | USER | Remove item |
| DELETE | `/` | USER | Clear entire cart |

### Orders — `/api/orders`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/` | USER | Place order from cart |
| GET | `/my` | USER | My order history (paginated) |
| GET | `/{id}` | USER | Order detail |
| PATCH | `/{id}/cancel` | USER | Cancel order (PENDING only) |
| GET | `/admin/all` | ADMIN | All orders (admin dashboard) |

### Payments — `/api/payments`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/{orderId}/confirm` | USER | Confirm payment with transaction ID |
| GET | `/{orderId}` | USER | Get payment status for an order |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+ running locally

### 1. Clone the repository

```bash
git clone https://github.com/your-username/shopnest.git
cd shopnest
```

### 2. Create the database

```sql
CREATE DATABASE shopnest_db;
```

### 3. Configure the application

Copy `application-dev.yml.example` to `application-dev.yml` and fill in your values (see [Configuration](#configuration)).

### 4. Run the application

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The API starts at `http://localhost:8080`.

Swagger UI is available at: `http://localhost:8080/swagger-ui/index.html`

---

## Configuration

`src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/shopnest_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update          # use 'create' on first run, then 'update'
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

  data:
    jpa:
      repositories:
        enabled: true

application:
  jwt:
    secret: your-256-bit-secret-key-here-change-in-production
    expiration-ms: 86400000     # 24 hours

server:
  port: 8080
```

> ⚠️ Never commit your JWT secret or database password. Add `application-dev.yml` to `.gitignore`.

---

## Authentication

This API uses **JWT (JSON Web Tokens)** for stateless authentication.

### Register and get a token

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Arun",
    "lastName": "Kumar",
    "email": "arun@example.com",
    "password": "secret123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "arun@example.com",
  "role": "USER"
}
```

### Use the token on protected routes

```bash
curl http://localhost:8080/api/cart \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

In Swagger UI, click **Authorize** (top right) and paste `Bearer <your-token>`.

---

## Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=OrderServiceTest

# Run with coverage report (requires jacoco plugin)
mvn verify
```

Test structure:

```
src/test/java/com/shopnest/
├── service/
│   ├── AuthServiceTest.java
│   ├── ProductServiceTest.java
│   ├── CartServiceTest.java
│   └── OrderServiceTest.java      # most important — tests @Transactional rollback
├── controller/
│   ├── AuthControllerTest.java    # @WebMvcTest + MockMvc
│   └── ProductControllerTest.java
└── repository/
    └── ProductRepositoryTest.java # @DataJpaTest with H2
```

---

## Build Order for Learners

If you are building this from scratch to learn Spring Boot, follow this order:

**Step 1 — Project setup**
Go to [start.spring.io](https://start.spring.io) and add: Spring Web, Spring Data JPA, Spring Security, MySQL Driver, Validation, Lombok, SpringDoc OpenAPI.

**Step 2 — Entities**
Write all entity classes with JPA annotations. Run the app once with `ddl-auto: create` to verify the schema is generated correctly.

**Step 3 — Auth module**
Build `User` entity → `UserRepository` → `UserDetailsServiceImpl` → `JwtUtil` → `JwtAuthFilter` → `SecurityConfig` → `AuthService` → `AuthController`. Test register and login with Postman before moving on.

**Step 4 — Product + Category CRUD**
The simplest service logic. Introduces pagination (`Pageable`) and dynamic queries (`Specification`).

**Step 5 — Cart**
Introduces session-aware logic and cascading. `addItem()` needs to handle both "new item" and "increment existing item" cases.

**Step 6 — Order with @Transactional**
The most complex module. Ties together Cart, Product (stock), Order, and Payment in one atomic operation. Deliberately break it (e.g. set stock to 0 mid-order) to see rollback in action.

**Step 7 — GlobalExceptionHandler**
Add `@ControllerAdvice` last so all your custom exceptions return clean JSON error bodies instead of Spring's default white-label error page.

**Step 8 — Swagger + polish**
Add SpringDoc, configure JWT header in `OpenApiConfig`, and verify every endpoint is documented.

---

## Contributing

Pull requests are welcome. For major changes, open an issue first to discuss what you'd like to change.

---

## License

[MIT](LICENSE)
