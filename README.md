# TODAK

'마인드카페'앱, '모씨'를 모티브로 한 'TODAK'이라는 고민상담 SNS 앱입니다. (서버와 연동은 안 되어 있습니다.)
<br> 사람들끼리 고민을 털어놓고 서로 소통하는 공간을 만들고자 이 앱을 만들게 되었습니다.

# 사용 기술
* Language : JAVA
* OS : Android
* Tool : Android studio

# 주요 기능
1. 로그인 & 회원가입
2. 고민 글 목록보기 & 글 상세보기
3. 고민 글 등록, 수정, 삭제
3. 댓글 기능
4. 좋아요 기능

## 1. 로그인 & 회원가입
* "회원목록" Shared Preference를 사용한 회원가입과 로그인.
* 회원정보는 이메일이 중복되지 않게, 이메일중복확인 시 바로 검색할 수 있게 key는 이메일 주소, value에는 이메일 주소, 닉네임, 비밀번호, 유저번호를 저장함.
* 유저번호가 겹치지 않게 하기 위해 key는 ID, value는 유저번호로도 저장함.
* 지금 보니 유저번호를 따로 만들 필요 없이, 이메일 주소로 유저를 구분해도 되었을 듯함.
* 그리고 닉네임도 중복되지 않게, key에 '이메일 주소+닉네임'으로 저장해두었어야 함.

<p><img src="https://user-images.githubusercontent.com/49344118/57299968-d183c500-7110-11e9-87d4-0da9681c75e0.gif" height="550">
<img src="https://user-images.githubusercontent.com/49344118/57196446-e7ba4580-6f97-11e9-8724-e923343862e6.gif" height="550"></p>


***
## 2. 고민 글 목록보기 & 글 상세보기
* 리스트뷰로 고민 글 목록 구현.
* 인텐트로 선택된 글의 상세보기화면으로 이동.
<img src="https://user-images.githubusercontent.com/49344118/57196447-e852dc00-6f97-11e9-9e55-20edc4616bce.gif" height="550">

***
## 3. 고민 글 등록, 수정, 삭제(자신의 글만 수정/삭제 가능, 닉네임 보이지 않기/보이기 선택 가능, 배경화면 선택 가능)
* 글 등록은 메인화면에서, 글 수정과 삭제는 글 상세보기화면에서 가능함.
* "고민목록" Shared Preference를 사용한 글 등록, 수정, 삭제.
* 만들 당시에, 인텐트를 많이 사용해보고자 인텐트를 주로 씀.

### 등록
* 카메라 또는 앨범인텐트를 사용해 이미지를 가져와 배경화면을 바꿀 수 있음.
(권한이 없는 경우, 권한을 요구하는 다이얼로그 창을 뜨게 함)
<img src="https://user-images.githubusercontent.com/49344118/57681298-5f5c3480-766a-11e9-9bc6-d45704499c77.gif" height="550">

### 수정
* 자신의 글만 수정 가능 (자신의 글이 아닐 경우, 글 상세보기화면 우측 상단에 메뉴 버튼이 안 보임).
* 리스트에서 클릭 된 글 번호를 가져와 수정함.
<img src="https://user-images.githubusercontent.com/49344118/57682097-fd043380-766b-11e9-85ef-5b0fc0e4779e.gif" height="550">

### 삭제
* 자신의 글만 삭제 가능 (자신의 글이 아닐 경우, 글 상세보기화면 우측 상단에 메뉴 버튼이 안 보임).
<p><img src="https://user-images.githubusercontent.com/49344118/57196920-441f6400-6f9c-11e9-92a3-483d46e4ff40.gif" height="550">
<img src="https://user-images.githubusercontent.com/49344118/57196922-441f6400-6f9c-11e9-9aed-e87e3c02730a.gif" height="550"></p>

***
## 4. 댓글 기능
### 등록
* 익명이 아닌 자신의 글에 댓글을 작성 시, 닉네임이 자신의 '닉네임(글쓴이)'으로 댓글이 작성됨.
* 익명으로 쓴 자신의 글에 댓글을 작성 시, 닉네임이 '글쓴이'로 댓글이 작성됨.
* 다른 사람의 글에 댓글 작성 시에는 자신의 닉네임으로 댓글이 작성됨.
* 스와이프 메뉴 리스트뷰로 댓글 리스트를 구현.
* '댓글+고민글고유번호'(ex. 댓글1) Shared Preference를 사용한 댓글 등록, 삭제.
* key는 "댓글+댓글 순서", value는 댓글 작성자, 댓글 내용 등을 gson을 사용해서 json형식으로 변환한 내용.
<p><img src="https://user-images.githubusercontent.com/49344118/57196864-d83cfb80-6f9b-11e9-8761-f04148af36b3.gif" height="550">
<img src="https://user-images.githubusercontent.com/49344118/57196862-d7a46500-6f9b-11e9-8a78-203010a3ad5e.gif" height="550"><p/>

### 삭제
* 삭제할 댓글을 오른쪽에서 왼쪽으로 스와이프 하는 경우, 삭제할 수 있는 휴지통 버튼이 나타남 (자신의 댓글만 스와이프시 기능이 나타나게 함).
<img src="https://user-images.githubusercontent.com/49344118/57196462-1801e400-6f98-11e9-9900-bfb782556c94.gif" height="550">

***
## 5. 좋아요 기능
* 고민 글 목록에서, 고민 글 상세보기화면에서 좋아요를 클릭할 수 있음.
* "좋아요한유저+고민글고유번호"(ex. 좋아요한유저1) Shared Preference를 사용한 좋아요 등록, 삭제.
* key는 유저번호, value는 빈칸.
<img src="https://user-images.githubusercontent.com/49344118/57196805-809e9000-6f9b-11e9-9c6e-566f067682c1.gif" height="550">
