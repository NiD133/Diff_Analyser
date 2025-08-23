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

public class CompactLinkedHashSet_ESTestTest14 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Integer[] integerArray0 = new Integer[0];
        CompactLinkedHashSet<Integer> compactLinkedHashSet0 = CompactLinkedHashSet.create(integerArray0);
        int int0 = compactLinkedHashSet0.adjustAfterRemove(73, (-1));
        assertEquals((-1), int0);
    }
}
