package fr.school.smartenergy.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlUtils {

    private XmlUtils() {}

    /** Reads all first-level text elements into a Map<tagName, textContent>. */
    public static Map<String, String> readElements(InputStream is) throws Exception {
        Map<String, String> map = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element el) {
                map.put(el.getTagName(), el.getTextContent());
            }
        }
        return map;
    }

    /** Writes a Map of settings as XML elements under a root element named 'settings'. */
    public static void writeElements(Map<String, String> values, String rootTag, OutputStream os) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement(rootTag);
        doc.appendChild(root);
        for (Map.Entry<String, String> entry : values.entrySet()) {
            Element el = doc.createElement(entry.getKey());
            el.setTextContent(entry.getValue());
            root.appendChild(el);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(doc), new StreamResult(os));
    }
}
