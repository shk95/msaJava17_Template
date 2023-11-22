<script setup>
import userService from "src/service/user";
import {reactive, toRaw} from "vue";

const signup = () => {
  userService.signup({userInfo: toRaw(userForm)})
    .then(({msg}) => {
      console.log(msg)
      alert(msg)
      location.href = "/user/login"
    })
    .catch((err) => {
      alert(err)
      console.log(err)
    })
}

const getPostcode = () => {
  return new Promise((resolve, reject) => {
    new daum.Postcode({
      oncomplete: function (data) {
        // Kakao에서 제공하는 data는 JSON구조로 주소 조회 결과값을 전달함
        // 주요 결과값
        // 주소 : data.address
        // 우편번호 : data.zonecode
        resolve({addr: `(${data.zonecode})${data.address}`});
      },
    }).open();
  })
}

const setAddr1 = (promise) => {
  promise
    .then(({addr}) => {
      userForm.addr1 = addr
    })
    .catch(err => {
      console.log(err);
      userForm.addr1 = ""
    })
}

const userForm = reactive({
  userid: "",
  username: "",
  email: "",
  password: "",
  confirmPassword: "",
  addr1: "",
  addr2: "",
});

</script>

<template>
  <q-page>
    <h2>회원 가입하기</h2>
    <hr/>
    <br/>
    <q-form autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" @submit="signup">
      <div class="divTable minimalistBlack">
        <div class="divTableBody">
          <div class="divTableRow">
            <div class="divTableCell">* 아이디
            </div>
            <div class="divTableCell">
              <q-input type="text" v-model="userForm.userid" style="width:95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">* 이름
            </div>
            <div class="divTableCell">
              <q-input type="text" v-model="userForm.username" style="width:95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">* 비밀번호
            </div>
            <div class="divTableCell">
              <q-input type="password" v-model="userForm.password" style="width:95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">* 비밀번호확인
            </div>
            <div class="divTableCell">
              <q-input type="password" v-model="userForm.confirmPassword" style="width:95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">* 이메일
            </div>
            <div class="divTableCell">
              <q-input type="email" v-model="userForm.email" style="width:95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">* 주소
            </div>
            <div class="divTableCell">
              <q-input readonly type="text" v-model="userForm.addr1" style="width:85%"/>
              <q-btn @click="()=>{ setAddr1(getPostcode()) }" label="우편번호" outline/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">* 상세 주소
            </div>
            <div class="divTableCell">
              <q-input type="text" v-model="userForm.addr2" style="width:95%"/>
            </div>
          </div>
        </div>
      </div>
      <div>
        <q-btn type="submit" label="회원가입" outline/>
      </div>
    </q-form>
  </q-page>
</template>

<style scoped>

</style>
