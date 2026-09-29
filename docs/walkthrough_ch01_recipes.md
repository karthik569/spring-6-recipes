# Chapter 1 — Spring IoC Recipes Walkthrough

**Date:** 2026-07-18  
**Book:** Spring 6 Recipes (Apress)  
**Project:** `spring-6-recipes`  
**Chapter Directory:** `ch01/`

---

## Overview

Chapter 1 covers the **Spring IoC (Inversion of Control) container** from the ground up. It uses two domain examples throughout:
- **Shop domain** — `Product`, `Battery`, `Disc`, `ShoppingCart`, `Cashier` (recipes 1.1–1.12, 1.15–1.25)
- **Calculator domain** — `ArithmeticCalculator`, `UnitCalculator` (recipes 1.13–1.14)

Each recipe (and its sub-variants `_i`, `_ii`, etc.) progressively builds on the previous one, introducing a new Spring concept each time.

---

## Recipe Index

### Recipe 1.1 — Your First Spring Bean & ApplicationContext

**Directories:** `recipe_1_1_i`, `recipe_1_1_ii`  
**Domain:** Sequence generator (formatted IDs like `30-100000-A`)

**What it teaches:**
- How to create a `@Configuration` class with a `@Bean` method
- Bootstrapping the Spring container via `AnnotationConfigApplicationContext`
- Retrieving a bean with `ctx.getBean(Sequence.class)`

**Key files:**
- `SequenceConfiguration.java` — `@Configuration` + `@Bean`
- `Sequence.java` — Plain Java bean with prefix/suffix/initial properties
- `Main.java` — Bootstraps context, retrieves and uses the bean

```java
@Configuration
public class SequenceConfiguration {
    @Bean
    public Sequence sequence() {
        var seq = new Sequence();
        seq.setPrefix("30");
        seq.setSuffix("A");
        seq.setInitial(100000);
        return seq;
    }
}

// Main.java
try (var ctx = new AnnotationConfigApplicationContext(SequenceConfiguration.class)) {
    var generator = ctx.getBean(Sequence.class);
    System.out.println(generator.nextValue()); // 30100000A
}
```

**Variant `_ii`:** Shows the same using component scanning / annotation-driven configuration.

---

### Recipe 1.2 — Setter Injection

**Directory:** `recipe_1_2`  
**Domain:** Shop

**What it teaches:**
- Wiring beans together using **setter injection** inside `@Bean` methods
- Calling setter methods on bean instances to inject primitive values and object references

---

### Recipe 1.3 — Constructor Injection

**Directories:** `recipe_1_3_i` through `recipe_1_3_vii`  
**Domain:** Shop

**What it teaches:**
- Wiring beans via **constructor arguments**
- Injecting primitives, Strings, other beans, Lists, Maps, Sets, Properties
- Each sub-variant adds a new collection/type scenario

---

### Recipe 1.4 — `@Autowired`

**Directories:** `recipe_1_4_i`, `recipe_1_4_ii`  
**Domain:** Shop

**What it teaches:**
- Using `@Autowired` for automatic dependency injection by **type**
- Field injection vs constructor injection with `@Autowired`
- Spring's auto-wiring mechanism

---

### Recipe 1.5 — `@Qualifier`

**Directories:** `recipe_1_5_i`, `recipe_1_5_ii`  
**Domain:** Shop

**What it teaches:**
- Handling ambiguity when multiple beans of the same type exist
- Using `@Qualifier("beanName")` alongside `@Autowired` to pick a specific bean
- Defining custom qualifier annotations

---

### Recipe 1.6 — `@Resource` and `@Inject`

**Directories:** `recipe_1_6_i`, `recipe_1_6_ii`  
**Domain:** Shop

**What it teaches:**
- JSR-250 `@Resource` annotation — injection by name
- JSR-330 `@Inject` annotation — standard alternative to `@Autowired`
- Differences between Spring's `@Autowired`, `@Resource`, and `@Inject`

---

### Recipe 1.7 — Bean References in Java Config

**Directory:** `recipe_1_7`  
**Domain:** Shop

**What it teaches:**
- How to wire one bean into another inside `@Configuration` classes
- Direct method calls between `@Bean` methods (Spring intercepts via CGLIB proxy)

---

### Recipe 1.8 — Collections and Inner Beans

**Directories:** `recipe_1_8_i` through `recipe_1_8_v`  
**Domain:** Shop

