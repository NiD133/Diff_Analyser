package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTestTest3 extends TestBase {

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
    public void creates_instances_with_arguments() {
        assertThat(new ConstructorInstantiator(false, "someString").newInstance(SomeClass2.class).getClass()).isEqualTo(SomeClass2.class);
    }
}
