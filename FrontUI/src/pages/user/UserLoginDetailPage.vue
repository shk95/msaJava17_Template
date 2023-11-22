<script setup>
import {onMounted, reactive} from "vue";
import userService from "src/service/user";

const getUserInfo = () => {
  userService.getUserInfo()
    .then(({userInfo}) => {
      user.userid = userInfo.userid
    })
    .catch((err) => {
      console.log('error: access denied', err)
      location.href = `/user/login`
    })
}

const user = reactive({
  userid: ''
})

onMounted(() => {
  getUserInfo()
})

</script>

<template>
  <q-page>
    <div class="divTable minimalistBlack">
      <div class="divTableBody">
        <div class="divTableRow">
          <div class="divTableCell">로그인된 사용자아이디</div>
          <div class="divTableCell">{{ user.userid }}</div>
        </div>
      </div>
    </div>
    <div></div>
    <br/><br/>
    <q-btn to="/" outline label="홈"/>
    <q-btn to="/user/info" outline label="회원정보"/>
    <q-btn to="/notice" outline label="공지사항"/>
  </q-page>
</template>

<style scoped>

</style>
