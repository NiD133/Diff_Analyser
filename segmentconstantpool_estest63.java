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

public class SegmentConstantPool_ESTestTest63 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test62() throws Throwable {
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        String[] stringArray0 = new String[6];
        segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray0, stringArray0, "<init>", "^6]wcG1bBG#D", 2355);
        stringArray0[0] = "((";
        // Undeclared exception!
        try {
            segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray0, stringArray0, (String) null, "^<init>.*", 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }
}
