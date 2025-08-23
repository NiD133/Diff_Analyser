package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTestTest1 extends TestBase {

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
    public void creates_instances() {
        assertThat(new ConstructorInstantiator(false, new Object[0]).newInstance(SomeClass.class).getClass()).isEqualTo(SomeClass.class);
    }
}