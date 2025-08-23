package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
// The original class name and inheritance from the scaffolding are preserved.
public class Uncheck_ESTestTest24 extends Uncheck_ESTest_scaffolding {

    /**
     * Tests that {@link Uncheck#test(IOPredicate, Object)} throws a NullPointerException
     * when the predicate is null.
     */
    @Test(expected = NullPointerException.class)
    public void testWithNullPredicateShouldThrowNullPointerException() {
        // The value passed here is irrelevant; the test ensures the null predicate is handled.
        Uncheck.test(null, "any value");
    }
}