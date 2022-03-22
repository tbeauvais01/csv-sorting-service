package org.some.any.my.csvsortingservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
class TestCsvSorter {

    @Autowired
    CsvSorter csvSorter;

    @Test
    void sortCsv() {
        List<String> headers = Stream.of("Header1", "Header2").collect(Collectors.toList());
        List<String> line1 = Stream.of("z", "a").collect(Collectors.toList());
        List<String> line2 = Stream.of("b", "x").collect(Collectors.toList());
        List<List<String>> splitCsv = Stream.of(headers, line1, line2).collect(Collectors.toList());
        String res = csvSorter.sortCsv(splitCsv, "Header1");
        Assertions.assertEquals("Header1, Header2\n" +
                "z, a\n" +
                "b, x\n",res );

        res = csvSorter.sortCsv(splitCsv, "Header2");
        Assertions.assertEquals("Header1, Header2\n" +
                "b, x\n" +
                "z, a\n", res );


        //Test sorting with dupes
        List<String> line3 = Stream.of("b", "x").collect(Collectors.toList());
        splitCsv.add(line3);

        res = csvSorter.sortCsv(splitCsv, "Header2");
        Assertions.assertEquals("Header1, Header2\n" +
                "b, x\n" +
                "b, x\n" +
                "z, a\n", res );
    }
}