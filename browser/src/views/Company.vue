<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 公司信息查看
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box" style="padding-bottom: 10px">
        <el-input v-model="searchId" placeholder="查找公司" style="width: 200px; margin-right: 20px"></el-input>
        <el-button type="primary" icon="Search" @click="handleSearch">查找</el-button>
      </div>
      <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="companyId" label="ID"></el-table-column>
        <el-table-column prop="company" label="公司名"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column prop="companyInf" label="简介"></el-table-column>
        <el-table-column label="操作" width="220" align="center" :hidden="false">
          <template #default="scope">
            <el-button text :icon="Edit" @click="handleEdit(scope.$index, scope.row)">
              修改
            </el-button>
            <el-button text :icon="Delete" @click="handleDelete(scope.row.companyId)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.current"
          :page-size="query.size"
          :total="pageTotal"
          @current-change="handlePageChange"
        ></el-pagination>
        <el-button type="primary" icon="Plus" @click="handleAdd" :hidden="0">添加</el-button>
      </div>
    </div>

    <!-- 编辑公司信息弹出框 -->
    <el-dialog title="编辑公司" v-model="editVisible" width="30%">
      <el-form label-width="70px">
        <el-form-item label="公司名称">
          <el-input v-model="form.company"></el-input>
        </el-form-item>
        <el-form-item label="公司地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.companyInf"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveEdit">确 定</el-button>
				</span>
      </template>
    </el-dialog>


    <!-- 添加公司信息弹出框 -->
    <el-dialog title="添加公司" v-model="addVisible" width="30%">
      <el-form label-width="100px">
        <el-form-item label="公司ID">
          <el-input v-model="form.companyId"></el-input>
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input v-model="form.company"></el-input>
        </el-form-item>
        <el-form-item label="公司地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="公司简介">
          <el-input v-model="form.companyInf"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="addVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveAdd">确 定</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {addCompany, addExpress, deleteCompany, findCompany, getAllCompany, updateCompany} from "@/api";
import {ElMessage} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
const searchId = ref()
// 表格编辑时弹窗和保存
const editVisible = ref(false)
// 表格添加时弹窗和保存
const addVisible = ref(false)
const tableData = ref([]);
const query = reactive({
  current: 1,
  size: 10
});
const pageTotal = ref(0);
// 分页导航
const handlePageChange = (val) => {
  query.current = val;
  getData();
};

//删除公司信息
const handleDelete = (companyId) =>{
  deleteCompany(companyId).then(res => {
    if (res.data.code === 10000) {
      ElMessage.success("删除成功")
      getData()
    } else {
      ElMessage.error("删除失败：" + res.data.info)
    }
  }).catch(err => {
    ElMessage.error("删除失败：" + err.message)
  })

}


let form = reactive({
  companyId: -1,
  company: '',
  address: '',
  companyInf: ''
})
let idx = -1;
//处理更新事件
const handleEdit = (index, row) => {
  idx = index;
  form.companyId = row.companyId,
  form.company = row.company
  form.address = row.address
  form.companyInf = row.companyInf
  editVisible.value = true
}
//更新编辑后的信息
const saveEdit = () =>{
  updateCompany(form).then(res => {
    if (res.data.code === 10000) {
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      if (form.company !== '') {
        tableData.value[idx].company = form.company
      }
      if (form.address !== '') {
        tableData.value[idx].address = form.address
      }
      if (form.companyInf !== '') {
        tableData.value[idx].companyInf = form.companyInf
      }
      editVisible.value = false
    } else {
      ElMessage.success(`修改失败` + res.data.info)
    }
  }).catch(err => {
    ElMessage.success(`修改失败` + err.message)
  })
}

//处理添加事件
const handleAdd = () => {
  form.companyId = null
  form.company = ''
  form.address = ''
  form.companyInf = ''
  addVisible.value = true
}

const saveAdd = () => {
  addCompany(form).then(res => {
    if (res.data.code === 10000) {
      ElMessage.success("添加成功")
      query.current = 1
      getData()
      addVisible.value = false
    } else {
      ElMessage.error("添加失败：" + res.data.info)
    }
  }).catch(err => {
    ElMessage.error("添加失败：" + err.message)
  })
}


// 获取表格数据
const getData = () => {
  getAllCompany(query.current, query.size).then(res => {
    if (res.data.code === 10000) {
      tableData.value = res.data.data.records
      pageTotal.value = res.data.data.total
    } else {
      ElMessage.error(res.data.info);
    }
  }).catch(err => {
    console.error(err)
    ElMessage.error(err.message);
  })
}

getData();

const handleSearch = () => {
  if (searchId.value) {
    findCompany(searchId.value, 1, 10).then(res => {
      if (res.data.code === 10000) {
        query.current = 1
        tableData.value = res.data.data.records
        const total = res.data.data.total
        if (total < 10) {
          pageTotal.value = total
        } else {
          pageTotal.value = 10
        }
      } else {
        ElMessage.error("获取公司信息失败：" + res.data.info);
      }
    }).catch(err => {
      console.error(err)
      ElMessage.error("获取公司信息失败：" + err.message);
    })
  } else {
    getData()
  }
}
</script>

<style scoped>

</style>
