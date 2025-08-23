package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    @Test
    public void joinShouldCorrectlyAppendElementsIncludingNullsAndTheTargetBuilderItself() {
        // Arrange
        // Create a joiner with an empty string as the delimiter.
        final AppendableJoiner<StringBuilder> joiner = AppendableJoiner.<StringBuilder>builder()
                .setDelimiter("")
                .get();

        final StringBuilder targetBuilder = new StringBuilder();
        
        // The array of elements to join contains nulls and, crucially, the target StringBuilder itself.
        final StringBuilder[] elementsToJoin = new StringBuilder[4];
        elementsToJoin[2] = targetBuilder; // elementsToJoin is now [null, null, targetBuilder, null]

        // Act
        // Join the elements into the targetBuilder.
        joiner.join(targetBuilder, elementsToJoin);

        // Assert
        // The expected result is formed by concatenating the string representation of each element.
        // The process is as follows:
        // 1. Append String.valueOf(elementsToJoin[0]=null) -> "null"
        //    targetBuilder is now "null"
        // 2. Append String.valueOf(elementsToJoin[1]=null) -> "null"
        //    targetBuilder is now "nullnull"
        // 3. Append String.valueOf(elementsToJoin[2]=targetBuilder) -> targetBuilder.toString() -> "nullnull"
        //    targetBuilder is now "nullnullnullnull"
        // 4. Append String.valueOf(elementsToJoin[3]=null) -> "null"
        //    targetBuilder is now "nullnullnullnullnull"
        final String expected = "nullnullnullnullnull";
        assertEquals(expected, targetBuilder.toString());
    }
}