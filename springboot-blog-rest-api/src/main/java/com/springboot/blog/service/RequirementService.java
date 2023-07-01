package com.springboot.blog.service;

import com.springboot.blog.payload.RequirementDto;
import com.springboot.blog.payload.RequirementResponse;

import java.util.List;

public interface RequirementService {
    RequirementDto createReqiurement(RequirementDto requirementDto);

    RequirementResponse getAllRequirements(int pageNo, int pageSize, String sortBy, String sortDir);

    RequirementDto getRequirementById(long id);

    RequirementDto updateRequirement(RequirementDto requirementDto, long id);

    void deleteRequirementById(long id);

    List<RequirementDto> getRequirementsByCategory(Long categoryId);
}
