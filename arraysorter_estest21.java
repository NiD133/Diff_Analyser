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

public class ArraySorter_ESTestTest21 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        int[] intArray0 = ArraySorter.sort((int[]) null);
        assertNull(intArray0);
    }
}
