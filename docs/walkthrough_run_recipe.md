# Running Recipe 2_9_iii and Standalone Recipes Analysis Walkthrough

This document records the changes made and analysis performed during the session to compile, run, and identify runnable recipes.

## 1. Compiling and Fixing Recipe 2_9_iii
When attempting to build `ch02:recipe_2_9_iii`, compilation failed due to a missing symbol `cnm` in `InterceptorConfiguration.java`:
```
C:\Users\sahuk\IdeaProjects\spring-6-recipes\ch02\recipe_2_9_iii\src\main\java\com\apress\spring6recipes\court\config\InterceptorConfiguration.java:26: error: cannot find symbol
		return new ExtensionInterceptor(cnm);
		                                ^
  symbol:   variable cnm
  location: class InterceptorConfiguration
```

### Fix Implemented
1. Inspected [ExtensionInterceptor.java](file:///c:/Users/sahuk/IdeaProjects/spring-6-recipes/ch02/recipe_2_shared/src/main/java/com/apress/spring6recipes/court/web/ExtensionInterceptor.java) and verified that the class does not define a constructor with arguments and does not use `cnm` in its logic.
2. Modified [InterceptorConfiguration.java](file:///c:/Users/sahuk/IdeaProjects/spring-6-recipes/ch02/recipe_2_9_iii/src/main/java/com/apress/spring6recipes/court/config/InterceptorConfiguration.java) to use the default no-argument constructor:
   ```diff
   -		return new ExtensionInterceptor(cnm);
   +		return new ExtensionInterceptor();
   ```
3. Re-ran the build, which completed successfully:
   ```cmd
   ./gradlew :ch02:recipe_2_9_iii:build
   ```

---

## 2. Running the Web Application without Docker
Since Docker was not running on the local system, we added the **Gretty plugin** (configured with Tomcat 10) to run the Spring MVC web application locally inside the JVM:

1. Added Gretty 4.0.3 to the root [build.gradle](file:///c:/Users/sahuk/IdeaProjects/spring-6-recipes/build.gradle) plugins block.
2. Configured and applied Gretty to Chapter 2 web subprojects in [ch02/build.gradle](file:///c:/Users/sahuk/IdeaProjects/spring-6-recipes/ch02/build.gradle):
   ```groovy
   apply plugin: 'org.gretty'
   gretty {
       servletContainer = 'tomcat10'
       contextPath = '/'
   }
   ```
3. Started the web application with the built WAR:
   ```cmd
   ./gradlew :ch02:recipe_2_9_iii:appStartWar
   ```
   The embedded Tomcat 10 server started and successfully listened on `http://localhost:8080`.

---

## 3. Analysis of Standalone vs Infrastructure-Dependent Recipes
We analyzed the repository configuration and `build.gradle` dependencies across all chapters to classify which recipes can be run locally out-of-the-box, and which require external services:

### A. Recipes Runnable Locally (Zero External Dependencies)
These recipes run standalone using pure Spring, in-memory structures, or in-memory H2 databases:
*   **Chapter 1 (Spring Core):** All recipes (run as standalone Java console apps).
*   **Chapter 8 (Spring Batch):** All recipes (configured to use in-memory/file-based H2 database for batch metadata).
*   **Chapter 14 (Caching):** Recipes `14_1_i` through `14_6_i` (use in-memory Caffeine cache or H2; excluding Redis-based `recipe_14_7_i`).
*   **Chapters 2 & 3 (Spring MVC / REST):** All recipes (run locally using an embedded Tomcat server via Gretty).
*   **Chapter 4 (Spring Webflux):** Reactor Netty server recipes.
*   **Chapter 5 (Spring Security):** Recipes using in-memory configurations or H2 (excluding LDAP `5_3_v`).
*   **Chapter 13 (Spring Testing):** Recipes `13_1_i` through `13_7_i` (excluding Testcontainers `13_8_i`/`ii`).

### B. Chapters Requiring External Infrastructure
These recipes cannot run out-of-the-box without configuring or starting external databases, servers, or brokers:
*   **Chapter 6 (Spring Data Access) & Chapter 7 (Spring Transaction Management):** Require a running **PostgreSQL** database instance.
*   **Chapter 9 (NoSQL Data Access):** Requires specific external NoSQL database servers depending on the recipe:
    *   `recipe_9_1_*`: **MongoDB**
    *   `recipe_9_2_*`: **Redis**
    *   `recipe_9_3_*`: **Neo4j**
    *   `recipe_9_4_*`: **Couchbase**
*   **Chapter 11 (Spring Messaging):** Requires external message brokers: **ActiveMQ Artemis**, **RabbitMQ**, or **Kafka**.
*   **Chapter 10 (Enterprise Integration) & Chapter 12 (Spring Integration):** Many recipes require external SMTP (mail) servers, JMS brokers, or active integration channels.
*   **Chapter 5 (Spring Security):** `recipe_5_3_v` requires an active **LDAP** directory server.
*   **Chapter 13 (Spring Testing):** `recipe_13_8_i`/`ii` require **Docker** to run database tests via Testcontainers.
*   **Chapter 14 (Caching):** `recipe_14_7_i` requires a running **Redis** cache server.

---

## 4. Commands to Run Standalone and Web Recipes

### A. Standalone Console Recipes (Chapters 1, 8, 14)
Since the project is configured with a Java 19 toolchain (class version 63.0), you must execute these applications using a JDK 19+ runtime (e.g., your local OpenJDK 23 or Gradle's auto-provisioned JDK 19).

1.  **Build the target recipe:**
    ```cmd
    ./gradlew :<chapter>:<recipe_name>:build
    ```
    *(e.g., `./gradlew :ch01:recipe_1_1_i:build`)*
    
2.  **Execute the shadow jar with a JDK 19+ runtime:**
    ```cmd
    & "C:\Users\sahuk\.jdks\openjdk-23\bin\java.exe" -cp <chapter>/<recipe_name>/build/libs/<recipe_name>-6.0.0-SNAPSHOT-all.jar <MainClass>
    ```
    *(e.g., `& "C:\Users\sahuk\.jdks\openjdk-23\bin\java.exe" -cp ch01/recipe_1_1_i/build/libs/recipe_1_1_i-6.0.0-SNAPSHOT-all.jar com.apress.spring6recipes.sequence.Main`)*

### B. Web Recipes (Chapters 2, 3)
Run these web recipes via the embedded Tomcat 10 container using Gretty:

1.  **Start the web application:**
    ```cmd
    ./gradlew :<chapter>:<recipe_name>:appStartWar
    ```
    *(e.g., `./gradlew :ch02:recipe_2_9_iii:appStartWar`)*
    
2.  **Access the application** at `http://localhost:8080` (e.g., `http://localhost:8080/welcome`).
3.  **Stop the server** once finished:
    ```cmd
    ./gradlew :<chapter>:<recipe_name>:appStop
    ```


