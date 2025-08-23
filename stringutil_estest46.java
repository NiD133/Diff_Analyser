package org.jsoup.internal;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Test for {@link StringUtil#join(Iterator, String)} to ensure it handles concurrent modifications correctly.
 */
public class StringUtilJoinIteratorTest {

    /**
     * Verifies that StringUtil.join() throws a ConcurrentModificationException
     * when the underlying collection is modified after an iterator has been created from it.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void joinWithIteratorThrowsExceptionWhenCollectionIsModified() {
        // Arrange: Create a list and an iterator for it.
        List<String> list = new LinkedList<>();
        Iterator<String> iterator = list.iterator();

        // Act: Modify the list after the iterator has been created. This action
        // invalidates the iterator, which should cause any subsequent use to fail.
        list.add("one");

        // Assert: Calling join with the now-invalidated iterator must throw
        // a ConcurrentModificationException. The @Test(expected=...) annotation handles this check.
        StringUtil.join(iterator, ", ");
    }
}