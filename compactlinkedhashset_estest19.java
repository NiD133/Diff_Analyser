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

public class CompactLinkedHashSet_ESTestTest19 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create();
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        // Undeclared exception!
        try {
            compactLinkedHashSet0.insertEntry(91, locale_Category0, 91, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.Objects", e);
        }
    }
}
