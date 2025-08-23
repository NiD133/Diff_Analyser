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

public class CompactLinkedHashSet_ESTestTest20 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        ImmutableSortedSet<Locale.Category> immutableSortedSet0 = ImmutableSortedSet.of(locale_Category0, locale_Category0, locale_Category0);
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create((Collection<? extends Locale.Category>) immutableSortedSet0);
        // Undeclared exception!
        try {
            compactLinkedHashSet0.insertEntry(133, locale_Category0, 133, 1485);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 133
            //
            verifyException("com.google.common.collect.CompactHashSet", e);
        }
    }
}
