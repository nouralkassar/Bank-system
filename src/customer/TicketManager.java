package customer;

import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    private final List<SupportTicket> tickets = new ArrayList<>();
public List<SupportTicket> getTickets() {
    return tickets;
}
    public void createTicket(String customerName, String message) {
        SupportTicket ticket = new SupportTicket(customerName, message);
        tickets.add(ticket);
        System.out.println("Created: " + ticket);
    }

    public void closeTicket(int id) {
        tickets.stream().filter(t -> t.getId() == id).forEach(t -> t.setStatus("Closed"));
    }

    public void listTickets() {
        tickets.forEach(System.out::println);
    }
}
