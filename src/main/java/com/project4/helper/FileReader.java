package com.project4.helper;

import com.project4.flows.Flow;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helps to read the file required to run the demo.
 */
public class FileReader {

    public static Map<String, List<String>> getAllFlows() throws IOException, URISyntaxException {
        return getAllFlows("project4input.txt");
    }

    /**
     * Reads file and returns all the Flow details in it.
     *
     * @param fileName the name of the file which is to be read.
     * @return the details of all the flows.
     * @throws IOException        if unable to read the file.
     * @throws URISyntaxException if unable to find the file.
     */
    public static Map<String, List<String>> getAllFlows(final String fileName) throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(FileReader.class.getClassLoader()
                .getResource(fileName)).toURI());
        List<Flow> flows;
        Stream<String> lines = Files.lines(path);
        flows = lines.map(line -> line.split("\\s+")).filter(values -> values.length == 2)
                .map(values -> new Flow(values[0], Integer.parseInt(values[1]))).collect(Collectors.toList());
        Map<String, List<String>> flowMap = new TreeMap<>();
        for (Flow flow : flows) {
            flowMap.put(flow.getID(), getElements(flow.getNumberOfPackets()));
        }
        return flowMap;
    }

    /**
     * Generates random elements for the flow.
     *
     * @param number the number of elements to be generated.
     * @return the list of elements.
     */
    public static List<String> getElements(int number) {
        Random r = new Random();
        r.setSeed(26800000);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(String.valueOf(Math.abs(r.nextInt(72850000))));
        }
        return list;
    }

}
