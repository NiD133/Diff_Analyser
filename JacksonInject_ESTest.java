package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JacksonInject.Value} class.
 * This class is responsible for containing and managing injection configuration.
 */
public class JacksonInjectValueTest {

    private static final JacksonInject.Value EMPTY_VALUE = JacksonInject.Value.empty();

    // ========================================================================
    // Factory Method Tests
    // ========================================================================

    @Test
    public void shouldCreateEmptyValueUsingEmptyFactory() {
        // When
        JacksonInject.Value value = JacksonInject.Value.empty();

        // Then
        assertNull("ID should be null for an empty value", value.getId());
        assertNull("useInput should be null for an empty value", value.getUseInput());
        assertNull("optional should be null for an empty value", value.getOptional());
        assertFalse("hasId should be false for an empty value", value.hasId());
        assertEquals("empty() should return the canonical EMPTY instance", EMPTY_VALUE, value);
    }

    @Test
    public void shouldCreateValueWithIdUsingForIdFactory() {
        // Given
        String id = "testId";

        // When
        JacksonInject.Value value = JacksonInject.Value.forId(id);

        // Then
        assertEquals(id, value.getId());
        assertTrue(value.hasId());
        assertNull(value.getUseInput());
        assertNull(value.getOptional());
    }

    @Test
    public void shouldCreateValueWithAllPropertiesUsingConstructFactory() {
        // Given
        String id = "testId";
        Boolean useInput = true;
        Boolean optional = false;

        // When
        JacksonInject.Value value = JacksonInject.Value.construct(id, useInput, optional);

        // Then
        assertEquals(id, value.getId());
        assertEquals(useInput, value.getUseInput());
        assertEquals(optional, value.getOptional());
        assertTrue(value.hasId());
    }

    @Test
    public void fromShouldReturnEmptyValueForNullAnnotation() {
        // When
        JacksonInject.Value value = JacksonInject.Value.from(null);
        
        // Then
        assertSame("from(null) should return the canonical EMPTY instance", EMPTY_VALUE, value);
    }

    // ========================================================================
    // Immutability and 'with...' Method Tests
    // ========================================================================

    @Test
    public void withIdShouldReturnNewInstanceWhenIdChanges() {
        // Given
        JacksonInject.Value original = JacksonInject.Value.forId("id1");
        
        // When
        JacksonInject.Value updated = original.withId("id2");
        
        // Then
        assertNotSame("A new instance should be created when the ID changes", original, updated);
        assertEquals("Original ID should be unchanged", "id1", original.getId());
        assertEquals("Updated value should have the new ID", "id2", updated.getId());
    }

    @Test
    public void withIdShouldReturnSameInstanceWhenIdIsUnchanged() {
        // Given
        JacksonInject.Value original = JacksonInject.Value.forId("id1");

        // When
        JacksonInject.Value updated = original.withId("id1");

        // Then
        assertSame("The same instance should be returned if the ID is not changed", original, updated);
    }

    @Test
    public void withUseInputShouldReturnNewInstanceWhenValueChanges() {
        // Given
        JacksonInject.Value original = EMPTY_VALUE;
        
        // When
        JacksonInject.Value updated = original.withUseInput(true);
        
        // Then
        assertNotSame("A new instance should be created when useInput changes", original, updated);
        assertTrue("Updated value should have the new useInput value", updated.getUseInput());
    }

    @Test
    public void withUseInputShouldReturnSameInstanceWhenValueIsUnchanged() {
        // Given
        JacksonInject.Value original = JacksonInject.Value.construct(null, true, null);

        // When
        JacksonInject.Value updated = original.withUseInput(true);

        // Then
        assertSame("The same instance should be returned if useInput is not changed", original, updated);
    }
    
    @Test
    public void withOptionalShouldReturnNewInstanceWhenValueChanges() {
        // Given
        JacksonInject.Value original = EMPTY_VALUE;
        
        // When
        JacksonInject.Value updated = original.withOptional(true);
        
        // Then
        assertNotSame("A new instance should be created when optional changes", original, updated);
        assertTrue("Updated value should have the new optional value", updated.getOptional());
    }

    @Test
    public void withOptionalShouldReturnSameInstanceWhenValueIsUnchanged() {
        // Given
        JacksonInject.Value original = JacksonInject.Value.construct(null, null, true);

        // When
        JacksonInject.Value updated = original.withOptional(true);

        // Then
        assertSame("The same instance should be returned if optional is not changed", original, updated);
    }

