package com.example.Job_Portal.employerEntity;

import com.example.Job_Portal.jobs.Jobs;
import com.example.Job_Portal.recruiter.Recruiter;
import com.example.Job_Portal.userEntity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employer")
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private User user;
    @Column(name = "company_name",unique = true)
    private String companyname;
    private String industry;
    @Column(name = "company_size")
    private String companysize;
    private String website;
    private String location;
    private String description;
    private String phone;
    private String email;


    @OneToMany(
            mappedBy = "employer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Recruiter> recruiter;
    @OneToMany(
            mappedBy = "employer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Jobs> jobs;
    public void updateFrom(Employer data) {

        if (data.getCompanyname() != null && !data.getCompanyname().isBlank())
            this.companyname = data.getCompanyname();

        if (data.getIndustry() != null && !data.getIndustry().isBlank())
            this.industry = data.getIndustry();

        if (data.getCompanysize() != null && !data.getCompanysize().isBlank())
            this.companysize = data.getCompanysize();

        if (data.getWebsite() != null && !data.getWebsite().isBlank())
            this.website = data.getWebsite();

        if (data.getLocation() != null && !data.getLocation().isBlank())
            this.location = data.getLocation();

        if (data.getDescription() != null && !data.getDescription().isBlank())
            this.description = data.getDescription();

        if (data.getPhone() != null && !data.getPhone().isBlank())
            this.phone = data.getPhone();

        if (data.getEmail() != null && !data.getEmail().isBlank())
            this.email = data.getEmail();
    }

}
