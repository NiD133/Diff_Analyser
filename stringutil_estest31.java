package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.junit.runner.RunWith;

public class StringUtil_ESTestTest31 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        LinkedList<Object> linkedList1 = new LinkedList<Object>();
        linkedList0.add((Object) linkedList1);
        linkedList1.add((Object) linkedList0);
        linkedList0.addAll((Collection<?>) linkedList1);
        // Undeclared exception!
        try {
            StringUtil.join((Collection<?>) linkedList0, "?h");
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
