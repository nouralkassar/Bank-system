package admin;

public class AccessControlService {
    public boolean canPerform(UserContext ctx, String action) {
        // simple RBAC rules example
        if (ctx.getRole() == Role.ADMIN) return true;
        if ("createAccount".equals(action) && ctx.getRole() == Role.MANAGER) return true;
        if ("approveTransaction".equals(action) && (ctx.getRole() == Role.MANAGER || ctx.getRole() == Role.TELLER)) return true;
        return false;
    }
}
