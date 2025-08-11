package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleObjectIdResolverTest {
    
    private SimpleObjectIdResolver resolver;
    private ObjectIdGenerator.IdKey firstKey;
    private ObjectIdGenerator.IdKey secondKey;
    
    @BeforeEach
    void setUp() {
        resolver = new SimpleObjectIdResolver();
        firstKey = createIdKey("user123");
        secondKey = createIdKey("user456");
    }
    
    @Test
    void shouldBindAndResolveMultipleUniqueItems() {
        // Given: Two unique key-value pairs
        String firstValue = "John Doe";
        String secondValue = "Jane Smith";
        
        // When: Binding both items to the resolver
        resolver.bindItem(firstKey, firstValue);
        resolver.bindItem(secondKey, secondValue);
        
        // Then: Both items should be resolvable and stored correctly
        assertEquals(firstValue, resolver.resolveId(firstKey));
        assertEquals(secondValue, resolver.resolveId(secondKey));
        assertEquals(2, resolver._items.size());
    }
    
    @Test
    void shouldAllowRebindingWithSameValue() {
        // Given: An item already bound to the resolver
        String value = "John Doe";
        resolver.bindItem(firstKey, value);
        
        // When: Rebinding the same key with the same value
        resolver.bindItem(firstKey, value);
        
        // Then: The operation should succeed without increasing item count
        assertEquals(value, resolver.resolveId(firstKey));
        assertEquals(1, resolver._items.size());
    }
    
    @Test
    void shouldPreventRebindingWithDifferentValue() {
        // Given: An item already bound to the resolver
        String originalValue = "John Doe";
        String conflictingValue = "Jane Smith";
        resolver.bindItem(firstKey, originalValue);
        
        // When: Attempting to rebind the same key with a different value
        // Then: Should throw IllegalStateException with descriptive message
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> resolver.bindItem(firstKey, conflictingValue)
        );
        
        String expectedMessage = "Object Id conflict: Id [ObjectId: key=user123, type=java.lang.String, scope=NONE]" +
                               " already bound to an Object (type: `java.lang.String`, value: \"" + originalValue + "\"):" +
                               " attempt to re-bind to a different Object (type: `java.lang.String`, value: \"" + conflictingValue + "\")";
        
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    private ObjectIdGenerator.IdKey createIdKey(String keyValue) {
        return new ObjectIdGenerator.IdKey(String.class, null, keyValue);
    }
}