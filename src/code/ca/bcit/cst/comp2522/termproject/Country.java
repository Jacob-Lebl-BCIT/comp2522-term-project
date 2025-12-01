package ca.bcit.cst.comp2522.termproject;

/**
 * Represents a country with its name, capital city, and three interesting facts.
 * This class encapsulates geographic and cultural data used by the WordGame
 * to generate trivia questions. Each country must have exactly three facts.
 * Instances are immutable after construction.
 *
 * @author Jacob
 * @version 1.0
 */
public final class Country
{
    private static final int REQUIRED_NUMBER_OF_FACTS = 3;

    private final String   name;
    private final String   capital;
    private final String[] facts;

    /**
     * Constructs a Country with the specified name, capital, and facts.
     * Creates a defensive copy of the facts array to ensure immutability.
     * All parameters must be non-null, and facts array must contain exactly
     * three non-null elements.
     *
     * @param name    the name of the country (e.g., "Canada")
     * @param capital the capital city of the country (e.g., "Ottawa")
     * @param facts   array of exactly three interesting facts about the country
     * @throws IllegalArgumentException if name is null or empty,
     *                                  capital is null or empty,
     *                                  facts is null,
     *                                  facts array length is not 3,
     *                                  or any fact is null or empty
     */
    public Country(final String name, final String capital, final String[] facts)
    {
        validateName(name);
        validateCapital(capital);
        validateFacts(facts);

        this.name = name;
        this.capital = capital;
        this.facts = new String[REQUIRED_NUMBER_OF_FACTS];

        for (int i = 0; i < REQUIRED_NUMBER_OF_FACTS; i++)
        {
            this.facts[i] = facts[i];
        }
    }

    /**
     * Returns the name of this country.
     *
     * @return the country name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the capital city of this country.
     *
     * @return the capital city name
     */
    public String getCapital()
    {
        return capital;
    }

    /**
     * Returns a defensive copy of the facts array for this country.
     * Modifications to the returned array will not affect the original.
     *
     * @return a new array containing the three facts about this country
     */
    public String[] getFacts()
    {
        final String[] factsCopy;
        factsCopy = new String[REQUIRED_NUMBER_OF_FACTS];

        for (int i = 0; i < REQUIRED_NUMBER_OF_FACTS; i++)
        {
            factsCopy[i] = facts[i];
        }

        return factsCopy;
    }

    /**
     * Returns a specific fact about this country by index.
     * Valid indices are 0, 1, and 2 corresponding to the three facts.
     *
     * @param index the zero-based index of the fact to retrieve (0-2)
     * @return the fact at the specified index
     * @throws IndexOutOfBoundsException if index is not 0, 1, or 2
     */
    public String getFact(final int index)
    {
        if (index < 0 || index >= REQUIRED_NUMBER_OF_FACTS)
        {
            throw new IndexOutOfBoundsException(
                "Fact index must be between 0 and " +
                (REQUIRED_NUMBER_OF_FACTS - 1) +
                ", but was: " + index
            );
        }

        return facts[index];
    }

    /**
     * Validates that the country name is not null or empty.
     *
     * @param name the country name to validate
     * @throws IllegalArgumentException if name is null or empty
     */
    private static void validateName(final String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }
    }

    /**
     * Validates that the capital city name is not null or empty.
     *
     * @param capital the capital city name to validate
     * @throws IllegalArgumentException if capital is null or empty
     */
    private static void validateCapital(final String capital)
    {
        if (capital == null || capital.trim().isEmpty())
        {
            throw new IllegalArgumentException("Capital cannot be null or empty");
        }
    }

    /**
     * Validates that the facts array is not null, has exactly three elements,
     * and each element is not null or empty.
     *
     * @param facts the facts array to validate
     * @throws IllegalArgumentException if facts is null,
     *                                  facts array length is not 3,
     *                                  or any fact is null or empty
     */
    private static void validateFacts(final String[] facts)
    {
        if (facts == null)
        {
            throw new IllegalArgumentException("Facts array cannot be null");
        }

        if (facts.length != REQUIRED_NUMBER_OF_FACTS)
        {
            throw new IllegalArgumentException(
                "Facts array must contain exactly " +
                REQUIRED_NUMBER_OF_FACTS +
                " elements, but contained: " + facts.length
            );
        }

        for (int i = 0; i < REQUIRED_NUMBER_OF_FACTS; i++)
        {
            if (facts[i] == null || facts[i].trim().isEmpty())
            {
                throw new IllegalArgumentException(
                    "Fact at index " + i + " cannot be null or empty"
                );
            }
        }
    }

    /**
     * Returns a string representation of this country.
     * Format: "Country{name='CountryName', capital='CapitalName'}"
     * Facts are not included in the string representation for brevity.
     *
     * @return a string representation of this country
     */
    @Override
    public String toString()
    {
        return "Country{name='" + name + "', capital='" + capital + "'}";
    }

    /**
     * Compares this country to another object for equality.
     * Two countries are considered equal if they have the same name (case-sensitive).
     * Capital and facts are not considered in equality comparison.
     *
     * @param obj the object to compare with this country
     * @return true if obj is a Country with the same name, false otherwise
     */
    @Override
    public boolean equals(final Object obj)
    {
        final boolean isEqual;

        if (this == obj)
        {
            isEqual = true;
        }
        else if (obj == null || getClass() != obj.getClass())
        {
            isEqual = false;
        }
        else
        {
            final Country other;
            other = (Country) obj;
            isEqual = name.equals(other.name);
        }

        return isEqual;
    }

    /**
     * Returns a hash code for this country based on its name.
     * Consistent with equals(), only the name is used for hashing.
     *
     * @return a hash code value for this country
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
