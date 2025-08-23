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

public class CompactLinkedHashSet_ESTestTest27 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = null;
        try {
            compactLinkedHashSet0 = new CompactLinkedHashSet<Locale.Category>((-439));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Expected size must be >= 0
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
