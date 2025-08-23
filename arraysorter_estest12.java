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

public class ArraySorter_ESTestTest12 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Integer[] integerArray0 = new Integer[5];
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0, 0, 0, 0).when(comparator0).compare(any(), any());
        Integer[] integerArray1 = ArraySorter.sort(integerArray0, (Comparator<? super Integer>) comparator0);
        assertEquals(5, integerArray1.length);
    }
}
