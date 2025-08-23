package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link SimpleObjectIdResolver} class.
 */
public class SimpleObjectIdResolverTest {

    /**
     * Tests that an existing ID can be re-bound to a new object.
     * A common use case is updating an object reference, and the resolver
     * should correctly return the most recently bound object for a given ID.
     */
    @Test
    public void shouldReturnNewlyBoundObjectWhenIdIsRebound() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // Create a unique key to represent an object's identity
        Object keyObject = new Object();
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(Object.class, null, keyObject);

        String firstObject = "Initial Version";
        String secondObject = "Updated Version";

        // Act & Assert: First binding
        
        // 1. Initially, the ID should not resolve to anything.
        assertNull("ID should be unresolved before any binding", resolver.resolveId(idKey));

        // 2. Bind the ID to the first object.
        resolver.bindItem(idKey, firstObject);
        
        // 3. Verify that the resolver now returns the first object.
        assertSame("ID should resolve to the first bound object", 
                     firstObject, resolver.resolveId(idKey));

        // Act & Assert: Second binding (re-binding the same ID)
        
        // 4. Re-bind the same ID to a different object.
        resolver.bindItem(idKey, secondObject);

        // 5. Verify that the resolver now returns the second, most recent object.
        assertSame("After re-binding, ID should resolve to the new object",
                     secondObject, resolver.resolveId(idKey));
    }
}