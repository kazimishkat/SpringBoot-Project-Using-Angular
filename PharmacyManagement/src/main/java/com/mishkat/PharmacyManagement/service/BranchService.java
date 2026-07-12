package com.mishkat.PharmacyManagement.service;

import com.mishkat.PharmacyManagement.dto.requestDTO.BranchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchResponseDto;

import java.util.List;

public interface BranchService {
    BranchResponseDto createBranch(BranchRequestDto dto);

    List<BranchResponseDto> getAllBranches();

    List<BranchResponseDto> getActiveBranches();

    BranchResponseDto getBranchById(Long id);

    BranchResponseDto getBranchByCode(String branchCode);

    BranchResponseDto updateBranch(Long id, BranchRequestDto dto);

    void deleteBranch(Long id);

    // Added for searching branches by name
    List<BranchResponseDto> searchBranchesByName(String name);
}