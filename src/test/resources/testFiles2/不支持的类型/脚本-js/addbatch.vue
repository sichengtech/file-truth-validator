<template>
  <section class="cms-body" v-loading="loading">
    <!-- 返回组件 -->
    <cms-back></cms-back>        
    <el-form  ref="form" :model="dataInfo" :rules="rules" 
        class="cms-form" label-width="162px">

        <div class="form-footer">
           <span class="gray">批量添加的方法：每行一条用户信息，按下面的“数据格式”可组织数据。</span> 
         </div>
         <div class="form-footer">
           <span class="gray">工号|密码|姓名|生日|会员组ID|Email|性别|手机号|座机号</span> 
         </div>
         <div class="form-footer">
           <span class="gray">生日格式: yyyy-MM-dd</span> 
         </div>         
         <div class="form-footer">
           <span class="gray">会员组ID: 1是默认会员组</span> 
         </div>          
         <div class="form-footer">
           <span class="gray">性别: 1男，0女</span>
         </div>

        <el-form-item label="批量数据" class="flex-100">
               <el-input v-model="dataInfo.batch" class="cms-width" type="textarea"></el-input>
         </el-form-item>


         <!-- 字段 -->
       <el-form-item v-for="(item,index) in itemList" :key="index" :label="item.label" :class="['flex-50']" :prop="item.field">
          <el-input class="cms-width" v-model="dataInfo['attr_'+item.field]" v-if="item.dataType==1"></el-input>
		   <!-- 整形文本 -->
          <el-input class="cms-width" v-model.number="dataInfo['attr_'+item.field]" v-if="item.dataType==2" type="number" min='0'></el-input>
          <!-- 浮点形文本 -->
          <el-input class="cms-width" v-model="dataInfo['attr_'+item.field]" v-if="item.dataType==3"></el-input>
          <!-- 文本区 -->
          <el-input class="cms-width" v-model="dataInfo['attr_'+item.field]" type="textarea" v-if="item.dataType==4">
          </el-input>
          <!-- 日期 -->
          <el-date-picker class="cms-width" v-model="dataInfo['attr_'+item.field]" type="date" value-format="yyyy-MM-dd" v-if="item.dataType==5"></el-date-picker>
          <!-- 发布时间，归档日期 -->
          
		   <!-- 下拉列表 -->
          <el-select class="cms-width" v-model="dataInfo['attr_'+item.field]" v-if="item.dataType==6">
            <el-option v-for="(opt,optIndex) in item.optValue" :key="optIndex" :label="opt" :value="opt">
            </el-option>
          </el-select>
		   
		    <!-- 自定义字段拼接名称-->
          <el-checkbox-group v-model="dataInfo['attr_'+item.field]" v-if="item.dataType==7">
            <el-checkbox v-for="(opt,optIndex) in item.optValue" :key="optIndex" :label="opt"></el-checkbox>
          </el-checkbox-group>
		  
		    <!-- 单选框 -->
          <el-radio-group v-model="dataInfo['attr_'+item.field]" v-if="item.dataType==8">
            <el-radio :label="opt" v-for="(opt,optIndex) in item.optValue" :key="optIndex">
            </el-radio>
          </el-radio-group>
        </el-form-item>
         <div class="form-footer">
           <el-button type="warning" @click="add(true)">保存并继续添加</el-button>
               <el-button type="primary"  @click="add(false)">提交</el-button>
           <el-button type="info" @click="$reset">重置</el-button>
           <span class="gray">
              选择“提交并继续添加”按钮会停留在添加页面；选择“提交”按钮后将跳转到对应的列表页</span> 
         </div>
    </el-form>
</section>
</template>
<script>
import axios from "axios";
import va from "@/rules";
import formMixns from "@/mixins/form";

