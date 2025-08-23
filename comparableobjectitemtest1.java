package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

public class ComparableObjectItemTestTest1 {

    /**
     * Some checks for the constructor.
     */
    @Test
    public void testConstructor() {
        // check null argument 1
        try {
            /* ComparableObjectItem item1 = */
            new ComparableObjectItem(null, "XYZ");
            fail("There should be an exception.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
