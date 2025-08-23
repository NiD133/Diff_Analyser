package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonSetter.Nulls;
import com.fasterxml.jackson.annotation.JsonSetter.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This test class focuses on the behavior of the {@link JsonSetter.Value} class.
 */
// The original class name and inheritance are kept for context,
// but would typically be renamed to something like "JsonSetterValueTest".
public class JsonSetter_ESTestTest3 extends JsonSetter_ESTest_scaffolding {

    /**
     * Verifies that the equals() method correctly returns false when comparing
     * two JsonSetter.Value instances that have different 'valueNulls' settings.
     */
    @Test
    public void equals_shouldReturnFalse_forInstancesWithDifferentValueNulls() {
        // Arrange
        // Create two JsonSetter.Value instances with different 'valueNulls' settings.
        // The factory method `forValueNulls` sets `contentNulls` to DEFAULT for both.
        Value valueWithSet = Value.forValueNulls(Nulls.SET);
        Value valueWithSkip = Value.forValueNulls(Nulls.SKIP);

        // Act
        // Compare the two instances for equality.
        boolean areEqual = valueWithSet.equals(valueWithSkip);

        // Assert
        // The instances should not be equal because their 'valueNulls' properties differ.
        assertFalse("Instances with different valueNulls should not be equal", areEqual);

        // This assertion confirms a characteristic of the factory method: `contentNulls`
        // is set to DEFAULT. This ensures the inequality is due solely to the
        // intended difference in `valueNulls`.
        assertEquals(Nulls.DEFAULT, valueWithSkip.getContentNulls());
    }
}