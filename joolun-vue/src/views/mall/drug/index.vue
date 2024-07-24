<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="药包编号" prop="drugNumber">
        <el-input
          v-model="queryParams.drugNumber"
          placeholder="请输入药包编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓储状态" prop="warehouseStatus">
        <el-select v-model="queryParams.warehouseStatus" placeholder="请选择仓储状态，是否售出" clearable size="small">
          <el-option
            v-for="dict in warehouseStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            @keyup.enter.native="handleQuery"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="药包状态" prop="drugStatus">
        <el-select v-model="queryParams.drugStatus" placeholder="请选择药包状态：0-未使用，1-已使用，2-已用完" clearable size="small">
          <el-option
              v-for="dict in drugStatusOptions"
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
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['mall:drug:add']"
        >人工录入药包</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mall:drug:remove']"
        >批量删除药包信息</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mall:drug:export']"
        >导出药包信息</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="drugList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="药包编号" align="center" prop="drugNumber" />
      <el-table-column label="药包剩余时间" align="center">
        <template slot-scope="scope">
          {{scope.row.timeRemaining}} 分钟
        </template>
      </el-table-column>
      <el-table-column label="所属设备编号" align="center" prop="equipmentNumber" />
      <el-table-column label="药包分类" align="center" prop="classifyName" />
      <el-table-column label="仓储状态" align="center" prop="warehouseStatus" :formatter="warehouseStatusFormat" />
      <el-table-column label="药包状态" align="center" prop="drugStatus" :formatter="drugStatusFormat" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="150">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="createTime" width="150">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mall:drug:edit']"
          >修改药包参数</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mall:drug:remove']"
          >删除药包信息</el-button>
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

    <!-- 添加或修改药包管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="药包编号" prop="drugNumber">
          <el-input v-model="form.drugNumber" placeholder="请输入药包编号" />
        </el-form-item>
        <el-form-item label="药包剩余时间" prop="timeRemaining">
          <el-input v-model="form.timeRemaining" placeholder="请输入药包剩余时间" />
        </el-form-item>
        <el-form-item label="设备id" prop="equipmentId">
          <el-input v-model="form.equipmentId" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="分类id" prop="classifyId">
          <el-input v-model="form.classifyId" placeholder="请输入分类id" />
        </el-form-item>
        <el-form-item label="仓储状态，是否售出">
          <el-radio-group v-model="form.warehouseStatus">
            <el-radio
              v-for="dict in warehouseStatusOptions"
              :key="dict.dictValue"
              :label="parseInt(dict.dictValue)"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="药包状态">
          <el-radio-group v-model="form.drugStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="药包状态" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入药包状态：0-未使用，1-已使用，2-已用完" />
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
import { listDrug, getDrug, delDrug, addDrug, updateDrug, exportDrug } from "@/api/mall/drug";

export default {
  name: "Drug",
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
      // 药包管理表格数据
      drugList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 仓储状态，是否售出字典
      warehouseStatusOptions: [],

      drugStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        drugNumber: null,
        timeRemaining: null,
        equipmentId: null,
        classifyId: null,
        warehouseStatus: null,
        drugStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        drugNumber: [
          { required: true, message: "药包编号不能为空", trigger: "blur" }
        ],
        timeRemaining: [
          { required: true, message: "药包剩余时间不能为空", trigger: "blur" }
        ],
        classifyId: [
          { required: true, message: "分类id不能为空", trigger: "blur" }
        ],
        warehouseStatus: [
          { required: true, message: "仓储状态，是否售出不能为空", trigger: "blur" }
        ],
        drugStatus: [
          { required: true, message: "药包状态：0-未使用，1-已使用，2-已用完不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "药包状态：0-未使用，1-已使用，2-已用完不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "药包状态：0-未使用，1-已使用，2-已用完不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "药包状态：0-未使用，1-已使用，2-已用完不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("warehouse_status").then(response => {
      this.warehouseStatusOptions = response.data;
    });
    this.getDicts("drug_status").then(response => {
      this.drugStatusOptions = response.data;
    });
  },
  methods: {
    /** 查询药包管理列表 */
    getList() {
      this.loading = true;
      listDrug(this.queryParams).then(response => {
        this.drugList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 仓储状态，是否售出字典翻译
    warehouseStatusFormat(row, column) {
      return this.selectDictLabel(this.warehouseStatusOptions, row.warehouseStatus);
    },
    drugStatusFormat(row, column) {
      return this.selectDictLabel(this.drugStatusOptions, row.warehouseStatus);
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
        drugNumber: null,
        timeRemaining: null,
        equipmentId: null,
        classifyId: null,
        warehouseStatus: 0,
        drugStatus: 0,
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
      this.title = "添加药包管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDrug(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改药包管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDrug(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDrug(this.form).then(response => {
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
      this.$confirm('是否确认删除药包管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDrug(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有药包管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDrug(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
