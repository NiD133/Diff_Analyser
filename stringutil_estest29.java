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

public class StringUtil_ESTestTest29 extends StringUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        StringBuilder stringBuilder0 = StringUtil.borrowBuilder();
        Iterator<StringBuilder> iterator0 = (Iterator<StringBuilder>) mock(Iterator.class, new ViolatedAssumptionAnswer());
        doReturn(true, false).when(iterator0).hasNext();
        doReturn(stringBuilder0).when(iterator0).next();
        String string0 = "";
        StringUtil.padding(122);
        StringUtil.releaseBuilderVoid(stringBuilder0);
        StringUtil.join(iterator0, "");
        char char0 = '3';
        StringUtil.isDigit(char0);
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = stringBuilder1.append(string0);
        boolean boolean0 = true;
        stringBuilder1.append(boolean0);
        StringBuilder stringBuilder3 = stringBuilder2.append((CharSequence) stringBuilder1);
        int int0 = 2224;
        char[] charArray0 = new char[7];
        charArray0[0] = char0;
        charArray0[1] = char0;
        charArray0[2] = char0;
        charArray0[3] = char0;
        charArray0[4] = char0;
        char char1 = 'f';
        charArray0[5] = char1;
        charArray0[6] = char0;
        // Undeclared exception!
        try {
            stringBuilder3.insert(int0, charArray0);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //
            // String index out of range: 2224
            //
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }
}
