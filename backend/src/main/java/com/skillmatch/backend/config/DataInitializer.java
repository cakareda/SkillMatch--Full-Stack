package com.skillmatch.backend.config;

import com.skillmatch.backend.model.Job;
import com.skillmatch.backend.model.Role;
import com.skillmatch.backend.model.User;
import com.skillmatch.backend.repository.JobRepository;
import com.skillmatch.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.skillmatch.backend.config.SkillExtractorUtil; // skill extraction util

@Configuration
public class DataInitializer {

    private static final String REMOTIVE_API_URL = "https://remotive.com/api/remote-jobs?limit=100"; // api url

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            JobRepository jobRepository,
            PasswordEncoder passwordEncoder, // encode admin password
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        return args -> {

            // create admin user
            final String adminEmail = "admin@skillmatch.com";
            final String adminPassword = "admin123";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User adminUser = new User();
                adminUser.setName("Admin");
                adminUser.setEmail(adminEmail);

                String encodedPassword = passwordEncoder.encode(adminPassword); // encode password
                adminUser.setPassword(encodedPassword);

                adminUser.setRole(Role.ROLE_ADMIN);
                userRepository.save(adminUser);

                System.out.println(">>> admin user created: " + adminEmail);
            }

            // fetch and process remotive jobs
            System.out.println(">>> deleting old jobs...");
            jobRepository.deleteAll();

            System.out.println(">>> fetching jobs from remotive api...");

            try {
                String jsonResponse = restTemplate.getForObject(REMOTIVE_API_URL, String.class);
                Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});

                if (responseMap != null && responseMap.containsKey("jobs")) {
                    List<Map<String, Object>> jobsData = (List<Map<String, Object>>) responseMap.get("jobs");
                    List<Job> jobsToSave = new ArrayList<>();

                    for (Map<String, Object> apiJobData : jobsData) {
                        Job job = new Job();
                        job.setTitle((String) apiJobData.get("title"));
                        job.setCompany((String) apiJobData.get("company_name"));
                        job.setLocation((String) apiJobData.get("candidate_required_location"));
                        job.setSalary((String) apiJobData.get("salary"));

                        String description = (String) apiJobData.get("description");
                        String cleanDescription = description.replaceAll("<[^>]*>", " "); // remove html tags
                        job.setDescription(cleanDescription);

                        List<String> skills = SkillExtractorUtil.extractSkillsFromDescription(cleanDescription); // extract skills
                        job.setRequiredSkills(skills);

                        job.setPostedDate(LocalDateTime.now()); // set post date

                        jobsToSave.add(job);
                    }

                    jobRepository.saveAll(jobsToSave); // save all jobs
                    System.out.println(">>> " + jobsToSave.size() + " jobs added to database.");
                }
            } catch (Exception e) {
                System.err.println("error fetching data from remotive api: " + e.getMessage());
            }
        };
    }
}
