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

public class CompactLinkedHashSet_ESTestTest31 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Locale.Category[] locale_CategoryArray0 = new Locale.Category[3];
        CompactLinkedHashSet<Object> compactLinkedHashSet0 = CompactLinkedHashSet.create();
        Object[] objectArray0 = compactLinkedHashSet0.toArray((Object[]) locale_CategoryArray0);
        assertSame(locale_CategoryArray0, objectArray0);
    }
}