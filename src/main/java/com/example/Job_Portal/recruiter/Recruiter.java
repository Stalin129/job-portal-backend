package com.example.Job_Portal.recruiter;

import com.example.Job_Portal.employerEntity.Employer;
import com.example.Job_Portal.userEntity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recruiter")
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employer employer;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String email;
    private String designation;
    private String experience;
    @Column(name = "Company_name")
    private String companyname;
    private String location;
    private String phone;
    @Column(name = "web_site")
    private String website;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastActive;
}
