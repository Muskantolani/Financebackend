package org.example.apitestingproject.dto;

import org.example.apitestingproject.entities.CardType;

import javax.smartcardio.Card;

public class UserDto {
        private int dbId;              // real USER_ID from DB
        private String displayId;      // e.g., "U001"
        private String name;
        private String email;
        private String status;
        private String cardType;

        public UserDto(int dbId, String displayId, String name, String email, String status, String cardType) {
            this.dbId = dbId;
            this.displayId = displayId;
            this.name = name;
            this.email = email;
            this.status = status;
            this.cardType = cardType;
        }
    // getters and setters

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
