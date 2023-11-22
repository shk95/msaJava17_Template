import {api} from "boot/axios";

const mapNoticeList = ({noticeList = []}) => {
  return noticeList.map(({notice = {}}) => {
    return mapNoticeDetail({notice})
  })
}

const mapNoticeDetail = ({notice = {}}) => {
  return {
    seq: notice.seq,
    userid: notice.userid,
    readCnt: notice.readCnt,
    title: notice.title,
    regDate: notice.regDate,
    content: notice.content,
    isNotice: notice.noticeYn === "Y", // boolean
  }
}

const mapNoticeToForm = ({notice = {}}) => {
  return {
    nSeq: notice.seq,
    user_id: notice.userid,
    readCnt: notice.readCnt,
    title: notice.title,
    regDt: notice.regDate,
    contents: notice.content,
    noticeYn: notice.isNotice, // boolean
  }
}

export default {
  NOTICE_FORM: {
    seq: "",
    title: "",
    content: "",
    userid: "",
    isNotice: false,
    readCnt: 0,
    regDate: "",
  },
  getCurrentSeq: ({route = undefined}) => {
    return route.query.seq || ""
  },
  getList: () => {
    return api({
      method: 'post',
      url: '/notice/noticeList',
    })
      .then(({data}) => {
        return {
          noticeList: mapNoticeList({noticeList: data})
        }
      })
      .catch((error) => {
        console.warn('Notice error : ', error)
        return {noticeList: []}
      })
  },
  getDetail: ({seq = ""}) => {
    return api({
      method: 'post',
      url: '/notice/noticeInfo',
    }, {
      nSeq: seq,
      readCntYn: 'Y'
    })
      .then(({data}) => {
        return {
          notice: mapNoticeDetail({notice: data})
        }
      })
      .catch((error) => {
        console.warn('Notice error : ', error)
        return {notice: {}}
      })
  },
  save: ({notice = {}}) => {
    return api({
      method: 'post',
      url: '/notice/noticeUpdate',
      data: mapNoticeToForm({notice})
    })
      .then(({data}) => {
        return {data}
      })
      .catch((error) => {
        console.warn('Notice error : ', error)
        return Promise.reject(error)
      })
  },
  delete: ({seq = ""}) => {
    return api({
      method: 'post',
      url: '/notice/noticeDelete',
      data: {nSeq: seq}
    })
      .then(({data}) => {
      })
      .catch((error) => {
        console.warn('Notice error : ', error)
      })
  }
}
