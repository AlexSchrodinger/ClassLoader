package ru.bernarsoft;


import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;

public class Main {

    public static void main(String[] args) throws Exception {



        JarClassLoader jarClassLoader = new JarClassLoader();
        Class animalClass = jarClassLoader.loadClassFromURL("Animal", "https://github.com/AlexSchrodinger/Animal/blob/master/Animal.jar?raw=true");
        Object o = animalClass.newInstance();

        SerializationXML serializationXML = new SerializationXML(o);
        Document document = serializationXML.getSerializedDoc();
        printXML(document);


    }

    private static void printXML(Document document) throws TransformerException {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(document), new StreamResult(out));
        System.out.println(out.toString());
    }

}
