package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.policy.PolicyCreateDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyResponse;
import com.project.HotelManagementSystem.dto.policy.PolicyUpdateDTO;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.entity.constants.StatusType;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public PolicyCreateDTO createPolicy(PolicyCreateDTO policyCreateDTO) {
        Optional<Policy> optionalPolicy = this.policyRepository.findByTitleAndDescription(policyCreateDTO.getTitle(), policyCreateDTO.getDescription());
        if (optionalPolicy.isPresent()) {
            throw new DuplicateException("policy",policyCreateDTO,"title","policies/create","An account with this title and description already exists");
        }
        Policy policy = modelMapper.map(policyCreateDTO, Policy.class);
        policy.setCreatedAt(LocalDateTime.now());
        policy.setStatus(StatusType.ACTIVE);
        policy.setCreatedBy(authService.getCurrentUser());
        Policy savedPolicy = this.policyRepository.save(policy);
        return modelMapper.map(savedPolicy,PolicyCreateDTO.class);
    }

    public PolicyUpdateDTO updatePolicy(Long id, PolicyUpdateDTO policyUpdateDTO) {
        Optional<Policy> optionalPolicy = this.policyRepository.findByTitleAndDescriptionAndIdNot(policyUpdateDTO.getTitle(), policyUpdateDTO.getDescription(),policyUpdateDTO.getId());
        if (optionalPolicy.isPresent()) {
            throw new DuplicateException("policy",policyUpdateDTO,"title","policies/edit","An account with this title and description already exists");
        }
        Policy policyOp = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("policy",policyUpdateDTO,"id","policies/edit","An account with this id cannot be found"));
        Policy policy = modelMapper.map(policyUpdateDTO, Policy.class);
        policyOp.setTitle(policy.getTitle());
        policyOp.setDescription(policy.getDescription());
        policyOp.setApplicableTo(policy.getApplicableTo());
        policyOp.setStatus(StatusType.ACTIVE);
        policyOp.setUpdatedAt(LocalDateTime.now());
        policyOp.setUpdatedBy(authService.getCurrentUser());
        Policy savedPolicy = policyRepository.save(policyOp);
        return modelMapper.map(savedPolicy,PolicyUpdateDTO.class);
    }

    public void deletePolicy(Long id) {
        Optional<Policy> policyOp = policyRepository.findById(id);
        if (policyOp.isEmpty()) {
            throw new ResourceNotFoundException("policy",policyOp,"id","policies","An account with this id cannot be found");
        }
        policyRepository.deleteById(id);
    }

    public PolicyDTO findPolicyById(Long id) {
        Optional<Policy> policyOp = policyRepository.findById(id);
        if (policyOp.isEmpty()) {
            throw new ResourceNotFoundException("policy",policyOp,"id","policies","An account with this id cannot be found");
        }
        return toDTO(policyOp.get());
    }

    public PolicyResponse findAllPoliciesWithPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndSortOrder);
        Page<Policy> page = policyRepository.findAll(pageable);
        List<PolicyDTO> policyDTOs = page.getContent().stream().map(p -> modelMapper.map(p, PolicyDTO.class)).toList();
        PolicyResponse policyResponse = new PolicyResponse();
        policyResponse.setPolicies(policyDTOs);
        policyResponse.setPageNumber(page.getNumber());
        policyResponse.setPageSize(page.getSize());
        policyResponse.setTotalElements(page.getTotalElements());
        policyResponse.setTotalPages(page.getTotalPages());
        policyResponse.setLastPage(page.isLast());
        return policyResponse;
    }

    public List<PolicyDTO> findAllPolicies() {
        List<Policy> policies = policyRepository.findAll();
        return policies.stream().map(p -> modelMapper.map(p, PolicyDTO.class)).collect(Collectors.toList());
    }

    private PolicyDTO toDTO(Policy policy) {
        PolicyDTO policyDTO = new  PolicyDTO();
        policyDTO.setId(policy.getId());
        policyDTO.setTitle(policy.getTitle());
        policyDTO.setDescription(policy.getDescription());
        policyDTO.setApplicableTo(policy.getApplicableTo());
        policyDTO.setStatus(policy.getStatus());
        policyDTO.setCreatedAt(policy.getCreatedAt());
        policyDTO.setUpdatedAt(policy.getUpdatedAt());
        policyDTO.setCreatedBy(policy.getCreatedBy());
        policyDTO.setUpdatedBy(policy.getUpdatedBy());
        return policyDTO;
    }
}