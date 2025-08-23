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

public class CompactLinkedHashSet_ESTestTest10 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = new CompactLinkedHashSet<Locale.Category>();
        CompactLinkedHashSet<Object> compactLinkedHashSet1 = CompactLinkedHashSet.create((Collection<?>) compactLinkedHashSet0);
        assertTrue(compactLinkedHashSet1.isEmpty());
    }
}
