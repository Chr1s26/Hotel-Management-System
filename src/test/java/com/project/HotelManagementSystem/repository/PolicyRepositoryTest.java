package com.project.HotelManagementSystem.repository;

import com.project.HotelManagementSystem.entity.Policy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PolicyRepositoryTest {

    @Autowired
    private PolicyRepository policyRepository;

    @Test
    public void savePolicy_ReturnSavedPolicy() {
        Policy policy = new Policy();
        policy.setTitle("No Smoking");
        policy.setDescription("Smoking is strictly prohibited.");
        policy.setApplicableTo("All Rooms");

        Policy saved = policyRepository.save(policy);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void updatePolicy_ReturnUpdatedPolicy() {
        Policy policy = new Policy();
        policy.setTitle("Old Title");
        policy.setDescription("Old Description");
        policy.setApplicableTo("Everyone");

        Policy saved = policyRepository.save(policy);
        saved.setTitle("Updated Title");

        Policy updated = policyRepository.save(saved);

        Assertions.assertThat(updated.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    public void deletePolicyById_ReturnEmptyOptional() {
        Policy policy = new Policy();
        policy.setTitle("Policy");
        policy.setDescription("Desc");
        policy.setApplicableTo("Some");

        Policy saved = policyRepository.save(policy);

        policyRepository.deleteById(saved.getId());

        Optional<Policy> result = policyRepository.findById(saved.getId());
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void findAllPolicies_ReturnListOfPolicies() {
        Policy p1 = new Policy();
        p1.setTitle("Policy A");
        p1.setDescription("Description A");
        p1.setApplicableTo("Guests");

        Policy p2 = new Policy();
        p2.setTitle("Policy B");
        p2.setDescription("Description B");
        p2.setApplicableTo("Staff");

        policyRepository.save(p1);
        policyRepository.save(p2);

        List<Policy> policies = policyRepository.findAll();

        Assertions.assertThat(policies).isNotNull();
        Assertions.assertThat(policies.size()).isEqualTo(2);
    }

    @Test
    public void findPolicyById_ReturnPolicy() {
        Policy policy = new Policy();
        policy.setTitle("Refund Policy");
        policy.setDescription("Non-refundable");
        policy.setApplicableTo("All");

        Policy saved = policyRepository.save(policy);

        Optional<Policy> found = policyRepository.findById(saved.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getTitle()).isEqualTo("Refund Policy");
    }

    @Test
    public void findAllWithPagination_ReturnPagedResult() {
        policyRepository.save(new Policy(null, "P1", "D1", "All", null));
        policyRepository.save(new Policy(null, "P2", "D2", "All", null));
        policyRepository.save(new Policy(null, "P3", "D3", "All", null));

        Page<Policy> page = policyRepository.findAll(PageRequest.of(0, 2));

        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
    }
}
