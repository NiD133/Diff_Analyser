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

public class LineIterator_ESTestTest6 extends LineIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        StringReader stringReader0 = new StringReader("remove Kot spported");
        stringReader0.close();
        LineIterator lineIterator0 = new LineIterator(stringReader0);
        // Undeclared exception!
        try {
            lineIterator0.next();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // java.io.IOException: Stream closed
            //
            verifyException("org.apache.commons.io.LineIterator", e);
        }
    }
}
