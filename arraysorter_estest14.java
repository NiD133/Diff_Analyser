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

public class ArraySorter_ESTestTest14 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Integer[] integerArray0 = new Integer[0];
        Integer[] integerArray1 = ArraySorter.sort(integerArray0);
        assertSame(integerArray0, integerArray1);
    }
}
