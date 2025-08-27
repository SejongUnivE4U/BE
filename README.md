
# 나만의 간편한 구강검진 E4U

<p align='center'>
    <img src="https://github.com/user-attachments/assets/6bf7a16e-cdc2-4297-9dfb-d7da8fdeee9b">
</p>

> E4U는 AI를 기반으로 간편하게 구강검진을 진행할 수 있는 서비스로, 치아 질환부터 치주질환까지 다양한 질환에 대한 진단을 받을 수 있습니다.

<div align=center>
    <img src="https://img.shields.io/badge/PyTorch-EE4C2C?style=flat&logo=PyTorch&logoColor=white">
    <img src="https://img.shields.io/badge/Python-3776AB?style=flat&logo=Python&logoColor=white">
    <img src="https://img.shields.io/badge/React-61DAFB?style=flat&logo=React&logoColor=white">
    <img src="https://img.shields.io/badge/FastAPI-009688?style=flat&logo=FastAPI&logoColor=white">
    <img src="https://img.shields.io/badge/spring-6DB33F?style=flat&logo=spring&logoColor=white">
</div>


## Table of content
- [Demo](#Demo)
- [Member](#Member)
- [Project TimeLine](#Timeline)
- [Project Background](#Background)
- [Service Arichitecture](#Service)
- [Modeling](#Modeling)
- [Development Environment](#dev_env)
- [Link](#link)

  

## Demo <a id='Demo'></a>
<div align="center">
    <img src="https://github.com/user-attachments/assets/99d8e17e-7bae-446c-8538-230f5a3d82a3" width="80%"></img>
</div>

- 다음 [링크](https://e4u-dev.netlify.app/)에서 직접 실행해보실 수 있습니다.
- 현재 모델 서버를 활용한 구강검진이 수월하지 않을 수 있습니다.



## Member <a id='Member'></a>

|이규섭|강은우|한경준|김해린|
|:--:|:--:|:--:|:--:|
|<a href='https://github.com/9sub'><img src='https://avatars.githubusercontent.com/u/113101019?v=4' width='100px'/></a>|<a href='https://github.com/euoonw'><img src='https://avatars.githubusercontent.com/u/80903685?v=4' width='100px'/></a>|<a href='https://github.com/hkjbrian'><img src='https://avatars.githubusercontent.com/u/63639963?v=4' width='100px'/></a>|<a href='https://github.com/jenny1zzang'><img src='https://avatars.githubusercontent.com/u/108577676?v=4' width='100px'/></a>|
|AI|Frontend|Backend|Backend|
|Modeling<br>Model Server|UI/UX Design<br>Implement FE|Implement BE<br>Serving|Implement BE<br>|


## Project TimeLine <a id='Timeline'></a>

<p align='center'>
    <img src='https://github.com/user-attachments/assets/7ca2d5a4-90a6-4dc6-bffb-96677f1bda1f', width="80%">
</p>


## Project Background <a id='Background'></a>

### 기획 의도 및 기대효과과

<p align='center'>
    <img src='https://github.com/user-attachments/assets/985cc3b5-f1ec-42e7-bee7-a19de0cc6a0e'>
</p>

- 배경 : 구강검진 수검률을 해마다 뒷걸음질 치며 조기에 질환을 발견하지 못해 비용과 시간이 과도하게 발생할 수 있는 확률이 높아졌습니다.
- 목적 : AI를 활용한 구강검진으로 조기진단이 가능하게 해 치료의 효율성을 높이고 비용을 절감해 줍니다. 구강건강에 대한 관심을 높여 구강건강 증진에 기여합니다.

---


## Service Architecture <a id='Service'></a>

<p align='center'>
    <img src="https://github.com/user-attachments/assets/afc26fc6-8367-4f73-9036-6dfc8a6feb50" width="80%">
</p>

- 웹 서버는 AWS를 통해 배포하였고 모델 서버는 로컬 컴퓨터 환경에서 호스팅 했습니다.
- 백엔드 서버는 프론트에서 받은 input 이미지를 모델서버로 보내게 되고, 이에 대한 각각의 결과를 전송합니다.


## Modeling <a id='Modeling'></a>


### Model Pipline

<p align='center'>
    <img src="https://github.com/user-attachments/assets/dde4d8b6-caed-4541-8276-cc6acf1a2111" width="80%">
</p>

모델 서버의 모델은 총 4개를 활용해 아래의 과정을 수행하게 됩니다.
1. Mouth Classification
    - 사용자가 입력한 이미지가 구강사진인지 판단합니다. 구강사진이라면 True를 아니라면 False를 반환하도록 해 구강사진일 경우에만 이후 과정을 수행할 수 있도록 했습니다.
2. Disease Detection
    - 구강내에 질환이 있는지 탐지합니다.
    - Text 모델에서 return된 가중치를 detection confidence 값에 적용해 더욱 정확한 질환 탐지를 가능하도록 했습니다.
3. Teeth Number Segmentation
    - 치아 경계를 segmentation해 치아 번호를 확인합니다.
    - Disease Detection에서 확인된 질환과 계산해 해당 질환이 어떤 위치에 있는지 확인합니다.
4. Text Processing
    - 사용자가 입력한 통증위치, 통증정도, 추가증상 텍스트를 분석해 예측한 질환이 Detection의 결과와 같다면 해당 위치의 질환에 가중치를 부여합니다.
    - Disease Detection 및 Teeth Number Segmentation 결과를 종합해 구강검진보고서를 출력합니다.
    - 통증정도, 예측된 질환 개수, 예측된 질환의 종류를 분석해 사용자의 구강점수를 정규화해 출력합니다.


### Train

#### Mouth Classification
- 구강 이미지 분류 모델로는 MobileNet V2를 사용해 학습했습니다.
- 구강 이미지는 보통 붉은색이 많아 보다 좋은 성능을 위해 구강이 아닌 사진을 붉은색으로 구성해 학습했습니다.

#### Disease Detection
- 구강질환 detection 모델은 [YOLOv11](https://docs.ultralytics.com/ko/models/yolo11/)를 사용했습니다.
- RGB 필터로 Red 채널의 이미지만을 뽑아 학습데이터에 추가하는 Data Augmentation을 진행해 학습했습니다.
- 탐지 가능한 질환의 종류는 총 11개로 "충치, 치석, 칸디다증, 구순포진, 치은염, 치주염, 치아 결손, 구강암, 구내염, 구강 편평태선, 치아변색" 입니다.

#### Teeth Number Segmentation
- 치아경계 segmentation 모델은 [YOLOv11-seg](https://docs.ultralytics.com/ko/tasks/segment/#models)를 사용했습니다.
- 치과임상에서 사용하는 FDI 명명법을 기준으로 라벨링된 데이터를 사용해 학습했습니다.

#### Text Model
- Text 모델은 ET5 모델을 사용했습니다.
- 학습시에 LoRA를 활용해 메모리 사용량과 계산량을 크게 줄이며 파인튜닝을 진행했습니다.


---

## Development environment <a id="dev_env">

Frontend와 Backend에 관한 내용은 [링크](https://github.com/SejongUnivE4U)에서 보실 수 있습니다.

- 언어 : Python
- 프레임워크 : FastAPI
- 모델 : YOLOv11, YOLOv11-seg, MobileNet V2, ET5
- 라이브러리 : [requirements.txt](https://github.com/9sub/E4U/blob/main/requirements.txt) 참조



## Link <a id="link">
- [종합설계보고서](https://github.com/user-attachments/files/18227384/2024-2._3.pdf)
