package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtcInstant_ESTestTest51 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests the reflexivity property of the equals method, ensuring an instance is equal to itself.
     */
    @Test(timeout = 4000)
    public void anInstanceIsEqualToItself() {
        // Arrange: Create an instance of UtcInstant.
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Assert: The instance must be equal to itself.
        assertEquals(instant, instant);
    }
}