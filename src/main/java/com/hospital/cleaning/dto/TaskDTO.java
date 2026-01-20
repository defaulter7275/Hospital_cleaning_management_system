package com.hospital.cleaning.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    @NotBlank
    private String description;
    @NotBlank
    private String category;
    private Boolean completed = false;
    private String notes;
    private Integer displayOrder;
}
