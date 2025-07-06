package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.validateMockitoUsage;

import org.junit.After;
import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

import java.util.Map;

/**
 * Test class for ArgumentCaptor.
 */
public class ArgumentCaptorTest {

    /**
     * Clean up the internal Mockito-Stubbing state after each test.
     */
    @After
    public void tearDown() {
        try {
            validateMockitoUsage();
        } catch (InvalidUseOfMatchersException ignore) {
            // Ignore the exception to ensure test execution completes successfully.
        }
    }

    /**
     * Verify that the initial capture returns null.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testInitialCaptureReturnsNull() throws Exception {
        // Arrange
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);

        // Act and Assert
        assertThat(captor.capture()).isNull();
    }

    /**
     * Verify that the captor type matches the specified class.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testCaptorTypeMatchesClass() throws Exception {
        // Arrange
        class Foo {}
        class Bar {}

        ArgumentCaptor<Foo> fooCaptor = ArgumentCaptor.forClass(Foo.class);
        ArgumentCaptor<Bar> barCaptor = ArgumentCaptor.forClass(Bar.class);

        // Act and Assert
        assertThat(fooCaptor.getCaptorType()).isEqualTo(Foo.class);
        assertThat(barCaptor.getCaptorType()).isEqualTo(Bar.class);
    }

    /**
     * Verify that the captor's class is inferred correctly.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testCaptorInferClass() throws Exception {
        // Arrange
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.captor();

        // Act and Assert
        assertThat(captor.getCaptorType()).isEqualTo(Map.class);
    }

    /**
     * Verify that calling captor with explicit varargs throws an exception.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testCaptorWithExplicitVarargsThrowsException() throws Exception {
        // Act and Assert
        assertThatThrownBy(() -> ArgumentCaptor.captor(1234L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ArgumentCaptor.captor("this shouldn't", "be here")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ArgumentCaptor.<String>captor((String[]) null)).isInstanceOf(IllegalArgumentException.class);
    }
}