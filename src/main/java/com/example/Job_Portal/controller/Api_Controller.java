package com.example.Job_Portal.controller;

import com.example.Job_Portal.authenticator.Authenticator;
import com.example.Job_Portal.candidate.Candidates;
import com.example.Job_Portal.candidate.CandidatesRepository;
import com.example.Job_Portal.employerEntity.Employer;
import com.example.Job_Portal.employerEntity.EmployerRepository;
import com.example.Job_Portal.jobAppllication.JobApplications;
import com.example.Job_Portal.jobAppllication.JobApplicationsRepository;
import com.example.Job_Portal.jobAppllication.UserApplicationsDTO;
import com.example.Job_Portal.jobs.JobDTO;
import com.example.Job_Portal.jobs.Jobs;
import com.example.Job_Portal.jobs.JobsRepository;
import com.example.Job_Portal.jwtAuth.JwtAuth;
import com.example.Job_Portal.recruiter.Recruiter;
import com.example.Job_Portal.recruiter.RecruiterRepository;
import com.example.Job_Portal.userEntity.*;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
@RequestMapping("/auth")
public class Api_Controller {
    private final UserRepository userRepository;
    private final CandidatesRepository candidatesRepository;
    private final RecruiterRepository recruiterRepository;
    private final EmployerRepository employerRepository;
    private final JobsRepository jobsRepository;
    private final JobApplicationsRepository jobApplicationsRepository;
    private final Authenticator authenticator;
    private final JwtAuth jwtAuth;

    Api_Controller(UserRepository userRepository, CandidatesRepository candidates,
                   RecruiterRepository recruiterRepository, EmployerRepository employerRepository,
                   JobsRepository jobsRepository, JobApplicationsRepository jobApplicationsRepository,
                   Authenticator authenticator, JwtAuth jwtAuth) {
        this.userRepository = userRepository;
        this.candidatesRepository = candidates;
        this.recruiterRepository = recruiterRepository;
        this.employerRepository = employerRepository;
        this.jobsRepository = jobsRepository;
        this.jobApplicationsRepository = jobApplicationsRepository;
        this.authenticator = authenticator;
        this.jwtAuth = jwtAuth;
    }

