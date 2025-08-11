package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonTypeInfoTest extends JsonTypeInfo_ESTest_scaffolding {

    // Test for JsonTypeInfo.Id default property name
    @Test(timeout = 4000)
    public void testCustomIdDefaultPropertyNameIsNull() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.CUSTOM;
        assertNull(id.getDefaultPropertyName());
    }

    // Tests for JsonTypeInfo.Value withPropertyName
    @Test(timeout = 4000)
    public void testValueWithNullPropertyNameReturnsSameInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertSame(value.withPropertyName(null), value);
    }

    @Test(timeout = 4000)
    public void testValueWithDifferentPropertyNameReturnsDifferentInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value newValue = value.withPropertyName("EXTERNAL_PROPERTY");
        assertNotEquals(value, newValue);
        assertFalse(newValue.getIdVisible());
    }

    // Tests for JsonTypeInfo.Value withRequireTypeIdForSubtypes
    @Test(timeout = 4000)
    public void testValueWithRequireTypeIdForSubtypesReturnsDifferentInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value newValue = value.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertNotEquals(value, newValue);
        assertFalse(newValue.getIdVisible());
    }

    // Tests for JsonTypeInfo.Value withIdVisible
    @Test(timeout = 4000)
    public void testValueWithIdVisibleReturnsDifferentInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value newValue = value.withIdVisible(true);
        assertNotEquals(value, newValue);
    }

    // Test for JsonTypeInfo.Value equality
    @Test(timeout = 4000)
    public void testValueEqualsItself() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertTrue(value.equals(value));
    }

    @Test(timeout = 4000)
    public void testValueEqualsNullReturnsFalse() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertFalse(value.equals(null));
    }

    @Test(timeout = 4000)
    public void testValueEqualsDifferentTypeReturnsFalse() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertFalse(value.equals("EXTERNAL_PROPERTY"));
    }

    // Test for JsonTypeInfo.Value toString
    @Test(timeout = 4000)
    public void testValueToString() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertEquals("JsonTypeInfo.Value(idType=NONE,includeAs=PROPERTY,propertyName=null,defaultImpl=NULL,idVisible=false,requireTypeIdForSubtypes=null)", value.toString());
    }

    // Test for JsonTypeInfo.Value isEnabled
    @Test(timeout = 4000)
    public void testValueIsEnabledReturnsFalseForEmptyValue() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertFalse(JsonTypeInfo.Value.isEnabled(value));
    }

    @Test(timeout = 4000)
    public void testValueIsEnabledReturnsFalseForNull() {
        assertFalse(JsonTypeInfo.Value.isEnabled(null));
    }

    // Test for JsonTypeInfo.Value from
    @Test(timeout = 4000)
    public void testValueFromNullReturnsNull() {
        assertNull(JsonTypeInfo.Value.from(null));
    }

    @Test(timeout = 4000)
    public void testValueFromThrowsNullPointerExceptionForInvalidJsonTypeInfo() {
        JsonTypeInfo jsonTypeInfo = mock(JsonTypeInfo.class, CALLS_REAL_METHODS);
        doReturn(null).when(jsonTypeInfo).defaultImpl();
        doReturn(null).when(jsonTypeInfo).include();
        doReturn(null).when(jsonTypeInfo).property();
        doReturn(null).when(jsonTypeInfo).requireTypeIdForSubtypes();
        doReturn(null).when(jsonTypeInfo).use();
        doReturn(false).when(jsonTypeInfo).visible();

        try {
            JsonTypeInfo.Value.from(jsonTypeInfo);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test for JsonTypeInfo.Value construct
    @Test(timeout = 4000)
    public void testValueConstructWithVisibleId() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        Class<JsonTypeInfo> defaultImpl = JsonTypeInfo.class;
        Boolean requireTypeIdForSubtypes = Boolean.TRUE;

        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(id, inclusion, "propertyName", defaultImpl, true, requireTypeIdForSubtypes);
        assertTrue(value.getIdVisible());
        assertEquals("propertyName", value.getPropertyName());
    }

    @Test(timeout = 4000)
    public void testValueConstructWithDefaultPropertyName() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.NAME;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.PROPERTY;
        Class<Integer> defaultImpl = Integer.class;
        Boolean requireTypeIdForSubtypes = Boolean.FALSE;

        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(id, inclusion, null, defaultImpl, false, requireTypeIdForSubtypes);
        assertEquals("@type", value.getPropertyName());
        assertFalse(value.getIdVisible());
    }
}