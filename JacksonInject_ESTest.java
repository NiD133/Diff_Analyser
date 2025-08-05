package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JacksonInject_ESTest extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEqualityWithDifferentOptionalValues() throws Throwable {
        Object id = new Object();
        Boolean useInput = Boolean.FALSE;
        Boolean optionalTrue = Boolean.TRUE;
        JacksonInject.Value originalValue = new JacksonInject.Value(id, useInput, optionalTrue);
        Boolean optionalFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = originalValue.withOptional(optionalFalse);

        assertFalse(originalValue.equals(modifiedValue));
        assertFalse(modifiedValue.equals(originalValue));
        assertFalse(originalValue.getUseInput());
        assertTrue(modifiedValue.hasId());
        assertFalse(modifiedValue.getUseInput());
    }

    @Test(timeout = 4000)
    public void testToStringRepresentation() throws Throwable {
        JacksonInject.Value valueWithNullId = JacksonInject.Value.forId(null);
        Boolean optionalFalse = Boolean.FALSE;
        JacksonInject.Value valueWithOptional = valueWithNullId.withOptional(optionalFalse);
        String expectedString = "JacksonInject.Value(id=null,useInput=null,optional=false)";

        assertEquals(expectedString, valueWithOptional.toString());
    }

    @Test(timeout = 4000)
    public void testWillUseInputReturnsFalse() throws Throwable {
        Boolean useInputFalse = Boolean.FALSE;
        Boolean optionalEmpty = new Boolean("");
        JacksonInject.Value value = JacksonInject.Value.construct(null, useInputFalse, optionalEmpty);

        assertFalse(value.willUseInput(false));
    }

    @Test(timeout = 4000)
    public void testGetOptionalReturnsNull() throws Throwable {
        Object id = new Object();
        Boolean useInputFalse = Boolean.FALSE;
        JacksonInject.Value value = JacksonInject.Value.construct(id, useInputFalse, null);

        assertTrue(value.hasId());
        assertNull(value.getOptional());
    }

    @Test(timeout = 4000)
    public void testWithOptionalCreatesNewInstance() throws Throwable {
        Object id = new Object();
        JacksonInject.Value originalValue = JacksonInject.Value.forId(id);
        Boolean optionalFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = originalValue.withOptional(optionalFalse);

        assertTrue(modifiedValue.hasId());
        assertFalse(modifiedValue.equals(originalValue));
        assertNotSame(modifiedValue, originalValue);
        assertNull(modifiedValue.getUseInput());
    }

    @Test(timeout = 4000)
    public void testWithOptionalNullCreatesNewInstance() throws Throwable {
        Boolean useInputInvalid = new Boolean("gJ%*=b`O<@AxM)o");
        Boolean optionalFalse = Boolean.FALSE;
        JacksonInject.Value originalValue = new JacksonInject.Value(null, useInputInvalid, optionalFalse);
        JacksonInject.Value modifiedValue = originalValue.withOptional(null);

        assertFalse(modifiedValue.equals(originalValue));
        assertNotSame(modifiedValue, originalValue);
    }

    @Test(timeout = 4000)
    public void testWithUseInputCreatesNewInstance() throws Throwable {
        Object id = new Object();
        JacksonInject.Value originalValue = JacksonInject.Value.forId(id);
        Boolean useInputFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = originalValue.withUseInput(useInputFalse);

        assertNotSame(modifiedValue, originalValue);
        assertFalse(modifiedValue.equals(originalValue));
        assertTrue(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testWithUseInputNullCreatesNewInstance() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;
        Boolean useInputFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = emptyValue.withUseInput(useInputFalse);
        JacksonInject.Value finalValue = modifiedValue.withUseInput(null);

        assertNotSame(finalValue, modifiedValue);
        assertTrue(finalValue.equals(emptyValue));
    }

    @Test(timeout = 4000)
    public void testWithIdCreatesNewInstance() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;
        Boolean useInputFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = emptyValue.withUseInput(useInputFalse);
        Object newId = new Object();
        JacksonInject.Value finalValue = modifiedValue.withId(newId);

        assertTrue(finalValue.hasId());
        assertFalse(modifiedValue.equals(emptyValue));
        assertNotSame(modifiedValue, emptyValue);
    }

    @Test(timeout = 4000)
    public void testWithIdFromEmptyValue() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        JacksonInject.Value modifiedValue = emptyValue.withId(emptyValue);

        assertTrue(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testConstructWithTrueOptional() throws Throwable {
        Boolean optionalTrue = Boolean.TRUE;
        JacksonInject.Value value = JacksonInject.Value.construct(null, null, optionalTrue);

        assertFalse(value.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualityWithSameValues() throws Throwable {
        Object id = new Object();
        Boolean useInputEmpty = new Boolean("");
        JacksonInject.Value value1 = new JacksonInject.Value(id, useInputEmpty, useInputEmpty);
        JacksonInject.Value value2 = JacksonInject.Value.construct(id, useInputEmpty, useInputEmpty);

        assertTrue(value1.equals(value2));
        assertTrue(value1.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualityWithSameId() throws Throwable {
        Object id = new Object();
        JacksonInject.Value value1 = JacksonInject.Value.forId(id);
        JacksonInject.Value value2 = JacksonInject.Value.forId(id);

        assertTrue(value1.equals(value2));
        assertTrue(value1.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentUseInput() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;
        Boolean useInputInvalid = new Boolean("zH78ih{ZO&er");
        JacksonInject.Value modifiedValue = emptyValue.withUseInput(useInputInvalid);

        assertFalse(modifiedValue.equals(emptyValue));
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentIds() throws Throwable {
        Object id1 = new Object();
        Boolean useInputEmpty = new Boolean("");
        JacksonInject.Value value1 = new JacksonInject.Value(id1, useInputEmpty, useInputEmpty);
        JacksonInject.Value value2 = JacksonInject.Value.construct("", useInputEmpty, useInputEmpty);

        assertFalse(value1.equals(value2));
        assertFalse(value2.hasId());
        assertFalse(value1.equals(value2));
    }

    @Test(timeout = 4000)
    public void testConstructWithEmptyValue() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        Boolean useInputFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = JacksonInject.Value.construct(emptyValue, useInputFalse, useInputFalse);

        assertFalse(modifiedValue.equals(emptyValue));
        assertTrue(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testEqualityWithSelf() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();

        assertTrue(emptyValue.equals(emptyValue));
    }

    @Test(timeout = 4000)
    public void testWillUseInputWithId() throws Throwable {
        Object id = new Object();
        JacksonInject.Value value = JacksonInject.Value.forId(id);

        value.willUseInput(false);
        assertTrue(value.hasId());
    }

    @Test(timeout = 4000)
    public void testHasIdWithEmptyValue() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        JacksonInject.Value modifiedValue = JacksonInject.Value.forId(emptyValue);

        assertTrue(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testHasIdWithEmptyValueReturnsFalse() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();

        assertFalse(emptyValue.hasId());
    }

    @Test(timeout = 4000)
    public void testWithOptionalSameInstance() throws Throwable {
        Object id = new Object();
        Boolean useInputEmpty = new Boolean("");
        JacksonInject.Value value = new JacksonInject.Value(id, useInputEmpty, useInputEmpty);
        JacksonInject.Value modifiedValue = value.withOptional(useInputEmpty);

        assertSame(modifiedValue, value);
        assertTrue(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testWithOptionalNullSameInstance() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        JacksonInject.Value modifiedValue = emptyValue.withOptional(null);

        assertSame(modifiedValue, emptyValue);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentOptionalValues() throws Throwable {
        Object id = new Object();
        JacksonInject.Value originalValue = JacksonInject.Value.forId(id);
        Boolean optionalFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = originalValue.withOptional(optionalFalse);

        assertTrue(modifiedValue.hasId());
        assertFalse(originalValue.equals(modifiedValue));
        assertFalse(modifiedValue.equals(originalValue));
    }

    @Test(timeout = 4000)
    public void testWithUseInputSameInstance() throws Throwable {
        Object id = new Object();
        Boolean useInputEmpty = new Boolean("");
        JacksonInject.Value value = new JacksonInject.Value(id, useInputEmpty, useInputEmpty);
        JacksonInject.Value modifiedValue = value.withUseInput(useInputEmpty);

        assertSame(modifiedValue, value);
        assertTrue(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testWithUseInputNullSameInstance() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        JacksonInject.Value modifiedValue = emptyValue.withUseInput(null);

        assertSame(modifiedValue, emptyValue);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentUseInputValues() throws Throwable {
        Object id = new Object();
        JacksonInject.Value originalValue = JacksonInject.Value.forId(id);
        Boolean useInputFalse = Boolean.FALSE;
        JacksonInject.Value modifiedValue = originalValue.withUseInput(useInputFalse);

        assertFalse(modifiedValue.equals(originalValue));
        assertTrue(modifiedValue.hasId());
        assertFalse(originalValue.equals(modifiedValue));
    }

    @Test(timeout = 4000)
    public void testWithIdSameInstance() throws Throwable {
        Object id = new Object();
        JacksonInject.Value originalValue = JacksonInject.Value.forId(id);
        JacksonInject.Value modifiedValue = originalValue.withId(id);

        assertSame(modifiedValue, originalValue);
    }

    @Test(timeout = 4000)
    public void testWithIdNullSameInstance() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        JacksonInject.Value modifiedValue = emptyValue.withId(null);

        assertSame(modifiedValue, emptyValue);
    }

    @Test(timeout = 4000)
    public void testWithIdCreatesNewInstance() throws Throwable {
        Boolean useInputTrue = Boolean.TRUE;
        JacksonInject.Value originalValue = JacksonInject.Value.construct(useInputTrue, useInputTrue, useInputTrue);
        JacksonInject.Value modifiedValue = originalValue.withId(null);

        assertNotSame(modifiedValue, originalValue);
        assertFalse(modifiedValue.hasId());
    }

    @Test(timeout = 4000)
    public void testWithIdCreatesNewInstanceWithDifferentId() throws Throwable {
        Object id = new Object();
        JacksonInject.Value originalValue = JacksonInject.Value.forId(id);
        JacksonInject.Value modifiedValue = originalValue.withId(originalValue);

        assertTrue(originalValue.hasId());
        assertNotSame(modifiedValue, originalValue);
        assertFalse(modifiedValue.equals(originalValue));
    }

    @Test(timeout = 4000)
    public void testFromNullJacksonInject() throws Throwable {
        JacksonInject.Value value = JacksonInject.Value.from(null);

        assertNull(value.getOptional());
    }

    @Test(timeout = 4000)
    public void testFromJacksonInjectThrowsException() throws Throwable {
        JacksonInject jacksonInjectMock = mock(JacksonInject.class, CALLS_REAL_METHODS);
        doReturn(null).when(jacksonInjectMock).useInput();
        doReturn(null).when(jacksonInjectMock).value();

        try {
            JacksonInject.Value.from(jacksonInjectMock);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetIdReturnsNull() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;
        Object id = emptyValue.getId();

        assertNull(id);
    }

    @Test(timeout = 4000)
    public void testValueForReturnsInterface() throws Throwable {
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        Class<JacksonInject> clazz = emptyValue.valueFor();

        assertTrue(clazz.isInterface());
    }
}