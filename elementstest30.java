package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ElementsTestTest30 {

    @Test
    public void forms() {
        Document doc = Jsoup.parse("<form id=1><input name=q></form><div /><form id=2><input name=f></form>");
        Elements els = doc.select("form, div");
        assertEquals(3, els.size());
        List<FormElement> forms = els.forms();
        assertEquals(2, forms.size());
        assertNotNull(forms.get(0));
        assertNotNull(forms.get(1));
        assertEquals("1", forms.get(0).id());
        assertEquals("2", forms.get(1).id());
    }
}
