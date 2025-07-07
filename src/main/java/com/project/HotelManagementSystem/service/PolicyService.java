package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.policy.PolicyCreateDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyDTO;
import com.project.HotelManagementSystem.dto.policy.PolicyResponse;
import com.project.HotelManagementSystem.dto.policy.PolicyUpdateDTO;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
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
    private final ModelMapper modelMapper;

    public PolicyCreateDTO createPolicy(PolicyCreateDTO policyCreateDTO) {
        Optional<Policy> optionalPolicy = this.policyRepository.findByTitleAndDescription(policyCreateDTO.getTitle(), policyCreateDTO.getDescription());
        if (optionalPolicy.isPresent()) {
            throw new DuplicateException("Policy with title " + policyCreateDTO.getTitle() + " And same description already exists");
        }
        Policy policy = modelMapper.map(policyCreateDTO, Policy.class);
        Policy savedPolicy = this.policyRepository.save(policy);
        return modelMapper.map(savedPolicy,PolicyCreateDTO.class);
    }

    public PolicyUpdateDTO updatePolicy(Long id, PolicyUpdateDTO policyUpdateDTO) {
        Optional<Policy> optionalPolicy = this.policyRepository.findByTitleAndDescription(policyUpdateDTO.getTitle(), policyUpdateDTO.getDescription());
        if (optionalPolicy.isPresent()) {
            throw new DuplicateException("Policy with title " + policyUpdateDTO.getTitle() + " And same description already exists");
        }
        Policy policyOp = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
        Policy policy = modelMapper.map(policyUpdateDTO, Policy.class);
        policyOp.setTitle(policy.getTitle());
        policyOp.setDescription(policy.getDescription());
        policyOp.setApplicableTo(policy.getApplicableTo());
        Policy savedPolicy = policyRepository.save(policyOp);
        return modelMapper.map(savedPolicy,PolicyUpdateDTO.class);
    }

    public void deletePolicy(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
        policyRepository.deleteById(id);
    }

    public PolicyDTO findPolicyById(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
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