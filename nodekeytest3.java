package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeKeyTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        NodeKey<String> k1 = new NodeKey<>(1, "S1");
        NodeKey<String> k2 = TestUtils.serialised(k1);
        assertEquals(k1, k2);
    }
}
