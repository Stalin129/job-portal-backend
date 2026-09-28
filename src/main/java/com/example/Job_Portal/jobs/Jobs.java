package com.example.Job_Portal.jobs;

import com.example.Job_Portal.employerEntity.Employer;
import com.example.Job_Portal.jobAppllication.JobApplications;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employer_id", "role"})
        }
)
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;
    @OneToMany(
            mappedBy = "jobs",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<JobApplications> job;
    private String role;
    private String location;
    private String jobtype;
    private String employementtype;
    private String experience;
    private String salary;
    private String skills;
    private String vacancies;
    private String deadline;
    private String jobdescribtion;
    private String status;
    private LocalDateTime appliedAt;

    @PrePersist
    public void onCreate() {
        appliedAt = LocalDateTime.now();
    }

}
