package wenjalan.questlogapp;
// Represents an instance of the app
// Owns User

public class QuestLog {
// Fields //
    private User user;

// Constructors //
    public QuestLog(String username) {
        this.user = new User(username);
        start();
    }

// Initialization //
    public void init() {
        // TODO: initialization
    }

// Methods //
    public void start() {

    }

}
