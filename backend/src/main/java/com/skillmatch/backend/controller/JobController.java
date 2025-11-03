package com.skillmatch.backend.controller;

import com.skillmatch.backend.model.Job;
import com.skillmatch.backend.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * JobController
 * -------------
 * Handles all CRUD operations for Job entities.
 * Data is fetched directly from the database (no external API calls).
 */
@RestController
@RequestMapping("/jobs") // ✅ Public API endpoint
@CrossOrigin(origins = "http://localhost:4200") // Allow frontend dev server
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    //  GET — Retrieve all jobs
    @GetMapping
    public List<Job> getAllJobs() {
        System.out.println(">>> [DEBUG] JobController: Fetching all jobs from database...");
        return jobRepository.findAll();
    }

    // POST — Create a new job
    // (Admin-only route)

    @PostMapping
    public Job addJob(@RequestBody Job job) {
        return jobRepository.save(job);
    }


    //  GET — Retrieve job by ID

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    //  PUT — Update existing job
    // (Partial update allowed)

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Job job = optionalJob.get();
        if (jobDetails.getTitle() != null) job.setTitle(jobDetails.getTitle());
        if (jobDetails.getCompany() != null) job.setCompany(jobDetails.getCompany());
        if (jobDetails.getLocation() != null) job.setLocation(jobDetails.getLocation());
        if (jobDetails.getDescription() != null) job.setDescription(jobDetails.getDescription());
        if (jobDetails.getSalary() != null) job.setSalary(jobDetails.getSalary());
        if (jobDetails.getRequiredSkills() != null && !jobDetails.getRequiredSkills().isEmpty()) {
            job.setRequiredSkills(jobDetails.getRequiredSkills());
        }

        Job updatedJob = jobRepository.save(job);
        return ResponseEntity.ok(updatedJob);
    }

    //  DELETE — Remove a job

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        if (!jobRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        jobRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
