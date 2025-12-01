# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

COMP2522 Term Project: Three Java games (WordGame, NumberGame, MyGame) built as a terminal-based menu system. This is an academic project with **strict coding style requirements** worth 51% of the grade.

**Package**: `ca.bcit.cst.comp2522.termproject`

## Critical Style Requirements

This project follows **extremely strict** Java coding standards documented in `COMP2522_Styles_Conventions.md`. **Before making ANY code changes**, you MUST:

1. Ask the user for permission if you need to break any style rule
2. Reference the style guide constantly - comments are worth 51% of the grade
3. Only use techniques taught in the course (no advanced Java features)

### Non-Negotiable Rules:
- All parameters MUST be `final`
- Variables: declare first, initialize separately (no `int x = 5;`)
- Code to interfaces: `List<String> names; names = new ArrayList<>();` (NOT `ArrayList<String>`)
- No magic numbers except loop counters
- Variable names include units: `priceUsd`, `weightKg`, `datePublished`
- Boolean variables are nouns, not verbs: `happy` not `isHappy`
- Constants in `UPPER_SNAKE_CASE` with units: `MAX_USERNAME_LENGTH_CHARS`
- JavaDoc comments on ALL public elements - must be comprehensive enough to rebuild the method
- No `System.out.print` in final code (only for debugging)
- Always use braces, even for one-line conditions
- `final` on everything: loop variables, catch variables, methods, classes unless they need to change
- No abbreviations: `quantity` not `qty`, `emailAddress` not `addr`

## Build and Run

```bash
# Compile (from project root)
javac -d out src/code/ca/bcit/cst/comp2522/termproject/*.java

# Run
java -cp out ca.bcit.cst.comp2522.termproject.Main
```

## Project Architecture

### Main.java
Entry point with infinite REPL menu loop. Prompts user to select:
- W: WordGame (geography trivia)
- N: NumberGame (GUI number placement game)
- M: MyGame (custom AI-assisted game)
- Q: Quit

### WordGame Architecture
**Data Model**:
- `Country.java`: Encapsulates country name, capital, and array of 3 facts
- `World.java`: HashMap<String, Country> for all countries loaded from `countries/[a-z].txt`
- `Score.java`: Tracks dateTimePlayed, numGamesPlayed, numCorrectFirstAttempt, numCorrectSecondAttempt, numIncorrectTwoAttempts

**File Format** (`countries/*.txt`):
```
CountryName:CapitalCity
Fact line 1
Fact line 2
Fact line 3
<blank line>
```

**Game Flow**:
1. Load all country files (a.txt - z.txt) into World HashMap at startup
2. Ask 10 random questions (3 types: capital→country, country→capital, fact→country)
3. Allow 2 guesses per question, track scoring
4. Display cumulative score across multiple games
5. On quit: append to `score.txt`, check/announce high score
6. **Must pass ScoreTest.java unit tests**

**Scoring**: First attempt = 2 points, second attempt = 1 point, incorrect = 0 points

### NumberGame Architecture
**GUI-based** game (use Swing). Create at least:
- 1 interface
- 1 abstract class
- 1 concrete class

**Game Mechanics**:
- 4×5 grid (20 buttons)
- Game generates 20 random integers (1-1000)
- User clicks squares to place numbers in ascending order
- Win: all 20 placed in order | Lose: can't place next number
- Track wins/losses and average successful placements

### MyGame
- Must apply concepts from **every course lesson**
- Create `applications.txt`: map each lesson to where it's used in MyGame code
- Create `prompts.txt`: 2-page reflection on AI collaboration experience
- Must be unique (not copy-pasteable from online sources)

## Data Files

**Country Data**: `countries/a.txt` through `countries/z.txt`
- Do NOT modify these files
- Format: `CountryName:CapitalCity` followed by 3 fact lines

**Score Tracking**: `score.txt`
- Auto-created/appended by WordGame
- Format:
  ```
  Date and Time: 2024-10-10 11:10:26
  Games Played: 1
  Correct First Attempts: 6
  Correct Second Attempts: 2
  Incorrect Attempts: 2
  Total Score: 14 points
  ```

## Testing

**ScoreTest.java**: Unit tests for Score class and scoring algorithm
- Must pass ALL tests
- Tests validate score calculation and file output format

## Constraints

- Only use Java concepts taught in COMP2522
- No ArrayList in declarations (use `List<>` interface)
- No advanced features (streams, lambdas, etc.) unless explicitly taught
- Must use arrays (not ArrayList) for Country facts and NumberGame grid
- DateTime handling: Use `LocalDateTime` and `DateTimeFormatter` as shown in project spec

## Submission Requirements

Files to include in .zip:
- Main.java, WordGame.java, Country.java, Score.java, NumberGame.java, MyGame.java
- score.txt, applications.txt, prompts.txt
- All custom interfaces/classes
- Two videos (technical + marketing)

**DO NOT** include country .txt files in submission.

## Implementation Checklist

### Core WordGame Components
- [ ] Complete Main.java menu system with input validation
- [ ] Create Country.java class with name, capital, and facts array
- [ ] Create World.java class with HashMap for country storage
- [ ] Create Score.java class with date, games, attempts tracking
- [ ] Implement WordGame file loading from countries/*.txt
- [ ] Implement WordGame question generation (3 types: capital→country, country→capital, fact→country)
- [ ] Implement WordGame scoring and game loop
- [ ] Implement score.txt file writing and high score tracking
- [ ] Pass all ScoreTest.java unit tests

### NumberGame Components
- [ ] Create NumberGame GUI with 4×5 grid (20 buttons)
- [ ] Implement NumberGame interface, abstract class, concrete class
- [ ] Implement NumberGame logic and win/loss detection

### MyGame Components
- [ ] Design and implement MyGame with AI assistance
- [ ] Create applications.txt mapping lessons to MyGame code
- [ ] Create prompts.txt reflection (2 pages)

## Notes

- Map<String, Runnable> confirmed as allowed (user has covered this in course)
- Check with user before using any data structure not explicitly covered in course
- Style violations can result in losing **up to 50% of marks**
- Comments must be detailed enough for another developer to rebuild methods
