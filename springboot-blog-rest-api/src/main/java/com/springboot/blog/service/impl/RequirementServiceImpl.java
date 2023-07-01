package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Requirement;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.RequirementDto;
import com.springboot.blog.payload.RequirementResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.RequirementRepository;
import com.springboot.blog.service.RequirementService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequirementServiceImpl implements RequirementService {

    private RequirementRepository requirementRepository;

    private ModelMapper mapper;

    private CategoryRepository categoryRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository, ModelMapper mapper,
                                  CategoryRepository categoryRepository) {
          this.requirementRepository = requirementRepository;
          this.mapper = mapper;
          this.categoryRepository = categoryRepository;
    }

    @Override
    public RequirementDto createReqiurement(RequirementDto requirementDto) {

        Category category = categoryRepository.findById(requirementDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requirementDto.getCategoryId()));

        // convert DTO to entity
        Requirement requirement = mapToEntity(requirementDto);
        requirement.setCategory(category);
        Requirement newRequirement = requirementRepository.save(requirement);

        // convert entity to DTO
        RequirementDto reqResponse = mapToDTO(newRequirement);
        return reqResponse;
    }

    @Override
    public RequirementResponse getAllRequirements(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Requirement> requirements = requirementRepository.findAll(pageable);

        // get content for page object
        List<Requirement> listOfRequirements = requirements.getContent();

        List<RequirementDto> content= listOfRequirements.stream().map(requirement -> mapToDTO(requirement)).collect(Collectors.toList());

        RequirementResponse requirementResponse = new RequirementResponse();
        requirementResponse.setContent(content);
        requirementResponse.setPageNo(requirements.getNumber());
        requirementResponse.setPageSize(requirements.getSize());
        requirementResponse.setTotalElements(requirements.getTotalElements());
        requirementResponse.setTotalPages(requirements.getTotalPages());
        requirementResponse.setLast(requirements.isLast());

        return requirementResponse;
    }

    @Override
    public RequirementDto getRequirementById(long id) {
        Requirement requirement = requirementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(requirement);
    }

    @Override
    public RequirementDto updateRequirement(RequirementDto requirementDto, long id) {
        // get post by id from the database
        Requirement requirement = requirementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        Category category = categoryRepository.findById(requirementDto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requirementDto.getCategoryId()));

        requirement.setTitle(requirementDto.getTitle());
        requirement.setDescription(requirementDto.getDescription());
        requirement.setContent(requirementDto.getContent());
        requirement.setCategory(category);
        Requirement updatedRequirement = requirementRepository.save(requirement);
        return mapToDTO(updatedRequirement);
    }

    @Override
    public void deleteRequirementById(long id) {
        // get post by id from the database
        Requirement requirement = requirementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        requirementRepository.delete(requirement);
    }

    @Override
    public List<RequirementDto> getRequirementsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<Requirement> requirements = requirementRepository.findByCategoryId(categoryId);

        return requirements.stream().map((post) -> mapToDTO(post))
                .collect(Collectors.toList());
    }

    // convert Entity into DTO
    private RequirementDto mapToDTO(Requirement requirement){
        RequirementDto requirementDto = mapper.map(requirement, RequirementDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return requirementDto;
    }

    // convert DTO to entity
    private Requirement mapToEntity(RequirementDto requirementDto){
        Requirement requirement = mapper.map(requirementDto, Requirement.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return requirement;
    }
}
