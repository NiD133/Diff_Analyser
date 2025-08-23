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

public class ArraySorter_ESTestTest1 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        short[] shortArray0 = new short[0];
        short[] shortArray1 = ArraySorter.sort(shortArray0);
        assertSame(shortArray1, shortArray0);
    }
}
