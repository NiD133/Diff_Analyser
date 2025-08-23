package org.joda.time.chrono;

import org.joda.time.chrono.AssembledChronology.Fields;
import org.junit.Test;

/**
 * Tests the internal behavior of the GregorianChronology class.
 */
public class GregorianChronology_ESTestTest9 {

    /**
     * Verifies that the internal assemble() method throws a NullPointerException
     * when its 'fields' argument is null. This ensures the method is robust
     * against invalid internal state during chronology construction.
     */
    @Test(expected = NullPointerException.class)
    public void assemble_shouldThrowNullPointerException_whenFieldsAreNull() {
        // Arrange
        GregorianChronology chronology = GregorianChronology.getInstance();
        Fields nullFields = null;

        // Act & Assert
        // The assemble() method is protected. We call it with a null argument
        // to confirm it fails fast, as expected. The @Test annotation
        // asserts that a NullPointerException is thrown.
        chronology.assemble(nullFields);
    }
}