**What it teaches:**
- Injecting `List`, `Set`, `Map`, and `Properties` as bean dependencies
- Defining inner/anonymous beans (beans used in only one place)
- Each variant covers a different collection type

---

### Recipe 1.9 — Spring Expression Language (SpEL)

**Directories:** `recipe_1_9_i` through `recipe_1_9_iv`  
**Domain:** Shop

**What it teaches:**
- Using `#{ }` SpEL expressions in `@Value` to inject computed values
- Accessing other bean properties, calling methods, using operators
- Examples: `#{product.price * 0.9}`, `#{systemProperties['user.home']}`

---

### Recipe 1.10 — Factory Methods & `FactoryBean`

**Directories:** `recipe_1_10_i`, `recipe_1_10_ii`, `recipe_1_10_iii`  
**Domain:** Shop (`ProductCreator`, `DiscountFactoryBean`)

**What it teaches:**
- `_i` — **Static factory method**: `ProductCreator.createProduct("battery")`
- `_ii` — **Instance factory method**: calling a method on another bean to produce a bean
- `_iii` — **Spring `FactoryBean<T>`**: implementing `FactoryBean` for complex object creation (e.g., `DiscountFactoryBean`)

---

### Recipe 1.11 — Bean Scopes

**Directory:** `recipe_1_11`  
**Domain:** Shop

**What it teaches:**
- **Singleton** scope (default) — one shared instance per container
- **Prototype** scope — new instance every time the bean is requested
- `@Scope("prototype")` annotation
- When to use each scope

---

### Recipe 1.12 — Spring Profiles

**Directory:** `recipe_1_12`  
**Domain:** Shop (seasonal discount configs)

**What it teaches:**
- `@Profile("profileName")` — activate different beans based on environment/profile
- Config classes: `ShopConfigurationSpr` (Spring), `ShopConfigurationSumWin` (Summer/Winter), `ShopConfigurationAut` (Autumn/Global)
- Setting active profiles via `ctx.getEnvironment().setActiveProfiles("summer")`

---

### Recipe 1.13 — AOP (Aspect-Oriented Programming)

**Directories:** `recipe_1_13_i` through `recipe_1_13_v`  
**Domain:** Calculator (`ArithmeticCalculator`, `UnitCalculator`)

**What it teaches:**

| Sub-recipe | AOP Concept |
|------------|-------------|
| `_i` | `@Aspect`, `@Before` / `@After` advice — `CalculatorLoggingAspect` |
| `_ii` | `@AfterReturning` — capturing and logging the return value |
| `_iii` | `@AfterThrowing` — intercepting exceptions |
| `_iv` | `@Around` — full control; wrapping method execution (`ProceedingJoinPoint`) |
| `_v` | `@Pointcut` — reusable pointcut expressions |

**Key annotations:**
- `@EnableAspectJAutoProxy` on config class
- `@Aspect` on the aspect class
- Pointcut expression: `execution(* com.apress..*.*(..))` 

---

### Recipe 1.14 — AOP Introductions

**Directory:** `recipe_1_14`  
**Domain:** Calculator

**What it teaches:**
- `@DeclareParents` — adding a new interface to an existing bean at runtime without modifying its source
- Mix-in pattern using AOP

---

### Recipe 1.15 — Bean Lifecycle: Init and Destroy Callbacks

**Directories:** `recipe_1_15_i`, `recipe_1_15_ii`  
**Domain:** Shop

**What it teaches:**
- `@PostConstruct` — method called after dependency injection is complete
- `@PreDestroy` — method called before the bean is removed from the container
- Using `initMethod` / `destroyMethod` attributes on `@Bean`

---

### Recipe 1.16 — `InitializingBean` & `DisposableBean`

**Directories:** `recipe_1_16_i`, `recipe_1_16_ii`  
**Domain:** Shop

**What it teaches:**
- Implementing `InitializingBean.afterPropertiesSet()` as an alternative to `@PostConstruct`
- Implementing `DisposableBean.destroy()` as an alternative to `@PreDestroy`
- Spring-specific interfaces vs standard JSR-250 annotations

---

### Recipe 1.17 — `BeanPostProcessor`

**Directories:** `recipe_1_17_i` through `recipe_1_17_iv`  
**Domain:** Shop

**What it teaches:**
- Implementing `BeanPostProcessor` to intercept every bean after instantiation
- `postProcessBeforeInitialization()` and `postProcessAfterInitialization()` hooks
- Use cases: auditing, proxying, custom annotation processing

---

### Recipe 1.18 — `BeanFactoryPostProcessor`

