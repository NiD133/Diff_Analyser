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

public class StringUtil_ESTestTest9 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        String string0 = StringUtil.padding(8188, 8188);
        StringBuilder stringBuilder0 = new StringBuilder((CharSequence) string0);
        StringBuilder stringBuilder1 = stringBuilder0.insert(8188, 0.0);
        stringBuilder1.append('\'');
        StringUtil.releaseBuilderVoid(stringBuilder1);
        assertSame(stringBuilder0, stringBuilder1);
    }
}
