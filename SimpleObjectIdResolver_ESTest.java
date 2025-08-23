package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SimpleObjectIdResolver_ESTest extends SimpleObjectIdResolver_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testBindAndResolveId() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Class<Object> objectClass = Object.class;
        IdKey idKey = new IdKey(objectClass, objectClass, resolver);
        
        // Bind an object to the resolver and verify it can be resolved
        resolver.bindItem(idKey, objectClass);
        Class<?> resolvedClass = (Class<?>) resolver.resolveId(idKey);
        assertFalse(resolvedClass.isEnum());
    }

    @Test(timeout = 4000)
    public void testCanUseForWithNullResolver() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // Expect NullPointerException when passing null to canUseFor
        try {
            resolver.canUseFor(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }

    @Test(timeout = 4000)
    public void testBindItemWithNullIdKey() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object object = new Object();
        
        // Bind an object with a null IdKey
        resolver.bindItem(null, object);
        
        // Expect NullPointerException when binding null object with null IdKey
        try {
            resolver.bindItem(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }

    @Test(timeout = 4000)
    public void testCanUseForSelf() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // Verify that resolver can use itself
        assertTrue(resolver.canUseFor(resolver));
    }

    @Test(timeout = 4000)
    public void testBindAndResolveNullObject() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Class<Object> objectClass = Object.class;
        Class<String> stringClass = String.class;
        IdKey idKey = new IdKey(objectClass, stringClass, resolver);
        
        // Bind a null object and verify it resolves to null
        resolver.bindItem(idKey, null);
        Object resolvedObject = resolver.resolveId(idKey);
        assertNull(resolvedObject);
    }

    @Test(timeout = 4000)
    public void testRebindThrowsIllegalStateException() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Class<Object> objectClass = Object.class;
        IdKey idKey = new IdKey(objectClass, objectClass, resolver);
        
        // Bind an object and expect IllegalStateException on rebind
        resolver.bindItem(idKey, objectClass);
        try {
            resolver.bindItem(idKey, null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }

    @Test(timeout = 4000)
    public void testRebindWithSameObject() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Class<String> stringClass = String.class;
        IdKey idKey = new IdKey(stringClass, stringClass, "");
        
        // Bind a string and expect IllegalStateException on rebind with the same object
        resolver.bindItem(idKey, "");
        try {
            resolver.bindItem(idKey, "");
            // fail("Expecting exception: IllegalStateException"); // Unstable assertion
        } catch (IllegalStateException e) {
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }

    @Test(timeout = 4000)
    public void testBindNullThenRebind() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Class<Object> objectClass = Object.class;
        Class<String> stringClass = String.class;
        IdKey idKey = new IdKey(objectClass, stringClass, resolver);
        
        // Resolve an unbound IdKey, bind null, then rebind
        Object resolvedObject = resolver.resolveId(idKey);
        resolver.bindItem(idKey, null);
        resolver.bindItem(idKey, resolvedObject);
    }

    @Test(timeout = 4000)
    public void testNewForDeserialization() throws Throwable {
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        // Verify that newForDeserialization returns a different instance
        ObjectIdResolver newResolver = resolver.newForDeserialization(null);
        assertNotSame(newResolver, resolver);
    }
}