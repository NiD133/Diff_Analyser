/*
 * Refactored test suite for JsonSetter.Value with improved readability
 */
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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class JsonSetter_ESTest extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMergeWithNullBaseReturnsNonNullValue() throws Throwable {
        Nulls nulls = Nulls.AS_EMPTY;
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(nulls, nulls);
        JsonSetter.Value result = JsonSetter.Value.merge(null, value);
        
        assertNotNull(result);
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentFactoryMethods() throws Throwable {
        Nulls valueNulls = Nulls.DEFAULT;
        Nulls contentNulls = Nulls.FAIL;
        JsonSetter.Value value1 = JsonSetter.Value.forContentNulls(contentNulls);
        JsonSetter.Value value2 = JsonSetter.Value.forValueNulls(valueNulls, contentNulls);
        
        assertTrue(value2.equals(value1));
        assertEquals(Nulls.FAIL, value2.getContentNulls());
        assertEquals(Nulls.DEFAULT, value2.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testNotEqualsWhenValueNullsDiffer() throws Throwable {
        JsonSetter.Value value1 = JsonSetter.Value.forValueNulls(Nulls.SET);
        JsonSetter.Value value2 = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        
        assertFalse(value1.equals(value2));
        assertEquals(Nulls.DEFAULT, value2.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testNotEqualsWithDifferentObjectType() throws Throwable {
        Nulls nulls = Nulls.AS_EMPTY;
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(nulls, nulls);
        Object other = new Object();
        
        assertFalse(value.equals(other));
        assertEquals(Nulls.AS_EMPTY, value.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, value.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testNotEqualsWithNullObject() throws Throwable {
        Nulls nulls = Nulls.AS_EMPTY;
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(nulls, nulls);
        
        assertFalse(value.equals(null));
        assertEquals(Nulls.AS_EMPTY, value.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, value.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() throws Throwable {
        Nulls nulls = Nulls.DEFAULT;
        JsonSetter.Value value = JsonSetter.Value.construct(nulls, nulls);
        
        assertTrue(value.equals(value));
    }

    @Test(timeout = 4000)
    public void testNotEqualsWhenContentNullsDiffer() throws Throwable {
        JsonSetter jsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonSetter).contentNulls();
        doReturn(null).when(jsonSetter).nulls();
        
        JsonSetter.Value value1 = JsonSetter.Value.from(jsonSetter);
        JsonSetter.Value value2 = JsonSetter.Value.forContentNulls(Nulls.SKIP);
        
        assertFalse(value1.equals(value2));
        assertEquals(Nulls.DEFAULT, value2.getValueNulls());
        assertEquals(Nulls.SKIP, value2.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testNonDefaultValueNullsWithDefault() throws Throwable {
        Nulls nulls = Nulls.DEFAULT;
        JsonSetter.Value value = JsonSetter.Value.construct(nulls, nulls);
        value.nonDefaultValueNulls();  // Should return null
    }

    @Test(timeout = 4000)
    public void testNonDefaultValueNullsWithNonDefault() throws Throwable {
        Nulls nulls = Nulls.AS_EMPTY;
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(nulls, nulls);
        value.nonDefaultValueNulls();  // Should return AS_EMPTY
        assertEquals(Nulls.AS_EMPTY, value.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, value.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithContentNullsReturnsNewInstanceWhenDifferent() throws Throwable {
        Nulls valueNulls = Nulls.DEFAULT;
        Nulls contentNulls = Nulls.SKIP;
        JsonSetter.Value original = JsonSetter.Value.construct(valueNulls, contentNulls);
        JsonSetter.Value result = original.withContentNulls(valueNulls);
        
        assertNotSame(original, result);
        assertEquals(Nulls.DEFAULT, original.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithContentNullsReturnsSameInstanceWhenNull() throws Throwable {
        JsonSetter.Value original = JsonSetter.Value.EMPTY;
        JsonSetter.Value result = original.withContentNulls(null);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testWithContentNullsReturnsSameInstanceWhenSettingToDefault() throws Throwable {
        JsonSetter jsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonSetter).contentNulls();
        doReturn(null).when(jsonSetter).nulls();
        
        JsonSetter.Value original = JsonSetter.Value.from(jsonSetter);
        JsonSetter.Value result = original.withContentNulls(Nulls.DEFAULT);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testNonDefaultContentNullsAfterWithValueNulls() throws Throwable {
        JsonSetter jsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonSetter).contentNulls();
        doReturn(null).when(jsonSetter).nulls();
        
        JsonSetter.Value original = JsonSetter.Value.from(jsonSetter);
        JsonSetter.Value modified = original.withValueNulls(Nulls.DEFAULT, Nulls.SKIP);
        modified.nonDefaultContentNulls();  // Should return SKIP
        assertEquals(Nulls.DEFAULT, modified.getValueNulls());
        assertEquals(Nulls.SKIP, modified.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testWithValueNullsSetsBothNulls() throws Throwable {
        JsonSetter jsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonSetter).contentNulls();
        doReturn(null).when(jsonSetter).nulls();
        
        JsonSetter.Value original = JsonSetter.Value.from(jsonSetter);
        JsonSetter.Value result = original.withValueNulls(Nulls.SKIP, Nulls.SKIP);
        
        assertEquals(Nulls.SKIP, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testWithValueNullsReturnsSameInstanceWhenNoChange() throws Throwable {
        Nulls nulls = Nulls.DEFAULT;
        JsonSetter.Value original = JsonSetter.Value.construct(nulls, nulls);
        JsonSetter.Value result = original.withValueNulls(nulls, null);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testWithValueNullsSingleArgReturnsSameInstanceWhenSettingToNull() throws Throwable {
        Nulls nulls = Nulls.DEFAULT;
        JsonSetter.Value original = JsonSetter.Value.construct(nulls, nulls);
        JsonSetter.Value result = original.withValueNulls(null);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testMergeTakesContentNullsFromOverride() throws Throwable {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value override = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.SKIP);
        JsonSetter.Value result = JsonSetter.Value.merge(base, override);
        
        assertNotSame(override, result);
        assertEquals(Nulls.SKIP, result.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testMergeWithBaseDefaultAndOverrideNonDefault() throws Throwable {
        JsonSetter jsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonSetter).contentNulls();
        doReturn(null).when(jsonSetter).nulls();
        
        JsonSetter.Value base = JsonSetter.Value.from(jsonSetter);
        JsonSetter.Value override = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value result = JsonSetter.Value.merge(base, override);
        
        assertNotSame(override, result);
        assertTrue(result.equals(override));
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testMergeWithOverrideHavingDefaultContentNullsReturnsBase() throws Throwable {
        JsonSetter.Value base = new JsonSetter.Value(Nulls.FAIL, Nulls.FAIL);
        JsonSetter.Value override = JsonSetter.Value.forValueNulls(Nulls.FAIL);
        JsonSetter.Value result = JsonSetter.Value.merge(base, override);
        
        assertSame(base, result);
        assertEquals(Nulls.FAIL, override.getValueNulls());
        assertEquals(Nulls.DEFAULT, override.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testWithOverridesReturnsSameInstanceWhenNoChange() throws Throwable {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value modified = base.withValueNulls(Nulls.DEFAULT);
        JsonSetter.Value result = base.withOverrides(modified);
        
        assertEquals(Nulls.DEFAULT, modified.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, modified.getContentNulls());
        assertSame(base, result);
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testWithOverridesWithNullReturnsSameValues() throws Throwable {
        Nulls nulls = Nulls.AS_EMPTY;
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(nulls, nulls);
        JsonSetter.Value result = value.withOverrides(null);
        
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testFromNullReturnsEmpty() throws Throwable {
        JsonSetter.Value result = JsonSetter.Value.from(null);
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testMergeOfTwoEmptyInstances() throws Throwable {
        JsonSetter jsonSetter = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonSetter).contentNulls();
        doReturn(null).when(jsonSetter).nulls();
        
        JsonSetter.Value empty = JsonSetter.Value.from(jsonSetter);
        JsonSetter.Value result = JsonSetter.Value.merge(empty, empty);
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test(timeout = 4000)
    public void testReadResolveForEmptyInstance() throws Throwable {
        JsonSetter.Value value = JsonSetter.Value.EMPTY;
        JsonSetter.Value result = (JsonSetter.Value) value.readResolve();
        assertNull(result.nonDefaultContentNulls());
    }

    @Test(timeout = 4000)
    public void testReadResolvePreservesState() throws Throwable {
        Nulls valueNulls = Nulls.DEFAULT;
        Nulls contentNulls = Nulls.SKIP;
        JsonSetter.Value value = JsonSetter.Value.construct(valueNulls, contentNulls);
        JsonSetter.Value result = (JsonSetter.Value) value.readResolve();
        
        assertEquals(Nulls.SKIP, result.getContentNulls());
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testToString() throws Throwable {
        Nulls nulls = Nulls.DEFAULT;
        JsonSetter.Value value = JsonSetter.Value.construct(nulls, nulls);
        String result = value.toString();
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", result);
    }

    @Test(timeout = 4000)
    public void testNonDefaultContentNullsForEmptyInstance() throws Throwable {
        JsonSetter.Value value = JsonSetter.Value.empty();
        value.nonDefaultContentNulls();  // Should return null
    }

    @Test(timeout = 4000)
    public void testGetContentNulls() throws Throwable {
        Nulls nulls = Nulls.SET;
        JsonSetter.Value value = JsonSetter.Value.construct(nulls, nulls);
        Nulls result = value.getContentNulls();
        assertEquals(Nulls.SET, result);
        assertEquals(Nulls.SET, value.getValueNulls());
    }

    @Test(timeout = 4000)
    public void testValueFor() throws Throwable {
        Nulls nulls = Nulls.DEFAULT;
        JsonSetter.Value value = JsonSetter.Value.construct(nulls, nulls);
        Class<JsonSetter> result = value.valueFor();
        assertEquals(JsonSetter.class, result);
    }

    @Test(timeout = 4000)
    public void testGetValueNulls() throws Throwable {
        Nulls nulls = Nulls.FAIL;
        JsonSetter.Value value = new JsonSetter.Value(nulls, nulls);
        Nulls result = value.getValueNulls();
        assertEquals(Nulls.FAIL, result);
    }
}