console.log( "INDEX.JS 열림" );

// [1] 전체조회 , function 함수명( 매개변수명 ){ } 
async function boardFindAll( ){ console.log( "boardFindAall 열림");
    // 1. 어디에: html table 본문에 , 식별 , .클래스명 vs #ID명  // document(HTML문서).query(질의/요청)Selector(선택자)
    let tbody = document.querySelector('.boardList');  
    // 2. 무엇을 ( HTTP 통신(AXIOS) 이용한 백엔드에게 요청 )
    // * await axios.HTTP메소드명( "HTTP주소" ) *현재 함수명 앞에 async 작성
    // * 동기화 하는 이유: 해당 통신 이후에 아래 코드 실행 하기 위해
    // * 비동기화( 요청 후 응답 대기없음 ) , 동기화( 요청 후 응답 대기 )
    let html = "";
    const 응답결과 = await axios.get( "http://127.0.0.1:8080/board/findall" );
    // { header:~~ , data:~~ , config: ~~ } // data:통신결과데이터
    const 게시물리스트 = 응답결과.data;
        for( let index = 0 ; index <= 게시물리스트.length-1 ; index++ ){
            const 게시물객체 = 게시물리스트[ index ]; 
            // `백틱이란? 문자열과 문자열 사이에 ${} 이용하여 변수 대입가능
            html += `<tr>
                    <td> ${ 게시물객체.no } </td> <td> ${ 게시물객체.writer } </td> 
                    <td> ${ 게시물객체.content } </td> 
                    <td> 
                        <button onclick="boardUpdate(${ 게시물객체.no })">수정</button> 
                        <button onclick="boardDelete(${ 게시물객체.no })">삭제</button> 
                    </td> 
                    </tr>`
        } // for end 
    // 3. 출력 , <마크업> inner </마크업>   // 동기화이므로 통신 결과 이후 실행
    tbody.innerHTML = html;
} // f end 
boardFindAll(); // * HTML(JS포함) 열릴 때 최초1번실행

// [2] 등록
async function boardSave(){ // 동기화함수 async
    // 1. 입력받은 값져오기 , .value : 입력상자에 입력된 값 반환 속성
    const content = document.querySelector('.content').value;
    const writer = document.querySelector('.writer').value;
    // 2. 저장: axios 이용하여 백엔드에게 저장 요청하고 응답받기 
    // await axios.http메소드( "주소" ) , 도메인 생략가능 ,
    const response = await axios.post( `/board/save?content=${ content }&writer=${ writer }` );
    // 3. 결과출력 
    if( response.data == true ){ 
        alert('저장 성공'); 
        boardFindAll(); // 저장 성공시 전체조회 재호출
    }
    else{ alert('저장 실패'); }
}// class end 

// [3] 수정
async function boardUpdate( no ){
    // 1. 수정할 내용 입력받기 prompt
    const content = prompt('수정할 내용: ');
    // 2. 수정처리 : axios 이용하여 백엔드에게 수정 요청/응답
    const response = await axios.put( `/board/update?no=${ no }&content=${ content }`);
    // 3. 결과
    if( response.data == true ){
        alert('수정 성공');
        boardFindAll();
    }else{ alert('수정 실패'); }
}

// [4] 삭제
async function boardDelete( no ){ // async( axios동기화 )
    // no : 삭제할 게시물번호 / 클릭한 게시물번호
    // 1. 삭제처리 : axios 이용하여 백엔드에게 삭제 요청/응답
    const response = await axios.delete( `/board/delete?no=${ no }` );
    // 2. 결과
    if( response.data == true ){
        alert('삭제 성공');
        boardFindAll();
    }else{
        alert('삭제 실패');
    }
}