    @PostMapping("register")
    public String register(@RequestBody User data) {
        String result = authenticator.register(data);
        return result;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest data) {
        try {
            User user = authenticator.login(data);
            String token = jwtAuth.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.ok(new LoginResponse(user.getId(),user.getUsername(),user.getEmail(),token, user.getRole()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/canditateAddiInfo")
    public String canditateAddiInfo(@RequestBody Candidates data) {
        candidatesRepository.save(data);
        return "";
    }
    @PostMapping("/candidateupdate")
    public Candidates candidateUpdate(@RequestBody Candidates data) {

        try {
            User user = userRepository
                    .findById(data.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            data.setUser(user);
            if(candidatesRepository.findByUserId(user.getId()).isPresent()){
                Optional<Candidates> candidates=candidatesRepository.findByUserId(user.getId());
                Candidates candidates1=candidates.get();
                candidates1.setEducation(data.getEducation());
                candidates1.setExperience(data.getExperience());
                candidates1.setGender(data.getGender());
                candidates1.setFullname(data.getFullname());
                candidates1.setPhone(data.getPhone());
                candidates1.setCity(data.getCity());
                candidates1.setReligion(data.getReligion());
                candidates1.setSkills(data.getSkills());
                candidates1.setPreferredrole(data.getPreferredrole());
                candidates1.setCgpa(data.getCgpa());
                candidatesRepository.save(candidates1);
                return candidates1;
            }

            Candidates candidate = candidatesRepository.save(data);
            return candidate;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/candidateupdateresume/{userId}")
    public String candidateupdateresume(
            @PathVariable Long userId,
            @RequestParam("resume") MultipartFile resume) {

        try {

            Optional<Candidates> candidates =
                    candidatesRepository.findByUserId(userId);

            Candidates candidates1 = candidates
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            candidates1.setResume(resume.getBytes());

            candidatesRepository.save(candidates1);

            return "Resume upload success";

        } catch (Exception e) {

            return "Resume upload failed";
        }
    }


    @GetMapping("/getcandidateinfo/{id}")
    public ResponseEntity<?> getcandidateinfo(@PathVariable Long id) {

        Optional<Candidates> candidate =
                candidatesRepository.findByUserId(id);

        if (candidate.isPresent()) {
            return ResponseEntity.ok(candidate.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidate profile not found");
    }
    @GetMapping("/getrecruiterinfo/{id}")
    public ResponseEntity<?> getrecruiterinfo(@PathVariable Long id) {

        Optional<Recruiter> recruiter =
                recruiterRepository.findByUserId(id);

        if (recruiter.isPresent()) {
            Recruiter recruiter1 =recruiter.get();
            recruiter1.setLastActive(LocalDateTime.now());
            recruiterRepository.save(recruiter1);
            return ResponseEntity.ok(recruiter.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidate profile not found");
    }
    @GetMapping("/getresume/{id}")
    public ResponseEntity<?> getResume(@PathVariable Long id) {

        Optional<Candidates> candidate =
                candidatesRepository.findByUserId(id);
        if(candidate.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(candidate.get().getResume());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Resume not found");
    }

    @PostMapping("/recruiterAddInfo")
    public ResponseEntity<?> recruiterAddinfo(@RequestBody Recruiter data) {
        Employer employer=employerRepository.findByCompanyname(data.getCompanyname());
        data.setEmployer(employer);
        data.setLastActive(LocalDateTime.now());
        return ResponseEntity.ok(recruiterRepository.save(data));
    }
    @PutMapping("/recruiterupdate")
    public ResponseEntity<?>recruiterupdate(@RequestBody Recruiter data){

        Optional<Recruiter> recruiter=recruiterRepository.findByUserId(data.getId());
        if(recruiter.isPresent()){
            Recruiter recruiter1 =recruiter.get();
            recruiter1.setDesignation(data.getDesignation());
            recruiter1.setEmail(data.getEmail());
            recruiter1.setLocation(data.getLocation());
            recruiter1.setCompanyname(data.getCompanyname());
            recruiter1.setExperience(data.getExperience());
            recruiter1.setWebsite(data.getWebsite());
            recruiter1.setPhone(data.getPhone());

            return ResponseEntity.ok(recruiterRepository.save(recruiter1));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile update failed");
    }
  @PostMapping("typeofemployer")
  public ResponseEntity<?> typeofemployer(){
        List<Employer> employer=employerRepository.findAll();
        return ResponseEntity .ok(employer.stream().map(employer1 -> employer1.getCompanyname()));
  }
    @PostMapping("/employer")
    public ResponseEntity<?> employer(@RequestBody Employer data) {
        if(!employerRepository.existsByCompanyname(data.getCompanyname())){

            return ResponseEntity.ok(employerRepository.save(data));
        }
        return ResponseEntity.badRequest().body("Companyname must unique");
    }
    @PutMapping("/employerupdate")
    public ResponseEntity<?> employerupdate(@RequestBody Employer data) {

        Optional<Employer> employer =
                employerRepository.findByUserId(data.getId());

        if (employer.isPresent()) {

            Employer employer1 = employer.get();
            employer1.updateFrom(data);
            employerRepository.save(employer1);
            return ResponseEntity.ok("Employer updated");
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/getemployer/{userId}")
    public ResponseEntity<?>getemployer(@PathVariable Long userId){
        Optional<Employer>employer=employerRepository.findByUserId(userId);
        if(employer.isPresent()){
            return ResponseEntity.ok(employer);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Profile not found");
    }

    @PostMapping("/postjobs")
    public ResponseEntity<?> jobs(@RequestBody Jobs data) {
        try {
            jobsRepository.save(data);

            return ResponseEntity.ok("posted");

        } catch (DataIntegrityViolationException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Already this job poasted");
        }

    }

    @PutMapping("/jobupdate")
    public ResponseEntity<?>jobupdate(@RequestBody Jobs data){
        Optional<Jobs> job=jobsRepository.findById(data.getId());
        if(job.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("job not found");
        }
            Jobs job1 = job.get();
            job1.setRole(data.getRole());
            job1.setLocation(data.getLocation());
            job1.setJobtype(data.getJobtype());
            job1.setEmployementtype(data.getEmployementtype());
            job1.setExperience(data.getExperience());
            job1.setStatus(data.getStatus());
            job1.setSalary(data.getSalary());
            job1.setSkills(data.getSkills());
            job1.setVacancies(data.getVacancies());
            job1.setDeadline(data.getDeadline());
            job1.setJobdescribtion(data.getJobdescribtion());
            jobsRepository.save(job1);
        return ResponseEntity.ok("job updated");

    }
    @DeleteMapping("/deletejob/{id}")
    public ResponseEntity<?>deletejob(@PathVariable Long id){
        jobsRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }


    @DeleteMapping("/deleteappliedjob/{id}")
    public String deleteappliedjob(@PathVariable Long id){
        try {
            jobApplicationsRepository.deleteById(id);
            return "Job deleted";
        } catch (RuntimeException e) {
            return e.getMessage();
        }

    }
    @GetMapping("/joblist")
    public List<JobDTO>jobsList(){
        List<Jobs>jobs=jobsRepository.findAll();
        return jobs.stream().map(job->new JobDTO(job)).toList();
    }


    @GetMapping("/searchjob/{role}/{location}")
    public ResponseEntity<?> searchjob(@PathVariable String role,@PathVariable String location){
        try {
            List<Jobs>jobs=jobsRepository.findByRoleAndLocation(role,location);
            if(jobs.isEmpty()){
                throw new RuntimeException("Job not found");
            }
            return ResponseEntity.ok(jobs.stream().map(job->new JobDTO(job)).toList());
        } catch (RuntimeException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/jobApplication")
    public String jobApplication(@RequestBody JobApplications data) {

        try {

            Long userId = data.getUser().getId();
            Long jobId = data.getJobs().getId();

            boolean alreadyApplied =
                    jobApplicationsRepository.existsByUserIdAndJobsId(
                            userId,
                            jobId
                    );

            if (alreadyApplied) {
                return "Already applied";
            }

            jobApplicationsRepository.save(data);

            return "Applied";

        } catch (Exception e) {
            return e.getMessage();
        }
    }
    @PutMapping("jobapplicantstatus/{userId}/{jobId}/{status}")
    public ResponseEntity<?>jobapplicantstatus(@PathVariable Long userId,@PathVariable Long jobId,@PathVariable String status ){

        try {

            JobApplications jobApplications = jobApplicationsRepository.findByJobsIdAndUserId(jobId, userId);
            System.out.println("APPLICATION FOUND = " + jobApplications);
            jobApplications.setStatus(status);
            jobApplicationsRepository.save(jobApplications);
            return ResponseEntity.ok("Status updated");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/delete_user/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        System.out.println("delete stage entered");
    try {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        return ResponseEntity.ok("Account deleted");
    } catch (Exception e) {
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
    }

    @DeleteMapping("/delete_employer/{id}")
    @Transactional
    public String deleteEmployer(@PathVariable Long id) {
        System.out.println("entered stage");

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));
        User user = employer.getUser();
        user.setEmployer(null);

        employerRepository.delete(employer);

        return "Employer deleted successfully";
    }

    @PostMapping("/userapplications/{username}")
    public List<UserApplicationsDTO> jobApplications(@PathVariable String username){
        Optional<User> users=userRepository.findByUsername(username);
        User user=users.get();
        Long userId=user.getId();
        List<JobApplications> jobApplications=jobApplicationsRepository.findByUser_Id(userId);

        return jobApplications.stream()
                .map(applications->new UserApplicationsDTO(
                        applications.getId(),applications.getJobs().getId(),
                        applications.getJobs().getEmployer().getCompanyname(),
                        username, applications.getJobs().getRole(),
                        applications.getStatus(),
                        applications.getAppliedAt().format(DateTimeFormatter.ofPattern("MMMM dd,yyyy")),
                        applications.getJobs().getSkills(),applications.getJobs().getDeadline(),
                        applications.getJobs().getExperience(),
                        applications.getJobs().getLocation(),
                        applications.getJobs().getSalary(),
                        applications.getJobs().getJobtype(),
                        applications.getJobs().getVacancies(),
                        applications.getJobs().getJobdescribtion())
                ).toList();
    }
    @PostMapping("/accountverifying")
    public String accountverifying(@RequestBody User data){
        return authenticator.verification(data.getUsername(), data.getPassword());
    }
    @PostMapping("/userdataupdate")
    public String userdataupdate(@RequestBody UpdateUser data){
        try {
            if (data.getUpdatename().length() >= 8 || data.getEmail().length() >= 1 || data.getPassword().length() >= 8) {

                return authenticator.userupdate(data);
            } else {
                return "Data must greater 8";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/employerapplicants/{id}")
    public ResponseEntity<?> employerapplicants(@PathVariable Long id) {

        if (employerRepository.findByUserId(id).isPresent()) {

            Employer employer = employerRepository.findByUserId(id).get();

            List<Jobs> jobs =
                    jobsRepository.findByEmployerId(employer.getId());

            if (jobs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No jobs found for this employer");
            }

            List<JobApplications> jobApplications = new ArrayList<>();

            for (Jobs job : jobs) {

                List<JobApplications> applications =
                        jobApplicationsRepository.findByJobsId(job.getId());

                jobApplications.addAll(applications);
            }
            return ResponseEntity.ok(jobApplications);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Employer not found");
    }
    @GetMapping("/recruiterapplicants/{id}")
    public ResponseEntity<?> recruiterapplicants(@PathVariable Long id) {
        System.out.println("Entered recruiterapplicants ");
        if (employerRepository.findById(id).isPresent()) {

            Employer employer = employerRepository.findById(id).get();

            List<Jobs> jobs =
                    jobsRepository.findByEmployerId(employer.getId());

            if (jobs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No jobs found for this employer");
            }

            List<JobApplications> jobApplications = new ArrayList<>();

            for (Jobs job : jobs) {

                List<JobApplications> applications =
                        jobApplicationsRepository.findByJobsId(job.getId());

                jobApplications.addAll(applications);
            }
            return ResponseEntity.ok(jobApplications);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Employer not found");
    }


    @GetMapping("/employerapplicantcandidate/{id}")
    public ResponseEntity<?> employerapplicantcandidate(@PathVariable Long id) {

        if (employerRepository.findByUserId(id).isPresent()) {

            Employer employer = employerRepository.findByUserId(id).get();

            List<Jobs> jobs =
                    jobsRepository.findByEmployerId(employer.getId());

            if (jobs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No jobs found for this employer");
            }

            List<JobApplications> jobApplications = new ArrayList<>();

            for (Jobs job : jobs) {

                List<JobApplications> applications =
                        jobApplicationsRepository.findByJobsId(job.getId());

                jobApplications.addAll(applications);
            }
            List<Map<String, Object>> candidatesinfo = new ArrayList<>();

            for (JobApplications jobApplication : jobApplications) {

                Optional<Candidates> candidates =
                        candidatesRepository.findByUserId(
                                jobApplication.getUser().getId()
                        );

                String role = jobApplication.getJobs().getRole();
                String status=jobApplication.getStatus();
                LocalDateTime appliedAt=jobApplication.getAppliedAt();
                String jobtype=jobApplication.getJobs().getJobtype();
                String location=jobApplication.getJobs().getLocation();
                String salary=jobApplication.getJobs().getSalary();
                Long jobid=jobApplication.getJobs().getId();

                candidates.ifPresent(candidate -> {

                    Map<String, Object> data = new HashMap<>();

                    data.put("candidate", candidate);
                    data.put("role", role);
                    data.put("status", status);
                    data.put("appliedAt", appliedAt);
                    data.put("jobtype",jobtype);
                    data.put("location",location);
                    data.put("salary",salary);
                    data.put("jobid",jobid);

                    candidatesinfo.add(data);
                });
            }

            return ResponseEntity.ok(candidatesinfo);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidateinfo not found");
    }

    @GetMapping("/recruiterapplicantcandidate/{id}")
    public ResponseEntity<?> recruiterapplicantcandidate(@PathVariable Long id) {
        System.out.println("Entered recruiterapplicantcandidate");

        if (employerRepository.findById(id).isPresent()) {

            Employer employer = employerRepository.findById(id).get();

            List<Jobs> jobs =
                    jobsRepository.findByEmployerId(employer.getId());

            if (jobs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No jobs found for this employer");
            }

            List<JobApplications> jobApplications = new ArrayList<>();

            for (Jobs job : jobs) {

                List<JobApplications> applications =
                        jobApplicationsRepository.findByJobsId(job.getId());

                jobApplications.addAll(applications);
            }
            List<Map<String, Object>> candidatesinfo = new ArrayList<>();

            for (JobApplications jobApplication : jobApplications) {

                Optional<Candidates> candidates =
                        candidatesRepository.findByUserId(
                                jobApplication.getUser().getId()
                        );

                String role = jobApplication.getJobs().getRole();
                String status=jobApplication.getStatus();
                LocalDateTime appliedAt=jobApplication.getAppliedAt();
                String jobtype=jobApplication.getJobs().getJobtype();
                String location=jobApplication.getJobs().getLocation();
                String salary=jobApplication.getJobs().getSalary();
                Long jobid=jobApplication.getJobs().getId();

                candidates.ifPresent(candidate -> {

                    Map<String, Object> data = new HashMap<>();

                    data.put("candidate", candidate);
                    data.put("role", role);
                    data.put("status", status);
                    data.put("appliedAt", appliedAt);
                    data.put("jobtype",jobtype);
                    data.put("location",location);
                    data.put("salary",salary);
                    data.put("jobid",jobid);

                    candidatesinfo.add(data);
                });
            }

            return ResponseEntity.ok(candidatesinfo);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidateinfo not found");
    }



    @GetMapping("/employerjobs/{id}")
    public ResponseEntity<?> employerJobs(@PathVariable Long id) {

        if (employerRepository.findByUserId(id).isPresent()) {

            Employer employer = employerRepository.findByUserId(id).get();

            List<Jobs> jobs =
                    jobsRepository.findByEmployerId(employer.getId());

            if (jobs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No jobs found for this employer");
            }

            List<Map<String, Object>> employerjob = new ArrayList<>();

            for (Jobs job : jobs) {

                List<JobApplications> jobApplications =
                        jobApplicationsRepository.findByJobsId(job.getId());

                int totaljobApplication = jobApplications.size();


                Map<String, Object> data = new HashMap<>();

                data.put("job", job);
                data.put("applicationCount", totaljobApplication);


                employerjob.add(data);
            }

            return ResponseEntity.ok(employerjob);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Employer not found");
    }
    @GetMapping("/recruiterjobs/{id}")
    public ResponseEntity<?> recruiterJobs(@PathVariable Long id) {
    System.out.println("Entered recruiterjobs");
        if (employerRepository.findById(id).isPresent()) {

            Employer employer = employerRepository.findById(id).get();

            List<Jobs> jobs =
                    jobsRepository.findByEmployerId(employer.getId());

            if (jobs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No jobs found for this employer");
            }

            List<Map<String, Object>> employerjob = new ArrayList<>();

            for (Jobs job : jobs) {

                List<JobApplications> jobApplications =
                        jobApplicationsRepository.findByJobsId(job.getId());

                int totaljobApplication = jobApplications.size();


                Map<String, Object> data = new HashMap<>();

                data.put("job", job);
                data.put("applicationCount", totaljobApplication);


                employerjob.add(data);
            }

            return ResponseEntity.ok(employerjob);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Employer not found");
    }

    @GetMapping("/recruiterlist/{id}")
    public ResponseEntity<?>recruiterlist(@PathVariable Long id){
        try {
            List<Recruiter> recruiters = recruiterRepository.findByEmployerId(id);
            return ResponseEntity.ok(recruiters);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/users")
    public ResponseEntity<?>users(){
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping("/employers")
    public ResponseEntity<?>employers(){
        try {
            List<Employer>employers=employerRepository.findAll();
            List<Map<String,Object>>employerAndjobs=new ArrayList<>();
            for(Employer employer:employers){
                List<Jobs>job=jobsRepository.findByEmployerId(employer.getId());
                Map<String,Object>data=new HashMap<>();
                int totaljobs=job.size();
                data.put("totaljobs",totaljobs);
                data.put("employer",employer);
                employerAndjobs.add(data);
            }

            return ResponseEntity.ok(employerAndjobs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/jobs")
    public ResponseEntity<?>jobs(){
        try{
            List<Jobs>jobs=jobsRepository.findAll();
            List<Map<String,Object>>jobsAndApplicants=new ArrayList<>();
            for(Jobs job:jobs){
                List<JobApplications>jobApplications=jobApplicationsRepository.findByJobsId(job.getId());
                int totalapplicants=jobApplications.size();
                Map<String,Object>data=new HashMap<>();
                data.put("totalapplicants",totalapplicants);
                data.put("jobs",job);
                jobsAndApplicants.add(data);
            }
            return ResponseEntity.ok(jobsAndApplicants);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/recruiters")
    public ResponseEntity<?>recruiters(){
        try{
            List<Recruiter>recruiters=recruiterRepository.findAll();
            List<Map<String,Object>>recruiterAndJobs=new ArrayList<>();
            for(Recruiter recruiter:recruiters){
                int totaljobs=jobsRepository.findByEmployerId(recruiter.getEmployer().getId()).size();
                Map<String,Object>data=new HashMap<>();
                data.put("totaljobs",totaljobs);
                data.put("recruiter",recruiter);
                recruiterAndJobs.add(data);
            }
            return ResponseEntity.ok(recruiterAndJobs);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/candidates")
    public ResponseEntity<?>candidates(){
        try{
            List<Candidates>candidates=candidatesRepository.findAll();
            List<Map<String,Object>>candidateAndapplicant=new ArrayList<>();
            for(Candidates candidates1:candidates){
                int totalapplicants=jobApplicationsRepository.findByUser_Id(candidates1.getUser().getId()).size();
                Map<String,Object>data=new HashMap<>();
                data.put("totalapplicants",totalapplicants);
                data.put("candidate",candidates1);
                candidateAndapplicant.add(data);
            }
            return ResponseEntity.ok(candidateAndapplicant);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/applicants")
    public ResponseEntity<?>applicants(){
        List<JobApplications>candidates=jobApplicationsRepository.findAll();
        return ResponseEntity.ok(candidates);
    }
    @DeleteMapping("/deleteapplicant/{id}")
    public ResponseEntity<?>deleteapplicant(@PathVariable Long id){
        jobApplicationsRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

}
