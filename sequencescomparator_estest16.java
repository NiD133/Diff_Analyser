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

public class SequencesComparator_ESTestTest16 extends SequencesComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        SequencesComparator<Object> sequencesComparator0 = null;
        try {
            sequencesComparator0 = new SequencesComparator<Object>((List<Object>) null, (List<Object>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.sequence.SequencesComparator", e);
        }
    }
}
