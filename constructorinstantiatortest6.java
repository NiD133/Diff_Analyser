package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTestTest6 extends TestBase {

    static class SomeClass {
    }

    class SomeInnerClass {
    }

    class ChildOfThis extends ConstructorInstantiatorTest {
    }

    static class SomeClass2 {

        SomeClass2(String x) {
        }
    }

    static class SomeClass3 {

        SomeClass3(int i) {
        }
    }

    @Test
    public void fails_when_null_is_passed_for_a_primitive() {
        assertThatThrownBy(() -> {
            new ConstructorInstantiator(false, new Object[] { null }).newInstance(SomeClass3.class).getClass();
        }).isInstanceOf(org.mockito.creation.instance.InstantiationException.class).hasMessageContaining("Unable to create instance of 'SomeClass3'.");
    }
}
