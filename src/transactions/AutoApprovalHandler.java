package transactions;

public class AutoApprovalHandler extends ApprovalHandler {
    private final double threshold;

    public AutoApprovalHandler(double threshold) {
        this.threshold = threshold;
    }

    @Override
public void approve(Transaction t) {
    if (t.amount <= 1000) {
        t.setStage("AUTO");
        t.setStatus("APPROVED");
        System.out.println("[AUTO] Approved " + t.id);

    } else if (next != null) {
        next.approve(t);
    }
    else {
            System.out.println("No handler available for tx " + t.id);
        }
}

}
