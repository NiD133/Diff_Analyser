package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTestTest7 extends TestBase {

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
    public void explains_when_constructor_cannot_be_found() {
        try {
            new ConstructorInstantiator(false, new Object[0]).newInstance(SomeClass2.class);
            fail();
        } catch (org.mockito.creation.instance.InstantiationException e) {
            assertThat(e).hasMessageContaining("Unable to create instance of 'SomeClass2'.\n" + "Please ensure that the target class has a 0-arg constructor.");
        }
    }
}
