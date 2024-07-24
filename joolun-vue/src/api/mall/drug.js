import request from '@/utils/request'

// 查询药包管理列表
export function listDrug(query) {
  return request({
    url: '/mall/drug/list',
    method: 'get',
    params: query
  })
}

// 查询药包管理详细
export function getDrug(id) {
  return request({
    url: '/mall/drug/' + id,
    method: 'get'
  })
}

// 新增药包管理
export function addDrug(data) {
  return request({
    url: '/mall/drug',
    method: 'post',
    data: data
  })
}

// 修改药包管理
export function updateDrug(data) {
  return request({
    url: '/mall/drug',
    method: 'put',
    data: data
  })
}

// 删除药包管理
export function delDrug(id) {
  return request({
    url: '/mall/drug/' + id,
    method: 'delete'
  })
}

// 导出药包管理
export function exportDrug(query) {
  return request({
    url: '/mall/drug/export',
    method: 'get',
    params: query
  })
}