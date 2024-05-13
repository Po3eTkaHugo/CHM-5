import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

public class GUI extends JFrame {
    private static final int RENDER_QUALITY = 200;

    private static final JLabel labelA = new JLabel("Левый конец отрезка(a): ");
    private static final JTextField inputA = new JTextField("0.2", 0);
    private static final JLabel labelB = new JLabel("Правый конец отрезка(b): ");
    private static final JTextField inputB = new JTextField("1", 0);
    private static final JLabel labelN = new JLabel("Количество подотрезков разбиения(n): ");
    private static final JTextField inputN = new JTextField("10", 0);
    private static final JLabel labelY0 = new JLabel("Значение, определяющее начальное условие задачи(y0): ");
    private static final JTextField inputY0 = new JTextField("1.1", 0);
    private static final JLabel labelEps = new JLabel("Точность(eps): ");
    private static final JTextField inputEps = new JTextField("0.01", 0);
    private static final JLabel labelTask = new JLabel("Номер задачи(task): ");
    private static final JTextField inputTask = new JTextField("1", 0);

    private static final JLabel labelMethod = new JLabel("Метод решения ОДУ: ");
    private static final ButtonGroup buttonGroup1 = new ButtonGroup();
    private static final JRadioButton inputMethod1 = new JRadioButton("Эйлер");
    private static final JRadioButton inputMethod2 = new JRadioButton("Рунге-Кутт");

    private static final JButton button = new JButton("Построить");
    private static final JLabel filler1 = new JLabel();
    private static final JLabel filler2 = new JLabel();

    private String graphType;

    public GUI() {

        super("ODU Solving");
        this.setSize(750, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        buttonGroup1.add(inputMethod1);
        buttonGroup1.add(inputMethod2);

        inputMethod1.doClick();

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(9, 2, 2, 2));
        container.add(labelA);
        container.add(inputA);
        container.add(labelB);
        container.add(inputB);
        container.add(labelN);
        container.add(inputN);
        container.add(labelY0);
        container.add(inputY0);
        container.add(labelEps);
        container.add(inputEps);

        container.add(labelMethod);
        container.add(filler1);
        //container.add(filler2);
        container.add(inputMethod1);
        container.add(inputMethod2);
        container.add(labelTask);
        container.add(inputTask);

        container.add(button);
        button.addActionListener(e -> {

                this.graphType = inputMethod1.isSelected() ? inputMethod1.getText() : inputMethod2.getText();
                JFreeChart[] graphs = createGraphs(
                        Double.parseDouble(inputA.getText()),
                        Double.parseDouble(inputB.getText()),
                        Double.parseDouble(inputY0.getText()),
                        Integer.parseInt(inputN.getText()),
                        inputMethod1.isSelected() ? Method.Euler : Method.RungeKutt,
                        Double.parseDouble(inputEps.getText()),
                        Integer.parseInt(inputTask.getText()));
                displayGraphs(graphs);
            }
        );
    }

    private void displayGraphs(JFreeChart[] graphs) {
        JFrame graphFrame = new JFrame(graphType);
        graphFrame.setSize(1920, 1000);
        graphFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane();
        graphFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel graphPanel = new JPanel(new GridLayout(1, 3));
        scrollPane.setViewportView(graphPanel);

        for (JFreeChart graph : graphs) {
            ChartPanel chartPanel = new ChartPanel(graph, true);
            graphPanel.add(chartPanel);
        }
        graphFrame.setVisible(true);
    }

