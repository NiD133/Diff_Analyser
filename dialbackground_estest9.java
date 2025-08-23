package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that two distinct DialBackground instances created with the default
     * constructor are considered equal. This confirms the correct implementation
     * of the equals() and hashCode() contract for default objects.
     */
    @Test
    public void twoDefaultInstancesShouldBeEqual() {
        // Arrange: Create two separate instances using the default constructor.
        // According to the source, this initializes them with a default paint (Color.WHITE)
        // and a StandardGradientPaintTransformer.
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();

        // Act & Assert: Verify that the two instances are equal.
        // We use assertEquals, which calls the .equals() method internally and provides
        // a more informative failure message than assertTrue(background1.equals(background2)).
        assertEquals(background1, background2);

        // Per the Java contract, if two objects are equal, their hash codes must also be equal.
        assertEquals("Hash codes must be equal for equal objects.",
                background1.hashCode(), background2.hashCode());
    }
}