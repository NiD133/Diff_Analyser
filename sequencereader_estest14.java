package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SequenceReader_ESTestTest14 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        ArrayDeque<StringReader> arrayDeque0 = new ArrayDeque<StringReader>();
        SequenceReader sequenceReader0 = new SequenceReader(arrayDeque0);
        char[] charArray0 = new char[7];
        // Undeclared exception!
        try {
            sequenceReader0.read(charArray0, (-1), (-1));
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Array Size=7, offset=-1, length=-1
            //
            verifyException("org.apache.commons.io.input.SequenceReader", e);
        }
    }
}
