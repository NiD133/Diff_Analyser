package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import org.junit.Assert;
import org.junit.Test;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TextRenderInfoTestTest1 {

    public static final int FIRST_PAGE = 1;

    public static final int FIRST_ELEMENT_INDEX = 0;

    private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);
        document.open();
        for (String string : text) {
            document.add(new Paragraph(string));
            document.newPage();
        }
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }

    private static class TextPositionRenderListener implements RenderListener {

        List<LineSegment> lineSegments = new ArrayList<LineSegment>();

        public List<LineSegment> getLineSegments() {
            return lineSegments;
        }

        public void renderText(TextRenderInfo renderInfo) {
            lineSegments.add(renderInfo.getBaseline());
        }

        public void beginTextBlock() {
        }

        public void endTextBlock() {
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }
    }

    private static class CharacterPositionRenderListener implements TextExtractionStrategy {

        public void beginTextBlock() {
        }

        public void renderText(TextRenderInfo renderInfo) {
            List<TextRenderInfo> subs = renderInfo.getCharacterRenderInfos();
            TextRenderInfo previousCharInfo = subs.get(0);
            for (int i = 1; i < subs.size(); i++) {
                TextRenderInfo charInfo = subs.get(i);
                Vector previousEndPoint = previousCharInfo.getBaseline().getEndPoint();
                Vector currentStartPoint = charInfo.getBaseline().getStartPoint();
                assertVectorsEqual(charInfo.getText(), previousEndPoint, currentStartPoint);
                previousCharInfo = charInfo;
            }
        }

        private void assertVectorsEqual(String message, Vector v1, Vector v2) {
            Assert.assertEquals(message, v1.get(0), v2.get(0), 1 / 72f);
            Assert.assertEquals(message, v1.get(1), v2.get(1), 1 / 72f);
        }

        public void endTextBlock() {
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }

        public String getResultantText() {
            return null;
        }
    }

    @Test
    public void testCharacterRenderInfos() throws Exception {
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), "ABCD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));
        PdfReader r = new PdfReader(bytes);
        PdfReaderContentParser parser = new PdfReaderContentParser(r);
        parser.processContent(FIRST_PAGE, new CharacterPositionRenderListener());
    }
}