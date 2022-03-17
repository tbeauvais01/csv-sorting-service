package org.some.any.my.csvsortingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CsvSortingController {
    @Autowired
    private CsvParser csvParser;

    @Autowired
    private CsvSorter csvSorter;

    @RequestMapping(value = "/sortCsv", consumes = "text/csv", method = RequestMethod.POST)
    public @ResponseBody String sortCsv(@RequestBody String csv, @RequestParam String header) {
        if(header == null) {
            return csv;
        }

        if (csv == null || csv.isEmpty()) {
            return "";
        }

        try {
            List<List<String>> splitCsv = csvParser.parseCsv(csv);
            if (splitCsv.size() != 0 && splitCsv.get(0).size() != 0) {
                List<String> headers = splitCsv.get(0);
                if (!headers.contains(header)) {
                    return "Header parameter is not in the provided header line.";
                }
            }
            return csvSorter.sortCsv(splitCsv, header);
        } catch (Exception e) {
            return e.getMessage();
        }

    }

}


