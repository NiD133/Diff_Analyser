package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its factory methods and equality contract.
 */
public class JsonSetterValueTest {

    /**
     * This test verifies that two {@link JsonSetter.Value} instances are considered equal
     * if they have the same internal properties, even if they are created using different
     * factory methods.
     */
    @Test
    public void shouldBeEqualWhenConstructedDifferentlyWithSameProperties() {
        // Given: Two JsonSetter.Value objects are configured to have the same
        // state (valueNulls=DEFAULT, contentNulls=FAIL), but are created
        // using different factory methods.

        // The forContentNulls() factory method sets valueNulls to DEFAULT implicitly.
        JsonSetter.Value valueFromContentNullsFactory = JsonSetter.Value.forContentNulls(Nulls.FAIL);

        // The forValueNulls() factory method sets both properties explicitly.
        JsonSetter.Value valueFromFullFactory = JsonSetter.Value.forValueNulls(Nulls.DEFAULT, Nulls.FAIL);

        // Then: The two objects should be considered equal, as their properties match.
        assertEquals(valueFromContentNullsFactory, valueFromFullFactory);

        // And: Their hash codes should also be equal, as per the equals/hashCode contract.
        assertEquals(valueFromContentNullsFactory.hashCode(), valueFromFullFactory.hashCode());

        // And: We can verify the state of one object to confirm our setup is correct.
        assertEquals(Nulls.DEFAULT, valueFromFullFactory.getValueNulls());
        assertEquals(Nulls.FAIL, valueFromFullFactory.getContentNulls());
    }
}