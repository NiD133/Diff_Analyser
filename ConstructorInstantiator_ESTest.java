package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for ConstructorInstantiator class.
 * Tests the instantiation of objects using constructor arguments and outer class instance handling.
 */
public class ConstructorInstantiatorTest {

    @Test
    public void shouldCreateObjectWithNoConstructorArgs() {
        // Given: A constructor instantiator with no arguments and no outer class instance
        Object[] noArgs = new Object[0];
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, noArgs);
        
        // When: Creating a new Object instance
        Object result = instantiator.newInstance(Object.class);
        
        // Then: Should successfully create the instance
        assertNotNull("Should create a valid Object instance", result);
    }

    @Test
    public void shouldCreateObjectWithNoArgsWhenOuterClassInstanceEnabled() {
        // Given: A constructor instantiator with outer class instance enabled but no args
        Object[] noArgs = new Object[0];
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true, noArgs);
        
        // When: Creating a new Object instance
        Object result = instantiator.newInstance(Object.class);
        
        // Then: Should successfully create the instance
        assertNotNull("Should create a valid Object instance even with outer class flag", result);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenConstructorArgsIsNull() {
        // Given: A constructor instantiator with null constructor arguments
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, null);
        
        // When: Attempting to create an Object instance
        // Then: Should throw NullPointerException
        instantiator.newInstance(Object.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenConstructorArgsDoNotMatchObjectConstructor() {
        // Given: Constructor args that don't match Object's available constructors
        Object[] incompatibleArgs = {new Object(), new Object(), null};
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, incompatibleArgs);
        
        // When: Attempting to create an Object instance with incompatible args
        // Then: Should throw RuntimeException due to no matching constructor
        instantiator.newInstance(Object.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenTryingToInstantiateIntegerWithClassArg() {
        // Given: Constructor args containing a Class object (incompatible with Integer constructors)
        Object[] classArgs = {Integer.class};
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, classArgs);
        
        // When: Attempting to create an Integer instance with Class argument
        // Then: Should throw RuntimeException due to no matching constructor
        instantiator.newInstance(Integer.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenTryingToInstantiateIntegerWithNullArg() {
        // Given: Constructor args with null value (ambiguous for Integer constructors)
        Object[] nullArgs = {null};
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true, nullArgs);
        
        // When: Attempting to create an Integer instance with null argument
        // Then: Should throw RuntimeException due to constructor matching issues
        instantiator.newInstance(Integer.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenTooManyArgsProvidedForInteger() {
        // Given: Too many constructor arguments for Integer class
        Object[] tooManyArgs = new Object[7]; // Integer doesn't have constructors with 7 args
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true, tooManyArgs);
        
        // When: Attempting to create an Object instance with excessive arguments
        // Then: Should throw RuntimeException due to no matching constructor
        instantiator.newInstance(Object.class);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenEmptyArgsProvidedForIntegerWithOuterInstance() {
        // Given: Empty constructor args for Integer with outer instance flag
        Object[] emptyArgs = new Object[0];
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true, emptyArgs);
        
        // When: Attempting to create an Integer instance
        // Then: Should throw RuntimeException (Integer requires constructor arguments)
        instantiator.newInstance(Integer.class);
    }
}