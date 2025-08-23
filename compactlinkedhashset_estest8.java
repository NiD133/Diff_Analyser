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

public class CompactLinkedHashSet_ESTestTest8 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Locale.Category[] locale_CategoryArray0 = new Locale.Category[9];
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create(locale_CategoryArray0);
        int int0 = compactLinkedHashSet0.firstEntryIndex();
        assertEquals(1, compactLinkedHashSet0.size());
        assertEquals(0, int0);
    }
}
