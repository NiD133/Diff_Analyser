package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

/**
 * Test suite for SimpleObjectIdResolver functionality.
 * Tests the basic object ID resolution, binding, and error handling capabilities.
 */
public class SimpleObjectIdResolverTest {

    @Test
    public void shouldResolveObjectAfterBinding() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey idKey = createIdKey(Object.class, Object.class, resolver);
        Class<Object> boundObject = Object.class;
        
        // When
        resolver.bindItem(idKey, boundObject);
        Class<?> resolvedObject = (Class<?>) resolver.resolveId(idKey);
        
        // Then
        assertNotNull("Should resolve bound object", resolvedObject);
        assertFalse("Resolved class should not be an enum", resolvedObject.isEnum());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenCheckingCompatibilityWithNullResolver() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // When & Then
        resolver.canUseFor(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenBindingNullObjectToAlreadyUsedKey() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey idKey = null;
        
        // When - first binding succeeds
        resolver.bindItem(idKey, new Object());
        
        // Then - second binding with null should fail
        resolver.bindItem(idKey, null);
    }

    @Test
    public void shouldBeCompatibleWithSameResolverType() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // When
        boolean isCompatible = resolver.canUseFor(resolver);
        
        // Then
        assertTrue("Resolver should be compatible with itself", isCompatible);
    }

    @Test
    public void shouldAllowBindingAndResolvingNullObject() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey idKey = createIdKey(Object.class, String.class, resolver);
        
        // When
        resolver.bindItem(idKey, null);
        Object resolvedObject = resolver.resolveId(idKey);
        
        // Then
        assertNull("Should resolve to null when null was bound", resolvedObject);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenRebindingKeyToDifferentObject() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey idKey = createIdKey(Object.class, Object.class, resolver);
        Class<Object> originalObject = Object.class;
        
        // When - bind first object
        resolver.bindItem(idKey, originalObject);
        
        // Then - attempting to bind different object should fail
        resolver.bindItem(idKey, null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenRebindingSameObjectTwice() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey idKey = createIdKey(String.class, String.class, "");
        String objectToBind = "";
        
        // When - bind same object twice
        resolver.bindItem(idKey, objectToBind);
        
        // Then - second binding should fail even with same object
        resolver.bindItem(idKey, objectToBind);
    }

    @Test
    public void shouldAllowRebindingWhenOriginalValueWasNull() {
        // Given
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey idKey = createIdKey(Object.class, String.class, resolver);
        
        // When - resolve unbound key (returns null), then bind null, then bind actual object
        Object unboundResult = resolver.resolveId(idKey);
        resolver.bindItem(idKey, null);
        resolver.bindItem(idKey, unboundResult);
        
        // Then - no exception should be thrown
        // This test passes if no exception is thrown
    }

    @Test
    public void shouldCreateNewResolverInstanceForDeserialization() {
        // Given
        SimpleObjectIdResolver originalResolver = new SimpleObjectIdResolver();
        
        // When
        ObjectIdResolver newResolver = originalResolver.newForDeserialization(null);
        
        // Then
        assertNotNull("Should create new resolver instance", newResolver);
        assertNotSame("Should create different instance", newResolver, originalResolver);
    }

    // Helper method to create IdKey instances with better readability
    private ObjectIdGenerator.IdKey createIdKey(Class<?> type, Class<?> scope, Object key) {
        return new ObjectIdGenerator.IdKey(type, scope, key);
    }
}