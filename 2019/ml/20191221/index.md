# intro.

20191221

# 1. Linear Regression

argmin, argmax: 

memo:

## 1.1 Least Square props & cons

cons: 수식적으로 풀 수 없거나 난해한 경우, 경험적으로 찾는 것도 가능 -> 뉴럴넷

## 1.2. Gradient Descent

parameter를 미분에 기반하여 찾음(sign of loss)

gd -> trial and error

최소제곱법을 쓰는 이유? 제곱을 하면 에러의 폭이 큰데, 에러의 폭이 커지면 하강을 더 많이 할 지 적게 할 지 결정하기 쉬움.

learning rate가 작으면 학습 속도가 느려짐, 그러나 커지면 explosion 발생할 가능성이 높음
-> 그런 이유로 충분히 작은 값들을 사용함.
(최저점 지나갈 가능성 존재)

## 1.2.1 adaptive gradient descent

naive한 방법이 아닌 이전의 변위를 합산해 하강 rate를 결정해 찾아가는 방법

## 1.2.2 결론

결정해를 도출 할 수 없는(비선형 미분 방정식 같은) 식을 경험적으로 찾기 위한 방법

- mini ei^2 => 학습 품질
- 학습법 (LS, GD)

# 2. MLE(Maximum Likelihood Estimation)

# 3. NLL

a, b와 연관 관계가 없기 때문에, 한 axis를 고정하고 각각의 optima를 구하는 것들이 이 방법들임.

하지만 a와 b가 관계가 있는, ab같은 것이 bilinear라고 말함.  

# 4. Loss, regularization cost

Loss: 식을 잘 맞췄냐
Regularization cost: 

fat data: 변수가 data보다 많음

Evaluation: table 오탈자 존재

# Why we use RMSE loss instead MAP

MAP은 절대값이라 최저점 근처에서 속도 저하가 없음

