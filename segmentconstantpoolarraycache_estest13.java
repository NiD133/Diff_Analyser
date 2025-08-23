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

public class SegmentConstantPoolArrayCache_ESTestTest13 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        SegmentConstantPoolArrayCache segmentConstantPoolArrayCache0 = new SegmentConstantPoolArrayCache();
        String[] stringArray0 = new String[9];
        List<Integer> list0 = segmentConstantPoolArrayCache0.indexesForArrayKey(stringArray0, stringArray0[0]);
        assertEquals(9, list0.size());
        assertNotNull(list0);
        List<Integer> list1 = segmentConstantPoolArrayCache0.indexesForArrayKey(stringArray0, "");
        assertTrue(list1.isEmpty());
    }
}
