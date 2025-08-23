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

public class SequencesComparator_ESTestTest2 extends SequencesComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        Object object0 = new Object();
        linkedList0.add(object0);
        linkedList0.add(object0);
        LinkedList<Object> linkedList1 = new LinkedList<Object>();
        linkedList0.add(object0);
        linkedList0.add(object0);
        linkedList1.add((Object) linkedList0);
        SequencesComparator<Object> sequencesComparator0 = new SequencesComparator<Object>(linkedList0, linkedList1);
        EditScript<Object> editScript0 = sequencesComparator0.getScript();
        assertEquals(5, editScript0.getModifications());
    }
}
