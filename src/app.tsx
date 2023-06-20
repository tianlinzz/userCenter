import Footer from '@/components/Footer';
import RightContent from '@/components/RightContent';
import { BookOutlined, LinkOutlined } from '@ant-design/icons';
import type { Settings as LayoutSettings } from '@ant-design/pro-components';
import { PageLoading, SettingDrawer } from '@ant-design/pro-components';
import type { RequestConfig, RunTimeLayoutConfig } from 'umi';
import { history, Link } from 'umi';
import defaultSettings from '../config/defaultSettings';
import { currentUser as queryCurrentUser } from './services/ant-design-pro/api';
import * as process from 'process';
import type { RequestOptionsInit } from 'umi-request';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';
/**
 * 白名称单，不需要登录的页面
 */
const weightList: string[] = ['/user/register', loginPath];

/** 获取用户信息比较慢的时候会展示一个 loading */
export const initialStateConfig = {
  loading: <PageLoading />,
};

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async (): Promise<any> => {
    try {
      return await queryCurrentUser();
    } catch (error) {
      history.push(loginPath);
    }
  };
  // 如果是白名单，不返回用户信息
  if (weightList.includes(location.pathname)) {
    return {
      fetchUserInfo,
      settings: defaultSettings,
    };
  }
  const currentUser = await fetchUserInfo();
  return {
    fetchUserInfo,
    currentUser,
    settings: defaultSettings,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
// @ts-ignore
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    waterMarkProps: {
      content: initialState?.currentUser?.username,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果是白名单，不执行
      if (weightList.includes(location.pathname)) return;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser) {
        history.push(loginPath);
      }
    },
    links: isDev
      ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined />
            <span>OpenAPI 文档</span>
          </Link>,
          <Link to="/~docs" key="docs">
            <BookOutlined />
            <span>业务组件文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    // @ts-ignore
    childrenRender: (children, props) => {
      if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {!props.location?.pathname?.includes('/login') && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState: any) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};

const handelRequest = (
  url: string,
  options: RequestOptionsInit,
): { url?: string; options?: RequestOptionsInit } => {
  // 设置token
  const token = localStorage.getItem('token');
  if (token) {
    options.headers = {
      ...options.headers,
      Authorization: JSON.stringify(token),
    };
  }
  options.credentials = 'include'; // 允许跨域携带cookie

  return { url, options };
};
const handelResponse = async (response: Response): Promise<any> => {
  const res: API.ResResult = await response.clone().json(); // 克隆一份响应然后json化，不然会报错
  if (res.code === 200) {
    // 成功, 直接返回数据
    return res?.data;
  }
  if (res?.code === 40100) {
    // 未登录，跳转到登录页
    history.push(loginPath);
    return Promise.reject(new Error('请先登录'));
  }
  // 其他错误的抛出，最好是异步错误，这样可以被内部封装的request捕获。然后还得是错误对象，这样才能被全局错误处理捕获
  return Promise.reject(new Error(res?.description || res?.msg || '网络错误'));
};
const prefix =
  process.env.NODE_ENV === 'production' ? 'http://114.132.229.206:8080' : 'http://localhost:8080'; // 开发环境下，代理到本地后端服务
export const request: RequestConfig = {
  prefix,
  timeout: 1000 * 10,
  requestInterceptors: [handelRequest],
  responseInterceptors: [handelResponse],
};
