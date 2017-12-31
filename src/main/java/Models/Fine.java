package Models;

public class Fine {
    private String userId, fine;

    public Fine(String userId, String fine) {
        this.userId = userId;
        this.fine = fine;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }
}
