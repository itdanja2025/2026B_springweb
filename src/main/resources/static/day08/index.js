// 
console.log( 'index.js open');

// [1] 변수 와 상수 선언 키워드
let count = 10;     // 변수의 선언과 초기화
count = 20;         // 변수값 수정 가능
const count2 = 20;  // 상수의 선언과 초기화
// count2 = 20;     // 상수값 수정 불가능
// * var 키워드 : let 키워드 없었던 시절에 사용된 키워드
var count3 = 30;
var count3 = 40; // var 키워드는 중복 변수명을 허용한다. 식별이 어렵다.

// [2] 백틱 : 문자열 템플릿 , 문자열내 JS표현식을 연결할때 사용한다. 
// [2] 템플릿 리터럴 (Template Literals)
// 동적 클래스명(className) 지정 또는 문자열 조합 시 사용
console.log( `Hello : ${ count }` );
let html = ``;
html += `<div> Hello : ${ count2 } <div> `
console.log( html );

// [3-1] 조건문1 : IF
const point = 85;
if( point >= 90 ){ console.log( "A학점"); }
else if( point >=80 ){ console.log( "B학점"); }
else{ console.log("C학점"); }
// [3-2] 조건문2 : 삼항연산자 , 조건 ? 참 : 거짓 , 간단한조건 에서 주로 사용됨
console.log( point >= 90 ? "A학점" : point >= 80 ? "B학점" : "C학점" );
// [3-3] 조건문3 : 단축평가 , 조건 && 참이면결과 , 조건 || 거짓이면결과
console.log( point >= 90 && "A학점" ); // 만약에 참이면 'A학점' 아니면 false 
console.log( point >= 90 || "A학점" ); // 만약에 참이면 true 아니면 "A학점"
// Nullish 병합 연산자(??): null 또는 undefined일 때 기본값 설정 (0, ""는 유효값으로 처리)
const userNickName = null;
console.log( userNickName ?? "익명 사용자" )

// [4] 반복문 : 
const array = [ 10 , 20 , 30 , 40 , 50 ]
for( let index = 0 ; index < array.length ; index++ ){ console.log( array[index] ) }
for( let index in array ){ console.log( array[index] ) }
for( let value of array ){ console.log( value ) }
//  forEach
array.forEach( (value) => { console.log( value ); } )
// ** map ** : forEach 다르게 return 가능하다.
let newArray = array.map( (value) => { console.log( value ); return value;  } )
// ** filter ** : 조건부 return 가능하다.  
let newArray2 = array.filter( (value) => { console.log( value ); return value > 20; })

// [5] 함수 : 
function fun1( param1 , param2 ){ } // 5-1 선언적 함수 선언
const fun2 = function( param1 , param2 ){ } // 5-2 익명 함수 선언
const fun3 = ( param1 , param2 ) => { } // 5-3 화살표 함수 선언
const fun4 = ( param1 , param2 = "강호동" ) => { } // 5-4 : 매개변수의 기본값 

fun1( 4 , 10 ) // 함수 호출 
fun2( 10 , "유재석" ); // 함수 호출 
fun3( 10 , { name : "유재석" } ); // 함수 호출 
fun4( 10 );

// [6] 객체 : 여러개의 값을 가진 (하나의)값 
// 변수/상수 는 값을 저장하는 상징적인 이름
const obj1 = { name : "유재석" , age : 40 }
const name2 = "강호동";
const age2 = 50;
const obj2 = { name2 , age2 }; // key 와 value의 변수명이 같으면 key 생략가능
const obj3 = [ "유재석" , 40 ]
console.log( obj1.name )
console.log( obj3[0] );

// *** 스프레드 연산자 : ... 배열이나 객체를 복사 할때 사용 , 왜? 주소값 변경 목적 **
// React State 업데이트 시 기존 메모리 주소(참조)를 변경하기 위한 필수 패턴
const obj4 = { ...obj1 , phone : "010"}; console.log( obj4 );
const obj5 = [ ...obj3 ]; console.log( obj5 ); // 값의 차이가 없지만 새로운 주소값으로 복사
const obj6 = [ 6 , 7 , ...obj3 ]; console.log( obj6 );

// [7] 구조 분해 할당 : 객체나 배열에서 값을 분해 하는 방법
// Props 전달 및 Hooks(useState 등) 사용 시 기본
console.log( "--------------------------------")
const user = { name: "유재석", age: 40, role: "MC" };
const { name, age, ...restInfo } = user; // 나머지 프로퍼티 묶기
console.log( name );    
console.log( age );
console.log( restInfo );

// [8] 비구조화 할당 과 나머지 연산자 
const [ num , ...intArray ]  = [ 1 , 2 , 3 , 4]
console.log( num ); // 순서대로(인덱스) 분해후 나머지는 ...에 저장한다.
console.log( intArray ); 

// [1] 콜백으로 사용될 함수 정의
function printSuccess(message) {
  console.log("성공: " + message);
}
// [2] 콜백 함수를 매개변수로 전달받는 본체 함수
function checkScore(score, onSuccess, onError) {
  if (score >= 80) {
    onSuccess("합격입니다!");
  } else {
    onError("불합격입니다.");
  }
}
// [3-2] 인자 자리에서 즉석으로 익명 람다식 전달 (React에서 가장 많이 쓰는 형태)
checkScore(50, printSuccess, (msg) => console.log("실패: " + msg));

// --------------------------------------------------
