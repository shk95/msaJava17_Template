<script setup>
import noticeService from "src/service/notice";
import {reactive} from "vue";
import {useRouter} from "vue-router";

const noticeList = reactive((async () => {
  const {noticeList} = await noticeService.getList()
  return noticeList
})())

const goDetailPage = ({seq} = "") => {
  const router = useRouter()
  router.push({path: "/notice/detail", query: {seq}})
}

</script>

<template>
  <q-page>
    <h2>공지사항</h2>
    <hr/>
    <br/>
    <div class="divTable minimalistBlack">
      <div class="divTableHeading">
        <div class="divTableRow">
          <div class="divTableHead">순번</div>
          <div class="divTableHead">제목</div>
          <div class="divTableHead">조회수</div>
          <div class="divTableHead">등록자</div>
          <div class="divTableHead">등록일</div>
        </div>
      </div>
      <div v-show="noticeList.length>0" class="divTableBody" id="noticeList">
        <div v-for="notice in noticeList" :key="notice.seq" class="divTableRow">
          <div v-if="notice.isNotice" class="divTableCell">공지사항</div>
          <div v-else class="divTableCell">{{ notice.seq }}</div>
          <div class="divTableCell">
            <a href="#" @click="()=>goDetailPage(notice.seq)">{{ notice.title }}</a>
          </div>
          <div class="divTableCell">{{ notice.readCnt }}</div>
          <div class="divTableCell">{{ notice.userid }}</div>
          <div class="divTableCell">{{ notice.regDate }}</div>
        </div>
      </div>
    </div>
    <q-btn to="/notice/new" label="글쓰기" outline/>
  </q-page>
</template>

<style scoped>

</style>
