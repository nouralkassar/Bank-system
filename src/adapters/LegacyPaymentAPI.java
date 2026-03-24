package adapters;

public class LegacyPaymentAPI {
    public boolean executePayment(double amount) {
        System.out.println("Legacy API executing payment: " + amount);
        return true;
    }
}
