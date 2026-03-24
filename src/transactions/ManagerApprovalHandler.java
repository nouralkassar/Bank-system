package transactions;

public class ManagerApprovalHandler extends ApprovalHandler {
    @Override
public void approve(Transaction t) {
    t.setStage("MANAGER");
    t.setStatus("APPROVED");
    System.out.println("[MANAGER] Approved " + t.id);
}

}
