package library.jpa.enum_;

public enum StatusCard {
    ACTIVE("Active"),
    INACTIVE("In-Active"),
    PENALTY("Penalty"),
    MISS("Miss");

    private String status;

    StatusCard(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
