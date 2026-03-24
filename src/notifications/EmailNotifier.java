package notifications;

public class EmailNotifier implements NotificationObserver {
    private final String email;

    public EmailNotifier(String email) {
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println("EMAIL to " + email + ": " + message);
        // real implementation would call SMTP / service
    }
}
