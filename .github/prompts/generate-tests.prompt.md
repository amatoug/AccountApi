---
description: "Generate JUnit 5 unit or integration tests for a given Java class in the BankApi project"
argument-hint: "Class or method to test (e.g. BankService.withdraw, BankController)"
agent: "agent"
tools: ["codebase"]
---

Generate tests for the class or method specified in the argument, following the existing test conventions in this Spring Boot project.

## Project test conventions

- **Unit tests** (no Spring context): place in `src/test/java/com/kata/bankapi/service/`, use plain `new` instantiation with a `@BeforeEach` setup method. See [BankServiceTest.java](../../src/test/java/com/kata/bankapi/service/BankServiceTest.java).
- **Integration tests** (full Spring context): place in `src/test/java/com/kata/bankapi/integration/`, annotate with `@SpringBootTest` + `@AutoConfigureMockMvc`, inject `MockMvc` and `ObjectMapper`. Protect endpoints with `@WithMockUser`. See [BankControllerIntegrationTest.java](../../src/test/java/com/kata/bankapi/integration/BankControllerIntegrationTest.java).
- Use **AssertJ** (`org.assertj.core.api.Assertions`) for assertions — never JUnit's `assertEquals`.
- Use **JUnit 5** (`@Test`, `@BeforeEach`) — no JUnit 4 annotations.
- Test method names follow the pattern `should_<expected_behavior>` or `should_<expected_behavior>_when_<condition>`.
- For controller tests use `mockMvc.perform(...)` with `.andExpect(status().isXxx())` and `jsonPath` assertions.
- For exception scenarios use `Assertions.assertThatThrownBy(...).isInstanceOf(XxxException.class)`.

## Custom exceptions to be aware of

| Exception | HTTP status |
|-----------|-------------|
| `AccountNotFoundException` | 404 Not Found |
| `AnauthorizedAmountException` | 400 Bad Request |
| `UserNameMandatoryException` | 400 Bad Request |
| `BankOverdraftAuthorizedExceededException` | 422 Unprocessable Entity |

## Steps

1. Read the target class with the codebase tool to understand its logic and existing tests.
2. Identify gaps: uncovered branches, edge cases, null inputs, boundary amounts.
3. Generate the test class (or append test methods to an existing one) following the conventions above.
4. Ensure every test is independent — do not share mutable state between tests.
5. Do not add comments or Javadoc unless the original tests have them.
