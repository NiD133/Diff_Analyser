package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class HashCodeAndEqualsSafeSetTestTest1 {

    @Rule
    public MockitoRule r = MockitoJUnit.rule();

    @Mock
    private UnmockableHashCodeAndEquals mock1;

    private static class UnmockableHashCodeAndEquals {

        @Override
        public final int hashCode() {
            throw new NullPointerException("I'm failing on hashCode and I don't care");
        }

        @Override
        public final boolean equals(Object obj) {
            throw new NullPointerException("I'm failing on equals and I don't care");
        }
    }

    @Test
    public void can_add_mock_that_have_failing_hashCode_method() throws Exception {
        new HashCodeAndEqualsSafeSet().add(mock1);
    }
}
