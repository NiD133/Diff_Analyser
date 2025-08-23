package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.Set;

/**
 * Tests the constructor behavior of the {@link CompositeSet} class.
 *
 * Note: This class retains the original name for context, but a more
 * descriptive name like {@code CompositeSetConstructorTest} would be preferable.
 */
public class CompositeSet_ESTestTest84 {

    /**
     * Tests that the varargs constructor {@code CompositeSet(Set...)} throws
     * a NullPointerException when passed a null array.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullSetArrayShouldThrowNullPointerException() {
        // The constructor should not accept a null array for the composited sets.
        // The cast to (Set<Object>[]) is necessary to avoid ambiguity between
        // the varargs constructor and the single-set constructor.
        new CompositeSet<Object>((Set<Object>[]) null);
    }
}