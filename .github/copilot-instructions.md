# GitHub Copilot Instructions - COMP2522 Term Project

## � CRITICAL: STYLE IS WORTH 51% OF YOUR GRADE

This is a COMP2522 academic project with **extremely strict** coding standards. Comments alone are worth 51% of the grade. Every single style rule must be followed precisely.

**Project**: Three Java games (WordGame, NumberGame, MyGame)
**Package**: `ca.bcit.cst.comp2522.termproject`
**Constraint**: Only use Java techniques taught in COMP2522 course

---

## Complete COMP2522 Style Rules

### 1. JavaDoc Comments Are Critical 

Each method must have enough JavaDoc comments that another developer could build the method just by reading them. Some methods may need *pages* of comments.

```
 RIGHT:
/**
 * Calculates the final price including tax and applies discount if applicable.
 * The discount is only applied if the customer has purchased more than the
 * minimum required quantity. Tax is calculated after the discount is applied.
 *
 * @param basePriceUsd the original price before any calculations
 * @param quantityItems the number of items purchased
 * @param discountRate the discount rate as a decimal (e.g., 0.15 for 15%)
 * @param taxRate the tax rate as a decimal (e.g., 0.12 for 12%)
 * @return the final price after discount and tax
 */
private double calculateFinalPriceUsd(final double basePriceUsd,
                                       final int quantityItems,
                                       final double discountRate,
                                       final double taxRate)

L WRONG:
// calculates price
private double calcPrice(double price, int qty, double disc, double tax)
```

### 2. Declare, Then Initialize 

Group declarations separately from initializations.

```
 RIGHT:
final int priceUsd;
final int quantityItems;
final String productName;
final List<String> customerNames;

priceUsd = 100;
quantityItems = 5;
productName = "Widget";
customerNames = new ArrayList<>();

System.out.println(productName + " costs " + priceUsd);

L WRONG:
final int priceUsd = 100;
final int quantityItems = 5;
final String productName = "Widget";
final List<String> customerNames = new ArrayList<>();
```

### 3. All Parameters Must Be `final` 

```
 RIGHT:
private String getFullName(final String firstName, final String lastName)
{
    return firstName + " " + lastName;
}

L WRONG:
private String getFullName(String firstName, String lastName)
{
    return firstName + " " + lastName;
}
```

### 4. Use Only What We've Learned 

Use only techniques taught in COMP2522 class. Homework tests your skill with what we've covered, nothing more.

- No streams unless explicitly taught
- No lambdas unless explicitly taught
- No advanced Java features not covered in course

### 5. Explicit Units Always 

Variable names should include their units.

```
 RIGHT:
final int priceUsd;
final double weightKg;
final String datePublished;
final int durationSeconds;
final double temperatureCelsius;

L WRONG:
final int price;
final double weight;
final String date;
final int duration;
final double temperature;
```

### 6. No Magic Numbers (except loop counters) 

Use constants instead of hard-coded values.

```
 RIGHT:
private final double TAX_RATE = 0.10;
private final int PERCENT_CONVERSION = 100;
private final double BASE_MULTIPLIER = 1.0;
private final double TAX_MULTIPLIER = BASE_MULTIPLIER + TAX_RATE;

System.out.println("Price has " + (TAX_RATE * PERCENT_CONVERSION) + "% tax");

L WRONG:
System.out.println("Price has " + (0.10 * 100) + "% tax");
if (count > 50)  // What does 50 mean?
```

Loop counters are the exception:
```
 ALLOWED:
for (int i = 0; i < 10; i++)
```

### 7. Verbs Are for Methods, Not Variables 

Boolean variable names should not be verbs.

```
 RIGHT:
private final boolean happy;
private final boolean valid;
private final boolean ready;

if (ready)
{
    start();
}

L WRONG:
private final boolean isHappy;
private final boolean isValid;
private final boolean isReady;
```

### 8. Use Proper Packages 

Always use a lowercase, reverse domain, meaningful, and unique package name.

```
 RIGHT:
package ca.bcit.cst.comp2522.termproject;

L WRONG:
package WhoCaresNotME;
package myproject;
package Project;
```

### 9. JavaDoc All Public Elements 

All non-private classes, constructors, and methods must have JavaDoc comments.

```
 RIGHT:
/**
 * Represents a country with its capital and interesting facts.
 *
 * @author Your Name
 * @version 1.0
 */
public class Country
{
    /**
     * Constructs a Country with the specified details.
     *
     * @param countryName the name of the country
     * @param capitalCityName the name of the capital city
     * @param facts an array of interesting facts about the country
     */
    public Country(final String countryName,
                   final String capitalCityName,
                   final String[] facts)
    {
        // implementation
    }
}
```

