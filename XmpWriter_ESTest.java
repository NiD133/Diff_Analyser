import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Understandable tests for the {@link XmpWriter} class.
 */
@DisplayName("XmpWriter Tests")
class XmpWriterTest {

    private ByteArrayOutputStream baos;
    private XmpWriter xmpWriter;

    @BeforeEach
    void setUp() throws IOException {
        baos = new ByteArrayOutputStream();
        xmpWriter = new XmpWriter(baos);
    }

    private String getXmpOutput() throws IOException {
        xmpWriter.close();
        return baos.toString(StandardCharsets.UTF_8.name());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor creates standard XMP structure")
        void defaultConstructor_createsStandardStructure() throws IOException {
            // When
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<pdf:Producer>"), "Should contain Producer");
            assertTrue(xmpOutput.contains("<dc:format>application/pdf</dc:format>"), "Should contain format");
        }

        @Test
        @DisplayName("Constructor with encoding sets it in the XML header")
        void constructor_withEncoding_setsInXmlHeader() throws IOException {
            // Given
            baos = new ByteArrayOutputStream();
            xmpWriter = new XmpWriter(baos, XmpWriter.UTF16BE, 0);

            // When
            xmpWriter.close();
            String xmpOutput = baos.toString(StandardCharsets.UTF_16BE.name());

            // Then
            assertTrue(xmpOutput.startsWith("<?xml version=\"1.0\" encoding=\"UTF-16BE\"?>"));
        }

        @Test
        @DisplayName("Constructor with PdfDictionary initializes properties")
        void constructor_withPdfDictionary_initializesProperties() throws IOException {
            // Given
            PdfDictionary info = new PdfDictionary();
            info.put(PdfName.TITLE, new PdfString("My Document Title"));
            info.put(PdfName.AUTHOR, new PdfString("John Doe"));

            // When
            xmpWriter = new XmpWriter(baos, info);
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<rdf:li xml:lang=\"x-default\">My Document Title</rdf:li>"));
            assertTrue(xmpOutput.contains("<rdf:li>John Doe</rdf:li>"));
        }

        @Test
        @DisplayName("Constructor with Map initializes properties")
        void constructor_withMap_initializesProperties() throws IOException {
            // Given
            Map<String, String> info = new HashMap<>();
            info.put("Title", "My Map Title");
            info.put("Author", "Jane Doe");
            info.put("Subject", null); // Null values should be ignored

            // When
            xmpWriter = new XmpWriter(baos, info);
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<rdf:li xml:lang=\"x-default\">My Map Title</rdf:li>"));
            assertTrue(xmpOutput.contains("<rdf:li>Jane Doe</rdf:li>"));
            assertFalse(xmpOutput.contains("Subject"));
        }

        @Test
        @DisplayName("Constructor with null info dictionary does not throw exception")
        void constructor_withNullInfoDictionary_doesNotThrow() {
            assertDoesNotThrow(() -> new XmpWriter(baos, (PdfDictionary) null));
        }

        @Test
        @DisplayName("Constructor with null info map does not throw exception")
        void constructor_withNullInfoMap_doesNotThrow() {
            assertDoesNotThrow(() -> new XmpWriter(baos, (Map<String, String>) null));
        }
    }

    @Nested
    @DisplayName("Property and Array Management")
    class PropertyManagementTests {

        @Test
        @DisplayName("setProperty adds a custom property")
        void setProperty_addsCustomProperty() throws XMPException, IOException {
            // When
            xmpWriter.setProperty(XMPConst.NS_XMP, "CustomProperty", "CustomValue");
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<xmp:CustomProperty>CustomValue</xmp:CustomProperty>"));
        }

        @Test
        @DisplayName("addDocInfoProperty correctly maps standard PDF info keys")
        void addDocInfoProperty_mapsStandardKeys() throws XMPException, IOException {
            // When
            xmpWriter.addDocInfoProperty(PdfName.KEYWORDS, "itext, xmp");
            xmpWriter.addDocInfoProperty(PdfName.CREATIONDATE, new PdfDate().getW3CDate());

            // Then
            String xmpOutput = getXmpOutput();
            assertTrue(xmpOutput.contains("<pdf:Keywords>itext, xmp</pdf:Keywords>"));
            assertTrue(xmpOutput.contains("<xmp:CreateDate>"));
        }

        @Test
        @DisplayName("appendArrayItem adds an item to an unordered array")
        void appendArrayItem_addsToUnorderedArray() throws XMPException, IOException {
            // When
            xmpWriter.appendArrayItem(XMPConst.NS_DC, "subject", "Subject A");
            xmpWriter.appendArrayItem(XMPConst.NS_DC, "subject", "Subject B");
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<dc:subject>"));
            assertTrue(xmpOutput.contains("<rdf:Bag>"));
            assertTrue(xmpOutput.contains("<rdf:li>Subject A</rdf:li>"));
            assertTrue(xmpOutput.contains("<rdf:li>Subject B</rdf:li>"));
        }

        @Test
        @DisplayName("appendOrderedArrayItem adds an item to an ordered array")
        void appendOrderedArrayItem_addsToOrderedArray() throws XMPException, IOException {
            // When
            xmpWriter.appendOrderedArrayItem(XMPConst.NS_DC, "creator", "Creator A");
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<dc:creator>"));
            assertTrue(xmpOutput.contains("<rdf:Seq>"));
            assertTrue(xmpOutput.contains("<rdf:li>Creator A</rdf:li>"));
        }

        @Test
        @DisplayName("appendAlternateArrayItem adds an item to an alternative array")
        void appendAlternateArrayItem_addsToAltArray() throws XMPException, IOException {
            // When
            xmpWriter.appendAlternateArrayItem(XMPConst.NS_DC, "title", "Title A");
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<dc:title>"));
            assertTrue(xmpOutput.contains("<rdf:Alt>"));
            assertTrue(xmpOutput.contains("<rdf:li xml:lang=\"x-default\">Title A</rdf:li>"));
        }
    }

    @Nested
    @DisplayName("Serialization and State")
    class SerializationAndStateTests {

        @Test
        @DisplayName("close serializes XMP data to the output stream")
        void close_serializesToStream() throws IOException {
            // When
            xmpWriter.close();

            // Then
            assertTrue(baos.size() > 0, "Output stream should not be empty");
            String xmpOutput = baos.toString(StandardCharsets.UTF_8.name());
            assertTrue(xmpOutput.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
            assertTrue(xmpOutput.endsWith("</rdf:RDF>\n<?xpacket end=\"w\"?>"));
        }
        
        @Test
        @DisplayName("serialize writes XMP data to a given external stream")
        void serialize_writesToExternalStream() throws XMPException {
            // Given
            ByteArrayOutputStream externalBaos = new ByteArrayOutputStream();

            // When
            xmpWriter.serialize(externalBaos);

            // Then
            assertTrue(externalBaos.size() > 0, "External stream should not be empty");
            String xmpOutput = externalBaos.toString();
            assertTrue(xmpOutput.contains("<rdf:RDF"));
        }

        @Test
        @DisplayName("close does nothing if output stream is null")
        void close_withNullStream_doesNothing() {
            assertDoesNotThrow(() -> {
                XmpWriter writerWithNullStream = new XmpWriter(null);
                writerWithNullStream.close();
            });
        }

        @Test
        @DisplayName("setReadOnly adds 'adobe:ns=\"adobe:ns/meta/\"' attribute")
        void setReadOnly_addsMetaAttribute() throws IOException {
            // When
            xmpWriter.setReadOnly();
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("xmlns:adobe=\"adobe:ns/meta/\""));
            assertTrue(xmpOutput.contains("adobe:mustUnderstand=\"1\""));
        }

        @Test
        @DisplayName("setAbout sets the 'rdf:about' attribute")
        void setAbout_setsRdfAboutAttribute() throws IOException {
            // When
            xmpWriter.setAbout("uuid:12345");
            String xmpOutput = getXmpOutput();

            // Then
            assertTrue(xmpOutput.contains("<rdf:Description rdf:about=\"uuid:12345\""));
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("setProperty with null schema URI throws XMPException")
        void setProperty_withNullSchema_throwsException() {
            // When & Then
            XMPException exception = assertThrows(XMPException.class, () -> {
                xmpWriter.setProperty(null, "prop", "value");
            });
            assertEquals("Empty schema namespace URI", exception.getMessage());
        }

        @Test
        @DisplayName("appendArrayItem with unregistered schema throws XMPException")
        void appendArrayItem_withUnregisteredSchema_throwsException() {
            // When & Then
            XMPException exception = assertThrows(XMPException.class, () -> {
                xmpWriter.appendArrayItem("unregistered:ns", "array", "value");
            });
            assertEquals("Unregistered schema namespace URI", exception.getMessage());
        }

        @Test
        @DisplayName("serialize to a null stream throws NullPointerException")
        void serialize_toNullStream_throwsException() {
            // The underlying Adobe XMP Core library throws an NPE.
            assertThrows(NullPointerException.class, () -> xmpWriter.serialize(null));
        }

        @Test
        @DisplayName("close throws IOException when underlying stream fails")
        void close_whenStreamFails_throwsIOException() throws IOException {
            // Given
            OutputStream failingStream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    throw new IOException("Stream is broken");
                }
            };
            XmpWriter writerWithFailingStream = new XmpWriter(failingStream);

            // When & Then
            IOException exception = assertThrows(IOException.class, writerWithFailingStream::close);
            assertEquals("Error writing to the OutputStream", exception.getMessage());
        }
    }
}