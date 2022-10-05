package com.example.moduledomain.member.repository;

import com.example.moduledomain.member.entity.Member;

import java.util.Optional;

public interface MemberSearchQueryRepository {

  Optional<Member> searchMember(String name);
}
