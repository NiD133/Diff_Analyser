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

public class HashCodeAndEqualsSafeSet_ESTestTest26 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = new HashCodeAndEqualsSafeSet();
        int int0 = hashCodeAndEqualsSafeSet0.size();
        assertEquals(0, int0);
    }
}
