package org.mockito.internal.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.invocation.TypeSafeMatching.matchesTypeSafe;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class TypeSafeMatchingTestTest5 {

    private static final Object NOT_A_COMPARABLE = new Object();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public IMethods mock;

    /**
     * Should not throw an {@link ClassCastException}
     */
    @Test
    public void compareToStringVsInt() {
        boolean match = matchesTypeSafe().apply(new StartsWith("Hello"), 123);
        assertThat(match).isFalse();
    }
}
