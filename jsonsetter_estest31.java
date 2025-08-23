package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the {@link JsonSetter.Value} class,
 * verifying the behavior of its constructor and getter methods.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the getValueNulls() method correctly returns the
     * {@link Nulls} value that was provided during the construction of a
     * {@link JsonSetter.Value} instance.
     */
    @Test
    public void getValueNulls_shouldReturnTheValueProvidedInConstructor() {
        // Arrange: Create a JsonSetter.Value instance with a specific setting for valueNulls.
        Nulls expectedValueNulls = Nulls.FAIL;
        // The contentNulls parameter is also required by the constructor, but not under test here.
        Nulls anyContentNulls = Nulls.SKIP;
        JsonSetter.Value setterValue = new JsonSetter.Value(expectedValueNulls, anyContentNulls);

        // Act: Retrieve the valueNulls setting from the instance.
        Nulls actualValueNulls = setterValue.getValueNulls();

        // Assert: The retrieved value should match the one provided during construction.
        assertEquals(expectedValueNulls, actualValueNulls);
    }
}