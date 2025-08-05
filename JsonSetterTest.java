package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTest {

    // Test class to simulate a field with JsonSetter annotation
    private static final class TestClass {
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    // Constant representing an empty JsonSetter.Value
    private static final JsonSetter.Value EMPTY_VALUE = JsonSetter.Value.empty();

    @Test
    public void testEmptyValueDefaults() {
        // Verify default null handling settings
        assertEquals(Nulls.DEFAULT, EMPTY_VALUE.getValueNulls());
        assertEquals(Nulls.DEFAULT, EMPTY_VALUE.getContentNulls());

        // Verify the class type
        assertEquals(JsonSetter.class, EMPTY_VALUE.valueFor());

        // Verify non-default null handling returns null
        assertNull(EMPTY_VALUE.nonDefaultValueNulls());
        assertNull(EMPTY_VALUE.nonDefaultContentNulls());
    }

    @Test
    public void testStandardMethods() {
        // Verify toString representation
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", EMPTY_VALUE.toString());

        // Verify hashCode is non-zero
        int hashCode = EMPTY_VALUE.hashCode();
        assertNotEquals(0, hashCode);

        // Verify equality checks
        assertEquals(EMPTY_VALUE, EMPTY_VALUE);
        assertNotEquals(null, EMPTY_VALUE);
        assertNotEquals("xyz", EMPTY_VALUE);
    }

    @Test
    public void testValueFromAnnotation() throws Exception {
        // Verify creating JsonSetter.Value from null returns EMPTY_VALUE
        assertSame(EMPTY_VALUE, JsonSetter.Value.from(null));

        // Retrieve annotation from TestClass field
        JsonSetter annotation = TestClass.class.getField("field").getAnnotation(JsonSetter.class);
        JsonSetter.Value valueFromAnnotation = JsonSetter.Value.from(annotation);

        // Verify null handling settings from annotation
        assertEquals(Nulls.FAIL, valueFromAnnotation.getValueNulls());
        assertEquals(Nulls.SKIP, valueFromAnnotation.getContentNulls());
    }

    @Test
    public void testConstructMethod() {
        // Verify constructing with nulls returns EMPTY_VALUE
        JsonSetter.Value constructedValue = JsonSetter.Value.construct(null, null);
        assertSame(EMPTY_VALUE, constructedValue);
    }

    @Test
    public void testFactoryMethods() {
        // Test forContentNulls factory method
        JsonSetter.Value contentNullsValue = JsonSetter.Value.forContentNulls(Nulls.SET);
        assertEquals(Nulls.DEFAULT, contentNullsValue.getValueNulls());
        assertEquals(Nulls.SET, contentNullsValue.getContentNulls());
        assertEquals(Nulls.SET, contentNullsValue.nonDefaultContentNulls());

        // Test forValueNulls factory method
        JsonSetter.Value valueNullsValue = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, valueNullsValue.getValueNulls());
        assertEquals(Nulls.DEFAULT, valueNullsValue.getContentNulls());
        assertEquals(Nulls.SKIP, valueNullsValue.nonDefaultValueNulls());
    }

    @Test
    public void testSimpleMerge() {
        // Test merging content nulls
        JsonSetter.Value mergedValue = EMPTY_VALUE.withContentNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, mergedValue.getContentNulls());

        // Test merging value nulls
        mergedValue = mergedValue.withValueNulls(Nulls.FAIL);
        assertEquals(Nulls.FAIL, mergedValue.getValueNulls());
    }

    @Test
    public void testWithMethods() {
        // Test withContentNulls method
        JsonSetter.Value valueWithContentNulls = EMPTY_VALUE.withContentNulls(null);
        assertSame(EMPTY_VALUE, valueWithContentNulls);

        valueWithContentNulls = valueWithContentNulls.withContentNulls(Nulls.FAIL);
        assertEquals(Nulls.FAIL, valueWithContentNulls.getContentNulls());
        assertSame(valueWithContentNulls, valueWithContentNulls.withContentNulls(Nulls.FAIL));

        // Test withValueNulls method
        JsonSetter.Value valueWithValueNulls = valueWithContentNulls.withValueNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, valueWithValueNulls.getValueNulls());
        assertNotEquals(valueWithContentNulls, valueWithValueNulls);

        // Test resetting to default nulls
        JsonSetter.Value resetValue = valueWithValueNulls.withValueNulls(null, null);
        assertEquals(Nulls.DEFAULT, resetValue.getContentNulls());
        assertEquals(Nulls.DEFAULT, resetValue.getValueNulls());
        assertSame(resetValue, resetValue.withValueNulls(null, null));

        // Test merging with overrides
        JsonSetter.Value mergedWithOverrides = resetValue.withOverrides(valueWithValueNulls);
        assertNotSame(valueWithValueNulls, mergedWithOverrides);
        assertEquals(mergedWithOverrides, valueWithValueNulls);
    }
}