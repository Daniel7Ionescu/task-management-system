package com.teamrocket.tms.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "project_name", length = 30)
    private String projectName;

    @OneToMany(mappedBy = "team")
    private List<User> userList = new ArrayList<>();
}
