package adapters;

public interface PaymentGateway {
    boolean pay(double amount);
}
