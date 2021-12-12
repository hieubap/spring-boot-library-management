package vn.isofh.jpa.enums;

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

    public static boolean isDefine(String status){
        return (status.equals(ACTIVE.getStatus()) ||status.equals(INACTIVE.getStatus()) ||status.equals(PENALTY.getStatus()) ||status.equals(MISS.getStatus()));
    }
    public static String getStatusString(){
        return "'"+ACTIVE.getStatus()+"' '"+INACTIVE.getStatus()+"' '"+PENALTY.getStatus()+"' '"+MISS.getStatus();
    }
}
