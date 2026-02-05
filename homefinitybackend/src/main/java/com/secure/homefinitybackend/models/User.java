package com.secure.homefinitybackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "mobile")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @Size(max = 120)
    @Column(name = "password")
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name")
    private String fullName;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    @Column(name = "mobile")
    private String mobile;

    // Account status flags
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    // Role relationship
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private Role role;

    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;

    // KYC fields
    @Column(name = "aadhar_number")
    private String aadharNumber;

    @Column(name = "pan_number")
    private String panNumber;

    // Gender & DOB
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // Address fields
    private String address;
    private String villageTown;
    private String postOffice;
    private String policeStation;
    private String district;
    private String state;
    private String pinCode;

    // Owner-specific fields (nullable for tenants)
    @Column(name = "registration_id")
    private String registrationId;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "ifsc_code")
    private String ifscCode;

    // Tenant-specific fields (nullable for owners)
    private String occupation;

    @Column(name = "company_institute")
    private String companyInstitute;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    // Constructors
    public User(String fullName, String email, String mobile, String password) {
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public User(String fullName, String email, String mobile) {
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return userId != null && userId.equals(((User) o).getUserId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}