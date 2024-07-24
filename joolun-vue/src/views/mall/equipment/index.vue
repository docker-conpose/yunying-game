<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备编码" prop="equipmentNumber">
        <el-input
          v-model="queryParams.equipmentNumber"
          placeholder="请输入设备编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="SIM卡" prop="iccid">
        <el-input
          v-model="queryParams.iccid"
          placeholder="请输入SIM卡卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="硬件版本" prop="hardwareVersion">
        <el-select v-model="queryParams.hardwareVersion" placeholder="请选择硬件版本" clearable size="small">
          <el-option
            v-for="dict in equipmentHardwareVersionOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            @keyup.enter.native="handleQuery"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="软件版本" prop="softwareVersion">
        <el-select v-model="queryParams.softwareVersion" placeholder="请选择软件版本" clearable size="small">
          <el-option
            v-for="dict in equipmentSoftwareVersionOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            @keyup.enter.native="handleQuery"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态" prop="equipmentStatus">
        <el-select v-model="queryParams.equipmentStatus" placeholder="0-离线，1-在线，2-使用中" clearable size="small">
          <el-option
          v-for="dict in equipmentStatusOptions"
          :key="dict.dictValue"
          :label="dict.dictLabel"
          :value="dict.dictValue"
          @keyup.enter.native="handleQuery"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仓储状态" prop="warehouseStatus">
        <el-select v-model="queryParams.warehouseStatus" placeholder="请选择仓储状态" clearable size="small">
          <el-option
            v-for="dict in warehouseStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            @keyup.enter.native="handleQuery"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['equipment:info:add']">人工录入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['equipment:info:remove']"
        >批量删除设备</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['equipment:info:export']"
        >导出设备信息</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备编码" align="center" prop="equipmentNumber" />
      <el-table-column label="SIM卡卡号" align="center" prop="iccid" />
      <el-table-column label="硬件版本" align="center" prop="hardwareVersion" :formatter="hardwareVersionFormat"/>
      <el-table-column label="软件版本" align="center" prop="softwareVersion" :formatter="softwareVersionFormat"/>
      <el-table-column label="设备状态" align="center" prop="equipmentStatus" :formatter="equipmentStatusFormat"/>
      <el-table-column label="仓储状态" align="center" prop="warehouseStatus" :formatter="warehouseStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['equipment:info:edit']"
          >更新设备参数</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['equipment:info:remove']"
          >删除设备数据</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备编码" prop="equipmentNumber">
          <el-input v-model="form.equipmentNumber" placeholder="请输入设备编码" />
        </el-form-item>
        <el-form-item label="SIM卡卡号" prop="iccid">
          <el-input v-model="form.iccid" placeholder="请输入SIM卡卡号" />
        </el-form-item>
        <el-form-item label="硬件版本" prop="hardwareVersion">
          <el-input v-model="form.hardwareVersion" placeholder="请输入硬件版本" />
        </el-form-item>
        <el-form-item label="软件版本" prop="softwareVersion">
          <el-input v-model="form.softwareVersion" placeholder="请输入软件版本" />
        </el-form-item>
        <el-form-item label="设备状态：0-离线，1-在线，2-使用中">
          <el-radio-group v-model="form.equipmentStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="仓储状态，是否售出">
          <el-radio-group v-model="form.warehouseStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="仓储状态，是否售出" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入仓储状态，是否售出" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo, exportInfo } from "@/api/mall/equipmentInfo";

export default {
  name: "Info",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 设备管理表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 设备状态字典
      equipmentStatusOptions: [],
      // 设备仓储状态字典
      warehouseStatusOptions: [],
      // 设备软件版本字典
      equipmentSoftwareVersionOptions: [],
      // 设备硬件版本字典
      equipmentHardwareVersionOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentNumber: null,
        iccid: null,
        hardwareVersion: null,
        softwareVersion: null,
        equipmentStatus: null,
        warehouseStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        equipmentNumber: [
          { required: true, message: "设备编码不能为空", trigger: "blur" }
        ],
        iccid: [
          { required: true, message: "SIM卡卡号不能为空", trigger: "blur" }
        ],
        hardwareVersion: [
          { required: true, message: "硬件版本不能为空", trigger: "blur" }
        ],
        softwareVersion: [
          { required: true, message: "软件版本不能为空", trigger: "blur" }
        ],
        warehouseStatus: [
          { required: true, message: "仓储状态不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("equipment_status").then(response => {
      this.equipmentStatusOptions = response.data;
    });
    this.getDicts("warehouse_status").then(response => {
      this.warehouseStatusOptions = response.data;
    });
    this.getDicts("equipment_info_software_version").then(response => {
      this.equipmentSoftwareVersionOptions = response.data;
    });
    this.getDicts("equipment_info_hardware_version").then(response => {
      this.equipmentHardwareVersionOptions = response.data;
    });
  },
  methods: {
    /** 查询设备管理列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        equipmentNumber: null,
        iccid: null,
        hardwareVersion: null,
        softwareVersion: null,
        equipmentStatus: 0,
        warehouseStatus: 0,
        createTime: null,
        updateTime: null,
        deleted: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "人工录入设备信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备参数";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInfo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除设备信息?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInfo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出设备数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    // 执行设备状态字典翻译
    equipmentStatusFormat(row, column) {
      return this.selectDictLabel(this.equipmentStatusOptions, row.equipmentStatus);
    },
    // 执行设备仓储状态字典翻译
    warehouseStatusFormat(row, column) {
      return this.selectDictLabel(this.warehouseStatusOptions, row.warehouseStatus);
    },
    hardwareVersionFormat(row, column) {
      return this.selectDictLabel(this.equipmentHardwareVersionOptions, row.hardwareVersion);
    },
    softwareVersionFormat(row, column) {
      return this.selectDictLabel(this.equipmentSoftwareVersionOptions, row.softwareVersion);
    },
  }
};
</script>
