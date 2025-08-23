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

public class SegmentConstantPoolArrayCache_ESTestTest11 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        SegmentConstantPoolArrayCache segmentConstantPoolArrayCache0 = new SegmentConstantPoolArrayCache();
        String[] stringArray0 = new String[8];
        SegmentConstantPoolArrayCache.CachedArray segmentConstantPoolArrayCache_CachedArray0 = segmentConstantPoolArrayCache0.new CachedArray(stringArray0);
        int int0 = segmentConstantPoolArrayCache_CachedArray0.lastKnownSize();
        assertEquals(8, int0);
    }
}
