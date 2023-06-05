import { Settings as LayoutSettings } from '@ant-design/pro-components';
import { SYSTEM_LOGO } from '../src/pages/constant';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // 拂晓蓝
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: '用户中心',
  pwa: false,
  logo: SYSTEM_LOGO,
  iconfontUrl: '',
};

export default Settings;