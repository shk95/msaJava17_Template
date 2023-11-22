<script setup>
import {toRaw} from "vue";
import noticeService from "src/service/notice";
import {useUserStore} from "stores/user";
import {useRoute, useRouter} from "vue-router";

const router = useRouter()
const route = useRoute()
const noticeSeq = (() => noticeService.getCurrentSeq({route}))()
const noticeDetail = (async () => {
  const {notice} = await noticeService.getDetail({seq: noticeSeq})
  return notice
})()

const isMine = ({noticeSeq = ""}) => {
  const loginUserid = toRaw(useUserStore().getUserid);
  if (noticeDetail.seq !== noticeSeq) {
    alert("잘못된 접근입니다.");
    router.push({path: "/notice"});
  }
  if (noticeDetail.userid !== loginUserid) {
    alert("작성자만 수정할 수 있습니다.");
    return false
  }
  return true
}

const editContent = () => {
  if (!isMine({noticeSeq})) {
    alert("작성자만 수정할 수 있습니다.");
    return
  }
  router.push({path: '/notice/edit', query: {seq: noticeSeq}})
}

const deleteContent = () => {
  if (!confirm("작성한 글을 삭제하시겠습니까?")) {
    return
  }
  if (!isMine({noticeSeq})) {
    alert("작성자만 삭제할수 있습니다.");
    return
  }
  noticeService.delete({seq: noticeSeq})
    .then(() => {
      alert("삭제되었습니다.");
      router.push({path: "/notice"});
    })
}
</script>

<template>
  <q-page>
    <h2>공지사항 상세보기</h2>
    <hr/>
    <br/>
    <div class="divTable minimalistBlack">
      <div class="divTableBody">
        <div class="divTableRow">
          <div class="divTableCell">제목
          </div>
          <div class="divTableCell">{{ noticeDetail.title }}</div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">공지글 여부
          </div>
          <div class="divTableCell">
            <q-checkbox v-model="noticeDetail.isNotice" checked-icon="task_alt" unchecked-icon="highlight_off"/>
          </div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">작성일
          </div>
          <div class="divTableCell">{{ noticeDetail.regDate }}</div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">조회수
          </div>
          <div class="divTableCell">{{ noticeDetail.readCnt }}</div>
        </div>
        <div class="divTableRow">
          <div class="divTableCell">내용
          </div>
          <div class="divTableCell" id="contents">{{ noticeDetail.content }}</div>
        </div>
      </div>
    </div>
    <div>
      <q-btn id="btnEdit" @click="editContent" label="수정"/>
      <q-btn id="btnDelete" @click="deleteContent" label="삭제"/>
      <q-btn id="btnList" to="/notice" label="목록"/>
    </div>
  </q-page>
</template>

<style scoped>

</style>
