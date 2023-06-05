import Footer from '@/components/Footer';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-components';
import React from 'react';
import styles from './index.less';
import { SYSTEM_LOGO } from '@/pages/constant';
import { message } from 'antd';
import { history } from 'umi';
import { register } from '@/services/ant-design-pro/api';

const Register: React.FC = () => {
  const handleSubmit = async (values: API.RegisterParams) => {
    // 注册
    const res = await register({ ...values });
    try {
      if (res.code === 200) {
        message.success('注册成功！');
        history.push('/user/login');
      } else {
        message.error(res.msg);
      }
    } catch (error) {
      message.error(res.msg);
    }
  };
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="用户中心"
          initialValues={{
            autoLogin: true,
          }}
          submitter={{
            // 看源码找配置项
            searchConfig: {
              submitText: '注册',
            },
          }}
          actions={[
            <a key="login" style={{ fontSize: '16px' }} onClick={() => history.push('/user/login')}>
              已有账户!,去登录
            </a>,
          ]}
          subTitle={<a style={{ fontSize: '22px' }}>注册</a>}
          onFinish={async (values) => {
            await handleSubmit(values as API.RegisterParams);
          }}
        >
          <ProFormText
            name="userAccount"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined className={styles.prefixIcon} />,
            }}
            placeholder={'请输入账户'}
            rules={[
              {
                required: true,
                message: '用户名是必填项！',
              },
            ]}
          />
          <ProFormText.Password
            name="userPassword"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            placeholder={'请输入密码'}
            rules={[
              {
                required: true,
                message: '密码是必填项！',
              },
              {
                min: 6,
                message: '密码长度必须大于或者等于6位！',
              },
              {
                max: 20,
                message: '密码长度必须小于或者等于20位！',
              },
            ]}
          />
          <ProFormText.Password
            name="checkPassword"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            placeholder={'请输入再次输入密码'}
            rules={[
              {
                required: true,
                message: '校验密码是必填项！',
              },
              {
                min: 6,
                message: '校验密码长度必须大于或者等于6位！',
              },
              {
                max: 20,
                message: '校验密码长度必须小于或者等于20位！',
              },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (getFieldValue('userPassword') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('校验密码和密码不一致！'));
                },
              }),
            ]}
          />
          <ProFormText
            name="userCode"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined className={styles.prefixIcon} />,
            }}
            placeholder={'请输入你对应的用户编码'}
            rules={[
              {
                required: true,
                message: '用户编码是必填项！',
              },
            ]}
          />
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Register;
