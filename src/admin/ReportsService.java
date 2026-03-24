package admin;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ReportsService {
    public Map<String, Object> generateAccountsSummary(List<String> accountIds) {
        Map<String,Object> report = new HashMap<>();
        report.put("count", accountIds.size());
        report.put("generatedAt", System.currentTimeMillis());
        // add more details in real implementation
        return report;
    }
}
