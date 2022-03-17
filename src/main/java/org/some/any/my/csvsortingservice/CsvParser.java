package org.some.any.my.csvsortingservice;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CsvParser {
    public  List<List<String>> parseCsv(String csv) {

        //split by line
        List<String> lines = splitByLine(csv);

        //split lines by comma
        List<List<String>> retVal = splitLinesByComma(lines);

        return retVal;
    }

    private List<List<String>> splitLinesByComma(List<String> lines) {
        List<List<String>> retVal = new ArrayList<>();
        int previousComma = 0;
        int maxSize = 0;
        int minSize = 0;
        for(String line: lines) {
            List<String> splitLine = new ArrayList<>();
            for(int i = 0; i < line.length(); i++) {
                char currentChar = line.charAt(i);

                if(currentChar == ',' || i == line.length() -1) {
                    //handle quoted commas
                    int doubleQuoteIndex = line.indexOf("\"", previousComma);
                    if( doubleQuoteIndex > 0 && doubleQuoteIndex < i && doubleQuoteIndex < line.length() -2) {
                        int nextDoubleQuoteIndex = line.indexOf('"', doubleQuoteIndex + 1);
                        int nextCommaAfterNextQuote = line.indexOf(",",nextDoubleQuoteIndex);
                        if (nextDoubleQuoteIndex > 0) {
                            boolean fullLine = nextDoubleQuoteIndex == line.length() - 1;
                            if (fullLine) {
                                splitLine.add(line.substring(previousComma).strip());
                            } else {
                                splitLine.add(line.substring(previousComma, nextCommaAfterNextQuote).strip());
                            }
                            previousComma = nextCommaAfterNextQuote < 0 ? nextDoubleQuoteIndex: nextCommaAfterNextQuote + 1;
                            i = previousComma;
                            continue;
                        } else {
                            throw new IllegalArgumentException("Input has mismatching quotes.");
                        }
                    }

                    //handle non quoted commas

                    boolean fullLine = i == line.length() -1;
                    if(fullLine) {
                        splitLine.add(line.substring(previousComma).strip());
                    } else {
                        splitLine.add(line.substring(previousComma, i).strip());
                    }
                    previousComma = i+1;
                }
            }
            previousComma = 0;
            maxSize = Math.max(splitLine.size(), maxSize);
            minSize = minSize == 0? splitLine.size(): Math.min(splitLine.size(), minSize);
            retVal.add(splitLine);
        }
        //ensure header and rows have the same number of elements
        if (minSize != maxSize) {
            throw new IllegalArgumentException("The header and rows have different number of elements.");
        }
        return retVal;
    }

    private List<String> splitByLine(String csv) {
        char lineFeed = '\n';
        char carriageReturn = '\r';
        List<String> lines = new ArrayList<>();
        int previousNewline = 0;
        for(int i = 0; i < csv.length(); i++) {
            char current = csv.charAt(i);
            if(current == lineFeed || current == carriageReturn || i == csv.length() -1) {
                //handle carriage return line feed
                if(current == carriageReturn && i < csv.length()-1 && csv.charAt(i+1) == lineFeed) {
                    if(i > previousNewline) {
                        String line = csv.substring(previousNewline, i).replace(carriageReturn,' ').strip();
                        //don't add empty lines
                        if(line.length() > 0) {
                            lines.add(line);
                        }
                    }
                    i++;
                    previousNewline = i+1;
                    continue;
                }

                if(i == csv.length() -1) {
                    if (i > previousNewline) {
                        String line = csv.substring(previousNewline).replace(carriageReturn, ' ').strip();
                        //don't add empty lines
                        if (line.length() > 0) {
                            lines.add(line);
                        }
                    }
                } else {
                    if(i > previousNewline) {
                        String line = csv.substring(previousNewline, i).strip();
                        if (line.length() > 0) {
                            lines.add(line);
                        }
                    }
                }
                previousNewline = i + 1;
            }
        }
        return lines;
    }
}
