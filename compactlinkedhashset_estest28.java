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

public class CompactLinkedHashSet_ESTestTest28 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        CompactLinkedHashSet<Integer> compactLinkedHashSet0 = CompactLinkedHashSet.createWithExpectedSize(2497);
        compactLinkedHashSet0.clear();
        assertEquals(0, compactLinkedHashSet0.size());
    }
}
