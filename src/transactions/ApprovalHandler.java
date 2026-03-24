package transactions;

public abstract class ApprovalHandler {
    protected ApprovalHandler next;

    public void setNext(ApprovalHandler next) {
        this.next = next;
    }

    public abstract void approve(Transaction tx);

}