# stocking-backend

## 숙소 검색 API
>REST방식 
 - 기본 요청 URL: http://52.79.141.237:8080/back/search

<br>

>인터페이스
 - 요청 인터페이스   

|요청 변수|값|설명|
|------|---|---|
|location|문자열(필수)|검색할 위치 값을 입력합니다.|
|checkIn|문자열|체크인 날짜를 입력합니다.|
|checkOut|문자열|체크아웃 날짜를 입력합니다.|
|guests|정수값|숙박할 인원을 입력합니다.|
|refund|불린|환불가능 여부를 입력합니다.|
|roomTypeHouse|불린|집전체 여부를 true/false로 입력합니다.|
|roomTypePrivate|불린|개인실 여부를 true/false로 입력합니다.|
|roomTypeShared|불린|다인실 여부를 true/false로 입력합니다.|
|neLat|실수값|우측상단 끝 위도를 입력합니다.|
|neLng|실수값|우측상단 끝 경도를 입력합니다.|
|swLat|실수값|좌측하단 끝 위도를 입력합니다.|
|swLng|실수값|좌측하단 끝 경도를 입력합니다.|
|priceMin|정수값|최소 가격(필터값)값을 입력합니다.|
|priceMax|정수값|최대 가격(필터값)값을 입력합니다.|
|instantBooking|불린|즉시예약 가능 여부를 true/false로 입력합니다.|
|bedCount|정수값|침대 갯수(필터값)를 입력합니다.|
|bedroomCount|정수값|침실 갯수(필터값)를 입력합니다.|
|bathCount|정수값|화장실 갯수(필터값)를 입력합니다.|
|convenience|불린|슈퍼호스트 및 장애인 편의시설 필터 여부를 true/false로 입력합니다.|
|amenityList|문자열|원하는 편의시설(필터값)들을 입력합니다.|
|facilityList|문자열|원하는 시설(필터값)들을 입력합니다.|
|hostLangList|문자열|호스트 언어(필터값)을 입력합니다.|
|page|정수값|검색 결과 페이지를 입력합니다.|


 - 응답 구조
 
|응답 필드|값|설명|
|------|---|---|
|homes|문자열|숙소 정보들을 출력합니다.|
|homeId|정수값|숙소의 고유ID를 출력합니다.|
|isSuperhost|불린|해당 숙소의 호스트가 슈퍼호스트인지 여부를 출력합니다.|
|isBookmarked|불린|해당 숙소가 북마크 되어있는지 여부를 출력합니다.|
|imageArray|문자열|해당 숙소의 사진들의 주소들을 출력합니다.|
|imageCount|정수값|해당 숙소의 사진의 갯수를 출력합니다.|
|subTitle|문자열|해당 숙소의 소제목을 출력합니다. (ex. MongMong-Toto의 호텔 객실)|
|title|문자열|해당 숙소 이름을 출력합니다.|
|feature|문자열|숙소의 최대인원, 침실, 침대, 욕실갯수를 출력합니다.|
|rating|실수값|해당 숙소의 평점을 출력합니다.|
|reviewCount|정수값|해당 숙소의 리뷰 갯수를 출력합니다.|
|price|정수값|해당 숙소의 가격을 출력합니다.|
|location|실수값|해당 숙소의 위치(lat:위도, lng:경도)를 가져옵니다.(ex. { lat: 37.650333, lng: 127.072783 })|
|recentHomes|문자열|최근 숙소들의 정보들을 출력합니다.|
|filterCondition|문자열|검색된 숙소들이 가지고 있는 필터 조건들을 출력합니다.|
|superhost|불린|필터 조건에 슈퍼호스트 여부가 있는지 출력합니다.|
|instantBooking|불린|필터 조건에 즉시예약가능 여부를 출력합니다.|
|bedroom|문자열|검색된 숙소들의 침대유형을 출력합니다.|
|convenienceList|문자열|검색된 숙소들의 편의시설들을 출력합니다.|
|facilityList|문자열|검색된 숙소들의 시설들을 출력합니다.|
|hostLangList|문자열|검색된 숙소들의 호스트 언어들을 출력합니다.|
|dataTotal|정수값|검색된 숙소들의 갯수를 출력합니다.|
|priceArray|문자열|가격그래프에 필요한 검색된 숙소들의 가격들을 2만원 단위로 출력합니다.|
|averagePrice|정수값|검색된 페이지의 가격들의 평균을 출력합니다.|


 - 응답 예시
 JSON : http://52.79.141.237:8080/back/search?location=서울&checkIn=2020.11.26&checkOut=2020.11.28&dateDiff=3&adult=1 ..
