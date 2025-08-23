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

public class StringUtil_ESTestTest23 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        try {
            StringUtil.resolve((URL) null, "B1s2U[+");
            fail("Expecting exception: MalformedURLException");
        } catch (MalformedURLException e) {
            //
            // no protocol: B1s2U[+
            //
            verifyException("java.net.URL", e);
        }
    }
}