export default {
  mixins: [formMixns], //普通表单变量
  data() {
      let self=this;
    function password() {
      return {
        validator: (rule, value, callback) => {
          if (value === "") {
            callback();
          } else if (value.length<self.passwordMinLen) {
            callback(new Error('密码长度不能小于系统设定值:'+self.passwordMinLen));
          } else {
            if (self.dataInfo.confirmPassword !== "") {
               self.$refs.form.validateField("confirmPassword");
            }
            callback();
          }
        },
        trigger: "blur"
      };
    }
    function confirmPassword() {
      return {
        validator: (rule, value, callback) => {
          if (value === "") {
            callback();
          } else if (value !== self.dataInfo.password) {
            callback(new Error('前后密码不一致'));
          } else {
            callback();
          }
        },
        trigger: "blur"
      };
    }
    let required = va.required('此项必填');
    let number = va.number('只能输入数字');
    let email = va.email('请输入正确的邮箱地址');
    let validateName = va.validateName('用户名已存在');
    let tel = va.tel('请输入正确的固定电话');
    let mobile = va.mobile('请输入正确的手机号');

    return {
      memberGroup: [], //业务变量会员组
      passwordMinLen:'',
      rules: {
        //校验规则
        username: [required, validateName],
        email: [email],
        password: [required,password()],
        confirmPassword: [required,confirmPassword()],
        groupId: [required, number],
        grain: [required, number],
        phone: [tel],
        mobile: [mobile]
      },
      itemList: [], //动态列表
    };
  },
  methods: {
    getDataInfo(id) {
      //重写获取表单数据
      let api = this.$api; //API地址
      axios
        .all([
          axios.post(api.memberGet, { id: id }), //axios批量发送请求
          axios.post(api.groupList),
          axios.post(api.memberGetField)
        ])
        .then(
          axios.spread((data, memberGroup,res) => {
            this.dataInfo = data.body; //将用户数据复制给dataInfo
            this.dataInfo.password = ""; //密码
            this.dataInfo.confirmPassword = ""; //重复密码
            this.dataInfo.gender=true;
            this.dataInfo.groupId=1;
            this.dataInfo.email='';
            this.passwordMinLen=data.body.passwordMinLen;//最小密码长度
            this.memberGroup = memberGroup.body; //将用户组数据复制给dataInfo
            this.$refs["form"].resetFields();
            this.loading = false;
            //获取自定义字段
          let itemList = res.body; //渲染数据字段模型
          for (let i in itemList) {
             if (itemList[i].dataType == 7) {
                let data = [];
                if (itemList[i].defValue != "") {
                  data = itemList[i].defValue.split(",");
                }
                //判断是否为多选框
                this.$set(this.dataInfo, "attr_" + itemList[i].field, data); //转换为数组
              } else {
                this.$set(
                  this.dataInfo,
                  "attr_" + itemList[i].field,
                  itemList[i].defValue
                );
              }
         
          }
          this.itemList = itemList;
          })
        )
        .catch(err => {
          this.loading = false;
        });
    },

    add(state) {
      let batchStr=this.dataInfo.batch;
      if(batchStr==""||batchStr==null){
        this.errorMessage("请输入信息");
        return;
      }


      let arr1=batchStr.split("\n");
      //let errorMsg="";
      for(let i=0;i<arr1.length;i++){
        let rowStr=arr1[i];
        console.log(rowStr);
        let arr2=rowStr.split("|");
        console.log(arr2);
        if(arr2.length!=9){
          let errorMsg="第"+(i+1)+"行信息数量不对，必须是9个值，格式请看上面的提示信息："+rowStr;
          this.errorMessage(errorMsg);
        }else{
          let userInfo={
            username:arr2[0],   //工号
            password:arr2[1],   //密码
            realname:arr2[2],   //姓名
            birthday:arr2[3],   //生日
            groupId:arr2[4],    //会员组ID
            email:arr2[5],      //Email
            gender:arr2[6],     //性别,性别: 1男，0女
            'mobile':arr2[7],     //手机号-mobile
            'phone':arr2[8],     //电话号-phone，座机号
            grain:"0"  //财富收益是必填项，给值0
          }

          if( !(userInfo.gender==0 || userInfo.gender==1)){   
            this.errorMessage("性别只能输入数字: 1男，0女");
            return;
          }

          console.log(userInfo);
          let msg="第"+(i+1)+"行添加成功："+rowStr;
          //this.successMessage(msg);
           
          //保存
          this.saveDataInfo(state,this.$api.memberSave,userInfo,"list");
        }

      }      
    },
    update(){

    }
  },
  created() {
    //初始获取数据
    this.getDataInfo(this.id);
  }
};
</script>

<style>

</style>
