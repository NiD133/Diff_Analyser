package org.apache.commons.codec.language;

import org.apache.commons.codec.AbstractStringEncoderTest;

/**
 * Tests the {@link Soundex} class, including the common test cases inherited from
 * {@link AbstractStringEncoderTest}.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    /**
     * Creates a new instance of the {@link Soundex} encoder for testing.
     * This is the concrete implementation required by the abstract test class.
     *
     * @return a new {@code Soundex} instance
     */
    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }
}