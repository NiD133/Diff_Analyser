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

public class ArraySorter_ESTestTest6 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        float[] floatArray0 = new float[1];
        float[] floatArray1 = ArraySorter.sort(floatArray0);
        assertSame(floatArray1, floatArray0);
    }
}
