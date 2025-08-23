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

public class TypeSafeMatchingTestTest9 {

    private static final Object NOT_A_COMPARABLE = new Object();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public IMethods mock;

    @Test
    public void dontMatchesWithSubTypeExtendingGenericClass() {
        final AtomicBoolean wasCalled = new AtomicBoolean();
        abstract class GenericMatcher<T> implements ArgumentMatcher<T> {
        }
        class TestMatcher extends GenericMatcher<Integer> {

            @Override
            public boolean matches(Integer argument) {
                wasCalled.set(true);
                return true;
            }
        }
        wasCalled.set(false);
        matchesTypeSafe().apply(new TestMatcher(), 123);
        assertThat(wasCalled.get()).isTrue();
        wasCalled.set(false);
        matchesTypeSafe().apply(new TestMatcher(), "");
        assertThat(wasCalled.get()).isFalse();
    }
}
