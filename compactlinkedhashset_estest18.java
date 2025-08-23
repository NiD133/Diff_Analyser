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

public class CompactLinkedHashSet_ESTestTest18 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Locale.Category[] locale_CategoryArray0 = new Locale.Category[1];
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create(locale_CategoryArray0);
        // Undeclared exception!
        try {
            compactLinkedHashSet0.moveLastEntry(1, 32);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 1
            //
            verifyException("com.google.common.collect.CompactHashSet", e);
        }
    }
}
