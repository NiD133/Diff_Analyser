package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HashCodeAndEqualsSafeSet_ESTestTest18 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper hashCodeAndEqualsMockWrapper0 = new HashCodeAndEqualsMockWrapper(hashCodeAndEqualsSafeSet0);
        hashCodeAndEqualsSafeSet0.add(hashCodeAndEqualsMockWrapper0);
        // Undeclared exception!
        try {
            hashCodeAndEqualsSafeSet0.toString();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not initialize plugin: interface org.mockito.plugins.MockMaker (alternate: null)
            //
            verifyException("org.mockito.internal.configuration.plugins.PluginLoader$1", e);
        }
    }
}
