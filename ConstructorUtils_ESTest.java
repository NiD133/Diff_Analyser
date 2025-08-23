package org.apache.commons.lang3.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class ConstructorUtilsReadableTest {

    // --- Happy-path behavior -------------------------------------------------

    @Test
    public void invokeExactConstructor_noArgs_returnsNewObject() throws Exception {
        Object instance = ConstructorUtils.invokeExactConstructor(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void invokeConstructor_noArgs_returnsNewObject() throws Exception {
        Object instance = ConstructorUtils.invokeConstructor(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

    @Test
    public void invokeConstructor_withCompatibleParam_findsMatchingConstructor() throws Exception {
        // Integer has a public ctor Integer(String)
        Integer value = ConstructorUtils.invokeConstructor(Integer.class, "123");
        assertEquals(Integer.valueOf(123), value);
    }

    @Test
    public void getAccessibleConstructor_publicNoArg_onObject_returnsSameInstance() {
        Constructor<Object> noArg = ConstructorUtils.getAccessibleConstructor(Object.class);
        assertNotNull(noArg);
        assertSame(noArg, ConstructorUtils.getAccessibleConstructor(noArg));
    }

    @Test
    public void getMatchingAccessibleConstructor_findsCompatibleConstructor() {
        // Integer(String) is compatible with parameter type String.class
        Constructor<Integer> ctor =
                ConstructorUtils.getMatchingAccessibleConstructor(Integer.class, String.class);
        assertNotNull(ctor);
        Class<?>[] paramTypes = ctor.getParameterTypes();
        assertEquals(1, paramTypes.length);
        assertEquals(String.class, paramTypes[0]);
    }

    // --- No matching constructor cases ---------------------------------------

    @Test
    public void getMatchingAccessibleConstructor_returnsNull_whenNoCompatibleConstructor() {
        // Object has only a no-arg constructor
        Constructor<Object> ctor =
                ConstructorUtils.getMatchingAccessibleConstructor(Object.class, String.class);
        assertNull(ctor);
    }

    @Test
    public void invokeExactConstructor_wrongArgsType_throwsNoSuchMethod() {
        // Object has only a no-arg ctor; passing args means "no such constructor"
        assertThrows(NoSuchMethodException.class,
                () -> ConstructorUtils.invokeExactConstructor(Object.class, new Object[] { "arg" }));
    }

    @Test
    public void invokeConstructor_wrongArgsType_throwsNoSuchMethod() {
        // Object has only a no-arg ctor; passing args means "no such constructor"
        assertThrows(NoSuchMethodException.class,
                () -> ConstructorUtils.invokeConstructor(Object.class, new Object[] { 1 }));
    }

    // --- Argument validation --------------------------------------------------

    @Test
    public void invokeExactConstructor_nullClass_throwsNPE() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> ConstructorUtils.invokeExactConstructor(null, new Object[0]));
        // Implementation uses Objects.requireNonNull(cls, "cls")
        assertEquals("cls", ex.getMessage());
    }

    @Test
    public void invokeConstructor_nullClass_throwsNPE_forBothOverloads() {
        NullPointerException ex1 = assertThrows(NullPointerException.class,
                () -> ConstructorUtils.invokeConstructor(null, new Object[0]));
        assertEquals("cls", ex1.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class,
                () -> ConstructorUtils.invokeConstructor(null, new Object[0], new Class<?>[0]));
        assertEquals("cls", ex2.getMessage());
    }

    @Test
    public void invokeConstructor_nullArgsButParamTypesProvided_throwsIAE() {
        // Parameter types indicate a 1-arg constructor but args array is null (treated as empty).
        assertThrows(IllegalArgumentException.class,
                () -> ConstructorUtils.invokeConstructor(Integer.class, null, new Class<?>[] { String.class }));
    }

    @Test
    public void getAccessibleConstructor_nullInputs_throwNPE() {
        NullPointerException exCtor = assertThrows(NullPointerException.class,
                () -> ConstructorUtils.getAccessibleConstructor((Constructor<Object>) null));
        assertEquals("ctor", exCtor.getMessage());

        NullPointerException exClass = assertThrows(NullPointerException.class,
                () -> ConstructorUtils.getAccessibleConstructor(null, new Class<?>[0]));
        assertEquals("cls", exClass.getMessage());
    }

    @Test
    public void getMatchingAccessibleConstructor_nullClass_throwsNPE() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> ConstructorUtils.getMatchingAccessibleConstructor(null, new Class<?>[0]));
        assertEquals("cls", ex.getMessage());
    }

    // --- Exceptions thrown by underlying constructor -------------------------

    @Test
    public void invokeConstructor_wrappedException_isInvocationTargetException() {
        // Integer(String) throws NumberFormatException when the content is not numeric.
        assertThrows(InvocationTargetException.class,
                () -> ConstructorUtils.invokeConstructor(Integer.class, "not-a-number"));
    }

    // --- Utility class constructor -------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void utilityClass_isInstantiable_forTools() {
        // The constructor is deprecated but intentionally public for tooling.
        ConstructorUtils utils = new ConstructorUtils();
        assertNotNull(utils);
    }
}