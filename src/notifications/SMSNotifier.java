package notifications;

public class SMSNotifier implements NotificationObserver {
    private final String phone;

    public SMSNotifier(String phone) {
        this.phone = phone;
    }

    @Override
    public void update(String message) {
        System.out.println("SMS to " + phone + ": " + message);
        // real implementation would call SMS gateway
    }
}
