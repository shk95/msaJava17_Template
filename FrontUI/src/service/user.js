import {api} from '/src/boot/axios.js'
import {useUserStore} from "stores/user";

const mapFormData = ({userInfo}) => {
  return {
    user_id: userInfo.userid,
    user_name: userInfo.username,
    password: userInfo.password,
    password2: userInfo.confirmPassword,
    email: userInfo.email,
    addr1: userInfo.addr1,
    addr2: userInfo.addr2,
  }
}
export default {
  login: ({loginInfo}) =>
    api({
      url: '/login/loginProc',
      method: 'post'
    }, {
      user_id: loginInfo.userid,
      password: loginInfo.password
    })
      .then(({data}) => {
        console.log(data)
        return {
          msg: data.msg,
          loginInfo: {}
        }
      })
      .catch((err) => {
        console.log(err)
        return Promise.reject(err)
      }),
  logout: () =>
    useUserStore().invalidate()
      .then(() => {
        console.log("logout success")
      }),
  signup: ({userInfo}) => {
    return api({
      url: '/reg/insertUserInfo',
      method: 'post'
    }, mapFormData({userInfo}))
      .then(({data}) => {
        console.log(data)
        return {
          msg: data.msg,
          userInfo: {}
        }
      })
      .catch((err) => {
        console.log(err)
        return Promise.reject(err)
      })
  },
  getUserInfo: () =>
    api({
      url: '/user/info',
      method: 'get'
    })
      .then(({data}) => {
        console.log(data)
        return {
          userInfo: {
            userid: data.userid,
            username: data.username,
            email: data.email,
            addr1: data.addr1,
            addr2: data.addr2,
          }
        }
      })
      .catch((err) => {
        console.log(err)
        return Promise.reject(err)
      })
  ,

}
