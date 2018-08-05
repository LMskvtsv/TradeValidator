package ru.service.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;


/**
 * Parser for holidays.xml file from resources folder.
 */
public class HolidaysParser extends Parser {

    /**
     * Store for holidays.
     */
    private final HashSet<Calendar> holidays = new HashSet<>();

    /**
     * Wrapper under super parse but with defined handler.
     *
     * @param fileName - file name.
     * @return - unique currencies collection.
     */
    public HashSet<Calendar> parse(String fileName) {
        super.parse(fileName, new HolidayHandler());
        return holidays;
    }

    /**
     * Handler to parse holidays.xml file. All dates should be stored in Date tag.
     * Date should have the following format: "yyyy-MM-dd". For example - 2018-08-24.
     */
    private class HolidayHandler extends DefaultHandler {

        private boolean holiday = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equalsIgnoreCase("date")) {
                holiday = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (holiday) {
                Calendar cal = Calendar.getInstance();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    cal.setTime(format.parse(new String(ch, start, length)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holidays.add(cal);
                holiday = false;
            }
        }
    }
}
