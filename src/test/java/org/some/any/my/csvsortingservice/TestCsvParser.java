package org.some.any.my.csvsortingservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
public class TestCsvParser {

    @Autowired
    CsvParser csvParser;

    @Test
    public void testParseCsv() {
        //test unix line feed LF
        List<List<String>> res = csvParser.parseCsv("\nheader1,header2\n" +
                "value1,value2\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("header2", res.get(0).get(1));
        Assertions.assertEquals("value1", res.get(1).get(0));
        Assertions.assertEquals("value2", res.get(1).get(1));

        //test windows carriage return linefeed CRLF
        List<List<String>> res2 = csvParser.parseCsv("\r\nheader1,header2\r\n" +
                "value1,value2");
        Assertions.assertEquals(res, res2);

        //test Mac carriage return CR
        List<List<String>> res3 = csvParser.parseCsv("\rheader1,header2\r" +
                "value1,value2\r");
        Assertions.assertEquals(res, res3);

        res = csvParser.parseCsv("\r");
        Assertions.assertTrue(res.isEmpty());

        res = csvParser.parseCsv("\n");
        Assertions.assertTrue(res.isEmpty());

        res = csvParser.parseCsv("\n\n");
        Assertions.assertTrue(res.isEmpty());

        res = csvParser.parseCsv("\r\n");
        Assertions.assertTrue(res.isEmpty());

        res = csvParser.parseCsv("\r\n\r\n");
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    public void testEmptyValuesInput() {
        List<List<String>> res = csvParser.parseCsv("\nheader1,header2,header3\n" +
                "value1,,value3\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("header2", res.get(0).get(1));
        Assertions.assertEquals("header3", res.get(0).get(2));
        Assertions.assertEquals("value1", res.get(1).get(0));
        Assertions.assertEquals("", res.get(1).get(1));
        Assertions.assertEquals("value3", res.get(1).get(2));

        res = csvParser.parseCsv("\nheader1,header2,header3\n" +
                "value1,,\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("header2", res.get(0).get(1));
        Assertions.assertEquals("header3", res.get(0).get(2));
        Assertions.assertEquals("value1", res.get(1).get(0));
        Assertions.assertEquals("", res.get(1).get(1));
        Assertions.assertEquals("", res.get(1).get(2));

        res = csvParser.parseCsv("\nheader1,header2,header3\n" +
                ",,\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("header2", res.get(0).get(1));
        Assertions.assertEquals("header3", res.get(0).get(2));
        Assertions.assertEquals("", res.get(1).get(0));
        Assertions.assertEquals("", res.get(1).get(1));
        Assertions.assertEquals("", res.get(1).get(2));

        res = csvParser.parseCsv("\n,,\n" +
                ",,\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("", res.get(0).get(0));
        Assertions.assertEquals("", res.get(0).get(1));
        Assertions.assertEquals("", res.get(0).get(2));
        Assertions.assertEquals("", res.get(1).get(0));
        Assertions.assertEquals("", res.get(1).get(1));
        Assertions.assertEquals("", res.get(1).get(2));
    }

    @Test
    public void testInputWithDoubleQuotedValues() {
        List<List<String>> res = csvParser.parseCsv("\nheader1,\"header \"\"2\"\"\",header3\n" +
                "value1,,value3\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("\"header \"\"2\"\"\"", res.get(0).get(1));
        Assertions.assertEquals("header3", res.get(0).get(2));
        Assertions.assertEquals("value1", res.get(1).get(0));
        Assertions.assertEquals("", res.get(1).get(1));
        Assertions.assertEquals("value3", res.get(1).get(2));
    }

    @Disabled
    @Test
    public void testInputWithDoubleQuotedValuesAndCommas() {
        List<List<String>> res = csvParser.parseCsv("\nheader1,\"header \"\",2\"\"\",header3\n" +
                "value1,,value3\n");
        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("\"header \"\"2\"\"\"", res.get(0).get(1));
        Assertions.assertEquals("header3", res.get(0).get(2));
        Assertions.assertEquals("value1", res.get(1).get(0));
        Assertions.assertEquals("", res.get(1).get(1));
        Assertions.assertEquals("value3", res.get(1).get(2));
    }


    @Test
    public void testParseMalformedQuotesException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->csvParser.parseCsv("\rheader1,header2\r" +
                "value1, \"value, 2\r"));
        Assertions.assertEquals("Input has mismatching quotes.",exception.getMessage());
    }

    @Test
    public void testParseCsvQuotedCommas() {
        List<List<String>> res = csvParser.parseCsv("\rheader1,header2,\"header3\"\r" +
                "value1, \"value, 2\", value3\r");

        Assertions.assertEquals(2, res.size());
        Assertions.assertEquals("header1", res.get(0).get(0));
        Assertions.assertEquals("header2", res.get(0).get(1));
        Assertions.assertEquals("\"header3\"", res.get(0).get(2));
        Assertions.assertEquals("value1", res.get(1).get(0));
        Assertions.assertEquals("\"value, 2\"", res.get(1).get(1));
        Assertions.assertEquals("value3", res.get(1).get(2));
    }

    @Test
    public void testParseCsvThrowsException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->csvParser.parseCsv("\rheader1,header2\r" +
                "value1\r"));
        Assertions.assertEquals("The header and rows have different number of elements.",exception.getMessage());
    }
}
