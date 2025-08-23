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

public class SegmentConstantPool_ESTestTest12 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        try {
            segmentConstantPool0.getInitMethodPoolEntry(3120, 3120, "Tried to get a value I don't know about: ");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Nothing but CP_METHOD can be an <init>
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }
}
