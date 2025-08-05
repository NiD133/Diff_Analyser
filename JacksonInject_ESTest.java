package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for JacksonInject.Value class functionality.
 * Tests cover construction, mutation, equality, and accessor methods.
 */
public class JacksonInjectValueTest {

    // Test data constants
    private static final Object TEST_ID = new Object();
    private static final String TEST_STRING_ID = "testId";
    private static final Boolean TRUE = Boolean.TRUE;
    private static final Boolean FALSE = Boolean.FALSE;

    // ========== Construction Tests ==========

    @Test
    public void testEmptyValueCreation() {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        
        assertNull("Empty value should have null ID", emptyValue.getId());
        assertNull("Empty value should have null useInput", emptyValue.getUseInput());
        assertNull("Empty value should have null optional", emptyValue.getOptional());
        assertFalse("Empty value should not have ID", emptyValue.hasId());
    }

    @Test
    public void testForIdCreation() {
        JacksonInject.Value value = JacksonInject.Value.forId(TEST_ID);
        
        assertEquals("Value should have the specified ID", TEST_ID, value.getId());
        assertTrue("Value should indicate it has an ID", value.hasId());
        assertNull("Value should have null useInput by default", value.getUseInput());
        assertNull("Value should have null optional by default", value.getOptional());
    }

    @Test
    public void testConstructWithAllParameters() {
        JacksonInject.Value value = JacksonInject.Value.construct(TEST_ID, FALSE, TRUE);
        
        assertEquals("Value should have the specified ID", TEST_ID, value.getId());
        assertEquals("Value should have the specified useInput", FALSE, value.getUseInput());
        assertEquals("Value should have the specified optional", TRUE, value.getOptional());
        assertTrue("Value should indicate it has an ID", value.hasId());
    }

    @Test
    public void testConstructWithNullId() {
        JacksonInject.Value value = JacksonInject.Value.construct(null, FALSE, TRUE);
        
        assertNull("Value should have null ID", value.getId());
        assertFalse("Value should not indicate it has an ID", value.hasId());
        assertEquals("Value should have the specified useInput", FALSE, value.getUseInput());
        assertEquals("Value should have the specified optional", TRUE, value.getOptional());
    }

    @Test
    public void testFromNullAnnotation() {
        JacksonInject.Value value = JacksonInject.Value.from(null);
        
        assertNull("Value from null annotation should have null optional", value.getOptional());
    }

    @Test(expected = NullPointerException.class)
    public void testFromAnnotationWithNullUseInput() {
        JacksonInject mockAnnotation = mock(JacksonInject.class);
        when(mockAnnotation.value()).thenReturn(null);
        when(mockAnnotation.useInput()).thenReturn(null);
        
        JacksonInject.Value.from(mockAnnotation);
    }

    // ========== Mutation Tests ==========

    @Test
    public void testWithIdCreatesNewInstance() {
        JacksonInject.Value original = JacksonInject.Value.forId(TEST_ID);
        Object newId = new Object();
        
        JacksonInject.Value modified = original.withId(newId);
        
        assertNotSame("withId should create new instance", original, modified);
        assertEquals("Modified value should have new ID", newId, modified.getId());
        assertTrue("Modified value should indicate it has an ID", modified.hasId());
    }

    @Test
    public void testWithIdSameValueReturnsSameInstance() {
        JacksonInject.Value original = JacksonInject.Value.forId(TEST_ID);
        
        JacksonInject.Value result = original.withId(TEST_ID);
        
        assertSame("withId with same value should return same instance", original, result);
    }

    @Test
    public void testWithIdNullOnEmptyReturnsSameInstance() {
        JacksonInject.Value empty = JacksonInject.Value.empty();
        
        JacksonInject.Value result = empty.withId(null);
        
        assertSame("withId(null) on empty should return same instance", empty, result);
    }

    @Test
    public void testWithUseInputCreatesNewInstance() {
        JacksonInject.Value original = JacksonInject.Value.forId(TEST_ID);
        
        JacksonInject.Value modified = original.withUseInput(FALSE);
        
        assertNotSame("withUseInput should create new instance", original, modified);
        assertEquals("Modified value should have new useInput", FALSE, modified.getUseInput());
    }

    @Test
    public void testWithUseInputSameValueReturnsSameInstance() {
        JacksonInject.Value original = new JacksonInject.Value(TEST_ID, FALSE, null);
        
        JacksonInject.Value result = original.withUseInput(FALSE);
        
        assertSame("withUseInput with same value should return same instance", original, result);
    }

    @Test
    public void testWithOptionalCreatesNewInstance() {
        JacksonInject.Value original = JacksonInject.Value.forId(TEST_ID);
        
        JacksonInject.Value modified = original.withOptional(TRUE);
        
        assertNotSame("withOptional should create new instance", original, modified);
        assertEquals("Modified value should have new optional", TRUE, modified.getOptional());
    }

    @Test
    public void testWithOptionalSameValueReturnsSameInstance() {
        JacksonInject.Value original = new JacksonInject.Value(TEST_ID, null, TRUE);
        
        JacksonInject.Value result = original.withOptional(TRUE);
        
        assertSame("withOptional with same value should return same instance", original, result);
    }

    // ========== Equality Tests ==========

