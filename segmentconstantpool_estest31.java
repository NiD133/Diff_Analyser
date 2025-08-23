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

public class SegmentConstantPool_ESTestTest31 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        String[] stringArray0 = new String[0];
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        String[] stringArray1 = new String[2];
        // Undeclared exception!
        try {
            segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray1, stringArray0, stringArray1[1], ":M", 2501);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }
}
