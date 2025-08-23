package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ArraySorter_ESTestTest26 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        char[] charArray0 = new char[4];
        char[] charArray1 = ArraySorter.sort(charArray0);
        assertSame(charArray1, charArray0);
    }
}
