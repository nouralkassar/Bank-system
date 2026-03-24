package banking_system;
import gui.MainFrame;
public class Main {
    public static void main(String[] args) {
         BankFacade facade = BankFacade.getInstance();
         facade.loadAccountsFromTxt();
         utils.AuditLog.loadFromFile();
         new MainFrame();
         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            facade.saveAccountsToTxt();
        }));
    }
}
