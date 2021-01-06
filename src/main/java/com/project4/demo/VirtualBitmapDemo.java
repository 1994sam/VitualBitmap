package com.project4.demo;

import com.project4.helper.FileReader;
import com.project4.helper.ScatterPlotHelper;
import com.project4.virtualbitmap.VirtualBitMap;
import org.jfree.ui.RefineryUtilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible to run a demo on the Virtual Bitmap.
 */
public class VirtualBitmapDemo {
    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchAlgorithmException {
        Map<String, List<String>> allFlows = FileReader.getAllFlows();
        VirtualBitMap<String> map = new VirtualBitMap<>(500000, 500);
        for (Map.Entry<String, List<String>> entry : allFlows.entrySet()) {
            map.addFlow(entry.getKey(), entry.getValue());
        }
        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : allFlows.entrySet()) {
            int actualValue = entry.getValue().size();
            int estimate = map.getEstimate(entry.getKey(), entry.getValue());
            System.out.println("n = " + actualValue + " n^ = " + estimate);
            result.put(actualValue, estimate);
        }
        ScatterPlotHelper helper = new ScatterPlotHelper("Project 4", "Virtual Bitmap", (HashMap) result);
        helper.pack();
        RefineryUtilities.centerFrameOnScreen(helper);
        helper.setVisible(true);
    }
}
