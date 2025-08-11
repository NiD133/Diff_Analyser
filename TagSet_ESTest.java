package org.jsoup.parser;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class TagSetTest {

    @Test
    public void htmlCopy_isInitiallyEqualThenDivergesAfterMutation() {
        TagSet base = TagSet.Html();
        TagSet copy = new TagSet(base);

        // A fresh copy should be equal to the source
        assertEquals(base, copy);

        // Mutate the copy by adding a new tag
        Tag custom = Tag.valueOf("custom").namespace("ns");
        copy.add(custom);

        // Now they should differ; the tag only exists in the copy
        assertNotEquals(base, copy);
        assertNull(base.get("custom", "ns"));
        assertSame(custom, copy.get("custom", "ns"));
    }

    @Test
    public void add_putsAndReplacesByNameAndNamespace() {
        TagSet set = new TagSet();

        Tag first = Tag.valueOf("widget").namespace("urn:example");
        set.add(first);
        assertSame(first, set.get("widget", "urn:example"));

        // Adding a tag with the same name/namespace should replace the old one
        Tag replacement = Tag.valueOf("widget").namespace("urn:example");
        replacement.set(Tag.SelfClose);
        set.add(replacement);

        Tag got = set.get("widget", "urn:example");
        assertSame(replacement, got);
        assertTrue(got.isSelfClosing());
    }

    @Test
    public void valueOf_createsUnknownTagAndCachesUsingProvidedNamespace() {
        TagSet set = new TagSet();

        // Using htmlDefault should normalize tag names (e.g., lower-case)
        Tag t1 = set.valueOf("GIZMO", "urn:example", ParseSettings.htmlDefault);

        // The same tag should now be retrievable from the cache
        Tag t2 = set.get("GIZMO", "urn:example");
        assertSame(t1, t2);

        // Unknown tag should not be considered a known HTML tag
        assertFalse("Expected an unknown tag", t1.isKnownTag());

        // Normalized form is lower-case under htmlDefault
        assertEquals("gizmo", t1.normalName());
    }

    @Test
    public void get_returnsNullWhenTagNotPresent() {
        TagSet set = new TagSet();
        assertNull(set.get("doesNotExist", "urn:example"));
    }

    @Test
    public void onNewTag_isInvokedForNewTags_andCanModifyThem() {
        TagSet set = TagSet.initHtmlDefault();
        AtomicInteger calls = new AtomicInteger();

        set.onNewTag(tag -> {
            calls.incrementAndGet();
            if (!tag.isKnownTag()) {
                tag.set(Tag.SelfClose);
            }
        });

        // Unknown tag should be customized (self-closing) and counted
        Tag unknown = set.valueOf("brandnew", "urn:example");
        assertEquals(1, calls.get());
        assertTrue(unknown.isSelfClosing());

        // Known tag should trigger the callback, but remain not self-closing
        Tag known = set.valueOf("p", "http://www.w3.org/1999/xhtml", ParseSettings.htmlDefault);
        assertEquals(2, calls.get());
        assertTrue(known.isKnownTag());
        assertFalse(known.isSelfClosing());
    }

    @Test
    public void invalidArguments_throwHelpfulExceptions() {
        TagSet set = TagSet.Html();

        assertThrows(IllegalArgumentException.class, () -> set.valueOf("", "", ParseSettings.preserveCase));
        assertThrows(IllegalArgumentException.class, () -> set.get(null, null));
        assertThrows(IllegalArgumentException.class, () -> set.onNewTag(null));
        assertThrows(IllegalArgumentException.class, () -> set.valueOf(null, null));
        assertThrows(NullPointerException.class, () -> set.add(null));
    }

    @Test
    public void tagClass_knownTagLookups() {
        assertTrue(Tag.isKnownTag("u"));           // known HTML tag
        assertFalse(Tag.isKnownTag("not-a-tag"));  // clearly unknown
    }

    @Test
    public void tag_valueOf_withHtmlDefaultNormalizesCase() {
        Tag t = Tag.valueOf("U", ParseSettings.htmlDefault);
        assertTrue(t.isKnownTag());
        assertEquals("u", t.toString()); // string form reflects normalized name
    }

    @Test
    public void onNewTag_returnsSameInstanceToAllowChaining() {
        TagSet set = TagSet.Html();
        TagSet chained = set.onNewTag(tag -> {}).onNewTag(tag -> {});
        assertSame(set, chained);
    }
}