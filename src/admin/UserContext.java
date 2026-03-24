package admin;

public class UserContext {
    private final String userId;
    private final Role role;

    public UserContext(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public Role getRole() { return role; }
}
