package com.skillmatch.backend.model;

import com.skillmatch.backend.config.StringListConverter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private String location;

    @Column(columnDefinition = "TEXT") // use text instead of varchar(1000)
    private String description;

    private String salary;

    // required skills stored as json string in db
    @Lob
    @Convert(converter = StringListConverter.class)
    private List<String> requiredSkills = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    private LocalDateTime postedDate;

    // default constructor
    public Job() {
        this.postedDate = LocalDateTime.now();
    }

    // constructor with skills
    public Job(String title, String company, String location, String description, String salary, List<String> requiredSkills) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.description = description;
        this.salary = salary;
        this.requiredSkills = requiredSkills;
        this.postedDate = LocalDateTime.now();
    }

    // backward compatible constructor
    public Job(String title, String company, String location, String description, String salary) {
        this(title, company, location, description, salary, new ArrayList<>());
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }

    public LocalDateTime getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate; }
}
