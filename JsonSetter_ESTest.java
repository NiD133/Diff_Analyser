package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * Test suite for JsonSetter.Value class functionality.
 * Tests creation, merging, equality, and configuration of JsonSetter values.
 */
public class JsonSetterValueTest {

    // ========== Factory Method Tests ==========
    
    @Test
    public void shouldCreateValueFromNullJsonSetter() {
        JsonSetter.Value result = JsonSetter.Value.from(null);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test
    public void shouldCreateValueFromMockedJsonSetter() {
        JsonSetter mockAnnotation = mock(JsonSetter.class);
        when(mockAnnotation.nulls()).thenReturn(null);
        when(mockAnnotation.contentNulls()).thenReturn(null);
        
        JsonSetter.Value result = JsonSetter.Value.from(mockAnnotation);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test
    public void shouldCreateValueForValueNullsOnly() {
        JsonSetter.Value result = JsonSetter.Value.forValueNulls(Nulls.SET);
        
        assertEquals(Nulls.SET, result.getValueNulls());
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test
    public void shouldCreateValueForBothValueAndContentNulls() {
        JsonSetter.Value result = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.SKIP);
        
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test
    public void shouldCreateValueForContentNullsOnly() {
        JsonSetter.Value result = JsonSetter.Value.forContentNulls(Nulls.FAIL);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.FAIL, result.getContentNulls());
    }

    @Test
    public void shouldConstructValueWithBothParameters() {
        JsonSetter.Value result = JsonSetter.Value.construct(Nulls.SET, Nulls.SKIP);
        
        assertEquals(Nulls.SET, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test
    public void shouldReturnEmptyInstance() {
        JsonSetter.Value empty = JsonSetter.Value.empty();
        
        assertEquals(Nulls.DEFAULT, empty.getValueNulls());
        assertEquals(Nulls.DEFAULT, empty.getContentNulls());
    }

    // ========== Merge Operation Tests ==========

    @Test
    public void shouldMergeWithNullBase() {
        JsonSetter.Value override = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        
        JsonSetter.Value result = JsonSetter.Value.merge(null, override);
        
        assertNotNull(result);
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    @Test
    public void shouldMergeOverridingContentNulls() {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value override = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.SKIP);
        
        JsonSetter.Value result = JsonSetter.Value.merge(base, override);
        
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test
    public void shouldMergeWithDefaultBase() {
        JsonSetter.Value base = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        JsonSetter.Value override = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        
        JsonSetter.Value result = JsonSetter.Value.merge(base, override);
        
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    @Test
    public void shouldReturnBaseWhenOverrideHasNoChanges() {
        JsonSetter.Value base = new JsonSetter.Value(Nulls.FAIL, Nulls.FAIL);
        JsonSetter.Value override = JsonSetter.Value.forValueNulls(Nulls.FAIL);
        
        JsonSetter.Value result = JsonSetter.Value.merge(base, override);
        
        assertSame(base, result);
    }

    @Test
    public void shouldMergeIdenticalValues() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = JsonSetter.Value.merge(value, value);
        
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    // ========== With Methods Tests ==========

    @Test
    public void shouldReturnSameInstanceWhenContentNullsUnchanged() {
        JsonSetter.Value original = JsonSetter.Value.EMPTY;
        
        JsonSetter.Value result = original.withContentNulls(null);
        
        assertSame(original, result);
    }

    @Test
    public void shouldReturnSameInstanceWhenSettingDefaultContentNulls() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withContentNulls(Nulls.DEFAULT);
        
        assertSame(original, result);
    }

    @Test
    public void shouldCreateNewInstanceWhenContentNullsChanged() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);
        
        JsonSetter.Value result = original.withContentNulls(Nulls.DEFAULT);
        
        assertNotSame(original, result);
        assertEquals(Nulls.DEFAULT, original.getValueNulls());
    }

    @Test
    public void shouldUpdateValueNullsWithBothParameters() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withValueNulls(Nulls.DEFAULT, Nulls.SKIP);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test
    public void shouldUpdateBothNullsSettings() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withValueNulls(Nulls.SKIP, Nulls.SKIP);
        
        assertEquals(Nulls.SKIP, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test
    public void shouldReturnSameInstanceWhenValueNullsUnchanged() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withValueNulls(Nulls.DEFAULT, null);
        
        assertSame(original, result);
    }

    @Test
    public void shouldHandleNullValueNullsParameter() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withValueNulls(null, Nulls.DEFAULT);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
    }

    @Test
    public void shouldReturnSameInstanceWhenSingleValueNullsUnchanged() {
        JsonSetter.Value original = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withValueNulls(null);
        
        assertSame(original, result);
    }

    @Test
    public void shouldUpdateValueNullsOnly() {
        JsonSetter.Value original = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        
        JsonSetter.Value result = original.withValueNulls(Nulls.DEFAULT);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    // ========== Override Tests ==========

    @Test
    public void shouldReturnSameInstanceWhenOverridingWithDefaults() {
        JsonSetter.Value original = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value overrides = original.withValueNulls(Nulls.DEFAULT);
        
        JsonSetter.Value result = original.withOverrides(overrides);
        
        assertSame(original, result);
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
    }

    @Test
    public void shouldHandleNullOverrides() {
        JsonSetter.Value original = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        
        JsonSetter.Value result = original.withOverrides(null);
        
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    // ========== Equality Tests ==========

    @Test
    public void shouldBeEqualWhenSameInstance() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        assertTrue(value.equals(value));
    }

    @Test
    public void shouldBeEqualWhenSameValues() {
        JsonSetter.Value value1 = JsonSetter.Value.forValueNulls(Nulls.DEFAULT, Nulls.FAIL);
        JsonSetter.Value value2 = JsonSetter.Value.forContentNulls(Nulls.FAIL);
        
        assertTrue(value1.equals(value2));
        assertEquals(Nulls.FAIL, value1.getContentNulls());
        assertEquals(Nulls.DEFAULT, value1.getValueNulls());
    }

    @Test
    public void shouldNotBeEqualWhenDifferentValueNulls() {
        JsonSetter.Value value1 = JsonSetter.Value.forValueNulls(Nulls.SET);
        JsonSetter.Value value2 = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        
        assertFalse(value1.equals(value2));
        assertEquals(Nulls.DEFAULT, value2.getContentNulls());
    }

    @Test
    public void shouldNotBeEqualToArbitraryObject() {
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        Object other = new Object();
        
        assertFalse(value.equals(other));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        
        assertFalse(value.equals(null));
    }

    @Test
    public void shouldNotBeEqualWhenDifferentContentNulls() {
        JsonSetter mockAnnotation = mock(JsonSetter.class);
        when(mockAnnotation.nulls()).thenReturn(null);
        when(mockAnnotation.contentNulls()).thenReturn(null);
        
        JsonSetter.Value value1 = JsonSetter.Value.from(mockAnnotation);
        JsonSetter.Value value2 = JsonSetter.Value.forContentNulls(Nulls.SKIP);
        
        assertFalse(value1.equals(value2));
        assertEquals(Nulls.DEFAULT, value2.getValueNulls());
        assertEquals(Nulls.SKIP, value2.getContentNulls());
    }

    // ========== Non-Default Value Tests ==========

    @Test
    public void shouldReturnNullForDefaultValueNulls() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        assertNull(value.nonDefaultValueNulls());
    }

    @Test
    public void shouldReturnValueForNonDefaultValueNulls() {
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        
        assertEquals(Nulls.AS_EMPTY, value.nonDefaultValueNulls());
    }

    @Test
    public void shouldReturnNullForDefaultContentNulls() {
        JsonSetter.Value empty = JsonSetter.Value.empty();
        
        assertNull(empty.nonDefaultContentNulls());
    }

    @Test
    public void shouldReturnValueForNonDefaultContentNulls() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);
        
        assertEquals(Nulls.SKIP, value.nonDefaultContentNulls());
    }

