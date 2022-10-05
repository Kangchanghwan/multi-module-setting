package com.example.moduledomain.member.repository;


import com.example.moduledomain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberSearchQueryRepository {
  Optional<Member> findByUserId(String userId);
}
