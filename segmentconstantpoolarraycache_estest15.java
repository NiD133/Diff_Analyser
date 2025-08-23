package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentConstantPoolArrayCache_ESTestTest15 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        SegmentConstantPoolArrayCache segmentConstantPoolArrayCache0 = new SegmentConstantPoolArrayCache();
        String[] stringArray0 = new String[1];
        IdentityHashMap<String[], SegmentConstantPoolArrayCache.CachedArray> identityHashMap0 = segmentConstantPoolArrayCache0.knownArrays;
        String[] stringArray1 = new String[0];
        SegmentConstantPoolArrayCache.CachedArray segmentConstantPoolArrayCache_CachedArray0 = segmentConstantPoolArrayCache0.new CachedArray(stringArray1);
        identityHashMap0.put(stringArray0, segmentConstantPoolArrayCache_CachedArray0);
        assertEquals(0, segmentConstantPoolArrayCache_CachedArray0.lastKnownSize());
        List<Integer> list0 = segmentConstantPoolArrayCache0.indexesForArrayKey(stringArray0, stringArray0[0]);
        assertNotNull(list0);
        assertEquals(1, list0.size());
    }
}
