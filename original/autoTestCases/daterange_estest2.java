package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

// Removed unused imports for clarity
// import static org.evosuite.runtime.EvoAssertions.*;
// import java.util.Date;
// import org.evosuite.runtime.EvoRunner;
// import org.evosuite.runtime.EvoRunnerParameters;
// import org.evosuite.runtime.mock.java.util.MockDate;
// import org.jfree.data.Range;
// import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void testDateRangeGetUpperMillis() throws Throwable {
        // Arrange: Create a DateRange with the same lower and upper bounds (-145.3).
        DateRange dateRange = new DateRange((-145.3), (-145.3));

        // Act: Get the upper bound in milliseconds.
        long upperMillis = dateRange.getUpperMillis();

        // Assert: Verify that the upper bound in milliseconds is -145L.
        // Also verify that the lower bound in milliseconds is -145L, 
        // confirming that the DateRange was correctly initialized.
        assertEquals(-145L, upperMillis);
        assertEquals(-145L, dateRange.getLowerMillis());
    }
}