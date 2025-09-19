package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "DOCUMENT_VERIFICATION")
public class DocumentVerification {

    public enum DocumentType { Aadhar, PAN, Passport }
    public enum VerificationStatus { Pending, Verified, Rejected }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCUMENT_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_DOC_USER"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "DOCUMENT_TYPE", length = 50,nullable = false)
    private DocumentType documentType;

    @Column(name = "DOCUMENT_NUMBER", length = 50, nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "DOCUMENT_FILE_PATH", length = 255)
    private String documentFilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "VERIFICATION_STATUS", length = 10)
    private VerificationStatus verificationStatus;

    @ManyToOne
    @JoinColumn(name = "VERIFIED_BY", foreignKey = @ForeignKey(name = "FK_DOC_ADMIN"))
    private Admin verifiedBy;

    @Column(name = "VERIFICATION_DATE")
    private LocalDate verificationDate;

    public DocumentVerification() {
    }

    public DocumentVerification(User user, DocumentType documentType, String documentNumber, String documentFilePath, VerificationStatus verificationStatus, Admin verifiedBy, LocalDate verificationDate) {
        this.user = user;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.documentFilePath = documentFilePath;
        this.verificationStatus = verificationStatus;
        this.verifiedBy = verifiedBy;
        this.verificationDate = verificationDate;
    }

    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public String getDocumentFilePath() { return documentFilePath; }
    public void setDocumentFilePath(String documentFilePath) { this.documentFilePath = documentFilePath; }

    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }

    public Admin getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(Admin verifiedBy) { this.verifiedBy = verifiedBy; }

    public LocalDate getVerificationDate() { return verificationDate; }
    public void setVerificationDate(LocalDate verificationDate) { this.verificationDate = verificationDate; }

    @Override
    public String toString() {
        return "DocumentVerification{" +
                "id=" + id +
                ", user=" + user +
                ", documentType=" + documentType +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentFilePath='" + documentFilePath + '\'' +
                ", verificationStatus=" + verificationStatus +
                ", verifiedBy=" + verifiedBy +
                ", verificationDate=" + verificationDate +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof DocumentVerification d)) return false; return Objects.equals(id,d.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

