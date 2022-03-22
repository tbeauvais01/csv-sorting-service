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
        List<SortItem> sortedFields = new ArrayList<>();
        for(int i = 1 ; i<splitCsv.size(); i++) {
            sortedFields.add( new SortItem(i, splitCsv.get(i).get(csvFieldForSort)));
        }

        Collections.sort(sortedFields);

        //build result
        //add header
        StringBuilder sb = new StringBuilder();
        sb.append(addLineToResult(splitCsv, 0));
        for(SortItem item: sortedFields) {
            sb.append(addLineToResult(splitCsv, item.getIndex()));
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

class SortItem implements Comparable<SortItem> {

    private int index;
    private String sortField;

    public SortItem(int index, String sortField) {
        this.index = index;
        this.sortField = sortField;
    }

    @Override
    public int compareTo(SortItem o) {
        Comparator<String> reverse = Collections.reverseOrder();
        int retVal  = reverse.compare(this.sortField, o.getSortField());

        //keep original ordering in case of dupes on sort field
        retVal = retVal == 0? this.index - o.getIndex()  : retVal;
        return retVal;
    }

    public int getIndex() {
        return index;
    }

    public String getSortField() {
        return sortField;
    }
}
