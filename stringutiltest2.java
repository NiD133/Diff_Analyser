package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTestTest2 {

    @Test
    public void padding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("  ", StringUtil.padding(2));
        assertEquals("               ", StringUtil.padding(15));
        // we default to tap out at 30
        assertEquals("                              ", StringUtil.padding(45));
        // memoization is up to 21 blocks (0 to 20 spaces) and exits early before min checks making maxPaddingWidth unused
        assertEquals("", StringUtil.padding(0, -1));
        assertEquals("                    ", StringUtil.padding(20, -1));
        // this test escapes memoization and continues through
        assertEquals("                     ", StringUtil.padding(21, -1));
        // this test escapes memoization and using unlimited length (-1) will allow requested spaces
        assertEquals("                              ", StringUtil.padding(30, -1));
        assertEquals("                                             ", StringUtil.padding(45, -1));
        // we tap out at 0 for this test
        assertEquals("", StringUtil.padding(0, 0));
        // as memoization is escaped, setting zero for max padding will not allow any requested width
        assertEquals("", StringUtil.padding(21, 0));
        // we tap out at 30 for these tests making > 30 use 30
        assertEquals("", StringUtil.padding(0, 30));
        assertEquals(" ", StringUtil.padding(1, 30));
        assertEquals("  ", StringUtil.padding(2, 30));
        assertEquals("               ", StringUtil.padding(15, 30));
        assertEquals("                              ", StringUtil.padding(45, 30));
        // max applies regardless of memoized
        assertEquals(5, StringUtil.padding(20, 5).length());
    }
}
