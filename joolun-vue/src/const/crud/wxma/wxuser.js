let dicDataSex = [{
  label: '未知',
  value: '0'
}, {
  label: '男',
  value: '1'
}, {
  label: '女',
  value: '2'
}]
let dicDataGroupType = [{
  label: '商户组',
  value: 0
}, {
  label: '运营组',
  value: 1
}]

export const tableOption = {
  dialogDrag:true,
  border: true,
  index: false,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  excelBtn: true,
  printBtn: true,
  viewBtn: true,
  searchShow: false,
  menuWidth: 250,
  menuType:'text',
  searchMenuSpan: 6,
  defaultSort:{
    prop: 'createTime',
    order: 'descending'
  },
  column: [
    {
      label: '用户标识',
      prop: 'openId',
      editDisplay:false
    },
    {
      label: '头像',
      prop: 'headimgUrl',
      imgWidth:50,
      dataType: 'string',
      type: 'upload',
      listType: 'picture-img',
      editDisplay:false
    },
    {
      label: '昵称',
      prop: 'nickName',
      width:100,
      sortable:true,
      search:true,
      editDisplay:false
    },
    {
      label: '性别',
      prop: 'sex',
      width: 60,
      type: 'select',
      sortable:true,
      search:true,
      editDisplay:false,
      slot:true,
      dicData: dicDataSex
    },
    {
      label: '用户语言',
      prop: 'language',
      sortable:true,
      editDisplay:false
    },
    {
      label: '用户组',
      prop: 'groupType',
      type: 'select',
      sortable:true,
      editDisplay:true,
      dicData: dicDataGroupType
    },
    {
      label: 'union_id',
      prop: 'unionId',
      hide:true,
      editDisplay:false
    },
    {
      label: '创建时间',
      prop: 'createTime',
      type: 'datetime',
      sortable:true,
      editDisplay:false
    },
    {
      label: '更新时间',
      prop: 'updateTime',
      type: 'datetime',
      sortable:true,
      editDisplay:false
    }
  ]
}
