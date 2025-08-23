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

public class SegmentConstantPool_ESTestTest64 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        String[] stringArray0 = new String[11];
        stringArray0[2] = "^]LzTC)tW7*]J5tWdvD$";
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        int int0 = segmentConstantPool0.matchSpecificPoolEntryIndex(stringArray0, stringArray0, "^]LzTC)tW7*]J5tWdvD$", "^<init>.*", (-1311));
        assertEquals((-1), int0);
    }
}
