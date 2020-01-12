# # Intro.

전처리와 특징 추출에 대해 정리한다.  

linearly inseparable
-> 선형적으로 분리 불가능한 것

좋은 벡터를 만들기 위한 것 -> 머신러닝의 핵심

### 17

• centering : 𝑋𝑗 ← 𝑋𝑗 − 𝑚𝑒𝑎𝑛(𝑋𝑗)
• Standardization : 𝑋𝑗 ← 𝑋𝑗−𝑚𝑒𝑎𝑛(𝑋𝑗)
𝜎𝑋𝑗
  • min-max scaling : 특정 범위내에 값이 존재하도록 특정합니다.

[0, 1] 해석력 좋음
[-1, 1] 내적 기반 벡터에 유리

### 28 

PCA의 가정
zero centered vector

SVD는 matrix decomposition임

직교 Ui

diagonal -> 분산 정도


### 36

내적 값이 유사도

linear kernel


### 39 

two moon?

kernel은 두 벡터의 유사도를 측정하는 score하는 함수라고 생각하면 "편함"

### 42

SNE

임베딩

고차원 공간을 축소한 공간에서도 클러스터들끼리 붙어 있길 원하는 경우

q는? 

### 43

B -> 어느 정도 유사한지 결정할 수 있는 파라미터
베타가 커지면 조금만 떨어져도 값이 뚝떨어짐


```text
$ pip install umap-learn
```