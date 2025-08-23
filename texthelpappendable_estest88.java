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

public class TextHelpAppendable_ESTestTest88 extends TextHelpAppendable_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test87() throws Throwable {
        TextHelpAppendable textHelpAppendable0 = TextHelpAppendable.systemOut();
        ArrayList<TextStyle> arrayList0 = new ArrayList<TextStyle>();
        TextStyle textStyle0 = TextStyle.DEFAULT;
        arrayList0.add(textStyle0);
        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1.add("|iq2*P~/");
        PriorityQueue<List<String>> priorityQueue0 = new PriorityQueue<List<String>>();
        priorityQueue0.add(arrayList1);
        TableDefinition tableDefinition0 = TableDefinition.from("|iq2*P~/", arrayList0, arrayList1, priorityQueue0);
        textHelpAppendable0.appendTable(tableDefinition0);
        assertEquals(74, textHelpAppendable0.getMaxWidth());
        assertEquals(3, textHelpAppendable0.getIndent());
    }
}
