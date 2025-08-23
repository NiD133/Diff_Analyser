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

public class SegmentConstantPoolArrayCache_ESTestTest4 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        SegmentConstantPoolArrayCache segmentConstantPoolArrayCache0 = new SegmentConstantPoolArrayCache();
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        segmentConstantPoolArrayCache0.lastIndexes = (List<Integer>) linkedList0;
        String[] stringArray0 = new String[2];
        segmentConstantPoolArrayCache0.lastArray = stringArray0;
        segmentConstantPoolArrayCache0.cacheArray(stringArray0);
        segmentConstantPoolArrayCache0.lastArray = stringArray0;
        List<Integer> list0 = segmentConstantPoolArrayCache0.indexesForArrayKey(stringArray0, (String) null);
        assertTrue(list0.isEmpty());
    }
}
