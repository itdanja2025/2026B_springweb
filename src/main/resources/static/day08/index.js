// .js파일은 .html 파일내 <script src="index.js"></script> 코드로 포함된다.
// 즉] js코드는 html내 포함된다.

// 1. 변수 와 상수 
let count = 10;     // let 변수선언 키워드
    count = 3;      // 변수는 수정 가능 
const count2 = 20;  // const 상수선언 키워드    
    // count2 = 4;     // 상수는 수정 불가능 , Uncaught TypeError: Assignment to constant variable.

// 2. 문자열 템플릿 , `백틱
console.log( `hello ${ count }` );
let html = `<div> hello ${ count2 } </div>`
console.log( html )

// 3. 조건문, if , 삼항연산자 , 단축평가 , null논리연산
const point = 85;
if( point >= 90 ){ console.log( 'A학점'); } // 만약에 point변수가 90 이상이면 A학점
else if( point >= 80 ){ console.log( 'B학점'); } // 만약에 point변수가 80이상이면 B학점
else{ console.log('C학점'); } // 그외 C학점
// 3-2 : 삼항연산자 ,       조건 ? 참 : 거짓 ,      조건1 ? 참1 : 조건2 ? 참2 : 거짓 
console.log( point >= 90 ? 'A학점' : point >= 80 ? 'B학점' : 'C학점' );
// 3-3 : 단축평가 , 조건 && 참이면결과 , 조건 || 거짓이면결과
console.log( point >= 90 && 'A학점' ) // 만약에 90점이상이면 'A학점'  아니면 false 
console.log( point >= 90 || 'A학점' ) // 만약에 90점이상이면 true 아니면 'A학점'
// 3-4 : null논리연산 , 조건 ?? null이면결과
const nickName = null;
console.log( nickName ?? '익명사용자' )

// 4. 반복문 : 
const array = [ 10 , 20 , 30 , 40 , 50 ]
// 4-1 : 일반for문
for( let index = 0 ; index < array.length ; index++ ){ console.log( array[index] ); }
// 4-2 : 향상된for문 , 반복변수명 in 배열명 , 반복변수명 of 배열명 
for( let index in array ){ console.log(array[index]); } // in 인덱스 하나씩 순회 
for( let value of array ){ console.log( value ); } // of 값 하나씩 순회
// 4-3 : forEach 순회 , map 순회+반환 , filter 순회+조건(논리)
array.forEach( (value) => { console.log( value ); } ) // 자바: -> , JS: =>
const newArray = array.map( (value) => { return value; } ); // forEach 반환없고 map 반환있음 
const newArray2 = array.filter( (value) => { return value >= 20; } ) // filter 조건에 따른 반환

// 5. 함수선언 
// 5-1 : 선언적 함수 
function func1( 매개변수1 , 매개변수2 ){ }  
// 5-2 : 익명(이름없는)함수 , 주로 변수/상수에 저장
const func2 = function( 매개변수1 , 매개변수2 ){ }
// 5-3 : 화살표/람다식 함수 , 주로 변수/상수에 저장 
const func3 = ( 매개변수1 , 매개변수2 ) => { }
// 5-4 : 매개변수에 기본값 대입가능 , 만일 인수가 없을 때 기본값 대입
const func4 = ( 매개변수1 , 매개변수2 , 매개변수3 = 'student' ) => { console.log( 매개변수3 ) }
// ---- 함수 호출
func1( 4 , 10 );
func2( 4 , 20 ); // 익명 또는 람다 함수는 변수명 호출한다.
func3( 10 , { name : '유재석'} );
func4( 10 , 4 ); // 만일 인수가 없는 매개변수는 기본값 대입된다. // 매개변수3 = 'student' 




