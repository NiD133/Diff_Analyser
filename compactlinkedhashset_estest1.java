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

public class CompactLinkedHashSet_ESTestTest1 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Integer[] integerArray0 = new Integer[2];
        CompactLinkedHashSet<Integer> compactLinkedHashSet0 = CompactLinkedHashSet.create(integerArray0);
        compactLinkedHashSet0.moveLastEntry(0, 0);
        assertEquals(1, compactLinkedHashSet0.size());
    }
}
