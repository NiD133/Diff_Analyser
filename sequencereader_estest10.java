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

public class SequenceReader_ESTestTest10 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        SequenceReader sequenceReader0 = null;
        try {
            sequenceReader0 = new SequenceReader((Iterable<? extends Reader>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // readers
            //
            verifyException("java.util.Objects", e);
        }
    }
}
