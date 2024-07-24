import request from '@/utils/request'

// 获取各个总数的数据
export function getTotalDataApi() {
  return request({
    url: '/system/home/getTotalDataInfo',
    method: 'get'
  })
}

export function getEquipmentChartApi() {
  return request({
    url: '/system/home/getEquipmentChart',
    method: 'get'
  })
}

export function getDrugInfoChartApi() {
  return request({
    url: '/system/home/getDrugInfoChartVo',
    method: 'get'
  })
}

export function getOrderInfoChartApi() {
  return request({
    url: '/system/home/getOrderChartVo',
    method: 'get'
  })
}
