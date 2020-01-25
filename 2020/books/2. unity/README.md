# Intro.

만드는데 집중하다 보니 내용 요약이라도 간단하게 해야할거 같아서.

# Chap 6.

- material은 game object surface color 결정
- Camera의 Clear Flags 변경하여 게임 배경 변경 가능
  - Skybox -> Solid Color
- Update()
  - 아두이노의 그것과 비슷한 느낌 ㅋㅋㅋㅋㅋ
- gameObject는 자신의 게임 오브젝트를 의미(this같은)
- GetComponent<?>() 메소드는 원하는 타입의 컴포넌트를 찾아옴
- Vector 3
  - HLSL 떠올려보자
- RigidBody는 axis에 대한 constraints 제어 가능
- velocity라는 속도 관련 변수가 존제
- `Input.GetKey()` 키 입력 감지
- `Input.GetAxis()` 축에 대해 매핑된 입력 감지
  - 값 구간은 [-1.0, 1.0]
  - 왜냐하면 여러 입력 기기(ex: 조이스틱)에 대응해야해서
  - 이 덕분에 멀티 플랫폼 대응에 유리

# Chap 7.

- Prefab으로 bullet과 같은 오브젝트 재사용 가능
- RigidBody.UseGravity를 끄면 중력 영향 안받음
- trigger collider는 물리적 작용은 실제로 하지 않고 충돌만 detection
- 오브젝트 충돌은 태그로 결정
- OnTriggerEnter(), OnCollisionEnter() 메소드를 오버라이드 하면 충돌 감지 가능
- OnCollisionEnter() 일반 충돌
- OnTriggerEnter() 트리거 충돌
- Instantiate()로 오브젝트 복사

# Chap 8.

- 프레임 고정에 맞춰 게임 로직을 짜면 안됨
  - 클라이언트 성능별로 다른 결과가 나옴 엌ㅋㅋ
- 여긴 UI도 게임 오브젝트네
- PlayerPrefs는 Set 같은 녀석임
- `SceneManager.LoadScene()`은 씬 입력을 받아서 씬을 로딩한다
  - 초기화에 쓰기 좋음
