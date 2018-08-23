package messageService;

public class Answer {
    private String userName;
    Integer messageNumber;

    public Answer(String userName, Integer messageNumber) {
        this.userName = userName;
        this.messageNumber = messageNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(Integer messageNumber) {
        this.messageNumber = messageNumber;
    }
}
