package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests Soundex compatibility with examples from a Microsoft SQL Server article.
 * This ensures that the Apache Commons Codec implementation of Soundex
 * produces results consistent with other known implementations.
 */
@DisplayName("Soundex MS SQL Server Compatibility Test")
public class SoundexMssqlCompatibilityTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * The test cases are sourced from an article on MS SQL Server's Soundex implementation.
     * Source: https://databases.about.com/library/weekly/aa042901a.htm
     *
     * @param name the input string to be encoded.
     * @param expectedSoundex the expected Soundex code.
     */
    @DisplayName("Should encode names to their expected Soundex codes from MS SQL Server examples")
    @ParameterizedTest(name = "encode(''{0}'') => ''{1}''")
    @CsvSource({
        "Ann,      A500",
        "Andrew,   A536",
        "Janet,    J530",
        "Margaret, M626",
        "Steven,   S315",
        "Michael,  M240",
        "Robert,   R163",
        "Laura,    L600",
        "Anne,     A500"
    })
    void testEncodeNamesFromMsSqlServerArticle(final String name, final String expectedSoundex) {
        assertEquals(expectedSoundex, getStringEncoder().encode(name));
    }
}