    @Test
    public void withMethodsShouldRevertToEmptyWhenAllPropertiesAreNulled() {
        // Given a value with all properties set
        JacksonInject.Value value = JacksonInject.Value.construct("id", true, true);
        assertNotEquals(EMPTY_VALUE, value);

        // When all properties are reverted to null
        JacksonInject.Value reverted = value.withId(null)
                                            .withUseInput(null)
                                            .withOptional(null);

        // Then the result should be equal to the canonical empty instance
        assertEquals("Reverting all properties to null should result in an empty value", EMPTY_VALUE, reverted);
    }

    // ========================================================================
    // Accessor and Logic Method Tests
    // ========================================================================

    @Test
    public void hasIdShouldReturnTrueForNonNullId() {
        assertTrue(JacksonInject.Value.forId("id").hasId());
        assertTrue(JacksonInject.Value.forId(new Object()).hasId());
    }

    @Test
    public void hasIdShouldReturnFalseForNullId() {
        assertFalse(EMPTY_VALUE.hasId());
        assertFalse(JacksonInject.Value.construct(null, true, true).hasId());
    }

    @Test
    public void willUseInputShouldReturnExplicitValueWhenSet() {
        // Given a value with useInput explicitly set to true
        JacksonInject.Value value = JacksonInject.Value.construct(null, true, null);
        
        // Then willUseInput should return true, ignoring the default
        assertTrue("Should return explicit value, ignoring default", value.willUseInput(false));
    }

    @Test
    public void willUseInputShouldReturnDefaultWhenNotSet() {
        // Given a value where useInput is not set (is null)
        JacksonInject.Value value = EMPTY_VALUE;
        
        // Then willUseInput should return the provided default value
        assertTrue("Should return true when default is true", value.willUseInput(true));
        assertFalse("Should return false when default is false", value.willUseInput(false));
    }

    @Test
    public void valueForShouldReturnJacksonInjectClass() {
        assertEquals(JacksonInject.class, EMPTY_VALUE.valueFor());
    }

    // ========================================================================
    // equals() and hashCode() Tests
    // ========================================================================

    @Test
    public void equalsShouldBeTrueForIdenticalValues() {
        // Given
        JacksonInject.Value value1 = JacksonInject.Value.construct("id", true, false);
        JacksonInject.Value value2 = JacksonInject.Value.construct("id", true, false);
        
        // Then
        assertTrue("Values with identical properties should be equal", value1.equals(value2));
        assertEquals("Hash codes should be equal for equal objects", value1.hashCode(), value2.hashCode());
    }

    @Test
    public void equalsShouldBeFalseForDifferentId() {
        JacksonInject.Value value1 = JacksonInject.Value.construct("id1", true, false);
        JacksonInject.Value value2 = JacksonInject.Value.construct("id2", true, false);
        assertFalse(value1.equals(value2));
    }

    @Test
    public void equalsShouldBeFalseForDifferentUseInput() {
        JacksonInject.Value value1 = JacksonInject.Value.construct("id", true, false);
        JacksonInject.Value value2 = JacksonInject.Value.construct("id", false, false);
        assertFalse(value1.equals(value2));
    }

    @Test
    public void equalsShouldBeFalseForDifferentOptional() {
        JacksonInject.Value value1 = JacksonInject.Value.construct("id", true, false);
        JacksonInject.Value value2 = JacksonInject.Value.construct("id", true, true);
        assertFalse(value1.equals(value2));
    }

    @Test
    public void equalsShouldBeFalseForNullAndDifferentTypes() {
        JacksonInject.Value value = JacksonInject.Value.forId("id");
        assertFalse("equals(null) should be false", value.equals(null));
        assertFalse("equals(OtherType) should be false", value.equals("id"));
    }

    // ========================================================================
    // toString() Test
    // ========================================================================

    @Test
    public void toStringShouldRenderCorrectly() {
        // Case 1: All values set
        JacksonInject.Value value1 = JacksonInject.Value.construct("myId", false, true);
        assertEquals("JacksonInject.Value(id=myId,useInput=false,optional=true)", value1.toString());

        // Case 2: Some values null
        JacksonInject.Value value2 = JacksonInject.Value.forId(null).withOptional(false);
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=false)", value2.toString());

        // Case 3: Empty value
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)", EMPTY_VALUE.toString());
    }
}