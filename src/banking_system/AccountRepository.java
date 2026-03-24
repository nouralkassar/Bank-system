package banking_system;

import accounts.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {

    private final Map<String, Account> accounts = new HashMap<>();

    public void add(Account acc) {
        accounts.put(acc.getId(), acc);
    }

    public Account get(String id) {
        Account acc = accounts.get(id);
        if (acc == null) {
            throw new RuntimeException("Account not found: " + id);
        }
        return acc;
    }

    public boolean exists(String id) {
        return accounts.containsKey(id);
    }
}
