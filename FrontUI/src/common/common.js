const isLogin = () => { // 쿠키에서 가져온 토큰의 유무로 로그인 확인
  const token = $.cookie(`${import.meta.env.VITE_ACCESS_TOKEN_NAME}`);
  return !!token
}

//글자 길이 바이트 단위로 체크하기(바이트값 전달)
function calBytes(str) {
  let tcount = 0;
  let tmpStr = String(str);
  let strCnt = tmpStr.length;
  let onechar;
  for (let i = 0; i < strCnt; i++) {
    onechar = tmpStr.charAt(i);
    if (escape(onechar).length > 4) {
      tcount += 2;
    } else {
      tcount += 1;
    }
  }
  return tcount;
}
