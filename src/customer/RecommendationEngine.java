package customer;

import accounts.Account;
import accounts.AccountGroup;
import accounts.BaseAccount;

public class RecommendationEngine {

    public void generateRecommendation(Account acc) {
        if (acc instanceof BaseAccount) {
            // حساب فعلي
            printRecommendation((BaseAccount) acc);
        } else if (acc instanceof AccountGroup) {
            // حساب مركب
            AccountGroup group = (AccountGroup) acc;
            for (Account child : group.getChildren()) {
                if (child instanceof BaseAccount) {
                    printRecommendation((BaseAccount) child);
                }
            }
        }
    }

    private void printRecommendation(BaseAccount acc) {
        double balance = acc.getBalance();
        String owner = acc.getOwnerName();

        if (balance > 1000) {
            System.out.println("Recommendation for " + owner +
                    ": Consider a high-interest savings account.");
        } else if (balance < 100) {
            System.out.println("Recommendation for " + owner +
                    ": Consider depositing regularly to maintain minimum balance.");
        } else {
            System.out.println("Recommendation for " + owner +
                    ": Your balance is healthy. Keep tracking your expenses.");
        }
    }
}
