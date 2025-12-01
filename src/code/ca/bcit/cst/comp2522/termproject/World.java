package ca.bcit.cst.comp2522.termproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Represents a collection of countries used for the WordGame trivia questions.
 * Stores countries in a map indexed by country name for efficient lookup.
 * Provides methods to add countries, retrieve specific countries, and select
 * random countries for quiz generation. The collection is mutable and designed
 * to be populated from country data files at game initialization.
 *
 * @author Jacob
 * @version 2025-11-30
 */
public final class World
{
    private final Map<String, Country> countries;
    private final Random               randomGenerator;

    /**
     * Constructs an empty World with no countries.
     * Initializes internal data structures for storing country information.
     * Countries must be added using the addCountry method after construction.
     */
    public World()
    {
        this.countries = new HashMap<>();
        this.randomGenerator = new Random();
    }

    /**
     * Adds a country to this world collection.
     * If a country with the same name already exists, it will be replaced
     * with the new country object. Country names are case-sensitive.
     *
     * @param country the country to add to this world
     * @throws IllegalArgumentException if country is null
     */
    public void addCountry(final Country country)
    {
        if (country == null)
        {
            throw new IllegalArgumentException("Country cannot be null");
        }

        countries.put(country.getName(), country);
    }

    /**
     * Retrieves a country by its exact name.
     * Name comparison is case-sensitive.
     *
     * @param name the name of the country to retrieve
     * @return the Country object with the specified name, or null if not found
     * @throws IllegalArgumentException if name is null or empty
     */
    public Country getCountry(final String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }

        return countries.get(name);
    }

    /**
     * Returns whether this world contains a country with the specified name.
     * Name comparison is case-sensitive.
     *
     * @param name the name of the country to check
     * @return true if a country with this name exists, false otherwise
     * @throws IllegalArgumentException if name is null or empty
     */
    public boolean hasCountry(final String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }

        return countries.containsKey(name);
    }

    /**
     * Returns the total number of countries in this world.
     *
     * @return the count of countries currently stored
     */
    public int getCountryCount()
    {
        return countries.size();
    }

    /**
     * Returns whether this world contains any countries.
     *
     * @return true if there are no countries in this world, false otherwise
     */
    public boolean isEmpty()
    {
        return countries.isEmpty();
    }

    /**
     * Returns a list of all country names in this world.
     * The order of names is not guaranteed. Modifying the returned list
     * will not affect the internal world data.
     *
     * @return a new list containing all country names
     */
    public List<String> getAllCountryNames()
    {
        final Set<String>  nameSet;
        final List<String> nameList;

        nameSet = countries.keySet();
        nameList = new ArrayList<>(nameSet);

        return nameList;
    }

    /**
     * Returns a list of all countries in this world.
     * The order of countries is not guaranteed. Modifying the returned list
     * will not affect the internal world data.
     *
     * @return a new list containing all Country objects
     */
    public List<Country> getAllCountries()
    {
        final List<Country> countryList;
        countryList = new ArrayList<>(countries.values());

        return countryList;
    }

    /**
     * Returns a randomly selected country from this world.
     * Each country has an equal probability of being selected.
     * Uses the internal random number generator for selection.
     *
     * @return a randomly selected Country object
     * @throws IllegalStateException if this world contains no countries
     */
    public Country getRandomCountry()
    {
        if (isEmpty())
        {
            throw new IllegalStateException("Cannot get random country from empty world");
        }

        final List<Country> allCountries;
        final int           randomIndex;
        final Country       randomCountry;

        allCountries = getAllCountries();
        randomIndex = randomGenerator.nextInt(allCountries.size());
        randomCountry = allCountries.get(randomIndex);

        return randomCountry;
    }

    /**
     * Removes all countries from this world.
     * After calling this method, the world will be empty.
     */
    public void clear()
    {
        countries.clear();
    }

    /**
     * Returns a string representation of this world.
     * Format: "World{countries=N}" where N is the number of countries.
     *
     * @return a string representation of this world
     */
    @Override
    public String toString()
    {
        return "World{countries=" + countries.size() + "}";
    }
}