    // ========== Serialization Tests ==========

    @Test
    public void shouldHandleReadResolveForEmptyValue() {
        JsonSetter.Value empty = JsonSetter.Value.EMPTY;
        
        JsonSetter.Value resolved = (JsonSetter.Value) empty.readResolve();
        
        assertNull(resolved.nonDefaultContentNulls());
    }

    @Test
    public void shouldHandleReadResolveForNonEmptyValue() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);
        
        JsonSetter.Value resolved = (JsonSetter.Value) value.readResolve();
        
        assertEquals(Nulls.SKIP, resolved.getContentNulls());
        assertEquals(Nulls.DEFAULT, resolved.getValueNulls());
    }

    // ========== Utility Method Tests ==========

    @Test
    public void shouldGenerateCorrectToString() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        String result = value.toString();
        
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", result);
    }

    @Test
    public void shouldReturnCorrectValueForClass() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        
        Class<JsonSetter> result = value.valueFor();
        
        assertEquals(9729, result.getModifiers());
    }

    @Test
    public void shouldGetContentNulls() {
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.SET, Nulls.SET);
        
        Nulls result = value.getContentNulls();
        
        assertEquals(Nulls.SET, result);
        assertEquals(Nulls.SET, value.getValueNulls());
    }

    @Test
    public void shouldGetValueNulls() {
        JsonSetter.Value value = new JsonSetter.Value(Nulls.FAIL, Nulls.FAIL);
        
        Nulls result = value.getValueNulls();
        
        assertEquals(Nulls.FAIL, result);
    }
}