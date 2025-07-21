package com.marriagebureau.dto;


import lombok.Data; 
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
// If not using Lombok, add getters, setters, and constructors.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String description; // Changed from 'task' in frontend mock to 'description'
    private String dueDate; // Changed from 'due' in frontend mock to 'dueDate'
    private String priority;

    // If not using Lombok, uncomment and generate these
    // public TaskDTO() {}
    // public TaskDTO(Long id, String description, String dueDate, String priority) { ... }
    // public Long getId() { return id; }
    // ... etc.
}