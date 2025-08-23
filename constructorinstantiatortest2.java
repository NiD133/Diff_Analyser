package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTestTest2 extends TestBase {

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
    public void creates_instances_of_inner_classes() {
        assertThat(new ConstructorInstantiator(true, this).newInstance(SomeInnerClass.class).getClass()).isEqualTo(SomeInnerClass.class);
        assertThat(new ConstructorInstantiator(true, new ChildOfThis()).newInstance(SomeInnerClass.class).getClass()).isEqualTo(SomeInnerClass.class);
    }
}
