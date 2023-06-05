import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import React, { useRef } from 'react';
import { getUserList } from '@/services/ant-design-pro/api';
import { Image } from 'antd';

export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};

const columns: ProColumns<API.CurrentUser>[] = [
  {
    title: '用户id',
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 72,
    align: 'center',
  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,
    ellipsis: true,
    align: 'center',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    copyable: true,
    ellipsis: true,
    align: 'center',
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    align: 'center',
    render: (_, record) => {
      return <Image src={record.avatarUrl} width={100} alt="avatar" />;
    },
  },
  {
    title: '性别',
    dataIndex: 'gender',
    valueEnum: {
      0: { text: '男', status: 'Success' },
      1: { text: '女', status: 'Success' },
    },
    align: 'center',
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    copyable: true,
    ellipsis: true,
    align: 'center',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    copyable: true,
    ellipsis: true,
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'date',
    align: 'center',
  },
  {
    title: '用户编码',
    dataIndex: 'userCode',
    ellipsis: true,
    align: 'center',
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    valueEnum: {
      0: { text: '普通用户', status: 'Success' },
      1: { text: '管理员', status: 'Success' },
    },
    align: 'center',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    valueType: 'date',
    align: 'center',
  },
  {
    title: '操作',
    valueType: 'option',
    align: 'center',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          // @ts-ignore
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,
      <TableDropdown
        key="actionGroup"
        onSelect={() => action?.reload()}
        menus={[
          { key: 'copy', name: '复制' },
          { key: 'delete', name: '删除' },
        ]}
      />,
    ],
  },
];

const UserManager: React.FC = () => {
  const actionRef = useRef<ActionType>();

  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (): Promise<any> => await getUserList()}
      editable={{
        // 直接查源码去搜索配置项就好了，官网就是一坨答辩什么都没有
        type: 'multiple',
        onSave: async (key, row): Promise<any | void> => {
          console.log(key, row);
        },
        onDelete: async (key, row): Promise<any | void> => {
          console.log(key, row);
        },
      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        onChange(value) {
          console.log('value: ', value);
        },
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {
          // @ts-ignore
          listsHeight: 400,
        },
      }}
      form={{
        // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="高级表格"
    />
  );
};

export default UserManager;
