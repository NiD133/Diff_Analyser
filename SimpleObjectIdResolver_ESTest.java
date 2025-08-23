package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleObjectIdResolverTest {

    // Helper to create a stable IdKey for tests
    private ObjectIdGenerator.IdKey id(Object rawKey) {
        return new ObjectIdGenerator.IdKey(Object.class, String.class, rawKey);
    }

    @Test
    public void bindAndResolve_returnsSameInstance() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object value = new Object();
        ObjectIdGenerator.IdKey key = id("id-1");

        resolver.bindItem(key, value);
        Object resolved = resolver.resolveId(key);

        assertSame("Resolved value should be the exact same instance that was bound", value, resolved);
    }

    @Test
    public void resolveUnknownId_returnsNull() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();

        assertNull("Unknown ids should resolve to null", resolver.resolveId(id("missing")));
    }

    @Test
    public void canUseFor_self_returnsTrue() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();

        assertTrue("Resolver should report it can be used for its own type", resolver.canUseFor(resolver));
    }

    @Test
    public void canUseFor_null_throwsNullPointerException() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();

        try {
            resolver.canUseFor(null);
            fail("Expected NullPointerException when passing null to canUseFor");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void bindingNullValue_canResolveNull() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key = id("nullable");

        resolver.bindItem(key, null);

        assertNull("Bound null should resolve back to null", resolver.resolveId(key));
    }

    @Test
    public void rebindSameKeyWithSameInstance_isAllowed() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key = id("same-instance");
        Object value = new Object();

        // Binding the same key to the exact same instance should not fail
        resolver.bindItem(key, value);
        resolver.bindItem(key, value);

        assertSame(value, resolver.resolveId(key));
    }

    @Test
    public void rebindSameKeyWithDifferentInstance_throwsIllegalStateException() {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key = id("conflict");
        Object first = new Object();
        Object second = new Object();

        resolver.bindItem(key, first);

        try {
            resolver.bindItem(key, second);
            fail("Expected IllegalStateException when re-binding key to a different instance");
        } catch (IllegalStateException expected) {
            // expected
        }
    }

    @Test
    public void newForDeserialization_returnsFreshResolver() {
        SimpleObjectIdResolver original = new SimpleObjectIdResolver();

        ObjectIdResolver fresh = original.newForDeserialization(null);

        assertNotSame("newForDeserialization should return a new resolver instance", original, fresh);
        assertTrue("Returned resolver should be of the same concrete type",
                fresh.getClass() == SimpleObjectIdResolver.class);

        // And it should start out empty
        assertNull(fresh.resolveId(id("not-bound")));
    }
}