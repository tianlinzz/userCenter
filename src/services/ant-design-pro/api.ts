// @ts-ignore
/* eslint-disable */
// @ts-ignore
import { request } from 'umi';

/** 获取当前的用户 GET /user/current */
export async function currentUser(options?: { [key: string]: any }) {
  return request<API.ResResult>('/user/current', {
    method: 'GET',
    ...(options || {}),
  });
}
/** 获取用户列表 GET /user/list */
export async function getUserList(options?: { [key: string]: any }) {
  return request<API.ResResult>('/user/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 退出登录接口 POST login/outLogin */
export async function outLogin(options?: { [key: string]: any }) {
  return request<API.ResResult>('/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 登录接口 POST /user/login */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<API.ResResult>('/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 注册接口 POST /user/register */
export async function register(body: API.RegisterParams, options?: { [key: string]: any }) {
  return request<API.ResResult>('/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
