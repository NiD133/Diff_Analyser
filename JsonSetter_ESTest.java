package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonSetter_ESTest extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMergeWithNullBase() throws Throwable {
        // Test merging a null base with a non-null JsonSetter.Value
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value mergedValue = JsonSetter.Value.merge(null, valueWithNullsAsEmpty);

        assertNotNull(mergedValue);
        assertEquals(Nulls.AS_EMPTY, mergedValue.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, mergedValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentContentNulls() throws Throwable {
        // Test equality of JsonSetter.Value with different content nulls
        JsonSetter.Value valueWithFailContentNulls = JsonSetter.Value.forContentNulls(Nulls.FAIL);
        JsonSetter.Value valueWithDefaultAndFail = JsonSetter.Value.forValueNulls(Nulls.DEFAULT, Nulls.FAIL);

        assertTrue(valueWithDefaultAndFail.equals(valueWithFailContentNulls));
        assertEquals(Nulls.FAIL, valueWithDefaultAndFail.getContentNulls());
        assertEquals(Nulls.DEFAULT, valueWithDefaultAndFail.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentValueNulls() throws Throwable {
        // Test equality of JsonSetter.Value with different value nulls
        JsonSetter.Value valueWithSetNulls = JsonSetter.Value.forValueNulls(Nulls.SET);
        JsonSetter.Value valueWithSkipNulls = JsonSetter.Value.forValueNulls(Nulls.SKIP);

        assertFalse(valueWithSetNulls.equals(valueWithSkipNulls));
        assertEquals(Nulls.DEFAULT, valueWithSkipNulls.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testEqualsWithNonJsonSetterObject() throws Throwable {
        // Test equality of JsonSetter.Value with a non-JsonSetter object
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        Object nonJsonSetterObject = new Object();

        assertFalse(valueWithNullsAsEmpty.equals(nonJsonSetterObject));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullObject() throws Throwable {
        // Test equality of JsonSetter.Value with null
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        assertFalse(valueWithNullsAsEmpty.equals(null));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() throws Throwable {
        // Test equality of JsonSetter.Value with itself
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);

        assertTrue(valueWithDefaultNulls.equals(valueWithDefaultNulls));
    }

    @Test(timeout = 4000)
    public void testFromJsonSetterWithNulls() throws Throwable {
        // Test creating JsonSetter.Value from a mocked JsonSetter with nulls
        JsonSetter mockJsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(mockJsonSetter).contentNulls();
        doReturn(null).when(mockJsonSetter).nulls();

        JsonSetter.Value valueFromMock = JsonSetter.Value.from(mockJsonSetter);
        JsonSetter.Value valueWithSkipContentNulls = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        assertFalse(valueFromMock.equals(valueWithSkipContentNulls));
        assertEquals(Nulls.DEFAULT, valueWithSkipContentNulls.getValueNulls());
        assertEquals(Nulls.SKIP, valueWithSkipContentNulls.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testNonDefaultValueNulls() throws Throwable {
        // Test nonDefaultValueNulls method
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        valueWithDefaultNulls.nonDefaultValueNulls();
    }

    @Test(timeout = 4000)
    public void testNonDefaultValueNullsWithAsEmpty() throws Throwable {
        // Test nonDefaultValueNulls method with AS_EMPTY nulls
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        valueWithNullsAsEmpty.nonDefaultValueNulls();

        assertEquals(Nulls.AS_EMPTY, valueWithNullsAsEmpty.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, valueWithNullsAsEmpty.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithContentNulls() throws Throwable {
        // Test withContentNulls method
        JsonSetter.Value valueWithDefaultAndSkip = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);
        JsonSetter.Value modifiedValue = valueWithDefaultAndSkip.withContentNulls(Nulls.DEFAULT);

        assertNotSame(modifiedValue, valueWithDefaultAndSkip);
        assertEquals(Nulls.DEFAULT, modifiedValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithContentNullsNoChange() throws Throwable {
        // Test withContentNulls method with no change
        JsonSetter.Value emptyValue = JsonSetter.Value.EMPTY;
        JsonSetter.Value modifiedValue = emptyValue.withContentNulls(null);

        assertSame(emptyValue, modifiedValue);
    }

    @Test(timeout = 4000)
    public void testWithContentNullsFromMock() throws Throwable {
        // Test withContentNulls method from a mocked JsonSetter
        JsonSetter mockJsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(mockJsonSetter).contentNulls();
        doReturn(null).when(mockJsonSetter).nulls();

        JsonSetter.Value valueFromMock = JsonSetter.Value.from(mockJsonSetter);
        JsonSetter.Value modifiedValue = valueFromMock.withContentNulls(Nulls.DEFAULT);

        assertSame(modifiedValue, valueFromMock);
    }

    @Test(timeout = 4000)
    public void testWithValueNullsFromMock() throws Throwable {
        // Test withValueNulls method from a mocked JsonSetter
        JsonSetter mockJsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(mockJsonSetter).contentNulls();
        doReturn(null).when(mockJsonSetter).nulls();

        JsonSetter.Value valueFromMock = JsonSetter.Value.from(mockJsonSetter);
        JsonSetter.Value modifiedValue = valueFromMock.withValueNulls(Nulls.DEFAULT, Nulls.SKIP);

        modifiedValue.nonDefaultContentNulls();
        assertEquals(Nulls.DEFAULT, modifiedValue.getValueNulls());
        assertEquals(Nulls.SKIP, modifiedValue.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testWithValueNullsSkip() throws Throwable {
        // Test withValueNulls method with SKIP nulls
        JsonSetter mockJsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(mockJsonSetter).contentNulls();
        doReturn(null).when(mockJsonSetter).nulls();

        JsonSetter.Value valueFromMock = JsonSetter.Value.from(mockJsonSetter);
        JsonSetter.Value modifiedValue = valueFromMock.withValueNulls(Nulls.SKIP, Nulls.SKIP);

        assertEquals(Nulls.SKIP, modifiedValue.getValueNulls());
        assertEquals(Nulls.SKIP, modifiedValue.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testWithValueNullsNoChange() throws Throwable {
        // Test withValueNulls method with no change
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        JsonSetter.Value modifiedValue = valueWithDefaultNulls.withValueNulls(Nulls.DEFAULT, null);

        assertSame(modifiedValue, valueWithDefaultNulls);
    }

    @Test(timeout = 4000)
    public void testWithValueNullsNullValue() throws Throwable {
        // Test withValueNulls method with null value
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        JsonSetter.Value modifiedValue = valueWithDefaultNulls.withValueNulls(null, Nulls.DEFAULT);

        assertEquals(Nulls.DEFAULT, modifiedValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithValueNullsNoChangeWithNull() throws Throwable {
        // Test withValueNulls method with no change using null
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        JsonSetter.Value modifiedValue = valueWithDefaultNulls.withValueNulls(null);

        assertSame(valueWithDefaultNulls, modifiedValue);
    }

    @Test(timeout = 4000)
    public void testMergeValues() throws Throwable {
        // Test merging two JsonSetter.Value instances
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value valueWithSkipContentNulls = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.SKIP);
        JsonSetter.Value mergedValue = JsonSetter.Value.merge(valueWithNullsAsEmpty, valueWithSkipContentNulls);

        assertNotSame(mergedValue, valueWithSkipContentNulls);
        assertEquals(Nulls.SKIP, mergedValue.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, mergedValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testMergeWithMock() throws Throwable {
        // Test merging a mocked JsonSetter.Value with another value
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter mockJsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(mockJsonSetter).contentNulls();
        doReturn(null).when(mockJsonSetter).nulls();

        JsonSetter.Value valueFromMock = JsonSetter.Value.from(mockJsonSetter);
        JsonSetter.Value mergedValue = JsonSetter.Value.merge(valueFromMock, valueWithNullsAsEmpty);

        assertNotSame(mergedValue, valueWithNullsAsEmpty);
        assertTrue(mergedValue.equals(valueWithNullsAsEmpty));
        assertEquals(Nulls.AS_EMPTY, mergedValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testMergeWithSameValues() throws Throwable {
        // Test merging two identical JsonSetter.Value instances
        JsonSetter.Value valueWithFailNulls = new JsonSetter.Value(Nulls.FAIL, Nulls.FAIL);
        JsonSetter.Value valueWithFailValueNulls = JsonSetter.Value.forValueNulls(Nulls.FAIL);
        JsonSetter.Value mergedValue = JsonSetter.Value.merge(valueWithFailNulls, valueWithFailValueNulls);

        assertEquals(Nulls.FAIL, valueWithFailValueNulls.getValueNulls());
        assertSame(mergedValue, valueWithFailNulls);
        assertEquals(Nulls.DEFAULT, valueWithFailValueNulls.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testWithOverrides() throws Throwable {
        // Test withOverrides method
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value valueWithDefaultValueNulls = valueWithNullsAsEmpty.withValueNulls(Nulls.DEFAULT);
        JsonSetter.Value overriddenValue = valueWithNullsAsEmpty.withOverrides(valueWithDefaultValueNulls);

        assertEquals(Nulls.DEFAULT, valueWithDefaultValueNulls.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, valueWithDefaultValueNulls.getContentNulls());
        assertSame(overriddenValue, valueWithNullsAsEmpty);
        assertEquals(Nulls.AS_EMPTY, overriddenValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithOverridesWithNull() throws Throwable {
        // Test withOverrides method with null
        JsonSetter.Value valueWithNullsAsEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value overriddenValue = valueWithNullsAsEmpty.withOverrides(null);

        assertEquals(Nulls.AS_EMPTY, overriddenValue.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, overriddenValue.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testFromNullJsonSetter() throws Throwable {
        // Test creating JsonSetter.Value from a null JsonSetter
        JsonSetter.Value valueFromNull = JsonSetter.Value.from(null);

        assertEquals(Nulls.DEFAULT, valueFromNull.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testMergeWithSelf() throws Throwable {
        // Test merging a JsonSetter.Value with itself
        JsonSetter mockJsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(mockJsonSetter).contentNulls();
        doReturn(null).when(mockJsonSetter).nulls();

        JsonSetter.Value valueFromMock = JsonSetter.Value.from(mockJsonSetter);
        JsonSetter.Value mergedValue = JsonSetter.Value.merge(valueFromMock, valueFromMock);

        assertEquals(Nulls.DEFAULT, mergedValue.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testReadResolveOnEmpty() throws Throwable {
        // Test readResolve method on an empty JsonSetter.Value
        JsonSetter.Value emptyValue = JsonSetter.Value.EMPTY;
        JsonSetter.Value resolvedValue = (JsonSetter.Value) emptyValue.readResolve();

        assertNull(resolvedValue.nonDefaultContentNulls());
    }

    @Test(timeout = 4000)
    public void testReadResolveOnConstructedValue() throws Throwable {
        // Test readResolve method on a constructed JsonSetter.Value
        JsonSetter.Value valueWithDefaultAndSkip = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);
        JsonSetter.Value resolvedValue = (JsonSetter.Value) valueWithDefaultAndSkip.readResolve();

        assertEquals(Nulls.SKIP, resolvedValue.getContentNulls());
        assertEquals(Nulls.DEFAULT, resolvedValue.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testToString() throws Throwable {
        // Test toString method
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        String stringValue = valueWithDefaultNulls.toString();

        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", stringValue);
    }

    @Test(timeout = 4000)
    public void testNonDefaultContentNullsOnEmpty() throws Throwable {
        // Test nonDefaultContentNulls method on an empty JsonSetter.Value
        JsonSetter.Value emptyValue = JsonSetter.Value.empty();
        emptyValue.nonDefaultContentNulls();
    }

    @Test(timeout = 4000)
    public void testGetContentNulls() throws Throwable {
        // Test getContentNulls method
        JsonSetter.Value valueWithSetNulls = JsonSetter.Value.construct(Nulls.SET, Nulls.SET);
        Nulls contentNulls = valueWithSetNulls.getContentNulls();

        assertEquals(Nulls.SET, contentNulls);
        assertEquals(Nulls.SET, valueWithSetNulls.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testValueFor() throws Throwable {
        // Test valueFor method
        JsonSetter.Value valueWithDefaultNulls = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        Class<JsonSetter> jsonSetterClass = valueWithDefaultNulls.valueFor();

        assertEquals(9729, jsonSetterClass.getModifiers());
    }

    @Test(timeout = 4000)
    public void testGetValueNulls() throws Throwable {
        // Test getValueNulls method
        JsonSetter.Value valueWithFailNulls = new JsonSetter.Value(Nulls.FAIL, Nulls.FAIL);
        Nulls valueNulls = valueWithFailNulls.getValueNulls();

        assertEquals(Nulls.FAIL, valueNulls);
    }
}