package ru.shaxowskiy.cloudfilestorage.dto;

import lombok.Builder;
import lombok.Data;
import ru.shaxowskiy.cloudfilestorage.models.ResourseType;

@Data
@Builder
public class ResourceInfoDTO {
    private String path;
    private String name;
    private Long size;
    private ResourseType type;
}
