package com.example.Job_Portal.jobAppllication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class UserApplicationsDTO {
    private Long userjobid;
    private Long id;
    private String companyname;
    private String username;
    private String role;
    private String status;
    private String appliedAt;
    private String skills;
    private String deadline;
    private String experience;
    private String location;
    private String salary;
    private String type;
    private String vacancies;
    private String jobdescribtion;

}
