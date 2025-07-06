package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.validateMockitoUsage;

import org.junit.After;
import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

import java.util.Map;

/**
 * Test suite for the ArgumentCaptor class.
 */
public class ArgumentCaptorTest {

    /**
     * Cleans up the internal Mockito state after each test to ensure no invalid matcher usage.
     */
    @After
    public void tearDown() {
        try {
            validateMockitoUsage();
        } catch (InvalidUseOfMatchersException ignore) {
            // Ignoring this exception as it is expected in some cases
        }
    }

    /**
     * Test to verify that the capture method returns null or default values.
     */
    @Test
    public void testCaptureMethodReturnsNullOrDefaultValue() {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        assertThat(captor.capture()).isNull();
    }

    /**
     * Test to verify that getCaptorType returns the correct class type for the captor.
     */
    @Test
    public void testGetCaptorTypeReturnsCorrectClassType() {
        class Foo {}
        class Bar {}

        ArgumentCaptor<Foo> fooCaptor = ArgumentCaptor.forClass(Foo.class);
        ArgumentCaptor<Bar> barCaptor = ArgumentCaptor.forClass(Bar.class);

        assertThat(fooCaptor.getCaptorType()).isEqualTo(Foo.class);
        assertThat(barCaptor.getCaptorType()).isEqualTo(Bar.class);
    }

    /**
     * Test to verify that captor infers the correct argument type when using the captor method.
     */
    @Test
    public void testCaptorInfersCorrectArgumentType() {
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.captor();
        assertThat(captor.getCaptorType()).isEqualTo(Map.class);
    }

    /**
     * Test to verify that calling captor with explicit varargs throws an IllegalArgumentException.
     */
    @Test
    public void testCaptorWithExplicitVarargsThrowsException() {
        // Test passing a single argument.
        assertThatThrownBy(() -> ArgumentCaptor.captor(1234L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Illegal argument");

        // Test passing multiple arguments.
        assertThatThrownBy(() -> ArgumentCaptor.captor("this shouldn't", "be here"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Illegal argument");

        // Test passing a totally null varargs array.
        assertThatThrownBy(() -> ArgumentCaptor.<String>captor((String[]) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Illegal argument");
    }
}