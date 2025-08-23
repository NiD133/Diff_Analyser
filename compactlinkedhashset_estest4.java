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

public class CompactLinkedHashSet_ESTestTest4 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Object[] objectArray0 = new Object[1];
        CompactLinkedHashSet<Object> compactLinkedHashSet0 = CompactLinkedHashSet.create(objectArray0);
        compactLinkedHashSet0.resizeEntries(41);
        assertEquals(1, compactLinkedHashSet0.size());
    }
}
