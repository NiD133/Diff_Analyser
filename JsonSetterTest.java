package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTest {
    private final static class Bogus {
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    // Tests for empty instance behavior
    @Test
    void emptyInstance_hasExpectedDefaultValues() {
        assertEquals(Nulls.DEFAULT, EMPTY.getValueNulls(), 
            "Empty instance should have DEFAULT value nulls");
        assertEquals(Nulls.DEFAULT, EMPTY.getContentNulls(), 
            "Empty instance should have DEFAULT content nulls");
    }

    @Test
    void emptyInstance_nonDefaultMethodsReturnNull() {
        assertNull(EMPTY.nonDefaultValueNulls(), 
            "Empty instance should return null for nonDefaultValueNulls");
        assertNull(EMPTY.nonDefaultContentNulls(), 
            "Empty instance should return null for nonDefaultContentNulls");
    }

    @Test
    void emptyInstance_valueForReturnsCorrectClass() {
        assertEquals(JsonSetter.class, EMPTY.valueFor(), 
            "valueFor() should return JsonSetter.class");
    }

    // Tests for standard methods (toString, hashCode, equals)
    @Test
    void toString_returnsExpectedFormat() {
        String expected = "JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)";
        assertEquals(expected, EMPTY.toString(), 
            "toString() should return expected format");
    }

    @Test
    void hashCode_emptyInstance_returnsNonZero() {
        assertNotEquals(0, EMPTY.hashCode(), 
            "hashCode() for empty instance should not be zero");
    }

    @Test
    void equals_withSameInstance_returnsTrue() {
        assertEquals(EMPTY, EMPTY, 
            "Instance should equal itself");
    }

    @Test
    void equals_withNull_returnsFalse() {
        assertNotEquals(null, EMPTY, 
            "Instance should not equal null");
    }

    @Test
    void equals_withDifferentType_returnsFalse() {
        assertNotEquals("xyz", EMPTY, 
            "Instance should not equal different type");
    }

    @Test
    void equals_withSameValues_returnsTrue() {
        JsonSetter.Value value1 = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        JsonSetter.Value value2 = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        assertEquals(value1, value2, 
            "Instances with same values should be equal");
    }

    @Test
    void equals_withDifferentValues_returnsFalse() {
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        JsonSetter.Value differentValue = JsonSetter.Value.forValueNulls(Nulls.FAIL);
        assertNotEquals(value, differentValue, 
            "Instances with different values should not be equal");
    }

    // Tests for fromAnnotation method
    @Test
    void fromAnnotation_withNull_returnsEmptyInstance() {
        assertSame(EMPTY, JsonSetter.Value.from(null), 
            "from(null) should return empty instance");
    }

    @Test
    void fromAnnotation_withAnnotation_returnsConfiguredValues() throws Exception {
        JsonSetter ann = Bogus.class.getField("field").getAnnotation(JsonSetter.class);
        JsonSetter.Value value = JsonSetter.Value.from(ann);
        
        assertEquals(Nulls.FAIL, value.getValueNulls(), 
            "Value nulls should match annotation configuration");
        assertEquals(Nulls.SKIP, value.getContentNulls(), 
            "Content nulls should match annotation configuration");
    }

    // Tests for construction methods
    @Test
    void construct_withNulls_returnsEmptyInstance() {
        assertSame(EMPTY, JsonSetter.Value.construct(null, null), 
            "construct(null, null) should return empty instance");
    }

    // Tests for factory methods
    @Test
    void forContentNulls_setsContentNullsCorrectly() {
        JsonSetter.Value value = JsonSetter.Value.forContentNulls(Nulls.SET);
        
        assertEquals(Nulls.DEFAULT, value.getValueNulls(), 
            "Value nulls should remain DEFAULT");
        assertEquals(Nulls.SET, value.getContentNulls(), 
            "Content nulls should be set to SET");
        assertEquals(Nulls.SET, value.nonDefaultContentNulls(), 
            "nonDefaultContentNulls should return SET");
    }

    @Test
    void forValueNulls_setsValueNullsCorrectly() {
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        
        assertEquals(Nulls.SKIP, value.getValueNulls(), 
            "Value nulls should be set to SKIP");
        assertEquals(Nulls.DEFAULT, value.getContentNulls(), 
            "Content nulls should remain DEFAULT");
        assertEquals(Nulls.SKIP, value.nonDefaultValueNulls(), 
            "nonDefaultValueNulls should return SKIP");
    }

    // Tests for with* methods
    @Test
    void withContentNulls_withNull_returnsOriginalInstance() {
        JsonSetter.Value result = EMPTY.withContentNulls(null);
        assertSame(EMPTY, result, 
            "Setting content nulls to null should return original instance");
    }

    @Test
    void withContentNulls_withNewValue_returnsNewInstance() {
        JsonSetter.Value result = EMPTY.withContentNulls(Nulls.FAIL);
        
        assertEquals(Nulls.FAIL, result.getContentNulls(), 
            "Content nulls should be updated");
        assertNotSame(EMPTY, result, 
            "Should return new instance when value changes");
    }

    @Test
    void withContentNulls_withSameValue_returnsSameInstance() {
        JsonSetter.Value original = EMPTY.withContentNulls(Nulls.FAIL);
        JsonSetter.Value result = original.withContentNulls(Nulls.FAIL);
        assertSame(original, result, 
            "Should return same instance when setting same value");
    }

    @Test
    void withValueNulls_withNewValue_returnsNewInstance() {
        JsonSetter.Value original = EMPTY.withContentNulls(Nulls.FAIL);
        JsonSetter.Value result = original.withValueNulls(Nulls.SKIP);
        
        assertEquals(Nulls.SKIP, result.getValueNulls(), 
            "Value nulls should be updated");
        assertEquals(Nulls.FAIL, result.getContentNulls(), 
            "Content nulls should remain unchanged");
    }

    @Test
    void withValueNulls_withNulls_returnsNewInstanceWithDefaults() {
        JsonSetter.Value original = JsonSetter.Value.forValueNulls(Nulls.SKIP, Nulls.FAIL);
        JsonSetter.Value result = original.withValueNulls(null, null);
        
        assertEquals(Nulls.DEFAULT, result.getValueNulls(), 
            "Value nulls should be reset to DEFAULT");
        assertEquals(Nulls.DEFAULT, result.getContentNulls(), 
            "Content nulls should be reset to DEFAULT");
    }

    @Test
    void withValueNulls_withSameValues_returnsSameInstance() {
        JsonSetter.Value original = JsonSetter.Value.forValueNulls(Nulls.SKIP, Nulls.FAIL);
        JsonSetter.Value result = original.withValueNulls(Nulls.SKIP, Nulls.FAIL);
        assertSame(original, result, 
            "Should return same instance when values don't change");
    }

    @Test
    void withOverrides_mergesValuesCorrectly() {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        JsonSetter.Value overrides = JsonSetter.Value.forContentNulls(Nulls.FAIL);
        JsonSetter.Value merged = base.withOverrides(overrides);
        
        assertEquals(overrides, merged, 
            "Merged value should match overrides");
        assertNotSame(overrides, merged, 
            "Merging should create new instance");
    }
}