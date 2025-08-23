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

public class SequenceReader_ESTestTest9 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ArrayList<StringReader> arrayList0 = new ArrayList<StringReader>();
        List<StringReader> list0 = arrayList0.subList(0, 0);
        StringReader stringReader0 = new StringReader("Array Size=");
        arrayList0.add(stringReader0);
        SequenceReader sequenceReader0 = null;
        try {
            sequenceReader0 = new SequenceReader(list0);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.ArrayList$SubList", e);
        }
    }
}
