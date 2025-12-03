# SpringBoot Governance Starter

## Overview

This repository is a SpringBoot starter that focuses on service governance solutions. It provides a collection of features to simplify service management and enhance system reliability. The key features include:

1. **Whitelist Enforcement**: A module to implement whitelist-based request filtering.
2. **Service Degradation with Hystrix**: Enables service circuit breaking and graceful fallback using Hystrix.
3. **Rate Limiting with RateLimiter**: Provides request throttling to control service traffic.
4. **Method Extension with Annotations**: Execute additional custom logic before the actual method execution via annotations.

Each directory in this repository corresponds to an independent project. The **`governance`** project consolidates all the starters into a unified package, whereas the **`governance-test`** project can be used to test the functionality of each feature in isolation.

---

## Class Diagram
![Class Diagram](https://github.com/ZeyuZhao-git/middlewares/blob/main/resources/class_diagram.png)

## How to Use

### Step 1. Build and Install Locally
First, package and install the module(s) you need into your local Maven repository by running:

```bash
mvn clean install
```

### Step 2. Add Starter Dependencies to Test Project
Include the starter dependencies in the `pom.xml` of the test project. For example:

```xml
<dependency>
    <groupId>cn.zeke.middleware</groupId>
    <artifactId>governance</artifactId>
    <version>version</version>
</dependency>
```

### Step 3. Configure Application Properties (Optional)
If you want to enable the whitelist feature, configure the **`application.yml`** file in the test project to specify your allowed IP addresses:

```yaml
zeke:
  whitelist: "userId1, userId2"
```

If you don't intend to use the whitelist module, this configuration step is not required.

---

## Features and Modules

### 1. Whitelist Filtering
The whitelist module enables request filtering based on predefined field. Use the configuration in `application.yml` to specify allowed value for your service.

Example:

```java
@WhitelistFilter
public void someCriticalService() {
    // Your method logic
}
```

---

### 2. Hystrix-based Service Degradation
With the Hystrix-based module, you can set up circuit breaking and fallback mechanisms to ensure high availability and fault isolation for your services.

- **How it works**: Annotate the methods that require circuit-breaking with the `@DoHystrix` annotation.
  
Example:

```java
@DoHystrix 
public void someCriticalService() {
    // Your method logic
}
```

Through this mechanism, service failures are properly handled without impacting the entire system.

---

### 3. Rate Limiting
The RateLimiter-based module helps you implement request throttling to manage traffic flows effectively, ensuring system reliability during high-demand periods.
```java
@DoRateLimiter
public void someMethod() {
    // Main method logic
}
```
---

### 4. Method Extension with Annotations
Extend your method logic effortlessly using custom annotations. You can define additional pre-processing logic that executes *before* the main method. For example:

```java
@DoMethodExt
public void someMethod() {
    // Main method logic
}
```
This allows flexible extension of method behaviors through reusable, annotation-based configurations.

---

## Testing Your Governance Setup

1. Use the **`governance-test`** project in this repository to simulate realistic scenarios.
2. Configure the test environments as described above.
3. Verify the governance features (e.g., whitelist filtering, Hystrix behavior, rate limiting, etc.) through functional testing.

---

## Conclusion

With this SpringBoot starter, you can easily integrate robust service governance mechanisms into your projects, including features like whitelisting, circuit breaking, rate limiting, and method extensions. Follow the setup steps above to get started and empower your services with efficient governance tools!

Enjoy seamless and reliable service governance! 🎉

--- 

Feel free to fork the repository and contribute to improve this governance starter further. 😊
