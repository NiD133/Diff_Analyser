package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class OrderedPropertiesTest extends OrderedProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testComputeIfAbsentAndRemove() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        Integer key = -1107;
        Integer value = -1107;
        ConstantTransformer<Object, Integer> transformer = new ConstantTransformer<>(value);
        Object result = properties.computeIfAbsent(value, transformer);
        boolean removed = properties.remove(key, result);
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testForEachWithMockedBiConsumer() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        BiConsumer<Object, Object> mockConsumer = mock(BiConsumer.class, new ViolatedAssumptionAnswer());
        properties.forEach(mockConsumer);
        assertTrue(properties.isEmpty());
    }

    @Test(timeout = 4000)
    public void testPutIfAbsentWithExistingKey() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        properties.setProperty("key", "value");
        Object result = properties.putIfAbsent("key", "value");
        assertEquals("value", result);
    }

    @Test(timeout = 4000)
    public void testMergeWithNullValue() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        BiFunction<Object, Object, Integer> mockFunction = mock(BiFunction.class, new ViolatedAssumptionAnswer());
        Object result = properties.merge(properties, null, mockFunction);
        assertNull(result);
        assertTrue(properties.isEmpty());
    }

    @Test(timeout = 4000)
    public void testKeySetContainsInsertedKey() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        Integer key = 61;
        properties.put(key, key);
        Set<Object> keys = properties.keySet();
        assertTrue(keys.contains(61));
    }

    @Test(timeout = 4000)
    public void testLoadPropertiesFromReader() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        StringReader reader = new StringReader("key=value");
        properties.load(reader);
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        assertFalse(entries.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCloneAndRemoveKey() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        HashMap<Integer, Integer> map = new HashMap<>();
        Integer key = 0;
        properties.putIfAbsent(key, map);
        Object cloned = properties.clone();
        properties.remove(key);
        try {
            cloned.toString();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("java.util.LinkedHashMap$LinkedHashIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringWithSelfReference() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        properties.put(properties, properties);
        String result = properties.toString();
        assertEquals("{(this Map)=(this Map)}", result);
    }

    // Additional tests with meaningful names and comments for clarity

    @Test(timeout = 4000)
    public void testRemoveWithNullKeyAndValue() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        try {
            properties.remove(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeIfAbsentWithNullFunction() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        try {
            properties.computeIfAbsent(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeWithNullBiFunction() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        Object key = new Object();
        try {
            properties.compute(key, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeIfAbsentWithExceptionTransformer() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        Transformer<Object, Integer> transformer = ExceptionTransformer.exceptionTransformer();
        try {
            properties.computeIfAbsent("key", transformer);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeIfAbsentWithSwitchTransformer() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        SwitchTransformer<Object, Integer> transformer = new SwitchTransformer<>(new Predicate[0], new Transformer[0], NOPTransformer.nopTransformer());
        try {
            properties.computeIfAbsent("key", transformer);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.collections4.functors.SwitchTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testClearProperties() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        properties.clear();
        assertTrue(properties.isEmpty());
    }

    @Test(timeout = 4000)
    public void testPutAllWithEmptyMap() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        HashMap<Integer, Integer> map = new HashMap<>();
        properties.putAll(map);
        assertTrue(properties.isEmpty());
    }

    @Test(timeout = 4000)
    public void testToStringWithEmptyProperties() throws Throwable {
        OrderedProperties properties = new OrderedProperties();
        String result = properties.toString();
        assertEquals("{}", result);
    }
}