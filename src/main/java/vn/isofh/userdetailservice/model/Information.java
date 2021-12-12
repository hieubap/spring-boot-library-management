package vn.isofh.userdetailservice.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import vn.isofh.security.Dao.FormEditUser;

import javax.persistence.*;

@Entity
@Table(name = "information")
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String phone;
    private Boolean gender;

    @OneToOne(mappedBy = "userInformation")
    private Account account;

    public Information(){}

    public Information(FormEditUser formEdit){
        setInformation(formEdit);
    }

    public void setInformation(FormEditUser dao) {
        this.name = dao.getName();
        this.age = dao.getAge();
        this.phone = dao.getPhone();

        if (dao.getGender()!= null)
        this.gender = dao.getGender().equals("MAN")?true:false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @JsonBackReference
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
