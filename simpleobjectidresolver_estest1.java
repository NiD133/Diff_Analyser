package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link SimpleObjectIdResolver}.
 */
public class SimpleObjectIdResolverTest {

    /**
     * Verifies that an object bound to an ID can be successfully retrieved
     * using that same ID.
     */
    @Test
    public void shouldReturnBoundObject_whenResolvingId() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // Create a unique key and an object to associate with it.
        Object objectToBind = new Object();
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(
                Object.class, // type
                null,         // scope
                "test-id-123" // key
        );

        // Act
        // Bind the object to the ID key.
        resolver.bindItem(idKey, objectToBind);
        
        // Attempt to resolve the ID key to get the object back.
        Object resolvedObject = resolver.resolveId(idKey);

        // Assert
        // The resolved object should be the exact same instance that was bound.
        assertSame("The resolved object should be the same instance that was originally bound.",
                   objectToBind, resolvedObject);
    }
}