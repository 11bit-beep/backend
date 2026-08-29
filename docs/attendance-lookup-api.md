# 반별 / 실별 출석 조회 API

학생 명단(`members`)을 기준으로 로그인 사용자(`users`)와 출석 기록(`attendance`)을 `LEFT JOIN`한다. 따라서 조회 날짜에 출석 행이 없는 학생도 누락되지 않고 `ABSENT`로 반환된다.

현재 `members`와 `users` 사이에 외래 키가 없으므로 두 테이블의 고유한 `username`을 연결 키로 사용한다.

## 출석 상태

| 상태 | 판정 기준 |
| --- | --- |
| `ABSENT` | 조회 조건에 맞는 출석 기록 없음 |
| `CHECKED_IN` | 출석했고 `checkOutAt`이 없음 |
| `CHECKED_OUT` | 출석했고 `checkOutAt`이 있음 |

같은 학생에게 같은 날짜의 출석 기록이 여러 건 있으면 `checkInAt`이 가장 최신인 한 건만 반환한다.

## 반별 조회

```http
GET /api/attendance/classes/{grade}/{studentClass}?date=2026-08-29
```

- `date`는 선택값이며 생략하면 서버의 오늘 날짜를 사용한다.
- 지정한 학년/반의 전체 학생이 학번 순서로 반환된다.

## 실별 조회

```http
GET /api/attendance/places/{place}?grade=1&studentClass=2&date=2026-08-29
```

- `grade`, `studentClass`는 예상 출석 명단의 범위를 정하는 필수값이다.
- `date`는 선택값이며 생략하면 서버의 오늘 날짜를 사용한다.
- 지정한 학년/반 학생 중 해당 `place`에 출석한 기록만 출석으로 판정한다. 다른 실에 출석한 학생은 이 실의 조회에서는 `ABSENT`다.
- 공백이나 한글이 포함된 `place`는 URL 인코딩해야 한다.

## 응답 예시

```json
{
  "date": "2026-08-29",
  "scope": "CLASS",
  "totalCount": 2,
  "attendedCount": 1,
  "absentCount": 1,
  "students": [
    {
      "memberId": 1,
      "name": "홍길동",
      "grade": 1,
      "studentClass": 2,
      "number": 1,
      "status": "CHECKED_IN",
      "checkInAt": "2026-08-29T08:10:00",
      "checkOutAt": null,
      "type": "NORMAL",
      "place": "LAB-1"
    },
    {
      "memberId": 2,
      "name": "김학생",
      "grade": 1,
      "studentClass": 2,
      "number": 2,
      "status": "ABSENT",
      "checkInAt": null,
      "checkOutAt": null,
      "type": null,
      "place": null
    }
  ]
}
```
