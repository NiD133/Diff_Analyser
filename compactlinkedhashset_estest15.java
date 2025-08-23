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

public class CompactLinkedHashSet_ESTestTest15 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        ImmutableSet<Locale.Category> immutableSet0 = ImmutableSet.of(locale_Category0);
        CompactLinkedHashSet<Locale.Category> compactLinkedHashSet0 = CompactLinkedHashSet.create((Collection<? extends Locale.Category>) immutableSet0);
        Object[] objectArray0 = new Object[0];
        compactLinkedHashSet0.elements = objectArray0;
        // Undeclared exception!
        try {
            compactLinkedHashSet0.toArray();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("com.google.common.collect.CompactHashSet", e);
        }
    }
}
