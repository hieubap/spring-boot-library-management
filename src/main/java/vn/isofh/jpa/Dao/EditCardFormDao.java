package vn.isofh.jpa.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class EditCardFormDao {
    private List<Long> listId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    public EditCardFormDao() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EditCardFormDao(List<Long> listId){
        this.listId = listId;
    }

    public List<Long> getListId() {
        return listId;
    }

    public void setListId(List<Long> listId) {
        this.listId = listId;
    }
}
