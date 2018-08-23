package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "messages_new")
public class MessageDataSet implements Serializable { // Serializable Important to Hibernate!
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", updatable = false)
    private Date date;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "userId", updatable = false)
    private String userId;

    @Column(name = "chatId", updatable = false)
    private String chatId;

    @Column(name = "userName", updatable = false)
    private String userName;

    @Column(name = "isCurse", updatable = false)
    private Boolean isCurse;

    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public MessageDataSet() {
    }

    public MessageDataSet(Date date, String text, String userId, String chatId, String userName, Boolean hasCurse) {
        this.date = date;
        this.text = text;
        this.userId = userId;
        this.chatId = chatId;
        this.userName = userName;
        this.isCurse = hasCurse;
    }

    @SuppressWarnings("UnusedDeclaration")

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getCurse() {
        return isCurse;
    }

    public void setCurse(Boolean curse) {
        isCurse = curse;
    }
}