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

public class TypeSafeMatchingTestTest10 {

    private static final Object NOT_A_COMPARABLE = new Object();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public IMethods mock;

    @Test
    public void passEveryArgumentTypeIfNoBridgeMethodWasGenerated() {
        final AtomicBoolean wasCalled = new AtomicBoolean();
        class GenericMatcher<T> implements ArgumentMatcher<T> {

            @Override
            public boolean matches(T argument) {
                wasCalled.set(true);
                return true;
            }
        }
        wasCalled.set(false);
        matchesTypeSafe().apply(new GenericMatcher<Integer>(), 123);
        assertThat(wasCalled.get()).isTrue();
        wasCalled.set(false);
        matchesTypeSafe().apply(new GenericMatcher<Integer>(), "");
        assertThat(wasCalled.get()).isTrue();
    }
}
