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

public class CompactLinkedHashSet_ESTestTest13 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Object[] objectArray0 = new Object[2];
        CompactLinkedHashSet<Object> compactLinkedHashSet0 = CompactLinkedHashSet.create(objectArray0);
        int int0 = compactLinkedHashSet0.adjustAfterRemove(1941, 1941);
        assertEquals(1941, int0);
        assertEquals(1, compactLinkedHashSet0.size());
    }
}
