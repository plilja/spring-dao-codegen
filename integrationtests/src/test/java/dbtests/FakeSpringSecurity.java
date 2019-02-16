package dbtests;

import dbtests.framework.CurrentUserProvider;

public class FakeSpringSecurity implements CurrentUserProvider {
    private String currentUser = "user";

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }
}
