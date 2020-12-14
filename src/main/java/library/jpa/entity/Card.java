package library.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import library.userdetailservice.model.Account;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp expiration_date;
    private String status;
    private Long id_account;

    @OneToOne(mappedBy = "cardInformation") // DDL error
    private Account account;

    @OneToMany(mappedBy = "card")
    private Collection<Session> list_Sessions;

    public void set(Card cardLibrary) {
        if (cardLibrary.getStatus() != null) {
            this.setStatus(cardLibrary.getStatus());
        }
        if (cardLibrary.getExpiration_date() != null) {
            this.setExpiration_date(cardLibrary.getExpiration_date());
        }
    }

    public Card() {
    }

    public Long getId_account() {
        return id_account;
    }

    public void setId_account(Long id_account) {
        this.id_account = id_account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Timestamp expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonBackReference
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @JsonManagedReference
    public Collection<Session> getList_Sessions() {
        return list_Sessions;
    }

    public void setList_Sessions(Collection<Session> list_Sessions) {
        this.list_Sessions = list_Sessions;
    }
}
