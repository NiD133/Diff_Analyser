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

public class ArraySorter_ESTestTest5 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        int[] intArray0 = new int[7];
        int[] intArray1 = ArraySorter.sort(intArray0);
        assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0 }, intArray1);
    }
}
