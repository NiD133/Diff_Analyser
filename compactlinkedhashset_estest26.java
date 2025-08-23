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

public class CompactLinkedHashSet_ESTestTest26 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Locale.Category[] locale_CategoryArray0 = new Locale.Category[1];
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create(locale_CategoryArray0);
        CompactLinkedHashSet<Object> compactLinkedHashSet1 = CompactLinkedHashSet.create((Collection<?>) compactLinkedHashSet0);
        // Undeclared exception!
        try {
            compactLinkedHashSet1.allocArrays();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Arrays already allocated
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
