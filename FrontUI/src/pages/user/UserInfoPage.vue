<script setup>
import userService from "src/service/user";
import {onMounted, reactive} from "vue";

const logout = () => { // 로그아웃
  userService.logout()
    .then(() => {
      alert("로그아웃 되었습니다.");
      location.href = '/'
    })
    .catch(() => {
      alert("로그아웃에 실패했습니다.");
    })
}

const getUserInfo = () => { // 사용자 정보 가져오기
  return user.getUserInfo()
    .then(({userInfo}) => {
      console.debug(userInfo);
      return userInfo
    })
    .catch((err) => {
      console.log("error: access denied", err)
      alert("접근 권한이 없습니다.");
      location.href = `/user/login`
    })
}

onMounted(async () => {
  const {userid, username, email, addr1, addr2} = await getUserInfo();
  user.userid = userid;
  user.username = username;
  user.email = email;
  user.addr1 = addr1;
  user.addr2 = addr2;
})

const user = reactive({
  userid: '',
  username: '',
  email: '',
  addr1: '',
  addr2: ''
})

</script>

<template>
  <q-page>
    <h2>회원정보 상세 보기</h2>
    <hr/>
    <br/>
    <div class="divTable minimalistBlack">
      <div class="divTableBody">
        <div class="divTableRow">
          <div class="divTableCell">아이디</div>
          <div class="divTableCell" v-text="user.userid"></div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">이름</div>
          <div class="divTableCell" v-text="user.username"></div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">이메일</div>
          <div class="divTableCell" v-text="user.email"></div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">주소</div>
          <div class="divTableCell" v-text="user.addr1"></div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">상세 주소</div>
          <div class="divTableCell" v-text="user.addr2"></div>
        </div>
      </div>
    </div>
    <br/>
    <div>
      <q-btn @click="logout" outline>로그아웃</q-btn>
    </div>
  </q-page>
</template>

<style scoped>

</style>
