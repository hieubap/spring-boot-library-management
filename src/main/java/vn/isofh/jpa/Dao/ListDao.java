package vn.isofh.jpa.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ListDao {
    private List<Long> listId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean status;

    public ListDao() {
    }

    public Boolean getStatus() {
        return status == null;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ListDao(List<Long> listId){
        this.listId = listId;
    }

    public List<Long> getListId() {
        return listId;
    }

    public void setListId(List<Long> listId) {
        this.listId = listId;
    }
}
