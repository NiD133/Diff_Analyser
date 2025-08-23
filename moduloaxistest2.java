package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModuloAxisTestTest2 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        ModuloAxis a1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis a2 = new ModuloAxis("Test", new Range(0.0, 1.0));
        assertEquals(a1, a2);
        a1.setDisplayRange(0.1, 1.1);
        assertNotEquals(a1, a2);
        a2.setDisplayRange(0.1, 1.1);
        assertEquals(a1, a2);
    }
}
