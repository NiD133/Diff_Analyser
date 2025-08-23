package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BoundedReader_ESTestTest1 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        StringReader stringReader0 = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 3);
        char[] charArray0 = new char[7];
        int int0 = boundedReader0.read(charArray0);
        assertEquals(3, int0);
        assertArrayEquals(new char[] { 'o', 'r', 'g', '\u0000', '\u0000', '\u0000', '\u0000' }, charArray0);
    }
}
