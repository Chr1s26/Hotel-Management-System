package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.policy.PolicyDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyResponse;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PolicyDTO createPolicy(Policy policy) {
        Policy savedPolicy = this.policyRepository.save(policy);
        return modelMapper.map(savedPolicy,PolicyDTO.class);
    }

    public PolicyDTO updatePolicy(Long id,Policy policy) {
        Optional<Policy> policyOp = policyRepository.findById(id);
        if(policyOp.isPresent()) {
            Policy updatedPolicy = policyOp.get();
            updatedPolicy.setTitle(policy.getTitle());
            updatedPolicy.setDescription(policy.getDescription());
            updatedPolicy.setApplicableTo(policy.getApplicableTo());
            Policy savedPolicy = policyRepository.save(updatedPolicy);
            return modelMapper.map(savedPolicy,PolicyDTO.class);
        }
        return null;
    }

    public void deletePolicy(Long id) {
        Optional<Policy> policyOp = policyRepository.findById(id);
        if(policyOp.isPresent()) {
            policyRepository.deleteById(id);
        }
    }

    public PolicyDTO findPolicyById(Long id) {
        Policy policy = this.policyRepository.findById(id).get();
        return modelMapper.map(policy,PolicyDTO.class);
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
}