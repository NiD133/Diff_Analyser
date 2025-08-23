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

public class ArraySorter_ESTestTest17 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        short[] shortArray0 = ArraySorter.sort((short[]) null);
        assertNull(shortArray0);
    }
}