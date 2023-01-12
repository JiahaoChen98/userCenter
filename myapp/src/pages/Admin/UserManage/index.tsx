import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable, TableDropdown} from '@ant-design/pro-components';
import {useRef} from 'react';
import {searchUsers} from "@/services/ant-design-pro/api";
import {Image} from "antd";


const columns: ProColumns<API.CurrentUser>[] = [
  {
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
    align:'center'
  },
  {
    title: '学号',
    dataIndex: 'studentNumber',
    copyable: true,
    align:'center'
  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,
    ellipsis: true,
    align:'center'
  },
  {
    title: '用户账户',
    dataIndex: 'userAccount',
    copyable: true,
    align:'center'
  },
  {
    title: '用户头像',
    dataIndex: 'avatarUrl',
    render: (_, record) => (
      <div>
        {/*<Image src={record.avatarUrl} width={100}/c>*/}
        <Image src={record.avatarUrl} width={100}></Image>
      </div>
    ),
  },
  {
    title: '性别',
    dataIndex: 'gender',
    align:'center'

  },
  {
    title: '电话',
    dataIndex: 'phone',
    copyable: true,
    align:'center'
  },
  {
    title: '邮箱',
    dataIndex: 'mail',
    copyable: true,
    align:'center'
  },
  {
    title: '用户状态',
    dataIndex: 'userStatus',
    align:'center'
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    valueType: 'select',
    valueEnum: {
      0: { text: '普通用户',status:'Default' },
      1: {
        text: '管理员',
        status: 'Success',
      },
    },
    align:'center'
  },
  {
    title: '用户创建时间',
    dataIndex: 'createTime',
    valueType: 'date',
    align:'center'
  },


  // {
  //   disable: true,
  //   title: '状态',
  //   dataIndex: 'state',
  //   filters: true,
  //   onFilter: true,
  //   ellipsis: true,
  //   valueType: 'select',
  //   valueEnum: {
  //     all: { text: '超长'.repeat(50) },
  //     open: {
  //       text: '未解决',
  //       status: 'Error',
  //     },
  //     closed: {
  //       text: '已解决',
  //       status: 'Success',
  //       disabled: true,
  //     },
  //     processing: {
  //       text: '解决中',
  //       status: 'Processing',
  //     },
  //   },
  // },
  // {
  //   disable: true,
  //   title: '标签',
  //   dataIndex: 'labels',
  //   search: false,
  //   renderFormItem: (_, { defaultRender }) => {
  //     return defaultRender(_);
  //   },
  //   render: (_, record) => (
  //     <Space>
  //       {record.labels.map(({ name, color }) => (
  //         <Tag color={color} key={name}>
  //           {name}
  //         </Tag>
  //       ))}
  //     </Space>
  //   ),
  // },
  // {
  //   title: '创建时间',
  //   key: 'showTime',
  //   dataIndex: 'created_at',
  //   valueType: 'date',
  //   sorter: true,
  //   hideInSearch: true,
  // },
  // {
  //   title: '创建时间',
  //   dataIndex: 'created_at',
  //   valueType: 'dateRange',
  //   hideInTable: true,
  //   search: {
  //     transform: (value) => {
  //       return {
  //         startTime: value[0],
  //         endTime: value[1],
  //       };
  //     },
  //   },
  // },
  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,
      <a href={record.url} target="_blank" rel="noopener noreferrer" key="view">
        查看
      </a>,
      <TableDropdown
        key="actionGroup"
        onSelect={() => action?.reload()}
        menus={[
          {key: 'copy', name: '复制'},
          {key: 'delete', name: '删除'},
        ]}
      />,
    ],
  },
];

export default () => {
  const actionRef = useRef<ActionType>();
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params = {}, sort, filter) => {
        console.log(sort, filter);
        const userList = await searchUsers()
        return {
          data: userList
        }
      }}
      editable={{
        type: 'multiple',
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
      // toolBarRender={() => [
      //   <Button key="button" icon={<PlusOutlined />} type="primary">
      //     新建
      //   </Button>
      //
      // ]}
    />
  );
};
