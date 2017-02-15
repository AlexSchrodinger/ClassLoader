package ru.bernarsoft;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SerializationXML {

    private Object object;

    public SerializationXML(Object object) {
        this.object = object;
    }

    public Document getSerializedDoc() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();


        Element element = doc.createElement(object.getClass().getName());
        doc.appendChild(element);

        Element fieldsObject = doc.createElement("Fields");
        element.appendChild(fieldsObject);

        for (Field field : object.getClass().getDeclaredFields()) {

            Element itemElement = doc.createElement(field.getName());
            fieldsObject.appendChild(itemElement);

            field.setAccessible(true);

            itemElement.setAttribute("type", field.getType().getName());
            itemElement.insertBefore(doc.createTextNode(String.valueOf(field.get(object))), itemElement.getLastChild());

        }

        Element methodsObject = doc.createElement("Methods");
        element.appendChild(methodsObject);


        return doc;
    }
}
