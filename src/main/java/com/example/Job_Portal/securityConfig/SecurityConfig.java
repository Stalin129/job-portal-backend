package com.example.Job_Portal.securityConfig;

import com.example.Job_Portal.jwtAuth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://3.84.98.92"

        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private final JwtAuthFilter jwtAuthFilter;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter){
    this.jwtAuthFilter=jwtAuthFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers("/auth/register","/auth/login")
                        .permitAll()
                        .requestMatchers("/auth/canditateAddiInfo").hasRole("CANDIDATE")
                        .requestMatchers("/candidateupdateresume").hasRole("CANDIDATE")
                        .requestMatchers("/auth/getcandidateinfo").hasAnyRole("CANDIDATE","EMPLOYER")
                        .requestMatchers("/auth/candidateupdate").hasRole("CANDIDATE")
                        .requestMatchers("/auth/getrecruiterinfo").hasRole("RECRUITER")
                        .requestMatchers("/auth/getresume").hasRole("CANDIDATE")
                        .requestMatchers("/auth/employer").hasRole("EMPLOYER")
                        .requestMatchers("/auth/recruiterlist").hasRole("EMPLOYER")
                        .requestMatchers("/auth/employerjobs").hasRole("EMPLOYER")
                        .requestMatchers("/auth/recruiterjobs").hasRole("RECRUITER")
                        .requestMatchers("/auth/recruiterupdate").hasRole("RECRUITER")
                        .requestMatchers("/auth/recruiterapplicants").hasRole("RECRUITER")
                        .requestMatchers("/auth/getemployer").hasRole("EMPLOYER")
                        .requestMatchers("/auth/typeofemployer").hasRole("RECRUITER")
                        .requestMatchers("/auth/employerupdate").hasRole("EMPLOYER")
                        .requestMatchers("/auth/employerapplicantcandidate").hasRole("EMPLOYER")
                        .requestMatchers("/auth/recruiterapplicantcandidate").hasRole("RECRUITER")
                        .requestMatchers("/auth/employerapplicants").hasRole("EMPLOYER")
                        .requestMatchers("/auth/delete_user").hasAnyRole("EMPLOYER","CANDIDATE","RECRUITER","ADMIN")
                        .requestMatchers("/auth/recruiterAddInfo").hasRole("RECRUITER")
                        .requestMatchers("/auth/postjobs").hasAnyRole("EMPLOYER","RECRUITER")
                        .requestMatchers("/auth/jobupdate").hasAnyRole("EMPLOYER","RECRUITER")
                        .requestMatchers("/auth/jobapplicantstatus").hasAnyRole("EMPLOYER","RECRUITER")
                        .requestMatchers("/auth/jobApplication").hasRole("CANDIDATE")
                        .requestMatchers("/auth/deleteappliedjob").hasRole("CANDIDATE")
                        .requestMatchers("/auth/deletejob").hasAnyRole("EMPLOYER","RECRUITER","ADMIN")
                        .requestMatchers("/auth/delete_employer").hasAnyRole("EMPLOYER","ADMIN")
                        .requestMatchers("/auth/userapplications").hasRole("CANDIDATE")
                        .requestMatchers("/auth/joblist").hasRole("CANDIDATE")
                        .requestMatchers("/auth/searchjob").hasRole("CANDIDATE")
                        .requestMatchers("/accountverifying").hasAnyRole("CANDIDATE","RECRUITER","EMPLOYER")
                        .requestMatchers("/userdataupdate").hasAnyRole("CANDIDATE","RECRUITER","EMPLOYER")
                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers("/auth/employers").hasRole("ADMIN")
                        .requestMatchers("/auth/jobs").hasRole("ADMIN")
                        .requestMatchers("/auth/recruiters").hasRole("ADMIN")
                        .requestMatchers("/auth/candidates").hasRole("ADMIN")
                        .requestMatchers("/auth/applicants").hasRole("ADMIN")
                        .requestMatchers("/auth/deleteapplicant").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