**Directory:** `recipe_1_18`  
**Domain:** Shop

**What it teaches:**
- Implementing `BeanFactoryPostProcessor` to modify **bean definitions** before any beans are created
- Accessing and modifying `BeanDefinition` objects
- Built-in example: `PropertySourcesPlaceholderConfigurer`

---

### Recipe 1.19 — Externalized Configuration

**Directory:** `recipe_1_19`  
**Domain:** Shop

**What it teaches:**
- `@PropertySource("classpath:shop.properties")` — loading `.properties` files
- `@Value("${discount.rate}")` — injecting property values
- Using the `Environment` abstraction to access properties programmatically

---

### Recipe 1.20 — Component Scanning

**Directories:** `recipe_1_20_i`, `recipe_1_20_ii`, `recipe_1_20_iii`  
**Domain:** Shop

**What it teaches:**
- `@Component`, `@Service`, `@Repository`, `@Controller` stereotype annotations
- `@ComponentScan("com.apress.spring6recipes.shop")` — auto-detecting beans
- Eliminating explicit `@Bean` methods for application classes

---

### Recipe 1.21 — Component Scan Filters

**Directory:** `recipe_1_21`  
**Domain:** Shop

**What it teaches:**
- `@ComponentScan` with `includeFilters` and `excludeFilters`
- Filter types: `ANNOTATION`, `ASSIGNABLE_TYPE`, `REGEX`, `ASPECTJ`
- Fine-grained control over which classes become beans

---

### Recipe 1.22 — `@Order`

**Directory:** `recipe_1_22`  
**Domain:** Shop

**What it teaches:**
- `@Order(1)` — controlling the order in which beans are injected into collections
- Use case: multiple `BeanPostProcessor` or ordered list of handlers

---

### Recipe 1.23 — Generic Bean Injection

**Directories:** `recipe_1_23_i`, `recipe_1_23_ii`  
**Domain:** Shop

**What it teaches:**
- Injecting parameterized/generic beans (e.g., `Repository<Product>`)
- Spring's ability to match generic type arguments during autowiring

---

### Recipe 1.24 — `@Lazy`

**Directories:** `recipe_1_24_i`, `recipe_1_24_ii`  
**Domain:** Shop

**What it teaches:**
- `@Lazy` — delaying bean initialization until it is first requested
- Useful for expensive beans or circular dependency breaking
- Can be combined with `@Autowired` for lazy injection points

---

### Recipe 1.25 — Application Events

**Directories:** `recipe_1_25_i`, `recipe_1_25_ii`, `recipe_1_25_iii`  
**Domain:** Shop

**What it teaches:**
- Publishing custom events by extending `ApplicationEvent`
- Listening via `@EventListener` (annotation-based)
- Listening via implementing `ApplicationListener<T>` (interface-based)
- Spring's built-in events: `ContextRefreshedEvent`, `ContextClosedEvent`

---

## Key Concepts Summary

| Concept | Recipes |
|---------|---------|
| Bean definition & `ApplicationContext` | 1.1 |
| Setter & constructor injection | 1.2, 1.3 |
| `@Autowired`, `@Qualifier`, `@Resource` | 1.4, 1.5, 1.6 |
| Java config bean wiring | 1.7 |
| Collections & inner beans | 1.8 |
| SpEL expressions | 1.9 |
| Factory methods & `FactoryBean` | 1.10 |
| Bean scopes (singleton/prototype) | 1.11 |
| Profiles | 1.12 |
| AOP (Before/After/Around/Throwing) | 1.13 |
| AOP Introductions | 1.14 |
| Lifecycle callbacks | 1.15, 1.16 |
| `BeanPostProcessor` | 1.17 |
| `BeanFactoryPostProcessor` | 1.18 |
| Externalized config (`@PropertySource`) | 1.19 |
| Component scanning | 1.20, 1.21 |
| `@Order`, generics, `@Lazy` | 1.22, 1.23, 1.24 |
| Application events | 1.25 |

---

## How to Run a Recipe

Each recipe is an independent Gradle subproject. To run `recipe_1_1_i`:

```bash
./gradlew :ch01:recipe_1_1_i:run
```

Or from within the IDE, run the `Main.java` class directly.

---

## Notes

- All recipes are in the package `com.apress.spring6recipes.*`
- The project uses **Spring 6** and **Java 17+**
- Gradle multi-project build — `settings.gradle` includes all subprojects
- The `build.gradle` in each recipe configures the `application` plugin with `mainClass`