### 10. Instance Variables Must Be: 

- `private`
- `final` unless they absolutely need to change

```
 RIGHT:
public class Score
{
    private final String dateTimePlayed;
    private final int numGamesPlayed;
    private int currentScore;  // OK: needs to change
}

L WRONG:
public class Score
{
    public String dateTimePlayed;  // Not private!
    String numGamesPlayed;  // Not private!
}
```

### 11. Class-Level JavaDoc Required 

Each class needs a JavaDoc with a full sentence description, `@author`, and `@version`.

```
 RIGHT:
/**
 * Manages the word game which tests players on geography trivia.
 * Loads country data from text files and presents random questions.
 *
 * @author John Doe
 * @version 1.0
 */
public class WordGame
{
}

L WRONG:
// Word game class
public class WordGame
{
}
```

### 12. One Method = One Action 

Split methods that do more than one thing.

```
 RIGHT:
private double calculateDiscountUsd(final double priceUsd,
                                     final double discountRate)
{
    // only calculates discount
}

private void printReceipt(final double totalUsd)
{
    // only prints receipt
}

L WRONG:
private double calculateDiscountAndPrintReceipt(final double priceUsd,
                                                 final double discountRate)
{
    // does TWO things!
}
```

### 13. Method Naming = camelCase Verbs 

Use verbs and no abbreviations for method names.

```
 RIGHT:
getTotalUsd()
calculateFinalPrice()
validateUserInput()
loadCountryData()

L WRONG:
doStuff()
getTot()
calc()
validate()  // OK for simple cases
```

### 14. Clear Variable Names 

Be descriptive with variable names.

```
 RIGHT:
final String emailSubject;
final double weightKg;
final int customerAgeBirthYears;
final String fullName;

L WRONG:
final String temp;
final double val;
final int x;
final String s;
```

### 15. One Class = One Responsibility 

Each class should have a single, well-defined purpose.

```
 RIGHT:
InvoiceCalculator  // calculates invoices
TaxHelper  // helps with tax calculations
EmailSender  // sends emails
Country  // represents a country

L WRONG:
Utils  // does everything!
Helper  // too vague
Manager  // what does it manage?
```

### 16. No Abbreviations or Slang 

Use full words in names.

```
 RIGHT:
final int quantityKg;
final String emailAddress;
final double discountRate;
final int numberAttempts;

L WRONG:
final int qty;
final String addr;
final double disc;
final int num;
final int cnt;
```

### 17. Constants Are Capitalized, Unit-Specific 

Constant names should be in `UPPER_SNAKE_CASE` and include units.

```
 RIGHT:
private static final int MAX_USERNAME_LENGTH_CHARS = 30;
private static final double TAX_RATE_PERCENT = 12.5;
private static final int TIMEOUT_SECONDS = 60;
private static final String DEFAULT_CURRENCY_CODE = "USD";

L WRONG:
private static final int MAX_LENGTH = 30;  // missing unit
private static final double taxRate = 12.5;  // not uppercase
private static final int TIMEOUT = 60;  // missing unit
```

### 18. No `System.out.print` in Final Code 

Use return values or logging instead of print statements. They're only for debugging.

```
 RIGHT (for debugging only):
System.out.println("DEBUG: price = " + priceUsd);

 RIGHT (for final code):
return resultString;  // return instead of printing

L WRONG (in final submission):
System.out.println("The price is " + price);  // Not for production!
```

### 19. Always Use Braces 

Even for one-line conditions.

```
 RIGHT:
if (happy)
{
    doThing();
}

for (int i = 0; i < 10; i++)
{
    process(i);
}

L WRONG:
if (happy)
    doThing();

if (happy) doThing();
```

### 20. File Name = Public Class Name 

The `.java` file name must match the public class name inside it.

```
 RIGHT:
// File: Country.java
public class Country { }

L WRONG:
// File: MyClass.java
public class Country { }
```

### 21. `final` Applies to Everything 

- Make methods `final` if they shouldn't be overridden
- Make classes `final` if they shouldn't be extended
- Use `final` for for-each and `catch` variables

```
 RIGHT:
for (final String name : names)
{
    process(name);
}

try
{
    doSomething();
}
catch (final IOException e)
{
    handleError(e);
}

L WRONG:
for (String name : names)  // missing final
{
    process(name);
}

catch (IOException e)  // missing final
{
    handleError(e);
}
```

### 22. Always Code to the Interface 

Never declare collections using concrete classes like `ArrayList`, `HashMap`, etc. Declare as an interface (`List` or `Map`), then initialize as the concrete class.

