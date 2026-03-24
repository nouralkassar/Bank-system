package transactions;

public class TellerApprovalHandler extends ApprovalHandler {
    private final double threshold;

    public TellerApprovalHandler(double threshold) {
        this.threshold = threshold;
    }

    @Override
public void approve(Transaction t) {
    if (t.amount <= 5000) {
        t.setStage("TELLER");
        t.setStatus("APPROVED");
        System.out.println("[TELLER] Approved " + t.id);

    } else if (next != null) {
        next.approve(t);
    }
}

}
