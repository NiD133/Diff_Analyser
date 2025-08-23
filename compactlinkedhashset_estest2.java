package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.Spliterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CompactLinkedHashSet_ESTestTest2 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        int int0 = 1;
        Integer[] integerArray0 = new Integer[7];
        CompactLinkedHashSet<Integer> compactLinkedHashSet0 = CompactLinkedHashSet.create(integerArray0);
        compactLinkedHashSet0.insertEntry(2, (Integer) int0, int0, 2788);
        assertEquals(1, compactLinkedHashSet0.size());
    }
}
