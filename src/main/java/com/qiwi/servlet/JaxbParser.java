package com.qiwi.servlet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by etrofimov on 13.07.17.
 */
public class JaxbParser {

    public static Object getObject(InputStream in, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(in);

        return object;
    }
}
