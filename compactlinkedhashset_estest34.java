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

public class CompactLinkedHashSet_ESTestTest34 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        ImmutableSet<Locale.Category> immutableSet0 = ImmutableSet.of(locale_Category0);
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create((Collection<? extends Locale.Category>) immutableSet0);
        compactLinkedHashSet0.toArray();
        assertTrue(compactLinkedHashSet0.contains(locale_Category0));
    }
}