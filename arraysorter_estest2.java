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

public class ArraySorter_ESTestTest2 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Object[] objectArray0 = new Object[0];
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        Object[] objectArray1 = ArraySorter.sort(objectArray0, (Comparator<? super Object>) comparator0);
        assertSame(objectArray1, objectArray0);
    }
}