```
 RIGHT:
// Declaring
private final List<String> names;
private final Map<String, Integer> scores;

// Initializing
names = new ArrayList<>();
scores = new HashMap<>();

L WRONG:
private final ArrayList<String> names = new ArrayList<>();
private final HashMap<String, Integer> scores = new HashMap<>();
```

---

## Common Patterns (Copy-Paste Ready)

### Pattern 1: Proper Variable Declaration

```
// Declaring (all final unless they need to change)
final int basePriceUsd;
final double taxRate;
final String customerName;
final List<String> orderItems;
final Map<String, Double> pricesUsd;

// Initializing
basePriceUsd = 100;
taxRate = 0.12;
customerName = "John Doe";
orderItems = new ArrayList<>();
pricesUsd = new HashMap<>();

// Using
final double totalUsd;
totalUsd = basePriceUsd * (1.0 + taxRate);
System.out.println(customerName + " owes " + totalUsd);
```

### Pattern 2: Proper Method Signature

```
/**
 * Calculates the total price including tax.
 * The tax is applied to the base amount using the provided rate.
 * Tax rate should be expressed as a decimal (e.g., 0.10 for 10%).
 *
 * @param baseAmountUsd the base price before tax in US dollars
 * @param taxRate the tax rate as a decimal between 0.0 and 1.0
 * @return the total price including tax in US dollars
 * @throws IllegalArgumentException if taxRate is negative or greater than 1.0
 */
private double calculateTotalPriceUsd(final double baseAmountUsd,
                                       final double taxRate)
{
    final double MIN_TAX_RATE = 0.0;
    final double MAX_TAX_RATE = 1.0;
    final double BASE_MULTIPLIER = 1.0;
    final double taxMultiplier;
    final double totalUsd;

    if (taxRate < MIN_TAX_RATE || taxRate > MAX_TAX_RATE)
    {
        throw new IllegalArgumentException("Tax rate must be between 0 and 1");
    }

    taxMultiplier = BASE_MULTIPLIER + taxRate;
    totalUsd = baseAmountUsd * taxMultiplier;

    return totalUsd;
}
```

### Pattern 3: Proper Loop with final

```
// For-each loop
final List<String> emailAddresses;
emailAddresses = getEmailAddresses();

for (final String emailAddress : emailAddresses)
{
    sendEmail(emailAddress);
}

// Traditional for loop
final int MAX_COUNT = 10;
for (int i = 0; i < MAX_COUNT; i++)
{
    processItem(i);
}
```

### Pattern 4: Proper Exception Handling

```
final String fileName;
final Scanner scanner;

fileName = "data.txt";

try
{
    scanner = new Scanner(new File(fileName));
    processFile(scanner);
    scanner.close();
}
catch (final FileNotFoundException e)
{
    System.err.println("File not found: " + fileName);
    handleError(e);
}
catch (final IOException e)
{
    System.err.println("Error reading file: " + fileName);
    handleError(e);
}
```

### Pattern 5: Proper Collections Declaration

```
// Declaring (code to interface)
private final List<Country> countries;
private final Map<String, Score> scoresById;
private final Set<String> uniqueNames;

// Constructor or method initialization
countries = new ArrayList<>();
scoresById = new HashMap<>();
uniqueNames = new HashSet<>();

// Adding elements
countries.add(newCountry);
scoresById.put("player1", playerScore);
uniqueNames.add("Canada");
```

### Pattern 6: Proper Class Structure

```
package ca.bcit.cst.comp2522.termproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a country with its capital city and interesting facts.
 * This class stores geography information for the word game trivia.
 *
 * @author Your Name
 * @version 1.0
 */
public class Country
{
    private final String countryName;
    private final String capitalCityName;
    private final String[] facts;  // Use array, not ArrayList, per spec

    /**
     * Constructs a Country with the specified information.
     *
     * @param countryName the name of the country (e.g., "Canada")
     * @param capitalCityName the name of the capital city (e.g., "Ottawa")
     * @param facts an array of exactly 3 interesting facts about the country
     * @throws IllegalArgumentException if facts array doesn't contain exactly 3 elements
     */
    public Country(final String countryName,
                   final String capitalCityName,
                   final String[] facts)
    {
        final int REQUIRED_FACTS_COUNT = 3;

        if (facts.length != REQUIRED_FACTS_COUNT)
        {
            throw new IllegalArgumentException("Must provide exactly 3 facts");
        }

        this.countryName = countryName;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    /**
     * Gets the name of the country.
     *
     * @return the country name
     */
    public String getCountryName()
    {
        return countryName;
    }

    /**
     * Gets the name of the capital city.
     *
     * @return the capital city name
     */
    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Gets a specific fact about the country.
     *
     * @param index the index of the fact (0, 1, or 2)
     * @return the fact at the specified index
     * @throws ArrayIndexOutOfBoundsException if index is not 0, 1, or 2
     */
    public String getFact(final int index)
    {
        return facts[index];
    }
}
```

