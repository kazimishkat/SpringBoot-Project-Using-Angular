package com.mishkat.PharmacyManagement.serviceImp;

import com.mishkat.PharmacyManagement.dto.mapper.BranchMapper;
import com.mishkat.PharmacyManagement.dto.requestDTO.BranchRequestDto;
import com.mishkat.PharmacyManagement.dto.responseDTO.BranchResponseDto;
import com.mishkat.PharmacyManagement.entity.Branch;
import com.mishkat.PharmacyManagement.repository.BranchRepository;
import com.mishkat.PharmacyManagement.service.BranchService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public BranchResponseDto createBranch(BranchRequestDto dto) {
        // নতুন ব্রাঞ্চ তৈরির আগে কোড ইউনিক কি না তা যাচাই করা
        if (branchRepository.findByBranchCode(dto.getBranchCode()).isPresent()) {
            throw new RuntimeException("Branch code already exists: " + dto.getBranchCode());
        }

        Branch branch = BranchMapper.toEntity(dto);
        // নতুন ব্রাঞ্চ বাই ডিফল্ট অ্যাকটিভ থাকবে
        branch.setIsActive(true);

        Branch savedBranch = branchRepository.save(branch);
        return BranchMapper.toDTO(savedBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponseDto> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(BranchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponseDto> getActiveBranches() {
        return branchRepository.findByIsActiveTrue().stream()
                .map(BranchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponseDto getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        return BranchMapper.toDTO(branch);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponseDto getBranchByCode(String branchCode) {
        Branch branch = branchRepository.findByBranchCode(branchCode)
                .orElseThrow(() -> new RuntimeException("Branch not found with code: " + branchCode));
        return BranchMapper.toDTO(branch);
    }

    @Override
    @Transactional
    public BranchResponseDto updateBranch(Long id, BranchRequestDto dto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

        // ব্রাঞ্চ কোড আপডেট করার সময় সেটি অন্য কোনো ব্রাঞ্চের সাথে মিলে যায় কি না তা চেক করা
        if (dto.getBranchCode() != null && !dto.getBranchCode().equals(branch.getBranchCode())) {
            if (branchRepository.findByBranchCode(dto.getBranchCode()).isPresent()) {
                throw new RuntimeException("Branch code already exists: " + dto.getBranchCode());
            }
        }

        BranchMapper.updateEntity(branch, dto);
        Branch savedBranch = branchRepository.save(branch);
        return BranchMapper.toDTO(savedBranch);
    }

    @Override
    @Transactional
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        branchRepository.delete(branch);
    }
}
