# 💻Front
## <HSU-CAPSTON>
⛳Sporty-UP⛳


## 💻 Developer

| 성명                                                 | 역할 및 담당업무                                                                                                        |
|----------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| <a href="https://github.com/vvan2"> 손주완 </a>   | **Frontend Developer**<br>- 
| <a href="https://github.com/Hwnsgus"> 황준현 </a>    | **Frontend Developer**<br>-                    |


### 👨‍💻GitHub Convention


**Category**  **TechStack** 
- Tool : Android Studio, flutter
- Language : kotlin
- Network : Retrofit, OkHttp, Gson 
- Service : Service 
- Asynchronous : Coroutines 
- Jetpack : DataBinding, Navigation 
- Image : Glide 

### [Branch]
     `main > develop > feat(issue)`
- 여기까지만 
     ``` 
     master: 라이브 서버에 제품으로 출시되는 브랜치.
     develop: 다음 출시 버전을 대비하여 개발하는 브랜치.
     feature: 추가 기능 개발 브랜치. develop 브랜치에 들어간다.
     release: 다음 버전 출시를 준비하는 브랜치. develop 브랜치를 release 브랜치로 옮긴 후 QA, 테스트를 진행하고 master 브랜치로 합친다.
     hotfix: master 브랜치에서 발생한 버그를 수정하는 브랜치.
     ```
---

### [Issue Convention]
   - 담당자(Assignees)를 명시할 것
   - Task list 기능을 적극 활용할 것
   - 기능 관련 Issue라면 GitHub Project와 PR과 연동하여 진행상황 공유
   ### issue template > branch > pr template > merge 식으로 issue 관리가능

---


### [Pull Request convention]
   - 제목은 '[#기능 번호] 변경 사항' 구조로 작성할 것
   - Issue와 연동할 것
      - 제목: **[Feat]** 핵심적인 부분만 간략하게 작성
   - 내용: 간결하게 리스트 방식으로 정리
   - 라벨: `FE`, `BE`, `기능추가`, `리팩토링`, `레이아웃`, `에러`

   > **자주 커밋하고 PR은 300자를 넘지 않도록 주의**  
   > (짧은 간격으로 자주 PR)

### [Commit convention]
**제목 타입**: <type>

- feat: 기능 (feature)
- fix: 버그 수정
- docs: 문서 작업 (documentation)
- style: 포맷팅, 세미콜론 누락 등.
- refactor: 리팩터링 : 결과의 변경 없이 코드의 구조를 재조정
- test: 테스트
- chore: 관리(maintain), 핵심 내용은 아닌 잡일 등

### [Code convention]

- 파일 소스명, package 통합
- 메서드 , 변수명 통합
- solid pattern 결정 후 구조화

### xml 작성시
### kotlin 작성시

>> 추가사항 계속 작성 요망

7. Android Studio, targetSDK, minSDK version 통일 
- Android Studio → 정해야됨
- targetSdk→ 34
- midSdk → 28
- jvmTarget = 1.8

8. IDE -> emulator or device 결정
- device 기준 pixel 고정 후 작업
---

