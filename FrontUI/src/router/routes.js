const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {
        path: '',
        component: () => import('pages/IndexPage.vue'),
      },
      {
        path: 'notice',
        children: [
          {
            path: '',
            component: () => import('pages/notice/NoticeListPage.vue')
          },
          {
            path: 'new',
            component: () => import('pages/notice/NoticeNewPage.vue')
          },
          {
            path: 'detail',
            component: () => import('pages/notice/NoticeDetailPage.vue')
          },
          {
            path: 'edit',
            component: () => import('pages/notice/NoticeEditPage.vue')
          },
        ]
      },
      {
        path: 'user',
        children: [
          {
            path: 'login',
            children: [
              {
                path: '',
                component: () => import('pages/user/UserLoginPage.vue')
              },
              {
                path: 'detail',
                component: () => import('pages/user/UserLoginDetailPage.vue')
              },
            ],
          },
          {
            path: 'signup',
            component: () => import('pages/user/UserSignupPage.vue')
          },
          {
            path: 'info',
            component: () => import('pages/user/UserInfoPage.vue')
          },
        ]
      },
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
