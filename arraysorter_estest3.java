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

public class ArraySorter_ESTestTest3 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Integer[] integerArray0 = new Integer[5];
        Integer integer0 = new Integer(0);
        integerArray0[0] = integer0;
        integerArray0[1] = integer0;
        integerArray0[2] = integerArray0[0];
        integerArray0[3] = integer0;
        integerArray0[4] = integerArray0[1];
        Object[] objectArray0 = ArraySorter.sort((Object[]) integerArray0);
        assertEquals(5, objectArray0.length);
    }
}
