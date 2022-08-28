package edu.self.practice.member.repository;

import edu.self.practice.member.constant.Gender;
import edu.self.practice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    List<Member> findMembersByGender(Gender gender);
}
