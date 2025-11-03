package com.skillmatch.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemotiveJobDTO {
    private String title;
    private String companyName;
    private String candidateRequiredLocation;
    private String description;
    private String salary;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @JsonProperty("company_name")
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    @JsonProperty("candidate_required_location")
    public String getCandidateRequiredLocation() { return candidateRequiredLocation; }
    public void setCandidateRequiredLocation(String candidateRequiredLocation) { this.candidateRequiredLocation = candidateRequiredLocation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
}

