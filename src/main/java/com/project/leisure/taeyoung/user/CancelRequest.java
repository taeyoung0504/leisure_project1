package com.project.leisure.taeyoung.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CancelRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reasonCancel;
    private Long bookNumValue;
    private String authenticatedUserName;
   private String result;
    // Add constructors, getters, setters, and other fields as needed
}