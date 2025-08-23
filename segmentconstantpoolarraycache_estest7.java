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

public class SegmentConstantPoolArrayCache_ESTestTest7 extends SegmentConstantPoolArrayCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        SegmentConstantPoolArrayCache segmentConstantPoolArrayCache0 = new SegmentConstantPoolArrayCache();
        // Undeclared exception!
        try {
            segmentConstantPoolArrayCache0.cacheArray((String[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPoolArrayCache$CachedArray", e);
        }
    }
}
