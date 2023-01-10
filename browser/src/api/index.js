import request from "@/utils/request";
import {getRefreshToken, getToken} from "@/utils/token";
import {useAccountStore} from "@/store/account";
import {stringify} from "qs";

// 登录
export const login = (username, password) => {
  return request({
    url: '/account/login',
    method: "post",
    data: stringify({
      username,
      password
    })
  })
}

// 刷新 token
export const refreshToken = () => {
  const accountStore = useAccountStore()
  return request({
    url: '/account/refreshToken',
    method: "post",
    data: stringify({
      refreshToken: getRefreshToken(),
      username: accountStore.user.username
    })
  })
}

// 登出
export const logout = () => {
  const accountStore = useAccountStore()
  return request({
    url: '/account/logout',
    method: "post",
    data: stringify({
      username: accountStore.user.username
    })
  })
}

// 注册
export const register = (username, password, phone) => {
  return request({
    url: '/account/register',
    method: "post",
    data: stringify({
      username: username,
      password: password,
      phone: phone
    })
  })
}

// 得到自身数据
export const getSelf = () => {
  return request({
    url: '/account/self',
    method: "get",
    headers: {
      'Authorization': "Bearer " + getToken()
    }
  })
}

// 得到所有用户信息
export const getUser = (current, size) => {
  return request({
    url: '/user/all',
    method: 'get',
    params: {
      current: current,
      size: size
    },
  })
}

// 得到当前登录用户的信息
export const getUserSelf = () => {
  return request({
    url: '/user/self',
    method: 'get',
    headers: {
      'Authorization': "Bearer " + getToken()
    }
  })
}

// 改变用户数据
export const updateUser = (userId, address, phone, sex, isAdmin) => {
  return request({
    url: '/user/update',
    method: 'post',
    data: stringify({
      userId: userId,
      address: address,
      phone: phone,
      sex: sex,
      isAdmin: isAdmin
    }),
  })
}

// 查找用户
export const findUser = (userId) => {
  return request({
    url: '/user/find',
    method: 'get',
    params: {
      userId: userId
    }
  })
}

// 得到所有快递
export const getAllExpress = (current, size) => {
  return request({
    url: '/express/all',
    method: 'get',
    params: {
      current: current,
      size: size
    }
  })
}

// 查询快递
export const findExpress = (expressId) => {
  return request({
    url: '/express/find',
    method: 'get',
    params: {
      expressId: expressId
    }
  })
}

// 删除快递
export const deleteExpress = (expressId) => {
  return request({
    url: '/express/delete',
    method: "post",
    data: stringify({
      expressId: expressId
    })
  })
}

// 更新快递
export const updateExpress = (express) => {
  return request({
    url: '/express/update',
    method: 'post',
    data: express
  })
}

// 添加快递
export const addExpress = (express) => {
  return request({
    url: '/express/add',
    method: 'post',
    data: express
  })
}

// 得到所有公司信息
export const getAllCompany = (current, size) => {
  return request({
    url: '/company/all',
    method: 'get',
    params: {
      current: current,
      size: size
    }
  })
}

export const addCompany = (company) => {
  return request({
    url: '/company/add',
    method: 'post',
    data: company
  })
}

// 查找公司信息
export const findCompany = (key, current, size) => {
  return request({
    url: '/company/find',
    method: 'get',
    params: {
      key: key,
      current: current,
      size: size
    }
  })
}

//删除公司信息
export const deleteCompany = (companyId) => {
  return request({
    url: '/company/delete',
    method: 'post',
    data: stringify({
      companyId: companyId
    })
  })
}

// 更新公司信息
export const updateCompany = (company) => {
  return request({
    url: '/company/update',
    method: 'post',
    data: company
  })
}


// 得到所有商品信息
export const getAllArticle = (current, size) => {
  return request({
    url: '/article/all',
    method: 'get',
    params: {
      current: current,
      size: size
    }
  })
}

// 查找商品信息
export const findArticle = (key, current, size) => {
  return request({
    url: '/article/find',
    method: 'get',
    params: {
      key: key,
      current: current,
      size: size
    }
  })
}

//修改商品信息
export const updateArticle = (article) => {
  return request({
    url: '/article/update',
    method: 'post',
    data: article
  })
}

export const deleteArticle = (articleId) => {
  return request({
    url: '/article/delete',
    method: 'post',
    data: stringify({
      articleId: articleId
    })
  })
}


export const addArticle = (article) => {
  return request({
    url: '/article/add',
    method: 'post',
    data: article
  })
}