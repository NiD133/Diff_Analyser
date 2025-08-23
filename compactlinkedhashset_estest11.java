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

public class CompactLinkedHashSet_ESTestTest11 extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        CompactLinkedHashSet<Integer> compactLinkedHashSet0 = new CompactLinkedHashSet<Integer>(8232);
        Set<Integer> set0 = compactLinkedHashSet0.convertToHashFloodingResistantImplementation();
        assertEquals(0, set0.size());
    }
}