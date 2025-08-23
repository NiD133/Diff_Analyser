package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology.Fields;

/**
 * This test class provides an improved, more understandable test for the
 * {@link CopticChronology} class. The original test was automatically generated
 * and lacked clarity. This version focuses on creating a clear, maintainable
 * test for the assemble() method.
 */
public class CopticChronology_ESTestTest2 {

    /**
     * Tests that the {@link CopticChronology#assemble(Fields)} method correctly
     * populates the essential date and time fields.
     * <p>
     * The original test called this method but then performed an unrelated assertion
     * on a static constant. This improved test verifies the actual side-effect of the
     * {@code assemble} method: that the fields object is properly initialized.
     * </p>
     */
    @Test(timeout = 4000)
    public void assemble_shouldPopulateChronologyFields() {
        // Arrange: Create a CopticChronology instance and a 'Fields' object to hold
        // the chronology's component fields. The 'Fields' object starts empty.
        CopticChronology chronology = CopticChronology.getInstance(DateTimeZone.UTC);
        Fields fields = new Fields();

        // Act: The assemble method is a protected method responsible for populating the
        // 'fields' object with all the specific fields used by the Coptic calendar system.
        chronology.assemble(fields);

        // Assert: Verify that the core chronology fields have been successfully
        // initialized and are not null. This confirms that the assemble() method
        // worked as expected.
        assertNotNull("The 'era' field should be initialized by assemble()", fields.era);
        assertNotNull("The 'year' field should be initialized by assemble()", fields.year);
        assertNotNull("The 'monthOfYear' field should be initialized by assemble()", fields.monthOfYear);
        assertNotNull("The 'dayOfMonth' field should be initialized by assemble()", fields.dayOfMonth);
        assertNotNull("The 'weekyear' field should be initialized by assemble()", fields.weekyear);
        assertNotNull("The 'dayOfWeek' field should be initialized by assemble()", fields.dayOfWeek);
        assertNotNull("The 'dayOfYear' field should be initialized by assemble()", fields.dayOfYear);
    }
}