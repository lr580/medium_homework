<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 商品信息查看
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box" style="padding-bottom: 10px">
        <el-input v-model="searchId" placeholder="查找商品" style="width: 200px; margin-right: 20px"></el-input>
        <el-button type="primary" icon="Search" @click="handleSearch">查找</el-button>
      </div>
      <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="articleId" label="ID"></el-table-column>
        <el-table-column prop="article" label="商品名"></el-table-column>
        <el-table-column prop="cost" label="价格"></el-table-column>
        <el-table-column prop="articleInf" label="介绍"></el-table-column>
        <el-table-column label="操作" width="220" align="center" :hidden="false">
          <template #default="scope">
            <el-button text :icon="Edit" @click="handleEdit(scope.$index, scope.row)">
              修改
            </el-button>
            <el-button text :icon="Delete" @click="handleDelete(scope.row.articleId)">
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
        <el-button type="primary" icon="Plus" @click="handleAdd" :hidden="0">添加商品</el-button>
      </div>
    </div>
    <!-- 编辑商品信息弹出框 -->
    <el-dialog title="编辑商品" v-model="editVisible" width="30%">
      <el-form label-width="70px">
        <el-form-item label="商品名称">
          <el-input v-model="form.article"></el-input>
        </el-form-item>
        <el-form-item label="商品价格">
          <el-input v-model="form.cost"></el-input>
        </el-form-item>
        <el-form-item label="商品简介">
          <el-input v-model="form.articleInf"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="editVisible = false">取 消</el-button>
					<el-button type="primary" @click="saveEdit">确 定</el-button>
				</span>
      </template>
    </el-dialog>

    <!-- 添加商品信息弹出框 -->
    <el-dialog title="添加商品" v-model="addVisible" width="30%">
      <el-form label-width="100px">
        <el-form-item label="商品ID">
          <el-input v-model="form.articleId"></el-input>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="form.article"></el-input>
        </el-form-item>
        <el-form-item label="商品价格">
          <el-input v-model="form.cost"></el-input>
        </el-form-item>
        <el-form-item label="商品简介">
          <el-input v-model="form.articleInf"></el-input>
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
import {
  addArticle,
  deleteArticle,
  findArticle,
  getAllArticle,
  updateArticle,
} from "@/api";
import {ElMessage} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
const searchId = ref()
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


let form = reactive({
  articleId: -1,
  article: '',
  cost: -1.0,
  articleInf: ''
})
let idx = -1;
//处理删除商品
const handleDelete = (articleId) =>{
  deleteArticle(articleId).then(res => {
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



//处理编辑界面
const handleEdit = (index, row) => {
      idx = index;
      form.articleId = row.articleId,
      form.article = row.article
      form.cost = row.cost
      form.articleInf = row.articleInf
      editVisible.value = true
}

//更新编辑后的信息
const saveEdit = () =>{
  console.log(form)
  updateArticle(form).then(res => {
    if (res.data.code === 10000) {
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      if (form.article !== '') {
        tableData.value[idx].article = form.article
      }
      if (form.cost !== -1) {
        tableData.value[idx].cost = form.cost
      }
      if (form.articleInf !== '') {
        tableData.value[idx].articleInf = form.articleInf
      }
      editVisible.value = false
    } else {
      ElMessage.error(`修改失败` + res.data.info)
    }
  }).catch(err => {
    console.log(err)
    ElMessage.error(`修改失败` + err.message)
  })
}

// 获取表格数据
const getData = () => {
  getAllArticle(query.current, query.size).then(res => {
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
    findArticle(searchId.value, 1, 10).then(res => {
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
        ElMessage.error("获取商品信息失败：" + res.data.info);
      }
    }).catch(err => {
      console.error(err)
      ElMessage.error("获取商品信息失败：" + err.message);
    })
  } else {
    getData()
  }
}
//处理添加事件
const handleAdd = () => {
  form.articleId = null
  form.article = ''
  form.cost = ''
  form.articleInf = ''
  addVisible.value = true
}

const saveAdd = () => {
  addArticle(form).then(res => {
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
</script>

<style scoped>

</style>
