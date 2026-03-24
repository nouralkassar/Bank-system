package adapters;

public class StripeAdapter implements PaymentGateway {
    private final StripeAPI stripeApi;

    public StripeAdapter(StripeAPI stripeApi) {
        this.stripeApi = stripeApi;
    }

    @Override
    public boolean pay(double amount) {
        long cents = Math.round(amount * 100);
        return stripeApi.charge(cents);
    }
}
