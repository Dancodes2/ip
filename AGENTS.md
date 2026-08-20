# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: [Beginner]
* IDE and level of expertise: [Beginner]

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.

# Advanced conventions

## Java coding conventions

Follow the SE-EDU Java coding standard used by CS2103T.

For topics not covered by these rules, follow the Google Java Style Guide.

### Naming

- Package names use lowercase.
- Class and enum names are nouns written in PascalCase.
- Variable names use camelCase.
- Method names are verbs written in camelCase.
- Constants use SCREAMING_SNAKE_CASE.
- Boolean variables and methods should read like booleans, preferably using prefixes such as:
  - `is`
  - `has`
  - `was`
  - `can`
  - `should`
- Collection variables should generally use plural names.
- Acronyms embedded in names should not be written entirely in uppercase.
  - Prefer `exportHtmlSource()` over `exportHTMLSource()`.
- Test methods may use the format:

```text
featureUnderTest_testScenario_expectedBehavior()
```

### Formatting

- Use 4 spaces for indentation.
- Do not use tabs for indentation.
- Aim to keep lines below 110 characters.
- Do not exceed 120 characters unless unavoidable.
- Wrapped lines should normally use 8 additional spaces of indentation.
- Use K&R-style braces.

Example:

```java
if (isValid) {
    processTask();
} else {
    showError();
}
```

- Always use braces for `if`, `else`, `for`, `while`, and other control-flow bodies, even when the body contains only one statement.
- Put spaces around operators.
- Put a space after commas.
- Put a space between Java control-flow keywords and `(`.
- Separate logical sections within a block using blank lines when it improves readability.

### Imports and packages

- Every class must belong to a package.
- Group related classes in appropriate packages.
- Keep import ordering consistent with the rest of the project.
- Use explicit imports.
- Do not use wildcard imports such as:

```java
import java.util.*;
```

### Variables and fields

- Declare variables in the smallest reasonable scope.
- Initialize variables when they are declared where practical.
- Avoid public mutable fields.
- Preserve encapsulation through appropriate access modifiers and methods.
- Write array types as:

```java
int[] values;
```

not:

```java
int values[];
```

- Avoid unnecessary use of `this`.
- Use `this.field = field` when required to distinguish a field from a shadowing parameter.

### Classes and methods

When applicable, organize class contents in this general order:

1. Class documentation
2. Class declaration
3. Static variables
4. Instance variables
5. Constructors
6. Methods

Access modifiers should appear first in method declarations.

Prefer:

```java
public static void run()
```

over:

```java
static public void run()
```

### Comments and Javadoc

- Write comments in English using American spelling.
- Public classes and public methods should have descriptive header comments/Javadoc, except where the course conventions permit omission, such as:
  - straightforward getters/setters,
  - test code,
  - overriding methods where inherited documentation applies exactly.
- Non-trivial private methods should have header comments when required by the full coding standard.
- Javadoc should describe intended behavior, not implementation mechanics.
- The first sentence of a method Javadoc should be a concise summary and normally begin with wording such as:
  - `Returns ...`
  - `Adds ...`
  - `Creates ...`
  - `Sends ...`
- Avoid comments that simply translate obvious Java statements into English.
- Indent comments consistently with the surrounding code.

### Switch statements

If a traditional `switch` case intentionally falls through to the next case, include:

```java
// Fallthrough
```

to make the intent explicit.

## Git conventions

Follow the CS2103T Git conventions below when proposing branch names or commit messages.

### Commit subject

Every commit must have a clear subject line.

- Aim for at most 50 characters.
- Hard limit: 72 characters.
- Use imperative mood.
- Capitalize the first letter.
- Do not end with a period.

Good:

```text
Add deadline parsing
```

Bad:

```text
Added deadline parsing
Adding deadline parsing
add deadline parsing
Add deadline parsing.
```

A scope or category prefix may be used where helpful, for example:

```text
Parser: Handle invalid dates
Main.java: Remove unused import
bug fix: Prevent duplicate tasks
chore: Update Gradle version
```

### Commit body

Non-trivial commits should include a body.

- Separate the subject and body with a blank line.
- Wrap body lines at 72 characters.
- Use blank lines between paragraphs.
- Use bullet points where they improve clarity.
- Explain WHAT changed and WHY.
- Do not spend the body describing HOW the implementation works when that is already evident from the diff.
- Avoid duplicating information already explained clearly in code comments.

A useful structure is:

```text
<current situation>

<why it needs to change>

<what this commit does>

<why this approach is appropriate>

<any other relevant information>
```

Use present tense for the existing situation and imperative wording for the change where appropriate.

Avoid unnecessary words such as `currently` or `originally` when simply describing the present state.

If a commit message requires a very long explanation covering multiple unrelated changes, prefer splitting the work into smaller logical commits.

### Branch names

Use meaningful kebab-case branch names.

Examples:

```text
add-deadline-command
refactor-parser
fix-task-index-validation
```

If a branch corresponds directly to an issue, use:

```text
issueNumber-some-keywords-from-issue-title
```

For example:

```text
1234-ui-freeze-error
```