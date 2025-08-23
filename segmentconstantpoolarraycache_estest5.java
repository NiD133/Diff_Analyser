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

public class SegmentConstantPoolArrayCache_ESTestTest5 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        SegmentConstantPoolArrayCache segmentConstantPoolArrayCache0 = new SegmentConstantPoolArrayCache();
        String[] stringArray0 = new String[8];
        List<Integer> list0 = segmentConstantPoolArrayCache0.indexesForArrayKey(stringArray0, "Trying to cache an array that already exists");
        assertNotNull(list0);
        SegmentConstantPoolArrayCache.CachedArray segmentConstantPoolArrayCache_CachedArray0 = segmentConstantPoolArrayCache0.new CachedArray(stringArray0);
        boolean boolean0 = segmentConstantPoolArrayCache0.arrayIsCached(segmentConstantPoolArrayCache_CachedArray0.primaryArray);
        assertTrue(boolean0);
    }
}
