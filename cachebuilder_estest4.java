package org.apache.ibatis.mapping;

import org.junit.Test;
import java.util.Properties;

/**
 * Test suite for {@link CacheBuilder}.
 * This class focuses on edge cases related to property handling.
 */
public class CacheBuilderTest {

    /**
     * Verifies that the build() method throws an exception when a property key is malformed.
     *
     * The underlying property-setting mechanism in MyBatis can fail with a
     * StringIndexOutOfBoundsException if it encounters a property key with an
     * unclosed square bracket. This test confirms that this edge case is handled
     * by throwing an exception, preventing silent failures or unpredictable behavior.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void buildShouldThrowExceptionForMalformedPropertyKeyWithUnclosedBracket() {
        // Arrange
        // A property key with an unclosed square bracket, which is known to cause parsing errors.
        final String malformedPropertyKey = "aProperty[";
        final String anyValue = "anyValue";
        final String cacheId = "testCache";

        Properties propertiesWithInvalidKey = new Properties();
        propertiesWithInvalidKey.setProperty(malformedPropertyKey, anyValue);

        CacheBuilder cacheBuilder = new CacheBuilder(cacheId);
        cacheBuilder.properties(propertiesWithInvalidKey);

        // Act & Assert
        // The build process attempts to set the properties on the cache instance.
        // This is expected to fail with a StringIndexOutOfBoundsException due to the malformed key.
        // The test passes if this specific exception is thrown.
        cacheBuilder.build();
    }
}