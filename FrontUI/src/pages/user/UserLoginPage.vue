<script setup>
import userService from "src/service/user";
import {reactive, toRaw} from "vue";

const loginInfo = reactive({
  userid: "",
  password: ""
})
const login = () => {
  userService.login({loginInfo: toRaw(loginInfo)})
    .then((res) => {
      console.log(res)
      alert(res.data.msg)
      location.href = "/user/login/detail"
    })
    .catch((err) => {
      alert(err)
      console.log(err)
    })
}
</script>

<template>
  <q-page>
    <h2>로그인하기</h2>
    <hr/>
    <br/>
    <q-form autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" @submit="login">
      <div class="divTable minimalistBlack">
        <div class="divTableBody">
          <div class="divTableRow">
            <div class="divTableCell">아이디
            </div>
            <div class="divTableCell">
              <q-input type="text" v-model="loginInfo.userid" style="width:95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">비밀번호
            </div>
            <div class="divTableCell">
              <q-input type="password" v-model="loginInfo.password" style="width:95%"/>
            </div>
          </div>
        </div>
      </div>
      <div>
        <q-btn type="submit" label="로그인"/>
        <q-btn to="/user/signup" label="회원가입 페이지"/>
        <q-btn to="/" label="메인화면 이동"/>
      </div>
    </q-form>

  </q-page>
</template>

<style scoped>

</style>
