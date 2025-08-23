package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its
 * factory methods and core functionality like equals and hashCode.
 */
public class JacksonInjectValueTest {

    private final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    @Test
    public void withIdShouldCreateNewInstanceAndBeIdempotent() {
        // Arrange
        final String id = "name";

        // Act: Create a new instance from EMPTY
        JacksonInject.Value v1 = EMPTY.withId(id);

        // Assert: A new instance is created with the correct id
        assertNotSame(EMPTY, v1, "Creating from EMPTY should produce a new instance");
        assertEquals(id, v1.getId(), "The new instance should have the correct id");

        // Act: Call withId again with the same id
        JacksonInject.Value v2 = v1.withId(id);

        // Assert: The same instance is returned (idempotency)
        assertSame(v1, v2, "Calling withId with the same id should return the same instance");
    }

    @Test
    public void withUseInputShouldCreateNewInstanceAndBeIdempotent() {
        // Arrange
        JacksonInject.Value base = JacksonInject.Value.forId("id");

        // Act: Create a new instance with a new 'useInput' value
        JacksonInject.Value v1 = base.withUseInput(Boolean.TRUE);

        // Assert: A new instance is created
        assertNotSame(base, v1, "Changing 'useInput' should produce a new instance");
        assertEquals(Boolean.TRUE, v1.getUseInput());

        // Act: Call withUseInput again with the same value
        JacksonInject.Value v2 = v1.withUseInput(Boolean.TRUE);

        // Assert: The same instance is returned (idempotency)
        assertSame(v1, v2, "Calling withUseInput with the same value should return the same instance");
    }

    @Test
    public void withOptionalShouldCreateNewInstanceAndBeIdempotent() {
        // Arrange
        JacksonInject.Value base = JacksonInject.Value.forId("id");

        // Act: Create a new instance with a new 'optional' value
        JacksonInject.Value v1 = base.withOptional(Boolean.TRUE);

        // Assert: A new instance is created
        assertNotSame(base, v1, "Changing 'optional' should produce a new instance");
        assertTrue(v1.getOptional(), "The new instance should have optional=true");

        // Act: Call withOptional again with the same value
        JacksonInject.Value v2 = v1.withOptional(Boolean.TRUE);

        // Assert: The same instance is returned (idempotency)
        assertSame(v1, v2, "Calling withOptional with the same value should return the same instance");
    }

    @Test
    public void equalsAndHashCodeShouldBehaveCorrectly() {
        // Arrange
        JacksonInject.Value base = JacksonInject.Value.forId("name");
        JacksonInject.Value sameAsBase = JacksonInject.Value.forId("name");
        JacksonInject.Value differentUseInput = base.withUseInput(true);
        JacksonInject.Value differentOptional = base.withOptional(true);

        // Assert for equals contract
        assertEquals(base, sameAsBase, "Instances with same properties should be equal");
        assertNotEquals(base, differentUseInput, "Instances with different 'useInput' should not be equal");
        assertNotEquals(base, differentOptional, "Instances with different 'optional' should not be equal");
        assertNotEquals(differentUseInput, base, "Equals should be symmetric");

        // Assert for hashCode contract
        assertEquals(base.hashCode(), sameAsBase.hashCode(), "Hash codes should be same for equal objects");
        assertNotEquals(0, base.hashCode(), "Hash code for a non-empty value should not be zero");
    }
}