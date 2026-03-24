import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import adapters.*;

public class AdapterPatternTest {

    @Test
    void testLegacyPaymentAdapter() {
        LegacyPaymentAPI legacyApi = new LegacyPaymentAPI();
        PaymentGateway adapter = new LegacyPaymentAdapter(legacyApi);

        assertTrue(adapter.pay(100.0), "Legacy Adapter should return true");
    }

    @Test
    void testStripeAdapterConversion() {

        StripeAPI stripeMock = mock(StripeAPI.class);
        when(stripeMock.charge(anyDouble())).thenReturn(true);

        StripeAdapter adapter = new StripeAdapter(stripeMock);

        adapter.pay(10.50);

        verify(stripeMock).charge(1050.0);
    }
}