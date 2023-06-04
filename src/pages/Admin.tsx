import { PageHeaderWrapper } from '@ant-design/pro-components';
import { Card } from 'antd';
import React from 'react';
const Admin: React.FC = ({ children }) => {
  return (
    <PageHeaderWrapper>
      <Card>{children}</Card>
    </PageHeaderWrapper>
  );
};
export default Admin;
