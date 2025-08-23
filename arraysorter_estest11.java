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

public class ArraySorter_ESTestTest11 extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Object[] objectArray0 = new Object[4];
        Object object0 = new Object();
        objectArray0[1] = object0;
        // Undeclared exception!
        try {
            ArraySorter.sort(objectArray0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // java.lang.Object cannot be cast to java.lang.Comparable
            //
            verifyException("java.util.ComparableTimSort", e);
        }
    }
}
