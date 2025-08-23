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

public class SequencesComparator_ESTestTest13 extends SequencesComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        linkedList0.add((Object) linkedList0);
        LinkedList<Object> linkedList1 = new LinkedList<Object>(linkedList0);
        linkedList0.add((Object) linkedList1);
        Object object0 = new Object();
        linkedList0.offerFirst(object0);
        linkedList0.add((Object) linkedList1);
        linkedList0.add((Object) linkedList1);
        SequencesComparator<Object> sequencesComparator0 = new SequencesComparator<Object>(linkedList1, linkedList0);
        EditScript<Object> editScript0 = sequencesComparator0.getScript();
        assertEquals(4, editScript0.getModifications());
    }
}
