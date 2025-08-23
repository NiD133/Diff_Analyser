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

public class StringUtil_ESTestTest64 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        String[] stringArray0 = new String[8];
        // Undeclared exception!
        try {
            StringUtil.in("", stringArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
