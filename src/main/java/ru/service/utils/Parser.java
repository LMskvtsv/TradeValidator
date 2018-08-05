package ru.service.utils;

import com.google.common.io.Resources;
import org.apache.log4j.Logger;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.util.HashSet;

/**
 * Parsers for xml files from resources folder.
 */
public class Parser {

    private final Logger logger = Logger.getLogger(Parser.class);

    public void parse(String fileName, DefaultHandler handler) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        javax.xml.parsers.SAXParser saxParser;
        try {
            saxParser = spf.newSAXParser();
            String s = Resources.getResource(fileName).getPath();
            saxParser.parse(s, handler);
        } catch (Exception e) {
            logger.error(String.format("Cannot parse %s", fileName));
            logger.error(e.getMessage(), e);
        }

    }
}
