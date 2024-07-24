import request from '@/utils/request'

export function listLog(query) {
  return request({
    url: '/system/ota/list',
    method: 'get',
    params: query
  })
}

export function addLog(data) {
  return request({
    url: '/system/ota',
    method: 'post',
    data: data
  })
}

export function updateLog(data) {
  return request({
    url: '/system/ota',
    method: 'put',
    data: data
  })
}
