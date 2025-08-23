package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TextHelpAppendable_ESTestTest45 extends TextHelpAppendable_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        TextHelpAppendable textHelpAppendable0 = TextHelpAppendable.systemOut();
        byte[] byteArray0 = new byte[5];
        byteArray0[3] = (byte) 12;
        ByteBuffer byteBuffer0 = ByteBuffer.wrap(byteArray0);
        Charset charset0 = Charset.defaultCharset();
        CharBuffer charBuffer0 = charset0.decode(byteBuffer0);
        TextStyle textStyle0 = TextStyle.DEFAULT;
        // Undeclared exception!
        try {
            textHelpAppendable0.makeColumnQueue(charBuffer0, textStyle0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.nio.HeapCharBuffer", e);
        }
    }
}
