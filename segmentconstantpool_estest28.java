package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentConstantPool_ESTestTest28 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        try {
            SegmentConstantPool.toIndex((-1975L));
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Cannot have a negative index.
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }
}
