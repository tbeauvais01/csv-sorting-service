package org.some.any.my.csvsortingservice;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CsvSorter {

    public String sortCsv(List<List<String>> splitCsv, String sortCriteria ) {
        //headers are the first list
        List<String> headers = splitCsv.get(0);
        Map<String, Integer> headerIndexes = new HashMap<>();
        int idx = 0;
        for(String header: headers) {
            headerIndexes.put(header, idx++);
        }
        Integer csvFieldForSort = headerIndexes.get(sortCriteria);

        //create map to sort fields by index
        Map<String, Integer> sortedFieldsMap = new TreeMap<>(Collections.reverseOrder());
        for(int i = 1 ; i<splitCsv.size(); i++) {
            sortedFieldsMap.put(splitCsv.get(i).get(csvFieldForSort), i);
        }

        Collection<Integer> sortedCsvLineOrder = sortedFieldsMap.values();

        //build result
        //add header
        StringBuilder sb = new StringBuilder();
        sb.append(addLineToResult(splitCsv, 0));
        for(Integer integer: sortedCsvLineOrder) {
            sb.append(addLineToResult(splitCsv, integer));
        }

        return sb.toString();
    }

    public String addLineToResult(List<List<String>> splitCsv, int idx) {
        StringBuilder sb = new StringBuilder();
        List<String> line = splitCsv.get(idx);
        int i = 0;
        for(String element : line) {
            //only append new line on last element
            if(i == line.size() -1) {
                sb.append(element).append("\n");
            } else {
                sb.append(element).append(", ");
            }
            i++;
        }
        return sb.toString();
    }
}
