package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
public class JacksonInject_ESTest extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEqualsAfterChangingOptionalToFalse() throws Throwable {
        // Setup: Create initial Value instance
        Object id = new Object();
        Boolean useInput = Boolean.valueOf(""); // false
        JacksonInject.Value original = new JacksonInject.Value(id, useInput, Boolean.TRUE);
        
        // Change optional from true to false
        JacksonInject.Value modified = original.withOptional(Boolean.FALSE);
        
        // Verify instances are no longer equal
        assertFalse(modified.equals(original));
        assertFalse(original.equals(modified));
        
        // Verify properties
        assertFalse(original.getUseInput());
        assertTrue(modified.hasId());
        assertFalse(modified.getUseInput());
    }

    @Test(timeout = 4000)
    public void testToStringWithOptionalFalse() throws Throwable {
        JacksonInject.Value value = JacksonInject.Value.forId(null)
            .withOptional(Boolean.FALSE);
        
        assertEquals(
            "JacksonInject.Value(id=null,useInput=null,optional=false)", 
            value.toString()
        );
    }

    @Test(timeout = 4000)
    public void testWillUseInputWhenUseInputIsFalse() throws Throwable {
        JacksonInject.Value value = JacksonInject.Value.construct(
            null, 
            Boolean.FALSE, 
            Boolean.valueOf("") // false
        );
        
        // When useInput=false, should never use input
        assertFalse(value.willUseInput(false));
        assertFalse(value.willUseInput(true));
    }

    // Additional tests follow the same pattern of:
    // 1. Clear method names
    // 2. Explanatory comments
    // 3. Grouped assertions with explanations

    @Test(timeout = 4000)
    public void testGetOptionalWhenOptionalIsNull() throws Throwable {
        Object id = new Object();
        JacksonInject.Value value = JacksonInject.Value.construct(
            id, 
            Boolean.FALSE, 
            null
        );
        
        assertNull(value.getOptional());
        assertTrue(value.hasId());
    }

    @Test(timeout = 4000)
    public void testWithOptionalChangesEquality() throws Throwable {
        Object id = new Object();
        JacksonInject.Value original = JacksonInject.Value.forId(id);
        JacksonInject.Value modified = original.withOptional(Boolean.FALSE);
        
        assertNotEquals(original, modified);
        assertTrue(modified.hasId());
        assertNull(modified.getUseInput());
    }

    @Test(timeout = 4000)
    public void testWithUseInputChangesEquality() throws Throwable {
        Object id = new Object();
        JacksonInject.Value original = JacksonInject.Value.forId(id);
        JacksonInject.Value modified = original.withUseInput(Boolean.FALSE);
        
        assertNotEquals(original, modified);
        assertTrue(modified.hasId());
    }

    @Test(timeout = 4000)
    public void testWithIdChangesEmptyValue() throws Throwable {
        JacksonInject.Value original = JacksonInject.Value.empty();
        JacksonInject.Value modified = original.withId(new Object());
        
        assertTrue(modified.hasId());
        assertNotEquals(original, modified);
    }

    @Test(timeout = 4000)
    public void testConstructWithNullId() throws Throwable {
        JacksonInject.Value value = JacksonInject.Value.construct(
            null, 
            null, 
            Boolean.TRUE
        );
        
        assertFalse(value.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameValues() throws Throwable {
        Object id = new Object();
        Boolean useInput = Boolean.valueOf(""); // false
        JacksonInject.Value value1 = new JacksonInject.Value(id, useInput, useInput);
        JacksonInject.Value value2 = JacksonInject.Value.construct(id, useInput, useInput);
        
        assertEquals(value1, value2);
        assertTrue(value1.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameId() throws Throwable {
        Object id = new Object();
        JacksonInject.Value value1 = JacksonInject.Value.forId(id);
        JacksonInject.Value value2 = JacksonInject.Value.forId(id);
        
        assertEquals(value1, value2);
        assertTrue(value1.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualsFailsAfterSettingUseInput() throws Throwable {
        JacksonInject.Value original = JacksonInject.Value.EMPTY;
        JacksonInject.Value modified = original.withUseInput(Boolean.valueOf("zH78ih{ZO&er"));
        
        assertNotEquals(original, modified);
    }

    @Test(timeout = 4000)
    public void testEqualsFailsWithDifferentIds() throws Throwable {
        Object id1 = new Object();
        Object id2 = "";
        Boolean useInput = Boolean.valueOf(""); // false
        
        JacksonInject.Value value1 = new JacksonInject.Value(id1, useInput, useInput);
        JacksonInject.Value value2 = JacksonInject.Value.construct(id2, useInput, useInput);
        
        assertNotEquals(value1, value2);
        assertTrue(value1.hasId());
        assertFalse(value2.hasId()); // Different ID
    }

    @Test(timeout = 4000)
    public void testEmptyValueNotEqualToNonEmpty() throws Throwable {
        JacksonInject.Value empty = JacksonInject.Value.empty();
        JacksonInject.Value nonEmpty = JacksonInject.Value.construct(
            empty, 
            Boolean.FALSE, 
            Boolean.FALSE
        );
        
        assertNotEquals(empty, nonEmpty);
        assertTrue(nonEmpty.hasId());
    }

    @Test(timeout = 4000)
    public void testValueEqualsItself() throws Throwable {
        JacksonInject.Value value = JacksonInject.Value.empty();
        assertTrue(value.equals(value));
    }

    @Test(timeout = 4000)
    public void testWillUseInputWithFalseDefault() throws Throwable {
        Object id = new Object();
        JacksonInject.Value value = JacksonInject.Value.forId(id);
        // Default behavior when useInput=null
        assertTrue(value.willUseInput(false));
    }

    @Test(timeout = 4000)
    public void testHasIdAfterForId() throws Throwable {
        JacksonInject.Value value = JacksonInject.Value.forId(JacksonInject.Value.EMPTY);
        assertTrue(value.hasId());
    }

    @Test(timeout = 4000)
    public void testEmptyValueHasNoId() throws Throwable {
        assertFalse(JacksonInject.Value.empty().hasId());
    }

    @Test(timeout = 4000)
    public void testWithOptionalSameValueReturnsSameInstance() throws Throwable {
        Object id = new Object();
        Boolean optional = Boolean.valueOf(""); // false
        JacksonInject.Value original = new JacksonInject.Value(id, optional, optional);
        JacksonInject.Value result = original.withOptional(optional);
        
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testEmptyWithOptionalNullReturnsSameInstance() throws Throwable {
        JacksonInject.Value original = JacksonInject.Value.empty();
        JacksonInject.Value result = original.withOptional(null);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testWithUseInputSameValueReturnsSameInstance() throws Throwable {
        Object id = new Object();
        Boolean useInput = Boolean.valueOf(""); // false
        JacksonInject.Value original = new JacksonInject.Value(id, useInput, useInput);
        JacksonInject.Value result = original.withUseInput(useInput);
        
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testEmptyWithUseInputNullReturnsSameInstance() throws Throwable {
        JacksonInject.Value original = JacksonInject.Value.empty();
        JacksonInject.Value result = original.withUseInput(null);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testWithIdSameValueReturnsSameInstance() throws Throwable {
        Object id = new Object();
        JacksonInject.Value original = JacksonInject.Value.forId(id);
        JacksonInject.Value result = original.withId(id);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testEmptyWithIdNullReturnsSameInstance() throws Throwable {
        JacksonInject.Value original = JacksonInject.Value.empty();
        JacksonInject.Value result = original.withId(null);
        assertSame(original, result);
    }

    @Test(timeout = 4000)
    public void testWithIdNullChangesId() throws Throwable {
        JacksonInject.Value original = JacksonInject.Value.construct(
            Boolean.TRUE, 
            Boolean.TRUE, 
            Boolean.TRUE
        );
        JacksonInject.Value result = original.withId(null);
        
        assertNotSame(original, result);
        assertFalse(result.hasId());
    }

    @Test(timeout = 4000)
    public void testWithIdDifferentValueChangesId() throws Throwable {
        Object id1 = new Object();
        JacksonInject.Value original = JacksonInject.Value.forId(id1);
        JacksonInject.Value result = original.withId(original); // Different ID
        
        assertNotSame(original, result);
        assertNotEquals(original, result);
        assertTrue(original.hasId());
    }

    @Test(timeout = 4000)
    public void testFromNullReturnsEmpty() throws Throwable {
        JacksonInject.Value result = JacksonInject.Value.from(null);
        assertNull(result.getOptional());
        assertSame(JacksonInject.Value.EMPTY, result);
    }

    @Test(timeout = 4000)
    public void testFromWithNullUseInputThrowsNPE() throws Throwable {
        JacksonInject mockAnnotation = mock(JacksonInject.class);
        doReturn(null).when(mockAnnotation).useInput();
        doReturn(null).when(mockAnnotation).value();
        
        try {
            JacksonInject.Value.from(mockAnnotation);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null useInput
        }
    }

    @Test(timeout = 4000)
    public void testGetIdOnEmptyReturnsNull() throws Throwable {
        assertNull(JacksonInject.Value.EMPTY.getId());
    }

    @Test(timeout = 4000)
    public void testValueForReturnsCorrectAnnotationType() throws Throwable {
        Class<?> result = JacksonInject.Value.empty().valueFor();
        assertEquals(JacksonInject.class, result);
    }
}