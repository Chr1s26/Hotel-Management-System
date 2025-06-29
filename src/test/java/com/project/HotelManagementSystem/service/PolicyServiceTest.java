package com.project.HotelManagementSystem.service;

import com.project.HotelManagementSystem.dto.policy.*;
import com.project.HotelManagementSystem.entity.Policy;
import com.project.HotelManagementSystem.exception.DuplicateException;
import com.project.HotelManagementSystem.exception.ResourceNotFoundException;
import com.project.HotelManagementSystem.repository.PolicyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PolicyService policyService;

    @Test
    void testCreatePolicy_Success() {
        PolicyCreateDTO createDTO = new PolicyCreateDTO(null, "Refund", "Refundable within 24h", "Customer");

        Policy mappedPolicy = new Policy();
        mappedPolicy.setTitle("Refund");
        mappedPolicy.setDescription("Refundable within 24h");
        mappedPolicy.setApplicableTo("Customer");

        Policy savedPolicy = new Policy();
        savedPolicy.setId(1L);
        savedPolicy.setTitle("Refund");
        savedPolicy.setDescription("Refundable within 24h");
        savedPolicy.setApplicableTo("Customer");

        PolicyCreateDTO resultDTO = new PolicyCreateDTO(1L, "Refund", "Refundable within 24h", "Customer");

        when(policyRepository.findByTitleAndDescription("Refund", "Refundable within 24h")).thenReturn(Optional.empty());
        when(modelMapper.map(createDTO, Policy.class)).thenReturn(mappedPolicy);
        when(policyRepository.save(mappedPolicy)).thenReturn(savedPolicy);
        when(modelMapper.map(savedPolicy, PolicyCreateDTO.class)).thenReturn(resultDTO);

        PolicyCreateDTO result = policyService.createPolicy(createDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Refund", result.getTitle());
    }

    @Test
    void testCreatePolicy_Duplicate() {
        PolicyCreateDTO dto = new PolicyCreateDTO(null, "Refund", "Refundable within 24h", "Customer");
        Policy existing = new Policy();

        when(policyRepository.findByTitleAndDescription(dto.getTitle(), dto.getDescription())).thenReturn(Optional.of(existing));

        Assertions.assertThrows(DuplicateException.class, () -> policyService.createPolicy(dto));
    }

    @Test
    void testUpdatePolicy_Success() {
        Long id = 1L;
        PolicyUpdateDTO updateDTO = new PolicyUpdateDTO(id, "Refund", "Refundable within 24h", "Customer");

        Policy existing = new Policy();
        existing.setId(id);
        existing.setTitle("Old");
        existing.setDescription("Old desc");
        existing.setApplicableTo("All");

        Policy mapped = new Policy();
        mapped.setTitle("Refund");
        mapped.setDescription("Refundable within 24h");
        mapped.setApplicableTo("Customer");

        Policy saved = new Policy();
        saved.setId(id);
        saved.setTitle("Refund");
        saved.setDescription("Refundable within 24h");
        saved.setApplicableTo("Customer");

        PolicyUpdateDTO expectedDTO = new PolicyUpdateDTO(id, "Refund", "Refundable within 24h", "Customer");

        when(policyRepository.findByTitleAndDescription(updateDTO.getTitle(), updateDTO.getDescription())).thenReturn(Optional.empty());
        when(policyRepository.findById(id)).thenReturn(Optional.of(existing));
        when(modelMapper.map(updateDTO, Policy.class)).thenReturn(mapped);
        when(policyRepository.save(existing)).thenReturn(saved);
        when(modelMapper.map(saved, PolicyUpdateDTO.class)).thenReturn(expectedDTO);

        PolicyUpdateDTO result = policyService.updatePolicy(id, updateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Refund", result.getTitle());
    }

    @Test
    void testUpdatePolicy_Duplicate() {
        Long id = 1L;
        PolicyUpdateDTO updateDTO = new PolicyUpdateDTO(id, "Refund", "Refundable within 24h", "Customer");

        Policy duplicate = new Policy();
        duplicate.setId(2L);

        when(policyRepository.findByTitleAndDescription(updateDTO.getTitle(), updateDTO.getDescription())).thenReturn(Optional.of(duplicate));

        Assertions.assertThrows(DuplicateException.class, () -> policyService.updatePolicy(id, updateDTO));
    }

    @Test
    void testUpdatePolicy_NotFound() {
        Long id = 1L;
        PolicyUpdateDTO updateDTO = new PolicyUpdateDTO(id, "Refund", "Refundable within 24h", "Customer");

        when(policyRepository.findByTitleAndDescription(updateDTO.getTitle(), updateDTO.getDescription())).thenReturn(Optional.empty());
        when(policyRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> policyService.updatePolicy(id, updateDTO));
    }

    @Test
    void testDeletePolicy_Success() {
        Long id = 1L;
        Policy existing = new Policy();
        existing.setId(id);

        when(policyRepository.findById(id)).thenReturn(Optional.of(existing));

        policyService.deletePolicy(id);

        verify(policyRepository).deleteById(id);
    }

    @Test
    void testDeletePolicy_NotFound() {
        Long id = 1L;

        when(policyRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> policyService.deletePolicy(id));
    }

    @Test
    void testFindPolicyById_Success() {
        Long id = 1L;
        Policy policy = new Policy();
        policy.setId(id);
        policy.setTitle("Refund");

        PolicyDTO dto = new PolicyDTO();
        dto.setTitle("Refund");

        when(policyRepository.findById(id)).thenReturn(Optional.of(policy));
        when(modelMapper.map(policy, PolicyDTO.class)).thenReturn(dto);

        PolicyDTO result = policyService.findPolicyById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Refund", result.getTitle());
    }

    @Test
    void testFindAllPolicies_Success() {
        List<Policy> policies = Arrays.asList(new Policy(), new Policy());

        when(policyRepository.findAll()).thenReturn(policies);
        when(modelMapper.map(any(Policy.class), eq(PolicyDTO.class))).thenReturn(new PolicyDTO());

        List<PolicyDTO> result = policyService.findAllPolicies();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testFindAllPoliciesWithPagination_Success() {
        List<Policy> policies = Arrays.asList(new Policy(), new Policy());
        Page<Policy> page = new PageImpl<>(policies);

        when(policyRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Policy.class), eq(PolicyDTO.class))).thenReturn(new PolicyDTO());

        PolicyResponse response = policyService.findAllPoliciesWithPagination(0, 10, "id", "asc");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getPolicies().size());
    }
}
