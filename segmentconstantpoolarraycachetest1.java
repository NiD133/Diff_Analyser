package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SegmentConstantPoolArrayCacheTestTest1 {

    @Test
    void testMultipleArrayMultipleHit() {
        final SegmentConstantPoolArrayCache arrayCache = new SegmentConstantPoolArrayCache();
        final String[] arrayOne = { "Zero", "Shared", "Two", "Shared", "Shared" };
        final String[] arrayTwo = { "Shared", "One", "Shared", "Shared", "Shared" };
        List<Integer> listOne = arrayCache.indexesForArrayKey(arrayOne, "Shared");
        List<Integer> listTwo = arrayCache.indexesForArrayKey(arrayTwo, "Shared");
        // Make sure we're using the cached values. First trip
        // through builds the cache.
        listOne = arrayCache.indexesForArrayKey(arrayOne, "Two");
        listTwo = arrayCache.indexesForArrayKey(arrayTwo, "Shared");
        assertEquals(1, listOne.size());
        assertEquals(2, listOne.get(0).intValue());
        // Now look for a different element in list one
        listOne = arrayCache.indexesForArrayKey(arrayOne, "Shared");
        assertEquals(3, listOne.size());
        assertEquals(1, listOne.get(0).intValue());
        assertEquals(3, listOne.get(1).intValue());
        assertEquals(4, listOne.get(2).intValue());
        assertEquals(4, listTwo.size());
        assertEquals(0, listTwo.get(0).intValue());
        assertEquals(2, listTwo.get(1).intValue());
        assertEquals(3, listTwo.get(2).intValue());
        assertEquals(4, listTwo.get(3).intValue());
        final List<Integer> listThree = arrayCache.indexesForArrayKey(arrayOne, "Not found");
        assertEquals(0, listThree.size());
    }
}
