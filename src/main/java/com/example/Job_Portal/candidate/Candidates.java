package com.example.Job_Portal.candidate;

import com.example.Job_Portal.jobAppllication.JobApplications;
import com.example.Job_Portal.userEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@Table(name = "candidates")
public class Candidates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String fullname;
    private String phone;
    private String gender;
    private String city;
    private String religion;
    @Lob
    private byte[] resume;
    private String skills;
    private String education;
    private String experience;
    private String preferredrole;
    private String cgpa;

}
