import {defineStore} from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userid: '',
    username: '',
    token: ''
  }),

  getters: {
    isLogin(state) {
      return !!state.userid
    },
    getUserid(state) {
      return state.userid
    },
    getUsername(state) {
      return state.username
    }
  },

  actions: {
    login({userid, username, token}) {
      this.userid = userid
      this.username = username
      this.token = token
    },
    invalidate() {
      this.userid = ''
      this.username = ''
      this.token = ''
    }
  }
})
