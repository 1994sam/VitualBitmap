package com.project4.helper;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Responsible to create scatter plot of the results.
 */
public class ScatterPlotHelper extends ApplicationFrame {
    public ScatterPlotHelper(String applicationTitle, String chartTitle, HashMap result) {
        super(applicationTitle);
        JPanel jpanel = createDemoPanel(chartTitle, result);
        jpanel.setPreferredSize(new Dimension(500, 450));
        setContentPane(jpanel);
    }

    public JPanel createDemoPanel(String chartTitle, Map result) {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(chartTitle,"Actual Value","Estimated Value",
                createDataset(chartTitle, result),
                PlotOrientation.VERTICAL, true, true, false);
        Shape diamond = ShapeUtilities.createDiamond(3);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShape(1, diamond);
        return new ChartPanel(jfreechart);
    }

    private XYDataset createDataset(String chartTitle, Map result) {
        final XYSeries series = new XYSeries(chartTitle);
        Iterator it = result.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            series.add((Integer) pair.getKey(), (Integer) pair.getValue());
            it.remove();
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }
}