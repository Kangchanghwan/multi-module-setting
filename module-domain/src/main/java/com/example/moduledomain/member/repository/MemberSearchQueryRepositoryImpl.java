package com.example.moduledomain.member.repository;

import com.example.moduledomain.member.entity.Member;
import com.example.moduledomain.member.entity.QMember;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.moduledomain.member.entity.QMember.*;

@RequiredArgsConstructor
public class MemberSearchQueryRepositoryImpl implements MemberSearchQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Member> searchMember(String userId) {
    return Optional.ofNullable(
      queryFactory.select(member)
        .from(member)
        .where(member.userId.eq(userId)).fetchOne());
  }
}
