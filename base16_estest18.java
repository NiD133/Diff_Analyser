package org.apache.commons.codec.binary;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests the constructor of the {@link Base16} class.
 */
public class Base16ConstructorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that the constructor throws a NullPointerException when a null CodecPolicy is provided.
     * The constructor's contract requires a non-null policy.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionWhenCodecPolicyIsNull() {
        // Arrange: Define the expected exception and its message.
        // The underlying BaseNCodec constructor enforces this with Objects.requireNonNull.
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("codecPolicy");

        // Act: Call the constructor with a null CodecPolicy, which should trigger the exception.
        new Base16(true, null);
    }
}