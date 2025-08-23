package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeKeyTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        NodeKey<String> k1 = new NodeKey<>(0, "A");
        NodeKey<String> k2 = new NodeKey<>(0, "A");
        assertEquals(k1, k2);
        assertEquals(k2, k1);
        k1 = new NodeKey<>(1, "A");
        assertNotEquals(k1, k2);
        k2 = new NodeKey<>(1, "A");
        assertEquals(k1, k2);
        k1 = new NodeKey<>(1, "B");
        assertNotEquals(k1, k2);
        k2 = new NodeKey<>(1, "B");
        assertEquals(k1, k2);
    }
}
