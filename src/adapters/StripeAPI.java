package adapters;

public class StripeAPI {
    public boolean charge(double amountInCents) {
        System.out.println("Stripe charging cents: " + (long)(amountInCents));
        return true;
    }
}
