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

public class CompactLinkedHashSet_ESTestTest7 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CompactLinkedHashSet<Integer> compactLinkedHashSet0 = CompactLinkedHashSet.createWithExpectedSize(31);
        Spliterator<Integer> spliterator0 = compactLinkedHashSet0.spliterator();
        assertNotNull(spliterator0);
    }
}
