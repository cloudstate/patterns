# Patterns
A demonstrator of different styles of coding patterns


### Condition
The Condition pattern demonstrates one approach to implementing complex boolean logic in a readable and maintainable way.

#### Example domain
We want to model how customers are allowed to enter a bar by defining a set of **Bar Rules**.

Customers is allowed to enter if all the following conditions are met:

1. The bar is open.
1. The customers are reasonably sober.
1. The customers meets the age requirements:
   - The legal age is **20 years or older**.
   - Late teens **(18–19 years old)** may enter only if accompanied by an adult.

An **adult** is defined as someone who is **35 years or older**.

#### Java

```java
public record Customer(int age, boolean sober) { }

public record Bar(boolean open) { }
```
> Note the lack of supporting methods in Customer. This will make the EnterBarRules class more bloated.


How to create an instance of the EnterBarRules class
```java
public class EnterBarRules extends AbstractRules<EnterBarRules> {

    public static EnterBarRules of(final Customer customer, final Bar bar) { ... }

    public static EnterBarRules of(final Customer customer, final Customer company, final Bar bar) { ... }

    ... 
   
}
```
<br/>

#### This is how to use the bar rules:
```java
var badBoyBozze = new Customer(24, false);
var wingmanBengt = new Customer(24, true);

var spyBar = new Bar(true);

var rules = EnterBarRules.of(badBoyBozze, wingmanBengt, spyBar);

// Boolean usage
if (rules.evaluate()) { ... }

// Execute a Runnable if the rules evaluates to true
rules.isTrue(() -> IO.println("Welcome"))

// ... or else
rules.isTrue(() -> IO.println("Welcome"))
        .orElse(() -> IO.println("Sorry, try another bar"));
```

#### The implementation details of EnterBarRules
The most important method in this class is where you will find the encoded requirements

```java
protected Condition conditions() {
    return barIsOpen()
            .and(allAreSober())
            .and(ofLegalAge().or(lateTeenWithAdultCompany()));
}
```

The supporting methods
```java
Condition barIsOpen() {
    return condition(bar::open);
}

Condition allAreSober() {
    return condition(() -> isSober(customer) && isSober(company));
}

Condition ofLegalAge() {
    return condition(() -> isLegalAge(customer) && isLegalAge(company));
}

Condition lateTeenWithAdultCompany() {
    return condition(() -> isLateTeen(customer) && isAdult(company) || isAdult(customer) && isLateTeen(company));
}
```



