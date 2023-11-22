<script setup>

</script>

<template>
  <q-page class="flex flex-center row">
    <div class="col-8">

      <h2>메인 화면</h2>
      <hr/>
      <br/>
      <div class="divTable minimalistBlack">
        <div class="divTableBody">
          <div class="divTableRow">
            <div class="divTableCell">공지사항 바로가기</div>
            <div class="divTableCell">
              <q-btn to="/notice/" label="공지사항" outline></q-btn>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">로그인하기</div>
            <div class="divTableCell">
              <q-btn to="/user/login" label="로그인" outline></q-btn>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">회원가입</div>
            <div class="divTableCell">
              <q-btn to="/user/signup" label="회원가입" outline></q-btn>
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>
