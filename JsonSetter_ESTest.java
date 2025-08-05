package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its factory methods,
 * merging logic, and property accessors.
 */
public class JsonSetterValueTest {

    private static final JsonSetter.Value EMPTY_VALUE = JsonSetter.Value.empty();
    private static final JsonSetter.Value ALL_SKIP_VALUE = JsonSetter.Value.construct(Nulls.SKIP, Nulls.SKIP);

    //region Factory Methods

    @Test
    public void from_shouldReturnEmptyInstance_whenGivenNull() {
        // Act
        JsonSetter.Value result = JsonSetter.Value.from(null);

        // Assert
        assertSame(EMPTY_VALUE, result);
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test
    public void from_shouldUseDefaultNulls_whenAnnotationMethodsReturnNull() {
        // Arrange
        JsonSetter mockAnnotation = mock(JsonSetter.class);
        doReturn(null).when(mockAnnotation).nulls();
        doReturn(null).when(mockAnnotation).contentNulls();

        // Act
        JsonSetter.Value result = JsonSetter.Value.from(mockAnnotation);

        // Assert
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test
    public void construct_shouldCreateInstanceWithGivenValues() {
        // Act
        JsonSetter.Value result = JsonSetter.Value.construct(Nulls.SET, Nulls.FAIL);

        // Assert
        assertEquals(Nulls.SET, result.getValueNulls());
        assertEquals(Nulls.FAIL, result.getContentNulls());
    }

    @Test
    public void forValueNulls_shouldSetContentNullsToDefault() {
        // Act
        JsonSetter.Value result = JsonSetter.Value.forValueNulls(Nulls.SKIP);

        // Assert
        assertEquals(Nulls.SKIP, result.getValueNulls());
        assertEquals(Nulls.DEFAULT, result.getContentNulls());
    }

    @Test
    public void forContentNulls_shouldSetValueNullsToDefault() {
        // Act
        JsonSetter.Value result = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        // Assert
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    //endregion

    //region Merging Logic

    @Test
    public void merge_shouldReturnOverride_whenBaseIsNull() {
        // Act
        JsonSetter.Value result = JsonSetter.Value.merge(null, ALL_SKIP_VALUE);

        // Assert
        assertSame(ALL_SKIP_VALUE, result);
    }

    @Test
    public void merge_shouldReturnBase_whenOverrideIsNull() {
        // Act
        JsonSetter.Value result = JsonSetter.Value.merge(ALL_SKIP_VALUE, null);

        // Assert
        assertSame(ALL_SKIP_VALUE, result);
    }

    @Test
    public void merge_shouldUseOverrideValues_whenBaseIsDefault() {
        // Arrange
        JsonSetter.Value baseValue = EMPTY_VALUE; // Contains DEFAULTs
        JsonSetter.Value overrideValue = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        // Act
        JsonSetter.Value result = JsonSetter.Value.merge(baseValue, overrideValue);

        // Assert
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    @Test
    public void merge_shouldKeepBaseValue_whenOverrideIsDefault() {
        // Arrange
        JsonSetter.Value baseValue = JsonSetter.Value.construct(Nulls.FAIL, Nulls.FAIL);
        JsonSetter.Value overrideValue = JsonSetter.Value.forValueNulls(Nulls.FAIL); // contentNulls is DEFAULT

        // Act
        JsonSetter.Value result = JsonSetter.Value.merge(baseValue, overrideValue);

        // Assert
        assertEquals(Nulls.FAIL, result.getValueNulls());
        assertEquals(Nulls.FAIL, result.getContentNulls()); // Kept from base
    }

    @Test
    public void withOverrides_shouldReturnSameInstance_whenGivenNull() {
        // Act
        JsonSetter.Value result = ALL_SKIP_VALUE.withOverrides(null);

        // Assert
        assertSame(ALL_SKIP_VALUE, result);
    }

    @Test
    public void withOverrides_shouldKeepOriginalValue_whenOverrideIsDefault() {
        // Arrange
        JsonSetter.Value baseValue = JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        // Override has a DEFAULT valueNulls, which should be ignored in favor of the base's specific setting.
        JsonSetter.Value overrideValue = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.AS_EMPTY);

        // Act
        JsonSetter.Value result = baseValue.withOverrides(overrideValue);

        // Assert
        assertSame(baseValue, result);
        assertEquals(Nulls.AS_EMPTY, result.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, result.getContentNulls());
    }

    //endregion

    //region Wither Methods

    @Test
    public void withValueNulls_shouldReturnNewInstanceWithUpdatedSettings() {
        // Act
        JsonSetter.Value result = EMPTY_VALUE.withValueNulls(Nulls.SKIP, Nulls.SET);

        // Assert
        assertNotSame(EMPTY_VALUE, result);
        assertEquals(Nulls.SKIP, result.getValueNulls());
        assertEquals(Nulls.SET, result.getContentNulls());
    }

    @Test
    public void withValueNulls_shouldReturnSameInstance_whenSettingsAreUnchanged() {
        // Act
        JsonSetter.Value result = EMPTY_VALUE.withValueNulls(Nulls.DEFAULT, Nulls.DEFAULT);

        // Assert
        assertSame(EMPTY_VALUE, result);
    }

    @Test
    public void withValueNulls_shouldReturnSameInstance_whenGivenNull() {
        // Act
        JsonSetter.Value result = ALL_SKIP_VALUE.withValueNulls(null, null);

        // Assert
        assertSame(ALL_SKIP_VALUE, result);
    }

    @Test
    public void withContentNulls_shouldReturnNewInstance_whenValueChanges() {
        // Act
        JsonSetter.Value result = EMPTY_VALUE.withContentNulls(Nulls.SKIP);

        // Assert
        assertNotSame(EMPTY_VALUE, result);
        assertEquals(Nulls.DEFAULT, result.getValueNulls());
        assertEquals(Nulls.SKIP, result.getContentNulls());
    }

    @Test
    public void withContentNulls_shouldReturnSameInstance_whenValueIsUnchanged() {
        // Act
        JsonSetter.Value result = EMPTY_VALUE.withContentNulls(Nulls.DEFAULT);

        // Assert
        assertSame(EMPTY_VALUE, result);
    }

    //endregion

    //region Accessors

    @Test
    public void nonDefaultValueNulls_shouldReturnNull_whenValueNullsIsDefault() {
        // Act & Assert
        assertNull(EMPTY_VALUE.nonDefaultValueNulls());
    }

    @Test
    public void nonDefaultValueNulls_shouldReturnValue_whenValueNullsIsNotDefault() {
        // Arrange
        JsonSetter.Value value = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY);

        // Act & Assert
        assertEquals(Nulls.AS_EMPTY, value.nonDefaultValueNulls());
    }

    @Test
    public void nonDefaultContentNulls_shouldReturnNull_whenContentNullsIsDefault() {
        // Act & Assert
        assertNull(EMPTY_VALUE.nonDefaultContentNulls());
    }

    @Test
    public void nonDefaultContentNulls_shouldReturnValue_whenContentNullsIsNotDefault() {
        // Arrange
        JsonSetter.Value value = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        // Act & Assert
        assertEquals(Nulls.SKIP, value.nonDefaultContentNulls());
    }

    @Test
    public void valueFor_shouldReturnJsonSetterClass() {
        // Act & Assert
        assertEquals(JsonSetter.class, EMPTY_VALUE.valueFor());
    }

    //endregion

    //region Standard Overrides (equals, hashCode, toString)

    @Test
    public void equals_shouldReturnTrue_whenComparingInstanceToItself() {
        // Act & Assert
        assertTrue(ALL_SKIP_VALUE.equals(ALL_SKIP_VALUE));
    }

    @Test
    public void equals_shouldReturnTrue_forInstancesWithIdenticalSettings() {
        // Arrange
        JsonSetter.Value value1 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SET);
        JsonSetter.Value value2 = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SET);

        // Act & Assert
        assertTrue(value1.equals(value2));
        assertEquals(value1.hashCode(), value2.hashCode());
    }

