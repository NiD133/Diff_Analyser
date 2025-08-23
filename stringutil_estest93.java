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

public class StringUtil_ESTestTest93 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test92() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        ListIterator<Object> listIterator0 = linkedList0.listIterator();
        String string0 = StringUtil.join((Iterator<?>) listIterator0, ")&*!.GRzo RF[l,9W");
        assertEquals("", string0);
    }
}
