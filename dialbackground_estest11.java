package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;

/**
 * Contains unit tests for the {@link DialBackground} class, focusing on the
 * hashCode() method's contract.
 */
public class DialBackgroundTest {

    /**
     * Verifies that two DialBackground instances that are equal according to the
     * equals() method also have the same hash code. This is a fundamental part
     * of the Java hashCode() contract.
     */
    @Test
    public void hashCode_shouldBeEqualForEqualObjects() {
        // Arrange: Create two identical DialBackground objects. By default, they
        // are initialized with a white background paint.
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();

        // Assert: The two objects should be considered equal, and therefore,
        // their hash codes must also be equal.
        assertEquals("Two default DialBackground objects should be equal.", background1, background2);
        assertEquals("Equal objects must have equal hash codes.", background1.hashCode(), background2.hashCode());
    }

    /**
     * Verifies that two unequal DialBackground instances are likely to have
     * different hash codes. While not a strict requirement of the hashCode()
     * contract, this is a desirable property for good hash functions and is
     * generally expected.
     */
    @Test
    public void hashCode_shouldBeDifferentForUnequalObjects() {
        // Arrange: Create two different DialBackground objects, one with a white
        // background and the other with a black background.
        DialBackground backgroundWhite = new DialBackground(Color.WHITE);
        DialBackground backgroundBlack = new DialBackground(Color.BLACK);

        // Assert: The two objects should not be equal, and their hash codes
        // should also be different.
        assertNotEquals("Objects with different paints should not be equal.", backgroundWhite, backgroundBlack);
        assertNotEquals("Unequal objects should ideally have different hash codes.",
                backgroundWhite.hashCode(), backgroundBlack.hashCode());
    }
}