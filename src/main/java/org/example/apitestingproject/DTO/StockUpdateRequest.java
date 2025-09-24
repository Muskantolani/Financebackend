package org.example.apitestingproject.dto;

public class StockUpdateRequest {
    private Integer newQty;   // set absolute stock
    private Integer delta;    // +/- adjustment
    private String reason;    // required only if delta != null

    public Integer getNewQty() { return newQty; }
    public void setNewQty(Integer newQty) { this.newQty = newQty; }
    public Integer getDelta() { return delta; }
    public void setDelta(Integer delta) { this.delta = delta; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
