package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    public Policy updatePolicy(Long id,Policy policy) {
        Optional<Policy> policyOp = policyRepository.findById(id);
        if(policyOp.isPresent()) {
            Policy updatedPolicy = policyOp.get();
            updatedPolicy.setTitle(policy.getTitle());
            updatedPolicy.setDescription(policy.getDescription());
            updatedPolicy.setApplicableTo(policy.getApplicableTo());
            policyRepository.save(updatedPolicy);
            return updatedPolicy;
        }
        return null;
    }

    public void deletePolicy(Long id) {
        Optional<Policy> policyOp = policyRepository.findById(id);
        if(policyOp.isPresent()) {
            policyRepository.deleteById(id);
        }
    }

    public Policy findPolicyById(Long id) {
        return policyRepository.findById(id).orElse(null);
    }

    public List<Policy> findAllPolicies() {
        return policyRepository.findAll();
    }
}