    @Test
    public void testEqualityWithSameValues() {
        JacksonInject.Value value1 = new JacksonInject.Value(TEST_ID, FALSE, TRUE);
        JacksonInject.Value value2 = JacksonInject.Value.construct(TEST_ID, FALSE, TRUE);
        
        assertTrue("Values with same parameters should be equal", value1.equals(value2));
        assertTrue("Values with same parameters should be equal (symmetric)", value2.equals(value1));
    }

    @Test
    public void testEqualityWithSameId() {
        JacksonInject.Value value1 = JacksonInject.Value.forId(TEST_ID);
        JacksonInject.Value value2 = JacksonInject.Value.forId(TEST_ID);
        
        assertTrue("Values with same ID should be equal", value1.equals(value2));
    }

    @Test
    public void testEqualityReflexive() {
        JacksonInject.Value value = JacksonInject.Value.empty();
        
        assertTrue("Value should equal itself", value.equals(value));
    }

    @Test
    public void testInequalityWithDifferentIds() {
        JacksonInject.Value value1 = new JacksonInject.Value(TEST_ID, FALSE, TRUE);
        JacksonInject.Value value2 = JacksonInject.Value.construct(TEST_STRING_ID, FALSE, TRUE);
        
        assertFalse("Values with different IDs should not be equal", value1.equals(value2));
        assertFalse("Values with different IDs should not be equal (symmetric)", value2.equals(value1));
    }

    @Test
    public void testInequalityWithDifferentUseInput() {
        JacksonInject.Value empty = JacksonInject.Value.EMPTY;
        JacksonInject.Value withUseInput = empty.withUseInput(FALSE);
        
        assertFalse("Values with different useInput should not be equal", empty.equals(withUseInput));
    }

    @Test
    public void testInequalityWithDifferentOptional() {
        JacksonInject.Value value1 = JacksonInject.Value.forId(TEST_ID);
        JacksonInject.Value value2 = value1.withOptional(FALSE);
        
        assertFalse("Values with different optional should not be equal", value1.equals(value2));
        assertFalse("Values with different optional should not be equal (symmetric)", value2.equals(value1));
    }

    // ========== Accessor Tests ==========

    @Test
    public void testHasIdWithNonNullId() {
        JacksonInject.Value value = JacksonInject.Value.forId(TEST_ID);
        
        assertTrue("Value with non-null ID should return true for hasId()", value.hasId());
    }

    @Test
    public void testHasIdWithNullId() {
        JacksonInject.Value value = JacksonInject.Value.empty();
        
        assertFalse("Value with null ID should return false for hasId()", value.hasId());
    }

    @Test
    public void testWillUseInputWithFalseUseInput() {
        JacksonInject.Value value = JacksonInject.Value.construct(null, FALSE, null);
        
        assertFalse("willUseInput should return false when useInput is FALSE", 
                   value.willUseInput(false));
    }

    @Test
    public void testWillUseInputWithNullUseInput() {
        JacksonInject.Value value = JacksonInject.Value.forId(TEST_ID);
        
        // This test verifies the method doesn't throw an exception with null useInput
        value.willUseInput(false);
    }

    @Test
    public void testGettersReturnCorrectValues() {
        JacksonInject.Value value = JacksonInject.Value.construct(TEST_ID, FALSE, TRUE);
        
        assertEquals("getId should return the ID", TEST_ID, value.getId());
        assertEquals("getUseInput should return the useInput value", FALSE, value.getUseInput());
        assertEquals("getOptional should return the optional value", TRUE, value.getOptional());
    }

    // ========== String Representation Tests ==========

    @Test
    public void testToStringFormat() {
        JacksonInject.Value value = JacksonInject.Value.forId(null).withOptional(FALSE);
        String result = value.toString();
        
        assertEquals("toString should format correctly", 
                    "JacksonInject.Value(id=null,useInput=null,optional=false)", result);
    }

    // ========== Utility Tests ==========

    @Test
    public void testValueForReturnsCorrectClass() {
        JacksonInject.Value value = JacksonInject.Value.empty();
        Class<JacksonInject> clazz = value.valueFor();
        
        assertTrue("valueFor should return JacksonInject interface", clazz.isInterface());
        assertEquals("valueFor should return JacksonInject class", JacksonInject.class, clazz);
    }

    // ========== Complex Scenario Tests ==========

    @Test
    public void testChainedMutations() {
        JacksonInject.Value original = JacksonInject.Value.EMPTY;
        
        JacksonInject.Value result = original
            .withUseInput(FALSE)
            .withId(TEST_ID)
            .withOptional(TRUE);
        
        assertEquals("Chained mutations should preserve ID", TEST_ID, result.getId());
        assertEquals("Chained mutations should preserve useInput", FALSE, result.getUseInput());
        assertEquals("Chained mutations should preserve optional", TRUE, result.getOptional());
        assertTrue("Result should have ID", result.hasId());
    }

    @Test
    public void testMutationPreservesOtherFields() {
        JacksonInject.Value original = new JacksonInject.Value(TEST_ID, FALSE, TRUE);
        
        JacksonInject.Value withNewId = original.withId(TEST_STRING_ID);
        
        assertEquals("Mutation should change only the target field", TEST_STRING_ID, withNewId.getId());
        assertEquals("Mutation should preserve useInput", FALSE, withNewId.getUseInput());
        assertEquals("Mutation should preserve optional", TRUE, withNewId.getOptional());
    }
}