    @Test
    public void equals_shouldReturnFalse_whenValueNullsDiffer() {
        // Arrange
        JsonSetter.Value value1 = JsonSetter.Value.forValueNulls(Nulls.SET);
        JsonSetter.Value value2 = JsonSetter.Value.forValueNulls(Nulls.SKIP);

        // Act & Assert
        assertFalse(value1.equals(value2));
    }

    @Test
    public void equals_shouldReturnFalse_whenContentNullsDiffer() {
        // Arrange
        JsonSetter.Value value1 = JsonSetter.Value.forContentNulls(Nulls.SET);
        JsonSetter.Value value2 = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        // Act & Assert
        assertFalse(value1.equals(value2));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedWithNull() {
        // Act & Assert
        assertFalse(ALL_SKIP_VALUE.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Act & Assert
        assertFalse(ALL_SKIP_VALUE.equals("not a Value object"));
    }

    @Test
    public void toString_shouldReturnExpectedFormat() {
        // Act
        String result = EMPTY_VALUE.toString();

        // Assert
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", result);
    }

    @Test
    public void readResolve_shouldReturnSharedEmptyInstance_forDefaultValue() throws Exception {
        // Act
        Object resolved = EMPTY_VALUE.readResolve();

        // Assert
        assertSame(EMPTY_VALUE, resolved);
    }

    @Test
    public void readResolve_shouldPreserveSettings_afterDeserialization() throws Exception {
        // Arrange
        JsonSetter.Value value = JsonSetter.Value.construct(Nulls.FAIL, Nulls.SKIP);

        // Act
        JsonSetter.Value resolved = (JsonSetter.Value) value.readResolve();

        // Assert
        assertNotSame(value, resolved); // Should be a new, but equal, instance
        assertEquals(value, resolved);
    }

    //endregion
}