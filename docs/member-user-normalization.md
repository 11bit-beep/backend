# Member–User 정규화 배포

이 변경은 `members.username`, `members.password`, `members.role`을 제거하고
`members.user_id`로 `users`를 참조합니다. 기존 `User`의 비밀번호·역할과 출석 기록은
그대로 둡니다. 이전 `MemberService.signUp`으로 생성된 Member에 User가 없으면
기존 비밀번호 해시를 사용해 학생 User를 생성합니다.

1. 애플리케이션 쓰기를 중지하고 MySQL 데이터베이스를 백업합니다.
2. [`db/manual/2026-10-27-member-user-preflight.sql`](../db/manual/2026-10-27-member-user-preflight.sql)을 실행합니다. 첫 번째 결과에 행이 있으면 역할 충돌을 해결한 뒤 진행합니다. 두 번째 결과의 Member-only 계정은 새 학생 User로 전환될 목록입니다.
3. [`db/manual/2026-10-27-member-user-normalization.sql`](../db/manual/2026-10-27-member-user-normalization.sql)을 **한 문장씩** 실행합니다. `unlinked_members`가 0인지 확인한 뒤 제약 조건과 중복 열 제거를 실행합니다. MySQL DDL은 자동 커밋되므로 오류가 나면 다음 문장을 실행하지 말고 백업에서 복구합니다.
4. 새 애플리케이션을 배포하고 회원가입, 내 정보 조회, 출석 조회, 탈퇴를 확인합니다.

새로 만든 빈 H2 테스트 데이터베이스는 JPA가 정규화된 스키마를 생성하므로 이
MySQL 이전 SQL을 실행하지 않습니다.
