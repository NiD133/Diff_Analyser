package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JacksonInjectTest {

    // Test class with JacksonInject annotations
    private static final class TestClass {
        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int field;

        @JacksonInject
        public int vanilla;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalField;
    }

    // Predefined empty JacksonInject.Value instance
    private static final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    @Test
    public void testEmptyValue() {
        // Verify that the EMPTY instance behaves as expected
        assertNull(EMPTY.getId(), "ID should be null");
        assertNull(EMPTY.getUseInput(), "UseInput should be null");
        assertTrue(EMPTY.willUseInput(true), "Should use input when default is true");
        assertFalse(EMPTY.willUseInput(false), "Should not use input when default is false");

        // Check that constructing with null or empty string returns EMPTY
        assertSame(EMPTY, JacksonInject.Value.construct(null, null, null), "Constructing with nulls should return EMPTY");
        assertSame(EMPTY, JacksonInject.Value.construct("", null, null), "Constructing with empty string should return EMPTY");
    }

    @Test
    public void testValueFromAnnotation() throws Exception {
        // Test extracting JacksonInject.Value from annotations
        assertSame(EMPTY, JacksonInject.Value.from(null), "Null annotation should return EMPTY");

        JacksonInject annotation = TestClass.class.getField("field").getAnnotation(JacksonInject.class);
        JacksonInject.Value value = JacksonInject.Value.from(annotation);
        assertEquals("inject", value.getId(), "ID should match annotation value");
        assertEquals(Boolean.FALSE, value.getUseInput(), "UseInput should match annotation value");
        assertEquals("JacksonInject.Value(id=inject,useInput=false,optional=false)", value.toString(), "String representation should match");

        assertFalse(value.equals(EMPTY), "Value should not equal EMPTY");
        assertFalse(EMPTY.equals(value), "EMPTY should not equal value");

        JacksonInject vanillaAnnotation = TestClass.class.getField("vanilla").getAnnotation(JacksonInject.class);
        value = JacksonInject.Value.from(vanillaAnnotation);
        assertEquals(JacksonInject.Value.construct(null, null, null), value, "Vanilla field should have default values");

        JacksonInject optionalAnnotation = TestClass.class.getField("optionalField").getAnnotation(JacksonInject.class);
        value = JacksonInject.Value.from(optionalAnnotation);
        assertEquals(JacksonInject.Value.construct(null, null, true), value, "Optional field should have optional set to true");
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testStandardMethods() {
        // Test standard methods like toString, hashCode, and equals
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)", EMPTY.toString(), "String representation should match");
        
        int hashCode = EMPTY.hashCode();
        assertNotEquals(0, hashCode, "Hash code should not be zero");

        assertEquals(EMPTY, EMPTY, "EMPTY should be equal to itself");
        assertFalse(EMPTY.equals(null), "EMPTY should not equal null");
        assertFalse(EMPTY.equals("xyz"), "EMPTY should not equal a different type");

        // Test equality and inequality of different JacksonInject.Value instances
        JacksonInject.Value value1 = JacksonInject.Value.construct("value", true, true);
        JacksonInject.Value value2 = JacksonInject.Value.construct("value", true, true);
        JacksonInject.Value valueWithNullId = JacksonInject.Value.construct(null, true, true);
        JacksonInject.Value valueWithNullUseInput = JacksonInject.Value.construct("value", null, true);
        JacksonInject.Value valueWithNullOptional = JacksonInject.Value.construct("value", true, null);
        JacksonInject.Value differentValue = JacksonInject.Value.construct("not equal", true, true);
        JacksonInject.Value differentUseInput = JacksonInject.Value.construct("value", false, true);
        JacksonInject.Value differentOptional = JacksonInject.Value.construct("value", true, false);

        assertEquals(value1, value2, "Values with same properties should be equal");
        assertNotEquals(value1, valueWithNullId, "Values with different IDs should not be equal");
        assertNotEquals(value1, valueWithNullUseInput, "Values with different UseInput should not be equal");
        assertNotEquals(value1, valueWithNullOptional, "Values with different Optional should not be equal");
        assertNotEquals(value1, differentValue, "Values with different properties should not be equal");
        assertNotEquals(value1, differentUseInput, "Values with different UseInput should not be equal");
        assertNotEquals(value1, differentOptional, "Values with different Optional should not be equal");
        assertNotEquals(value1, "string", "Value should not equal a different type");
    }

    @Test
    public void testFactoryMethods() throws Exception {
        // Test factory methods for creating new JacksonInject.Value instances
        JacksonInject.Value valueWithId = EMPTY.withId("name");
        assertNotSame(EMPTY, valueWithId, "New instance should be created with different ID");
        assertEquals("name", valueWithId.getId(), "ID should be updated");
        assertSame(valueWithId, valueWithId.withId("name"), "Same instance should be returned when ID is unchanged");

        JacksonInject.Value valueWithUseInput = valueWithId.withUseInput(Boolean.TRUE);
        assertNotSame(valueWithId, valueWithUseInput, "New instance should be created with different UseInput");
        assertFalse(valueWithId.equals(valueWithUseInput), "Values should not be equal after UseInput change");
        assertSame(valueWithUseInput, valueWithUseInput.withUseInput(Boolean.TRUE), "Same instance should be returned when UseInput is unchanged");

        JacksonInject.Value valueWithOptional = valueWithId.withOptional(Boolean.TRUE);
        assertNotSame(valueWithId, valueWithOptional, "New instance should be created with different Optional");
        assertFalse(valueWithId.equals(valueWithOptional), "Values should not be equal after Optional change");
        assertTrue(valueWithOptional.getOptional(), "Optional should be true");
        assertSame(valueWithOptional, valueWithOptional.withOptional(Boolean.TRUE), "Same instance should be returned when Optional is unchanged");

        int hashCode = valueWithUseInput.hashCode();
        assertNotEquals(0, hashCode, "Hash code should not be zero");
    }
}