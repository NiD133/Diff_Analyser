package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeKeyTestTest2 {

    /**
     * Confirm that cloning works.
     *
     * @throws CloneNotSupportedException
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        NodeKey<String> k1 = new NodeKey<>(2, "A");
        NodeKey<String> k2 = (NodeKey<String>) k1.clone();
        assertNotSame(k1, k2);
        assertSame(k1.getClass(), k2.getClass());
        assertEquals(k1, k2);
    }
}