---

## Project-Specific Requirements

### WordGame Classes

**Country.java**
- Instance variables: `String countryName`, `String capitalCityName`, `String[] facts`
- Use **array** (not ArrayList) for facts
- Exactly 3 facts per country

**World.java**
- Instance variable: `Map<String, Country>` (HashMap implementation)
- Key: country name (String)
- Value: Country object
- Load data from `countries/a.txt` through `countries/z.txt`

**Score.java**
- Instance variables:
  - `String dateTimePlayed` (format: "yyyy-MM-dd HH:mm:ss")
  - `int numGamesPlayed`
  - `int numCorrectFirstAttempt`
  - `int numCorrectSecondAttempt`
  - `int numIncorrectTwoAttempts`
- Must pass ScoreTest.java unit tests
- Calculate points: first attempt = 2 points, second attempt = 1 point, incorrect = 0 points

**DateTime handling:**
```
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

final LocalDateTime currentTime;
final DateTimeFormatter formatter;
final String formattedDateTime;

currentTime = LocalDateTime.now();
formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
formattedDateTime = currentTime.format(formatter);
```

**Country file format** (`countries/*.txt`):
```
CountryName:CapitalCity
Fact line 1
Fact line 2
Fact line 3
<blank line>
NextCountry:NextCapital
...
```

**score.txt format:**
```
Date and Time: 2024-10-10 11:10:26
Games Played: 1
Correct First Attempts: 6
Correct Second Attempts: 2
Incorrect Attempts: 2
Total Score: 14 points
```

### NumberGame Requirements

- **GUI**: Use Swing (JFrame, JButton, JPanel, etc.)
- **Grid**: 4 rows � 5 columns = 20 buttons
- **OOP Requirements**:
  - Create at least 1 interface
  - Create at least 1 abstract class
  - Create at least 1 concrete class
- **Arrays**: Must use arrays (not ArrayList) for the game grid
- **Numbers**: Generate random integers from 1-1000 (inclusive)

### MyGame Requirements

- Must demonstrate concepts from **every course lesson**
- Create `applications.txt` mapping each lesson to specific code locations
- Must be unique (not findable online as-is)
- Style must match COMP2522 standards

---

## Forbidden Patterns L

### DO NOT write code like this:

```
L void foo(String name)  // parameters not final
L int x = 5;  // declare-and-initialize
L ArrayList<String> names;  // concrete type, not interface
L if (count > 100)  // magic number
L int qty;  // abbreviation
L boolean isHappy;  // verb in boolean name
L int price;  // missing unit
L System.out.println("result");  // in final code
L if (x) doThing();  // missing braces
L for (String s : list)  // missing final
L private ArrayList<String> list = new ArrayList<>();  // all wrong!
```

---

## Pre-Code Generation Checklist

Before generating any code, verify:

- [ ] Am I using only techniques taught in COMP2522?
- [ ] Are **all** parameters marked `final`?
- [ ] Did I declare variables first, then initialize separately?
- [ ] Am I coding to interfaces (`List` not `ArrayList`, `Map` not `HashMap`)?
- [ ] Do **all** variable names include units (`priceUsd`, `weightKg`)?
- [ ] Are constants in `UPPER_SNAKE_CASE` with units?
- [ ] Do I have **comprehensive** JavaDoc comments (detailed enough to rebuild the method)?
- [ ] Are **all** loop variables marked `final` (`for (final String item : items)`)?
- [ ] Did I avoid **all** abbreviations (`quantity` not `qty`)?
- [ ] Did I use braces for **all** conditionals and loops?
- [ ] Are boolean variables nouns, not verbs (`happy` not `isHappy`)?
- [ ] Did I avoid magic numbers (except loop counters)?

---

## Build and Run Commands

```bash
# Compile all Java files
javac -d out src/code/ca/bcit/cst/comp2522/termproject/*.java

# Run the main program
java -cp out ca.bcit.cst.comp2522.termproject.Main

# Run with assertions enabled (for testing)
java -ea -cp out ca.bcit.cst.comp2522.termproject.Main
```

---

## Remember

**STYLE IS WORTH 51% OF YOUR GRADE**

Every single rule matters. When in doubt:
1. Check this file
2. Check `COMP2522_Styles_Conventions.md`
3. Ask the user before breaking any rule
