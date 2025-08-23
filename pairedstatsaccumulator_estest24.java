package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest24 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add(154.3044396684968, 154.3044396684968);
        Stats stats0 = new Stats((-9223372036854775808L), (-9223372036854775808L), (-9223372036854775808L), (-716.5114168133774), 1586.65153);
        ArrayDeque<Integer> arrayDeque0 = new ArrayDeque<Integer>();
        Iterator<Integer> iterator0 = arrayDeque0.descendingIterator();
        Stats stats1 = Stats.of(iterator0);
        PairedStats pairedStats0 = new PairedStats(stats0, stats1, (-716.5114168133774));
        // Undeclared exception!
        try {
            pairedStatsAccumulator0.addAll(pairedStats0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
