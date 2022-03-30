## JOIN이란 

데이터베이스 내에서 2개 이상의 테이블을 합쳐 SELECT하는 방법 중 하나   



### 주의사항

- 테이블을 **JOIN하려면 최소 1개이상의 같은 컬럼을 가지고 있어야 한다.** 

- 2개 이상의 테이블을 JOIN하기 때문에, 모든 컬럼의 참조는 명확하게 사용해야한다. 

  - ```
    SELECT A.COL
    FROM A INNER JOIN B
    ON A.KEY = B.KEY;
    ```

- 어떤 JOIN인지 명시하지 않는다면 DEFAULT로 INNER JOIN을 사용한다. 

***첫 기준이 되는 테이블이 뭐냐에 따라 성능이 달라지며 조인은 외래키를 통해 이루어진다.***   

***일반적으로 INNER JOIN은 교집합만 나타내기때문에 OUTER JOIN보다 성능이 더 좋다!***   



1. INNER JOIN : 내부 조인 -> 교집합
2. LEFT/RIGHT JOIN -> 부분집합
3. OUTER JOIN : 외부 조인 -> 합집합 ( MYSQL은 없어서 LEFT+RIGHT)

**TABLE A**

| ID   | ENAME |
| ---- | ----- |
| 1    | AAA   |
| 2    | BBB   |
| 3    | CCC   |

**TABLE B**

| ID   | KNAME |
| ---- | ----- |
| 1    | 가    |
| 2    | 나    |
| 4    | 라    |
| 5    | 마    |



### INNER JOIN

- 내부 조인을 말한다.

- A와 B의 교집합(A값 전체와 B테이블의 Key값이 같은 것들을 가져옴)

- ```
  SELECT A.ID, A.ENAME, A.KNAME
  FROM A INNER JOIN B
  ON A.ID = B.ID 
  ```

| ID   | ENAME | KNAME |
| ---- | ----- | ----- |
| 1    | AAA   | 가    |
| 2    | BBB   | 나    |



### LEFT JOIN

- 왼쪽 조인 

- 조인 기준 왼쪽에 있는건 다 SELECT됨 ( 공통 + LEFT )

- ```
  SELECT A.ID , A.ENAME, A.KNAME
  FROM A LEFT OUTER JOIN B
  ON A.ID = B.ID
  ```

| ID   | ENAME | KNAME |
| ---- | ----- | ----- |
| 1    | AAA   | 가    |
| 2    | BBB   | 나    |
| 3    | CCC   | NULL  |



- 조인 기준 왼쪽에 있는거 '만' SELECT (A-B)

- LEFT가 가지고 있는 것 중 공통적인 부분을 제외한 LEFT JOIN값 중에서 WHERE조건으로 NULL인 값을 조회하는것

- ```
  SELECT A.ID, A.ENAME, A.KNAME
  FROM A LEFT OUTER JOIN B
  ON A.ID = B.ID;
  WHERE B.ID IS NULL
  ```

| ID   | ENAME | KNAME |
| ---- | ----- | ----- |
| 3    | CCC   | NULL  |



### RIGHT JOIN

- 오른쪽 기준

- 조인 기준 오른쪽에 있는거 다 SELECT됨

- ```
  SELECT A.ID, A.ENAME, A.KNAME
  FROM A RIGHT OUTHER JOIN B
  ON A.ID = B.ID
  ```

| ID   | ENAME | KNAME |
| ---- | ----- | ----- |
| 1    | AAA   | 가    |
| 2    | BBB   | 나    |
| 4    | NULL  | 라    |
| 5    | NULL  | 마    |



### FULL OUTER JOIN

- MYSQL은 FULL OUTER JOIN을 지원하지 않음

- ```
  SELECT *
  FROM A LEFT JOIN B
  UNION
  SELECT *
  FROM A RIGHT JOIN B
  이런식으로 UNION 사용 
  ```



- 출처
  - https://pearlluck.tistory.com/46



## Spring JPQL에서의 FETCH JOIN

이건 SQL 조인이 아니고 JPQL에서 성능 최적화를 위해 제공하는 기능  

연관된 엔티티나 컬렉션을 SQL 한번에 조회하는 기능 

```sql
select m from Member m join fetch m.team
```

이것은 SQL로 번역됨

```sql
SELECT M.*, T.* FROM MEMBER M
INNER JOIN TEAM T ON M.TEAM=T.ID
```



또한 1:N에서 JOIN은 데이터가 중복될 수 있다. 팀 A입장에서는 1개지만 멤버가 2명이어서 2개의 row를 가져올 수있다.   

![스크린샷 2021-01-04 오후 5 30 06](https://user-images.githubusercontent.com/43809168/103515686-86582180-4eb2-11eb-819e-5d576e83c9af.png)  

### Fetch join + Distinct

```sql
-- JPQL에서 제공 
select distinct t From Team t join fetch t.members;
```

