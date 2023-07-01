package com.springboot.blog.controller;

import com.springboot.blog.payload.RequirementDto;
import com.springboot.blog.payload.RequirementResponse;
import com.springboot.blog.service.RequirementService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@Tag(
        name = "CRUD REST APIs for Requirement Resource"
)
public class RequirementController {

    private RequirementService requirementService;

    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @Operation(
            summary = "Create Requirement REST API",
            description = "Create Requirement REST API is used to save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    // create blog post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RequirementDto> createRequirement(@Valid @RequestBody RequirementDto requirementDto){
        return new ResponseEntity<>(requirementService.createReqiurement(requirementDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Requirements REST API",
            description = "Get All Requirements REST API is used to fetch all the posts from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // get all posts rest api
    @GetMapping
    public RequirementResponse getAllRequirements(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return requirementService.getAllRequirements(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(
            summary = "Get Requirement By Id REST API",
            description = "Get Requirement By Id REST API is used to get single post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // get post by id
    @GetMapping("/{id}")
    public ResponseEntity<RequirementDto> getRequirementById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(requirementService.getRequirementById(id));
    }

    @Operation(
            summary = "update Requirement REST API",
            description = "Update Requirement REST API is used to update a particular post in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    // update post by id rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RequirementDto> updateRequirement(@Valid @RequestBody RequirementDto requirementDto, @PathVariable(name = "id") long id){

       RequirementDto reqResponse = requirementService.updateRequirement(requirementDto, id);

       return new ResponseEntity<>(reqResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Requirement REST API",
            description = "Delete Requirement REST API is used to delete a particular post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    // delete post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequirement(@PathVariable(name = "id") long id){

        requirementService.deleteRequirementById(id);

        return new ResponseEntity<>("Requirement entity deleted successfully.", HttpStatus.OK);
    }

    // Build Get Posts by Category REST API
    // http://localhost:8080/api/posts/category/3
    @GetMapping("/category/{id}")
    public ResponseEntity<List<RequirementDto>> getRequirementByCategory(@PathVariable("id") Long categoryId){
        List<RequirementDto> requirementDtos = requirementService.getRequirementsByCategory(categoryId);
        return ResponseEntity.ok(requirementDtos);
    }
}
