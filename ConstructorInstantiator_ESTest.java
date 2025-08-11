package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;

import static org.junit.Assert.*;

/**
 * Tests for ConstructorInstantiator focusing on behavior rather than EvoSuite-specific details.
 * The tests use descriptive names and comments to clarify intent and expected outcomes.
 */
public class ConstructorInstantiatorTest {

    @Test
    public void createsInstanceWithDefaultConstructor_whenNoArgsProvided() {
        // Given no constructor arguments
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true);

        // When creating an Object (which has a public no-arg constructor)
        Object instance = instantiator.newInstance(Object.class);

        // Then a new instance is created
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void nullArgsArray_throwsNullPointerException() {
        // Given a null var-args array (as opposed to an empty array)
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, (Object[]) null);

        // When/Then: Mockito's implementation expects to NPE in this situation
        assertThrows(NullPointerException.class, () -> instantiator.newInstance(Object.class));
    }

    @Test
    public void noMatchingConstructor_withTooManyArgs_throwsInstantiationException() {
        // Given arguments that do not match any constructor on Object
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, "x", 123);

        // When/Then: no matching constructor exists -> InstantiationException (Mockito's runtime exception)
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(Object.class));
    }

    @Test
    public void noMatchingConstructor_withWrongArgType_throwsInstantiationException() {
        // Given an argument of type Class for Integer, which has constructors like (int) and (String), but not (Class)
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, Integer.class);

        // When/Then: no constructor of Integer takes a Class parameter
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(Integer.class));
    }

    @Test
    public void classWithoutNoArgConstructor_throwsInstantiationException() {
        // Given no arguments
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true);

        // When/Then: Integer has no public no-arg constructor -> cannot instantiate
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(Integer.class));
    }
}