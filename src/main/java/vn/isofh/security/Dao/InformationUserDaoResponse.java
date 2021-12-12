package vn.isofh.security.Dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.isofh.jpa.entity.Card;
import vn.isofh.userdetailservice.model.Information;
import vn.isofh.userdetailservice.model.Account;
import vn.isofh.userdetailservice.model.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InformationUserDaoResponse {
    private Long id_account;
    private String username;
    private String password;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Information user_Information;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role_Information;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Card card_Information;

    public InformationUserDaoResponse() {
    }
    public InformationUserDaoResponse(Account account){
        this.id_account = account.getId();
        this.username = account.getUsername();
        this.setRole_Information(account.getRoleInformation());
        this.setUser_Information(account.getUserInformation());
        this.setCard_Information(account.getCardInformation());
    }

    public Information getUser_Information() {
        return user_Information;
    }

    public void setUser_Information(Information user_Information) {
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
