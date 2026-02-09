package com.secure.homefinitybackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    private String ownerUserName;
}
