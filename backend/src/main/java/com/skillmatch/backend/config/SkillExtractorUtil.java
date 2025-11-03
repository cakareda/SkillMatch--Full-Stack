package com.skillmatch.backend.config;

import java.util.*;
import java.util.stream.Collectors;

public class SkillExtractorUtil {

    // predefined list of detectable skills (expandable)
    private static final Set<String> KNOWN_SKILLS = new HashSet<>(Arrays.asList(
            "Java", "Spring Boot", "Python", "Django", "JavaScript", "React", "Angular", "Vue",
            "Node.js", "TypeScript", "HTML", "CSS", "SQL", "MySQL", "PostgreSQL", "MongoDB",
            "AWS", "Azure", "GCP", "Kubernetes", "Docker", "Terraform", "CI/CD", "Jenkins",
            "Git", "Linux", "Bash", "Scrum", "Agile", "Jira", "Figma", "Photoshop", "Swift", "Kotlin",
            "Ruby on Rails", "PHP", "Laravel", "C#", ".NET", "Unity", "Unreal Engine"
    ));

    /**
     * Extracts known skills from a given job description.
     *
     * @param description the job description text
     * @return list of detected skills
     */
    public static List<String> extractSkillsFromDescription(String description) {
        if (description == null || description.isEmpty()) {
            return List.of();
        }

        String lowerCaseDesc = description.toLowerCase();

        // match known skills by checking lowercase presence in the text
        return KNOWN_SKILLS.stream()
                .filter(skill -> lowerCaseDesc.contains(skill.toLowerCase()))
                .collect(Collectors.toList());
    }
}
