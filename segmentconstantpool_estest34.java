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

public class SegmentConstantPool_ESTestTest34 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        String[] stringArray0 = new String[8];
        stringArray0[1] = "^<init>.*";
        int int0 = segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray0, "^<init>.*", 0);
        assertEquals(1, int0);
    }
}
