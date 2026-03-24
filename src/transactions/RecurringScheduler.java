package transactions;

import java.util.ArrayList;
import java.util.List;

public class RecurringScheduler {

    private final List<RecurringTransaction> list = new ArrayList<>();

    public void add(RecurringTransaction rt) {
        list.add(rt);
    }

    public List<RecurringTransaction> due() {
        List<RecurringTransaction> result = new ArrayList<>();
        for (RecurringTransaction rt : list) {
            if (rt.shouldRun()) result.add(rt);
        }
        return result;
    }

    public void updateAfterRun() {
        for (RecurringTransaction rt : list) {
            if (rt.shouldRun()) rt.updateNextRun();
        }
    }
}
