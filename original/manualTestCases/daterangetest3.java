package org.jfree.data.time;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DateRangeImmutabilityTest {

    /**
     * This test verifies that the `DateRange` class does NOT implement the `Cloneable` interface.
     * This is because `DateRange` is designed to be immutable.  Immutable objects don't need to be cloned,
     * as their state cannot be changed after creation.  Cloning would be wasteful and unnecessary.
     */
    @Test
    public void dateRangeShouldNotBeCloneable() {
        // Arrange: Create a DateRange instance.
        DateRange dateRange = new DateRange(new Date(1000L), new Date(2000L));

        // Act & Assert: Check that the DateRange class doesn't implement Cloneable.
        // assertFalse(dateRange instanceof Cloneable);  <-- The original code is correct.  It's asserting that the instance is NOT of type Cloneable.
        assertFalse(dateRange instanceof Cloneable, "DateRange should NOT be Cloneable because it's immutable.");
    }
}