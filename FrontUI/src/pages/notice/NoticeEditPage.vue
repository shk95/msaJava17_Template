<script setup>
import {reactive, toRaw} from "vue";
import {useRoute, useRouter} from "vue-router";
import noticeService from "src/service/notice";

const route = useRoute();
const noticeSeq = (noticeService.getCurrentSeq({route}))()
const noticeForm = (async () => {
  const {notice} = await noticeService.getDetail({seq: noticeSeq})
  return reactive(notice)
})()

const router = useRouter();
const submitForm = () => {
  const formData = toRaw(noticeForm)
  noticeService.save({notice: formData})
    .then(({data}) => {
      router.push({path: "/notice"})
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
    <h2>공지사항 수정하기</h2>
    <hr/>
    <br/>
    <q-form autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false"
            @submit="submitForm" @reset="resetForm">
      <div class="divTable minimalistBlack">
        <div class="divTableBody">
          <div class="divTableRow">
            <div class="divTableCell">제목</div>
            <div class="divTableCell">
              <q-input type="text" maxlength="100" v-model="noticeForm.title" style="width: 95%"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">공지글 여부</div>
            <div class="divTableCell">
              <q-checkbox v-model="noticeForm.isNotice"/>
            </div>
          </div>
          <div class="divTableRow">
            <div class="divTableCell">조회수</div>
            <div class="divTableCell">{{ noticeForm.readCnt }}</div>
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
        <q-btn type="submit">수정</q-btn>
        <q-btn type="reset">다시 작성</q-btn>
      </div>
    </q-form>
  </q-page>
</template>

<style scoped>

</style>
