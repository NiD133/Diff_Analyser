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

public class SegmentConstantPool_ESTestTest65 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test64() throws Throwable {
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        String[] stringArray0 = new String[1];
        stringArray0[0] = "v^<iPit>.*";
        // Undeclared exception!
        try {
            segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray0, stringArray0, "v^<iPit>.*", "v^<iPit>.*", (-1089));
            fail("Expecting exception: Error");
        } catch (Error e) {
            //
            // regex trying to match a pattern I don't know: v^<iPit>.*
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentConstantPool", e);
        }
    }
}
