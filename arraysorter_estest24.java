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

public class ArraySorter_ESTestTest24 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        double[] doubleArray0 = new double[8];
        double[] doubleArray1 = ArraySorter.sort(doubleArray0);
        assertEquals(8, doubleArray1.length);
    }
}