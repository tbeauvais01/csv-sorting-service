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
        return splitLinesByComma(lines);
    }

    private List<List<String>> splitLinesByComma(List<String> lines) {
        List<List<String>> retVal = new ArrayList<>();
        int tokenStartIndex = 0;
        int maxSize = 0;
        int minSize = 0;
        for(String line: lines) {
            List<String> splitLine = new ArrayList<>();
            for(int i = 0; i < line.length(); i++) {
                char currentChar = line.charAt(i);

                if(currentChar == ',' || i == line.length() -1) {
                    //handle quoted commas
                    int doubleQuoteIndex = line.indexOf("\"", tokenStartIndex);
                    if( doubleQuoteIndex > 0 && doubleQuoteIndex < i && doubleQuoteIndex < line.length() -2) {
                        int nextDoubleQuoteIndex = line.indexOf('"', doubleQuoteIndex + 1);
                        int nextCommaAfterNextQuote = line.indexOf(",",nextDoubleQuoteIndex);
                        if (nextDoubleQuoteIndex > 0) {
                            boolean fullLine = nextDoubleQuoteIndex == line.length() - 1;
                            if (fullLine) {
                                splitLine.add(line.substring(tokenStartIndex).strip());
                            } else {
                                splitLine.add(line.substring(tokenStartIndex, nextCommaAfterNextQuote).strip());
                            }
                            tokenStartIndex = nextCommaAfterNextQuote < 0 ? nextDoubleQuoteIndex: nextCommaAfterNextQuote + 1;
                            i = tokenStartIndex;
                            continue;
                        } else {
                            throw new IllegalArgumentException("Input has mismatching quotes.");
                        }
                    }

                    //handle non quoted commas

                    boolean fullLine = i == line.length() -1;
                    boolean contiguousCommas = i > 0 && line.charAt(i -1) == ',' && currentChar == ',';
                    boolean leadingOrTrailingEmptyValue = i == 0 || fullLine && line.charAt(line.length() - 1) == ',';
                    if (contiguousCommas || leadingOrTrailingEmptyValue) {
                        splitLine.add("");
                        if(contiguousCommas && leadingOrTrailingEmptyValue) {
                            splitLine.add("");
                        }
                    } else if(fullLine) {
                        splitLine.add(line.substring(tokenStartIndex).strip());
                    } else {
                        splitLine.add(line.substring(tokenStartIndex, i).strip());
                    }
                    tokenStartIndex = i+1;
                }
            }
            tokenStartIndex = 0;
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
