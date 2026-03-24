import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import transactions.*;

public class ApprovalChainTest {

    @Test
    void testAutoApprovalForSmallAmount() {
        AutoApprovalHandler autoHandler = new AutoApprovalHandler(1000);
        Transaction tx = new Transaction("acc1", "acc2", 500); // مبلغ صغير

        autoHandler.approve(tx);

        assertEquals("APPROVED", tx.getStatus());
        assertEquals("AUTO", tx.getStage());
    }

    @Test
    void testForwardToNextHandlerUsingMockito() {

        ApprovalHandler nextMock = mock(ApprovalHandler.class);

        AutoApprovalHandler autoHandler = new AutoApprovalHandler(1000);
        autoHandler.setNext(nextMock);

        Transaction tx = new Transaction("acc1", "acc2", 2000);
        autoHandler.approve(tx);

        verify(nextMock, times(1)).approve(tx);
        System.out.println("Mockito verified that the transaction was forwarded!");
    }
}