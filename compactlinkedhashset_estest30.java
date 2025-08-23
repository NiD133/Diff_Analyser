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

public class CompactLinkedHashSet_ESTestTest30 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Object[] objectArray0 = new Object[2];
        Object object0 = new Object();
        objectArray0[0] = object0;
        CompactLinkedHashSet<Object> compactLinkedHashSet0 = CompactLinkedHashSet.create(objectArray0);
        EmptyImmutableSetMultimap emptyImmutableSetMultimap0 = EmptyImmutableSetMultimap.INSTANCE;
        ImmutableCollection<Object> immutableCollection0 = emptyImmutableSetMultimap0.createValues();
        boolean boolean0 = compactLinkedHashSet0.retainAll(immutableCollection0);
        assertTrue(compactLinkedHashSet0.isEmpty());
        assertTrue(boolean0);
    }
}
