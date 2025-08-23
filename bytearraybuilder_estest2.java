package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest2 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder();
        ByteArrayBuilder byteArrayBuilder1 = ByteArrayBuilder.fromInitial(byteArrayBuilder0.NO_BYTES, 2000);
        byte[] byteArray0 = byteArrayBuilder1.finishCurrentSegment();
        byteArrayBuilder0.write(byteArray0);
        assertEquals(0, byteArrayBuilder1.getCurrentSegmentLength());
        assertEquals(500, byteArrayBuilder0.getCurrentSegmentLength());
    }
}
