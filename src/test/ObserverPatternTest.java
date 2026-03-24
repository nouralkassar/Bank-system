

import static org.junit.jupiter.api.Assertions.*;

import notifications.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class ObserverPatternTest {

    @Test
    void testEmailAndSMSNotifications() {
        NotificationSubject subject = new NotificationSubject();

        EmailNotifier emailUser = new EmailNotifier("noor@example.com");
        SMSNotifier smsUser = new SMSNotifier("0933123456");

        subject.addObserver(emailUser);
        subject.addObserver(smsUser);


        assertDoesNotThrow(() -> subject.notifyObservers("برجاء العلم أنه تم إيداع مبلغ في حسابك"));
    }

    @Test
    void testRemoveObserver() {
        NotificationSubject subject = new NotificationSubject();
        EmailNotifier emailUser = new EmailNotifier("test@test.com");

        subject.addObserver(emailUser);
        subject.removeObserver(emailUser);

        assertDoesNotThrow(() -> subject.notifyObservers("رسالة بعد الحذف"));
    }

    @Test
    void testObserverErrorHandling() {
        NotificationSubject subject = new NotificationSubject();

        subject.addObserver(msg -> {
            throw new RuntimeException("Simulated Error");
        });

        assertDoesNotThrow(() -> subject.notifyObservers("اختبار معالجة الأخطاء"));
    }
}