import request from '@/utils/request'

// 查询设备管理列表
export function listInfo(query) {
  return request({
    url: '/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询设备管理详细
export function getInfo(id) {
  return request({
    url: '/equipment/' + id,
    method: 'get'
  })
}

// 新增设备管理
export function addInfo(data) {
  return request({
    url: '/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备管理
export function updateInfo(data) {
  return request({
    url: '/equipment/',
    method: 'put',
    data: data
  })
}

// 删除设备管理
export function delInfo(id) {
  return request({
    url: '/equipment/' + id,
    method: 'delete'
  })
}

// 导出设备管理
export function exportInfo(query) {
  return request({
    url: '/equipment/export',
    method: 'get',
    params: query
  })
}
