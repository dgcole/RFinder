package rfinder.Tasks;

import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import rfinder.Hazeron.Resource;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DistributionCalculatorTask extends Task<XYChart.Series<Integer, Double>> {
    private final ArrayList<Resource> resources;

    public DistributionCalculatorTask(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    @Override
    protected XYChart.Series<Integer, Double> call() {
        ArrayList<AtomicInteger> occurrences = new ArrayList<>();
        for (int i = 0; i < 255; i++) occurrences.add(new AtomicInteger(0));
        int total = 0;
        for (Resource r : resources) {
            occurrences.get(r.getQ1() - 1).incrementAndGet();
            total++;
            if (r.getQ2() != 0) {
                occurrences.get(r.getQ2() - 1).incrementAndGet();
                total++;
            }

            if (r.getQ3() != 0) {
                occurrences.get(r.getQ3() - 1).incrementAndGet();
                total++;
            }
        }

        XYChart.Series<Integer, Double> series = new XYChart.Series<>();
        series.setName("Quality");
        for (int i = 0; i < 255; i++) {
            series.getData().add(new XYChart.Data<>(i + 1, ((double) occurrences.get(i).get() * 100.0) / total));
        }
        return series;
    }
}
