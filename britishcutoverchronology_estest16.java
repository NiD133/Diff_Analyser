package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Unit tests for the {@link BritishCutoverChronology} class.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that calling range() with a null ChronoField throws a NullPointerException.
     *
     * <p>The method contract requires a non-null field. Passing null is a
     * programming error that should result in a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void range_whenFieldIsNull_throwsNullPointerException() {
        BritishCutoverChronology.INSTANCE.range(null);
    }
}