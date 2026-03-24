package adapters;

public class LegacyPaymentAdapter implements PaymentGateway {
    private final LegacyPaymentAPI api;

    public LegacyPaymentAdapter(LegacyPaymentAPI api) {
        this.api = api;
    }

    @Override
    public boolean pay(double amount) {
        return api.executePayment(amount);
    }
}
