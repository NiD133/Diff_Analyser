package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link SimpleObjectIdResolver} class.
 */
public class SimpleObjectIdResolverTest {

    /**
     * Verifies that if an ID is explicitly bound to a null object,
     * resolving that same ID correctly returns null.
     */
    @Test
    public void resolveId_shouldReturnNull_whenIdIsBoundToNull() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // Create a unique key to represent an object's ID.
        // The key is a composite of type, scope, and the actual key value.
        Object keyPayload = "some-unique-id";
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(Object.class, String.class, keyPayload);

        // Act
        // Bind the ID key to a null value.
        resolver.bindItem(idKey, null);
        
        // Attempt to retrieve the object associated with the ID key.
        Object resolvedObject = resolver.resolveId(idKey);

        // Assert
        // The resolved object should be the null value that was bound.
        assertNull("The resolver should return null for an ID explicitly bound to null.", resolvedObject);
    }
}