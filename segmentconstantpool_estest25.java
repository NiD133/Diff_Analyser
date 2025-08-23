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

public class SegmentConstantPool_ESTestTest25 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        boolean boolean0 = SegmentConstantPool.regexMatches("^<init>.*", "");
        assertFalse(boolean0);
    }
}
