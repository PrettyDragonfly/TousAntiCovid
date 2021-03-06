package fr.camillebour.covidapp.models;

import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {

    public Notification() {
    }

    public Notification(String message, int t) {
        this.message = message;
        this.type = t;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "opened")
    private boolean opened;

    @Column(length = 250)
    private String message;

    private int type;
    
    public int getType() {
        return type;
    }
    
    public void setType(int t) {
        type = t;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
