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

public class StringUtil_ESTestTest94 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test93() throws Throwable {
        StringUtil.StringJoiner stringUtil_StringJoiner0 = new StringUtil.StringJoiner("auzz./f?g8Z@iU:MH");
        StringUtil.StringJoiner stringUtil_StringJoiner1 = stringUtil_StringJoiner0.append("auzz./f?g8Z@iU:MH");
        assertSame(stringUtil_StringJoiner1, stringUtil_StringJoiner0);
    }
}
