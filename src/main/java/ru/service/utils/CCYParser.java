package ru.service.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashSet;

/**
 * Parser for currencies.xml file from resources folder.
 */
public class CCYParser extends Parser {

    /**
     * Store for currencies.
     */
    private final HashSet<String> ccyCodes = new HashSet<>();

    /**
     * Wrapper under super parse but with defined handler.
     *
     * @param fileName - file name. Original source: https://www.currency-iso.org/en/home/tables/table-a1.html
     * @return - unique currencies collection.
     */
    public HashSet<String> parse(String fileName) {
        super.parse(fileName, new CCYHandler());
        return ccyCodes;
    }

    /**
     * Handler to parse currencies.xml file.
     */
    private class CCYHandler extends DefaultHandler {

        boolean ccy = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equalsIgnoreCase("Ccy")) {
                ccy = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (ccy) {
                ccyCodes.add(new String(ch, start, length));
                ccy = false;
            }
        }
    }
}
