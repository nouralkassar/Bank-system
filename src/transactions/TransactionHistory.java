// package transactions;

// import java.util.ArrayList;
// import java.util.List;

// public class TransactionHistory {
//     private final List<Transaction> list = new ArrayList<>();

//     public void add(Transaction tx) {
//         list.add(tx);
//     }

//     public List<Transaction> all() {
//         return new ArrayList<>(list);
//     }
// }
package transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {

    private final List<Transaction> history = new ArrayList<>();

    public void add(Transaction t) {
        history.add(t);
    }

    public List<Transaction> getAll() {
        return history;
    }

    public Transaction findById(String id) {
        for (Transaction t : history) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    // ----------------------------
    // New Functions for UI Support
    // ----------------------------
    public void updateStatus(String id, String status) {
        Transaction t = findById(id);
        if (t != null) t.setStatus(status);
    }

    public void updateStage(String id, String stage) {
        Transaction t = findById(id);
        if (t != null) t.setStage(stage);
    }
}
