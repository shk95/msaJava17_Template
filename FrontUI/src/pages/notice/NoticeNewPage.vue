<script setup>
import noticeService from 'src/service/notice'
import {reactive, toRaw} from "vue";

const noticeForm = reactive(noticeService.NOTICE_FORM)
const submitForm = () => {
  const notice = toRaw(noticeForm)
  noticeService.save({notice})
    .then(({data}) => {
      console.log(data)
    })
    .catch((error) => {
      console.error(error)
    })
}

const resetForm = () => {

}

</script>

<template>
  <q-page>

    <h2>공지사항 등록하기</h2>
    <hr/>
    <br/>
    <q-form autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false"
            @submit="submitForm" @reset="resetForm">
      <div class="divTable minimalistBlack">
        <div class="divTableBody">
          <div class="divTableRow">
            <div class="divTableCell">제목
            </div>
            <div class="divTableCell">
              <q-input type="text" maxlength="100" v-model="noticeForm.title" style="width: 95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">공지글 여부
            </div>
            <div class="divTableCell">
              <q-checkbox v-model="noticeForm.isNotice"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">내용</div>
            <div class="divTableCell">
              <q-input type="textarea" v-model="noticeForm.content" style="width: 95%; height: 400px"/>
            </div>
          </div>
        </div>
      </div>
      <div>
        <q-btn type="submit" label="등록"/>
        <q-btn type="reset" label="다시 작성"/>
      </div>
    </q-form>
  </q-page>
</template>

<style scoped>

</style>
