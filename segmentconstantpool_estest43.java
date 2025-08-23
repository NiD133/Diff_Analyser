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

public class SegmentConstantPool_ESTestTest43 extends SegmentConstantPool_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        SegmentConstantPool segmentConstantPool0 = new SegmentConstantPool((CpBands) null);
        ClassFileEntry classFileEntry0 = segmentConstantPool0.getValue(12, (-1L));
        assertNull(classFileEntry0);
    }
}
