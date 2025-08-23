package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TagSet#hashCode()} method.
 */
public class TagSetHashCodeTest {

    @Test
    public void hashCodeIsConsistentForEqualEmptyTagSets() {
        // Arrange: Create two separate but identical TagSet instances.
        // According to the equals() contract, two new empty TagSets should be equal.
        TagSet tagSetA = new TagSet();
        TagSet tagSetB = new TagSet();

        // Assert:
        // 1. Verify the two empty TagSets are considered equal. This is a precondition
        //    for testing the hashCode contract.
        assertEquals(tagSetA, tagSetB);

        // 2. Per the Java hashCode() contract, if two objects are equal,
        //    their hash codes must also be equal.
        assertEquals(tagSetA.hashCode(), tagSetB.hashCode());

        // 3. Verify that the hash code is consistent across multiple calls on the same
        //    unmodified object.
        int initialHashCode = tagSetA.hashCode();
        assertEquals(initialHashCode, tagSetA.hashCode());
    }
}