package org.example.apitestingproject.DTO;

import java.util.Map;

public class AudiLogDTO {
    private String actionType;
    private Integer userId;
    private Integer adminId;
    private Map<String, Object> actionDetails;
//    private  String actionDetails;

    // getters and setters
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public Map<String, Object> getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(Map<String, Object> actionDetails) {
        this.actionDetails = actionDetails;
    }


//    public String getActionDetails() {
//        return actionDetails;
//    }
//
//    public void setActionDetails(String actionDetails) {
//        this.actionDetails = actionDetails;
//    }
}