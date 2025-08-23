package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SegmentConstantPoolArrayCacheTestTest3 {

    @Test
    void testSingleSimpleArray() {
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
        final String[] array = { "Zero", "One", "Two", "Three", "Four" };
        final List<Integer> list = arrayCache.indexesForArrayKey(array, "Three");
        assertEquals(1, list.size());
        assertEquals(3, list.get(0).intValue());
    }
}
