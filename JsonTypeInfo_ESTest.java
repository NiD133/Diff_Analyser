package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
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
public class JsonTypeInfo_ESTest extends JsonTypeInfo_ESTest_scaffolding {

    // Tests for JsonTypeInfo.Id
    @Test(timeout = 4000)
    public void testIdGetDefaultPropertyName_CustomId_ReturnsNull() {
        JsonTypeInfo.Id customId = JsonTypeInfo.Id.CUSTOM;
        assertNull(customId.getDefaultPropertyName());
    }

    // Tests for JsonTypeInfo.Value methods
    @Test(timeout = 4000)
    public void testValueWithPropertyName_Null_ReturnsSameInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value result = value.withPropertyName(null);
        assertSame(value, result);
    }

    @Test(timeout = 4000)
    public void testValueEquals_DifferentPropertyName_ReturnsFalse() {
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value value2 = value1.withPropertyName("EXTERNAL_PROPERTY");
        assertNotEquals(value1, value2);
        assertFalse(value2.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueEquals_DifferentRequireTypeIdForSubtypes_ReturnsFalse() {
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.EMPTY;
        Boolean requireTypeId = Boolean.TRUE;
        JsonTypeInfo.Value value2 = value1.withRequireTypeIdForSubtypes(requireTypeId);
        assertNotEquals(value1, value2);
        assertFalse(value2.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueEquals_DifferentIdVisible_ReturnsFalse() {
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value value2 = value1.withIdVisible(true);
        assertNotEquals(value1, value2);
    }

    @Test(timeout = 4000)
    public void testValueEquals_DifferentConstruction_ReturnsFalse() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Id id = JsonTypeInfo.Id.DEDUCTION;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value constructedValue = JsonTypeInfo.Value.construct(
            id, inclusion, "", Object.class, false, Boolean.FALSE
        );
        assertNotEquals(emptyValue, constructedValue);
        assertFalse(constructedValue.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueEquals_NonValueObject_ReturnsFalse() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertNotEquals(value, "EXTERNAL_PROPERTY");
    }

    @Test(timeout = 4000)
    public void testValueEquals_Null_ReturnsFalse() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        assertNotEquals(value, null);
    }

    @Test(timeout = 4000)
    public void testValueEquals_SameInstance_ReturnsTrue() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.TRUE
        );
        assertEquals(value, value);
        assertEquals("propertyName", value.getPropertyName());
        assertFalse(value.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueToString_EmptyValue_ReturnsCorrectRepresentation() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        String expected = "JsonTypeInfo.Value(idType=NONE,includeAs=PROPERTY,propertyName=null," +
                          "defaultImpl=NULL,idVisible=false,requireTypeIdForSubtypes=null)";
        assertEquals(expected, value.toString());
    }

    @Test(timeout = 4000)
    public void testValueToString_NonEmptyValue_ReturnsCorrectRepresentation() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.FALSE
        );
        String expected = "JsonTypeInfo.Value(idType=MINIMAL_CLASS,includeAs=WRAPPER_OBJECT," +
                          "propertyName=propertyName,defaultImpl=java.lang.Object," +
                          "idVisible=false,requireTypeIdForSubtypes=false)";
        assertEquals(expected, value.toString());
    }

    @Test(timeout = 4000)
    public void testIsEnabled_EmptyValue_ReturnsFalse() {
        assertFalse(JsonTypeInfo.Value.isEnabled(JsonTypeInfo.Value.EMPTY));
    }

    @Test(timeout = 4000)
    public void testIsEnabled_NonEmptyValue_ReturnsTrue() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.TRUE
        );
        assertTrue(JsonTypeInfo.Value.isEnabled(value));
        assertEquals("propertyName", value.getPropertyName());
        assertFalse(value.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testIsEnabled_NullValue_ReturnsFalse() {
        assertFalse(JsonTypeInfo.Value.isEnabled(null));
    }

    @Test(timeout = 4000)
    public void testValueWithRequireTypeIdForSubtypes_Null_ReturnsSameInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value result = value.withRequireTypeIdForSubtypes(null);
        assertSame(value, result);
    }

    @Test(timeout = 4000)
    public void testValueWithRequireTypeIdForSubtypes_SameBooleanValue_ReturnsEqualInstance() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value original = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.TRUE
        );
        JsonTypeInfo.Value modified = original.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertEquals(original, modified);
        assertNotSame(original, modified);
        assertEquals("propertyName", modified.getPropertyName());
        assertFalse(modified.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueWithIdVisible_False_ReturnsSameInstance() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.TRUE
        );
        JsonTypeInfo.Value result = value.withIdVisible(false);
        assertSame(value, result);
        assertEquals("propertyName", result.getPropertyName());
    }

    @Test(timeout = 4000)
    public void testValueWithInclusionType_SameInclusion_ReturnsSameInstance() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value result = value.withInclusionType(JsonTypeInfo.As.PROPERTY);
        assertSame(value, result);
    }

    @Test(timeout = 4000)
    public void testValueWithInclusionType_DifferentInclusion_ReturnsNotEqual() {
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value value2 = value1.withInclusionType(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotEquals(value1, value2);
        assertFalse(value2.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueWithIdType_SameIdType_ReturnsSameInstance() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.TRUE
        );
        JsonTypeInfo.Value result = value.withIdType(id);
        assertSame(value, result);
        assertEquals("propertyName", result.getPropertyName());
        assertFalse(result.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueWithIdType_DifferentIdType_ReturnsDifferentInstance() {
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value value2 = value1.withIdType(JsonTypeInfo.Id.CLASS);
        assertEquals(JsonTypeInfo.Id.CLASS, value2.getIdType());
        assertFalse(value2.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueWithDefaultImpl_NonNull_ReturnsDifferentInstance() {
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value value2 = value1.withDefaultImpl(Object.class);
        assertNotEquals(value1, value2);
        assertNotSame(value1, value2);
        assertFalse(value2.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testFrom_NullAnnotation_ReturnsNull() {
        assertNull(JsonTypeInfo.Value.from(null));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testFrom_InvalidAnnotation_ThrowsNullPointerException() {
        JsonTypeInfo ann = mock(JsonTypeInfo.class);
        doReturn(null).when(ann).use();
        doReturn(null).when(ann).include();
        doReturn(null).when(ann).property();
        doReturn(null).when(ann).defaultImpl();
        doReturn(null).when(ann).requireTypeIdForSubtypes();
        doReturn(false).when(ann).visible();

        JsonTypeInfo.Value.from(ann);
    }

    @Test(timeout = 4000)
    public void testValueConstruct_WithNonNullValues_Succeeds() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.CLASS,
            JsonTypeInfo.As.WRAPPER_OBJECT,
            "propertyName",
            JsonTypeInfo.Value.class,
            true,
            Boolean.FALSE
        );
        assertTrue(value.getIdVisible());
        assertEquals("propertyName", value.getPropertyName());
    }

    @Test(timeout = 4000)
    public void testValueConstruct_WithNullPropertyName_ReturnsDefaultPropertyName() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.NAME,
            JsonTypeInfo.As.PROPERTY,
            null,
            Integer.class,
            false,
            Boolean.FALSE
        );
        assertEquals("@type", value.getPropertyName());
        assertFalse(value.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueGetDefaultImpl_NonEmptyValue_ReturnsExpectedClass() {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS,
            JsonTypeInfo.As.WRAPPER_OBJECT,
            "propertyName",
            Integer.class,
            false,
            Boolean.TRUE
        );
        assertEquals(Integer.class, value.getDefaultImpl());
        assertFalse(value.getIdVisible());
        assertEquals("propertyName", value.getPropertyName());
    }

    @Test(timeout = 4000)
    public void testValueGetIdVisible_EmptyValue_ReturnsFalse() {
        assertFalse(JsonTypeInfo.Value.EMPTY.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueGetPropertyName_NonEmptyValue_ReturnsExpectedName() {
        JsonTypeInfo.Value value = new JsonTypeInfo.Value(
            JsonTypeInfo.Id.DEDUCTION,
            JsonTypeInfo.As.WRAPPER_OBJECT,
            "propertyName",
            Integer.class,
            true,
            Boolean.TRUE
        );
        assertEquals("propertyName", value.getPropertyName());
        assertTrue(value.getIdVisible());
    }

    @Test(timeout = 4000)
    public void testValueGetIdType_NonEmptyValue_ReturnsExpectedId() {
        JsonTypeInfo.Id expectedId = JsonTypeInfo.Id.CLASS;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            expectedId,
            JsonTypeInfo.As.EXISTING_PROPERTY,
            "propertyName",
            Object.class,
            true,
            Boolean.FALSE
        );
        assertEquals(expectedId, value.getIdType());
        assertTrue(value.getIdVisible());
        assertEquals("propertyName", value.getPropertyName());
    }

    @Test(timeout = 4000)
    public void testValueGetRequireTypeIdForSubtypes_EmptyValue_ReturnsNull() {
        assertNull(JsonTypeInfo.Value.EMPTY.getRequireTypeIdForSubtypes());
    }

    @Test(timeout = 4000)
    public void testValueGetInclusionType_NonEmptyValue_ReturnsExpectedInclusion() {
        JsonTypeInfo.As expectedInclusion = JsonTypeInfo.As.EXISTING_PROPERTY;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.CLASS,
            expectedInclusion,
            "propertyName",
            Object.class,
            true,
            Boolean.FALSE
        );
        assertEquals(expectedInclusion, value.getInclusionType());
        assertTrue(value.getIdVisible());
        assertEquals("propertyName", value.getPropertyName());
    }

    @Test(timeout = 4000)
    public void testValueWithDefaultImpl_SameClass_ReturnsSameInstance() {
        JsonTypeInfo.Id id = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            id, inclusion, "propertyName", Object.class, false, Boolean.TRUE
        );
        JsonTypeInfo.Value result = value.withDefaultImpl(Object.class);
        assertSame(value, result);
        assertEquals("propertyName", result.getPropertyName());
        assertFalse(result.getIdVisible());
    }
}