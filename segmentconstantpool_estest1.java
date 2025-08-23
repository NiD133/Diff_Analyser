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

public class SegmentConstantPool_ESTestTest1 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        String[] stringArray0 = new String[6];
        int int0 = segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray0, stringArray0[0], (-182));
        assertEquals((-1), int0);
    }
}
