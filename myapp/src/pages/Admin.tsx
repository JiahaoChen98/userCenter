import {HeartTwoTone, SmileTwoTone} from '@ant-design/icons';
import {PageHeaderWrapper} from '@ant-design/pro-components';
import {Alert, Card, Typography} from 'antd';
import React from 'react';

const Admin: React.FC = (props) => {
  const {children} = props;
  return (
    <PageHeaderWrapper>
      {children}
    </PageHeaderWrapper>
  );
};
export default Admin;
