package library.security.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import library.jpa.entity.Card;
import library.jpa.entity.User;
import library.userdetailservice.model.Account;
import library.userdetailservice.model.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InformationUserDao {
    private Long id_account;
    private String username;
    private String password;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user_Information;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role_Information;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Card card_Information;

    public InformationUserDao() {
    }
    public InformationUserDao(Account account){
        this.id_account = account.getId();
        this.username = account.getUsername();
        this.setRole_Information(account.getRoleInformation());
        this.setUser_Information(account.getUserInformation());
        this.setCard_Information(account.getCardInformation());
    }

    public User getUser_Information() {
        return user_Information;
    }

    public void setUser_Information(User user_Information) {
        this.user_Information = user_Information;
    }

    public Role getRole_Information() {
        return role_Information;
    }

    public void setRole_Information(Role role_Information) {
        this.role_Information = role_Information;
    }

    public Card getCard_Information() {
        return card_Information;
    }

    public void setCard_Information(Card card_Information) {
        this.card_Information = card_Information;
    }

    public Long getId_account() {
        return id_account;
    }

    public void setId_account(Long id_account) {
        this.id_account = id_account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