    private JFreeChart[] createGraphs(double a, double b, double y0, int n, Method method, double eps, int task) {
        XYSeries function = new XYSeries("f(x)");
        XYSeries counted = new XYSeries("c(x)");
        XYSeries u1First = new XYSeries("u1First(x)");
        XYSeries u2First = new XYSeries("u2First(x)");
        XYSeries u1 = new XYSeries("u1(x)");
        XYSeries u2 = new XYSeries("u2(x)");

        int nIter = n - 1;
        double delta = 1.0;
        switch (task){
            case 1: {
                do {
                    nIter++;
                    ODUSolving oduSolving = new ODUSolving(a, b, y0, nIter, method);
                    ODUSolving oduSolving2n = new ODUSolving(a, b, y0, 2 * nIter, method);
                    delta = Math.abs(oduSolving.y[oduSolving.n] - oduSolving2n.y[oduSolving2n.n]) / (Math.pow(2, 4) - 1);
                } while (delta > eps);

                ODUSolving oduSolving = new ODUSolving(a, b, y0, nIter, method);
                for (int i = 0; i <= oduSolving.n; i++) {
                    function.add(oduSolving.x[i], Function.funcExact(oduSolving.x[i]));
                    counted.add(oduSolving.x[i], oduSolving.y[i]);
                }
                break;
            }
            case 2: {
                ODUSolving oduSolvingFirst = new ODUSolving(a, b, y0, n, method);
                for (int i = 0; i <= oduSolvingFirst.n; i++) {
                    function.add(oduSolvingFirst.x[i], oduSolvingFirst.y[i]);
                }
                do {
                    nIter++;
                    ODUSolving oduSolving = new ODUSolving(a, b, y0, nIter, method);
                    ODUSolving oduSolving2n = new ODUSolving(a, b, y0, 2 * nIter, method);
                    delta = Math.abs(oduSolving.y[oduSolving.n] - oduSolving2n.y[oduSolving2n.n]) / (Math.pow(2, 4) - 1);
                } while (delta > eps);

                ODUSolving oduSolving = new ODUSolving(a, b, y0, nIter, method);
                for (int i = 0; i <= oduSolving.n; i++) {
                    counted.add(oduSolving.x[i], oduSolving.y[i]);
                }
                break;
            }
            case 3: {
                ODUSystemSolving oduSystemSolvingFirst = new ODUSystemSolving(0, 2, 119.0/900.0, 211.0/900.0, n, method);
                for (int i = 0; i <= oduSystemSolvingFirst.n; i++) {
                    u1First.add(oduSystemSolvingFirst.x[i], oduSystemSolvingFirst.u1[i]);
                    u2First.add(oduSystemSolvingFirst.x[i], oduSystemSolvingFirst.u2[i]);
                }

                do {
                    nIter++;
                    ODUSystemSolving oduSystemSolving = new ODUSystemSolving(0, 2, 119.0/900.0, 211.0/900.0, nIter, method);
                    ODUSystemSolving oduSystemSolving2n = new ODUSystemSolving(0, 2, 119.0/900.0, 211.0/900.0, 2 * nIter, method);
                    delta = Math.max(Math.abs(oduSystemSolving.u1[oduSystemSolving.n] - oduSystemSolving2n.u1[oduSystemSolving2n.n]) / (Math.pow(2, 3) - 1),
                            Math.abs(oduSystemSolving.u2[oduSystemSolving.n] - oduSystemSolving2n.u2[oduSystemSolving2n.n]) / (Math.pow(2, 3) - 1));
                } while (delta > eps);

                ODUSystemSolving oduSystemSolving = new ODUSystemSolving(0, 2, 119.0/900.0, 211.0/900.0, nIter, method);
                for (int i = 0; i <= oduSystemSolving.n; i++) {
                    u1.add(oduSystemSolving.x[i], oduSystemSolving.u1[i]);
                    u2.add(oduSystemSolving.x[i], oduSystemSolving.u2[i]);
                }
                break;
            }
        }

        NumberAxis xAxis = new NumberAxis("X");
        NumberAxis yAxis = new NumberAxis("Y");

        xAxis.setAutoRange(true);
        yAxis.setAutoRange(true);

        XYSeriesCollection bothUFirst = new XYSeriesCollection();
        bothUFirst.addSeries(u1First);
        bothUFirst.addSeries(u2First);

        XYSeriesCollection bothU = new XYSeriesCollection();
        bothU.addSeries(u1);
        bothU.addSeries(u2);

        XYSeriesCollection bothFunc = new XYSeriesCollection();
        bothFunc.addSeries(function);
        bothFunc.addSeries(counted);

        JFreeChart chartFunction = ChartFactory.createXYLineChart("f(x)", "X", "Y",
                task == 3 ? bothUFirst : new XYSeriesCollection(function), PlotOrientation.VERTICAL, true, true, false);

        JFreeChart chartCounted = ChartFactory.createXYLineChart("c(x)", "X", "Y",
                task == 3 ? bothU : bothFunc, PlotOrientation.VERTICAL, true, true, false);

        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        renderer1.setSeriesLinesVisible(0, true);
        renderer1.setSeriesShapesVisible(0, true);
        renderer1.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        renderer1.setSeriesPaint(0, Color.RED);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesLinesVisible(0, true);
        renderer2.setSeriesShapesVisible(0, true);
        renderer2.setSeriesShape(0, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        renderer2.setSeriesPaint(0, Color.GREEN);

        chartFunction.getXYPlot().setDomainAxis(xAxis);
        chartFunction.getXYPlot().setRangeAxis(yAxis);
        chartFunction.getXYPlot().setRenderer(renderer1);

        chartCounted.getXYPlot().setDomainAxis(xAxis);
        chartCounted.getXYPlot().setRangeAxis(yAxis);
        chartCounted.getXYPlot().setRenderer(renderer2);

        return new JFreeChart[]{
                chartFunction,
                chartCounted
        };
    }
}
