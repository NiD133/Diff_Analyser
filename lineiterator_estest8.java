package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LineIterator_ESTestTest8 extends LineIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        LineIterator lineIterator0 = null;
        try {
            lineIterator0 = new LineIterator((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // reader
            //
            verifyException("java.util.Objects", e);
        }
    }
}
