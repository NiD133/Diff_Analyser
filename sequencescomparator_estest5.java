package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SequencesComparator_ESTestTest5 extends SequencesComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        LinkedList<Object> linkedList1 = new LinkedList<Object>(linkedList0);
        List<Object> list0 = linkedList0.subList(0, 0);
        linkedList0.add((Object) linkedList1);
        linkedList1.add((Object) list0);
        SequencesComparator<Object> sequencesComparator0 = new SequencesComparator<Object>(linkedList1, linkedList0);
        // Undeclared exception!
        try {
            sequencesComparator0.getScript();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.SubList", e);
        }
    }
}
