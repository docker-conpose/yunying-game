<template>

</template>

<script>
import CountTo from 'vue-count-to'
import LineChart from './system/index/LineChart'
import PieChart from './system/index/PieChart'
import BarChart from './system/index/BarChart'
import {
  geDrugInfoChartApi,
  getDataTotalApi,
  getDrugInfoChartApi,
  getEquipmentChartApi, getOrderInfoChartApi,
  getTotalDataApi
} from "@/api/mall/home";

export default {
  components: {
    CountTo,
    LineChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      userTotal: 0,
      equipmentTotal: 0,
      drugTotal: 0,
      orderTotal: 0,
      userOrderCnt: { dayData: [], userCnt: [], orderCnt: [] },
      equipmentChart: { titleText: '', categorySellData: [] },
      drugChart: { titleText: '', categorySellData: [] },
      orderChart: { titleText: '', categorySellData: [] }
    }
  },
  mounted() {
  },
  created() {
    this.getTotalData()
    this.getOrderCnt()
    this.getEquipmentChart()
    this.getDrugChart()
    this.getOrderChart()
  },
  methods: {
    getTotalData() {
      getTotalDataApi().then(response => {
        this.userTotal = response.userTotal;
        this.equipmentTotal = response.equipmentTotal;
        this.drugTotal = response.drugTotal;
        this.orderTotal = response.orderTotal;
      })
    },
    getOrderCnt(){
      this.userOrderCnt ={dayData: [], userCnt: [], orderCnt: []}
    },
    getEquipmentChart(){
      getEquipmentChartApi().then(response => {
        this.equipmentChart ={ titleText:'设备状态饼状图', categorySellData: [
            { value: response.equipmentOnline, name: '在线设备('+response.equipmentOnline+'台)' },
            { value: response.equipmentInUse, name: '在使用设备('+response.equipmentInUse+'台)' },
            { value: response.equipmentOffline, name: '离线设备('+response.equipmentOffline+'台)' }
          ] }
      })
    },
    getDrugChart(){
      getDrugInfoChartApi().then(response => {
        this.drugChart ={ titleText:'药包状态饼状图', categorySellData: [
            { value: response.usedUpDrug, name: '已用完('+response.usedUpDrug+'件)' },
            { value: response.inUseDrug, name: '正在使用('+response.inUseDrug+'件)' },
            { value: response.unusedDrug, name: '未使用('+response.unusedDrug+'件)' }
          ] }
      })
    },
    getOrderChart(){
      getOrderInfoChartApi().then(response => {
        this.orderChart ={ titleText:'未结束订单饼状图', categorySellData: [
            { value: response.waitForSending, name: '等待发货数量('+response.waitForSending+'单)' },
            { value: response.waitForDelivery, name: '等待收货数量('+response.waitForDelivery+'单)' },
            { value: response.waitForConfirmation, name: '等待确认数量('+response.waitForConfirmation+'单)' }
          ] }
      })
    },
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

.panel-group {
  margin-top: 18px;

  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
        background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shoppingCard {
        background: #34bfa3
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shoppingCard {
      color: #34bfa3
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>

