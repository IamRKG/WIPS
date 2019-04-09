package com.ford.purchasing.wips.common.layer.util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public final class StringUtil {

    private static StringBuilder output;

    private StringUtil() {

    }

    public static String createBlankSpaces(final int numbersSpace) {
        final String blankSpace = " ";
        final StringBuilder blankSpaces = new StringBuilder();
        for (int i = 1; i <= numbersSpace; i++) {
            blankSpaces.append(blankSpace);
        }
        return blankSpaces.toString();
    }

    public static List<String> splitString(final String remarks) {
        final List<String> remarksList = new ArrayList<String>();
        final String[] tempnewli = remarks.split("\\r?\\n");
        for (final String tempnewstring : tempnewli) {
            remarksList.addAll(splitStringAndBuild(tempnewstring, 72));
        }
        return remarksList;
    }

    public static List<String> splitStringPcsRemarks(final String remarks) {
        final List<String> remarksList = new ArrayList<String>();
        final String[] tempnewli = remarks.split("\\r?\\n");
        for (final String tempnewstring : tempnewli) {
            remarksList.addAll(splitStringAndBuild(tempnewstring, 69));
        }
        return remarksList;
    }

    private static void splitStringWithoutSpaces(String remarks, final List<String> remarksList,
            final int wordLengthCount) {
        while (remarks.length() > wordLengthCount) {
            remarksList.add(remarks.substring(0, wordLengthCount));
            remarks = remarks.substring(wordLengthCount, remarks.length());
        }
        output.append(remarks);
    }

    private static List<String> splitStringAndBuild(final String remarks, final int wordLengthCount) {

        final String[] temp = remarks.split("(?<=\\s)");
        output = new StringBuilder(remarks.length());
        final List<String> remarksList = new ArrayList<String>();
        for (String word : temp) {
            if (word.length() > wordLengthCount) {
                word = output.toString() + word;
                output = new StringBuilder();
                splitStringWithoutSpaces(word, remarksList, wordLengthCount);
            } else {
                if ((output.length() + word.length()) > wordLengthCount) {
                    remarksList.add(output.toString());
                    output = new StringBuilder();
                }
                output.append(word);
            }
        }
        if (output.toString().trim().length() > 0) {
            remarksList.add(output.toString());
        }
        return remarksList;
    }

    public static String buildUserRemarks(final String[] userEnteredRemark) {
        final StringBuilder userEnteredRemarks = new StringBuilder();
        for (final String remarks : userEnteredRemark) {
            userEnteredRemarks.append(remarks + "\r\n");
        }
        return userEnteredRemarks.toString();
    }

    public static String retrieveCost(final String cost) {
        return (lastChar(cost) == '+' || lastChar(cost) == '-') ? cost.substring(0, cost.length() - 1) : cost;
    }

    private static char lastChar(final String cost) {
        return cost.charAt(cost.length() - 1);
    }

    public static String retriveCostSign(final String cost) {
        return (lastChar(cost) == '-') ? "-" : "+";
    }
}
