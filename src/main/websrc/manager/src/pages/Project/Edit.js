import React, { Component, Fragment } from 'react';
import { Card, Tabs, Form, Input, Divider, Button, Select, Row, Col, Cascader, Modal, Radio, Checkbox, DatePicker, Upload, message, Icon } from 'antd';
import styles from './style.less';
import moment from 'moment';
import imgpng from '@/assets/img.png';
import classNames from 'classnames';
import { connect } from 'dva';
import router from 'umi/router';

const RadioGroup = Radio.Group;
const TabPane = Tabs.TabPane;
const Option = Select.Option;
const FormItem = Form.Item;
const formProps = {
    labelCol:{span: 4},
    wrapperCol:{ span: 8 },
};
const formUploadProps = {
  labelCol:{span: 4},
  wrapperCol:{ span: 20 },
};
const formUploadProps1 = {
  labelCol:{span: 4},
  wrapperCol:{ span: 12 },
};
const subjects = ['语文','数学','英语','物理','化学','生物','科学','信息技术','通用技术','音乐','体育','美术','政治','历史','地理'];

@connect(({ project, loading }) => ({
  project,
  loading: loading.models.project,
}))
@Form.create()
export default class Edit extends Component{
  state = {
    areaIdIndex:{},
    visible:false,//添加学校弹窗
    index:null,//添加学校index
    group:null,
    areaId:null,//添加学校区域
    key:'1',//tab的key
    category_item:{
        paramSetting:[],
        stages:[],
        maxMember:1,
        minMember:0,
        group:[]
    },//选择比赛列表后基础信息详情
    count:null,//基础信息成员数量
    teachersCount:null,//指导老师信息成员数量
    areaOption:{},//基础信息区域选择
    status:{},//左侧tab目录
    project_item:{
      members:[],
      areas:{},

    },//项目基础信息详情
    school_list:{},//基础信息学校选择
    project_img:{},//项目基础信息图片
    tab_status:{},//信息完成状态
    plan:null,//计划书
  }

  showModal = (index) => {
    const { dispatch,form:{getFieldValue} } = this.props;
    const areaId = getFieldValue(`members[${index}][areaId]`);
    const group = getFieldValue(`members[${index}][group]`);
    if(!areaId){
      message.error('请先选择区域！');
      return
    }
    if(!group){
      message.error('请先选择组别！');
      return
    }
    this.setState({
      visible: true,
      index:index,
      areaId:areaId[areaId.length-1],
      group:group
    });
  }

  handleOk = (e) => {
    const { dispatch,form:{validateFields,resetFields} } = this.props;
    const { areaId } = this.state;
    const self = this;
    validateFields(['schoolName',],(err, values) => {
      if (!err) {
        dispatch({
          type:'project/fetch_school_add',
          payload:{
            name:values.schoolName,
            areaId:areaId
          },
          callback:()=>{
            self.setState({
              visible: false,
            });
            resetFields(['schoolName']);
            dispatch({
              type:'project/fetch_school_list',
              payload:{
                  areaId:areaId,
                  start:0,
                  count:100000
              },
              callback:(list)=>{
                  const { school_list, index } = self.state;
                  school_list[`index${index}`] = list;
                  self.setState({
                    school_list:school_list,
                  }) 
              }
            })
          }
        })
      }
    })
  }

  closeModal = () => {
    this.setState({
      visible: false,
    });
  }

  normFile = (e) => {
    if (Array.isArray(e)) {
      return e;
    }
    if (e.file.status) {
      return e && e.fileList;
    }else{
      return e && e.fileList && e.fileList.filter(el=>{
        if(el.response){
          return true;
        }else{
          return false;
        }
      });
    }
  };

  getTabLabel = (key) => {
    const { category_item } = this.state;
    switch (key) {
        case '1':
        return '基础信息';
        case '2':
        return (category_item && category_item.name === '科幻文学赛') ?"文章基础信息": "项目基础信息";
        case '3':
        return '指导老师信息';
        case '4':
        return '初赛项目资料';
        case '5':
        return '复赛项目资料';
        case '6':
        return '决赛项目资料';
        case '7':
        return '提交状态';
    }
  }

  onChange = (key) => {
    const { dispatch, form:{ resetFields } } = this.props;
    if(key==='7'){
      const { status } = this.state;
      dispatch({
        type:'project/fetch_status',
        payload:{
         id:status.id
        },
        callback:(data)=>{
          this.setState({
            tab_status:data,
            key:key
          })
        }
      })
    }else if(key==='2'){
        const { status } = this.state;
        dispatch({
            type:'project/fetch_project_item',
            payload:{
             id:status.id
            },
            callback:(data)=>{
              this.setState({
                project_item:data,
                key:key
              })
            }
          })
    }else if(key==='3'){
        const { status } = this.state;
        dispatch({
            type:'project/fetch_teacher_item',
            payload:{
             id:status.id
            },
            callback:(data)=>{
              this.setState({
                teachersCount:data.teachers.length,
                key:key
              })
              resetFields(['teacher_number']);
            }
          })
    }else if(key==='4'){
      const { status } = this.state;
      dispatch({
        type:'project/fetch_material_item',
        payload:{
         id:status.id,
         stageType:1
        },
        params:{
            save:true
        },
        callback:(data)=>{
          this.setState({
            plan:data.plan,
            key:key
          })
        }
      })
    }else if(key==='5'){
      const { status } = this.state;
      dispatch({
        type:'project/fetch_material_item',
        payload:{
         id:status.id,
         stageType:2
        },
        params:{
            save:true
        },
        callback:(data)=>{
          this.setState({
            plan:data.plan,
            key:key
          })
        }
      })
    }else if(key==='6'){
      const { status } = this.state;
      dispatch({
        type:'project/fetch_material_item',
        payload:{
         id:status.id,
         stageType:3
        },
        params:{
            save:true
        },
        callback:(data)=>{
          this.setState({
            plan:data.plan,
            key:key
          })
        }
      })
    }else{
      this.setState({
        key
      })
    }
  }

  componentDidMount(){
    const { dispatch,match:{ params:{id} } } = this.props;
    dispatch({
        type:'project/fetch_basis_item',
        payload:{
          id:id
        },
        callback:(basis)=>{
            let areaIdIndex  ={};
            basis.members.forEach((element,index) => {
              areaIdIndex[`index${index}`] = true;
              dispatch({
                type:'project/fetch_school_list',
                payload:{
                    areaId:element.areas.district.id,
                    start:0,
                    count:100000,
                    group:element.group
                },
                callback:(list)=>{
                    const { school_list } = this.state;
                    school_list[`index${index}`] = list;
                    this.setState({
                      school_list:school_list,
                    }) 
                }
                })
            });
            this.setState({
              areaIdIndex:areaIdIndex,
            })
            dispatch({
                type:'project/fetch_category_item',
                payload:{
                 id:basis.category.id
                },
                callback:(data)=>{
                  let areaOptions={};
                  outer:
                  for (let i = 0; i < basis.members.length; i++) {
                    const e = basis.members[i];
                    e.areas.province.tree = e.areas.province.tree.map(ele=>{
                      return{
                        value: ele.key,
                        label: ele.title,
                        children:e.areas.city.tree.map(ele1=>{
                          return {
                            value: ele1.key,
                            label: ele1.title,
                            isLeaf:true
                          }
                        })
                      }
                    })
                    inner:
                    for (let j = 0; j < data.area.length; j++) {
                      const el = data.area[j];
                      if(e.areas.province.id === el.id){
                        areaOptions[i] = [{
                          value: e.areas.province.id,
                          label: e.areas.province.name,
                          children:e.areas.province.tree
                        }];
                      }
                    }
                  }
                  this.setState({
                    category_item:data,
                    count:basis.count,
                    areaOption:areaOptions
                  })
                }
              })  
        }
    }) 
    dispatch({
        type:'project/fetch_status',
        payload:{
         id:id
        },
        callback:(data)=>{
          this.setState({
            status:data,
          })
        }
      })
    dispatch({
      type:'project/fetch_category_list',
      payload:{
        start:0,
        count:1000
      }
    })  
  }

  disabledEndDate = (current) => {
    return current && current > moment().endOf('day');
  }

  normBeforeUpload = (file) => {
    const is200KB = 1024*200;
    const size = file.size;
    if((size>is200KB)){
      message.error('图片大小不能超过200KB!');
      return false;
    }else{
      return true;
    }
  }

  fileMove = (file,id,stage,fileType) => {
    const { dispatch } = this.props;
    if(file.response){
      dispatch({
        type:'global/fetch_del_file',
        payload:{
         path:file.response.value,
         id:id,
         stage:stage,
         fileType:fileType
        }
      })
    }
    return true;
  }

  normFileUpload = (e,maxNum) => {
    if (Array.isArray(e)) {
      return e;
    }
    if (e.file.status) {
      return e && e.fileList;
    }else{
      return e && e.fileList && e.fileList.filter(el=>{
        if(el.response){
          return true;
        }else{
          return false;
        }
      });
    }
  };

  beforeUpload = (file,name,maxNum) => {
    const { form: {getFieldValue} } = this.props;
    const value = getFieldValue(name);
    if(value && value.length===maxNum){
      message.warning(`只能上传${maxNum}个文件！`);
      return false;
    }
    const isImg = file.name.includes('.jpg') || file.name.includes('.jpeg') || file.name.includes('.png');
    const isVideo = file.name.includes('.mp4') || file.name.includes('.wmv');
    const size = file.size;
    const isLt512M = 1024*1024*512;
    const isLt10M = 1024*1024*10;
    if(isImg){
      if((size>isLt10M)){
        message.error('图片大小不能超过10MB!');
        return false;
      }else{
        return true;
      }
    }
    if(isVideo){
      if((size>isLt512M)){
        message.error('视频大小不能超过512MB!');
        return false;
      }else{
        return true;
      }
    }
  }

  categoryChange = (value) => {
    const { dispatch, form:{ setFieldsValue } } = this.props;
    const { count } = this.state;
    dispatch({
      type:'project/fetch_category_item',
      payload:{
       id:value
      },
      callback:(data)=>{
        let areaOptions={};
        for (let index = 0; index < count; index++) {
          areaOptions[index] = data.area.map(e=>{
            return {
              value: e.id,
              label: e.name,
              isLeaf:false
            }
          })
          setFieldsValue({[`members[${index}][areaId]`]:undefined});
          setFieldsValue({[`members[${index}][schoolId]`]:undefined});
        }
        setFieldsValue({'count':undefined});
        this.setState({
          count:null,
          category_item:data,
          areaOption:areaOptions
        })
      }
    })  
  }

  loadCascaderData = (selectedOptions,index) => {
    const { dispatch } = this.props;
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    dispatch({
      type:'project/fetch_area_tree',
      payload:{
       id:targetOption.value
      },
      params:{
        notSetData:false
      },
      callback:(data)=>{
        targetOption.loading = false;
        targetOption.children = data.map(e=>{
          return {
            label: e.title,
            value: e.key,
            isLeaf:e.isLeaf
          }
        })
        this.setState({
          areaOption: {
            ...this.state.areaOption,
            // [this.state.areaOption[index]]:targetOption
          },
        });
      }
    })  
  }

  countChange = (value) => {
    const { category_item, areaOption } = this.state;
      let areaOptions={};
      for (let index = 0; index < value; index++) {
        if(!areaOption[index]){
          areaOptions[index] = category_item.area.map(e=>{
            return {
              value: e.id,
              label: e.name,
              isLeaf:false
            }
          })
        }else{
          areaOptions[index] = areaOption[index];
        }
      }
      this.setState({
        count:value,
        areaOption:{
          ...areaOptions,
        }
      })
  }

  teachersChange = (value) => {
    this.setState({
      teachersCount:value
    })
  }

  cascaderChange = (value,selectedOptions,index) => {
    const { dispatch, form:{ setFieldsValue } } = this.props;
    const { areaIdIndex } = this.state;
    this.setState({
      areaId:value[value.length-1],
      areaIdIndex:{
        ...areaIdIndex,
        [`index${index}`]:true
      }
    })
    setFieldsValue({[`members[${index}][schoolId]`]:undefined});
    setFieldsValue({[`members[${index}][group]`]:undefined});
  }

  groupChange = (value,index) => {
    const { dispatch, form:{ setFieldsValue, getFieldValue } } = this.props;
    let { school_list } = this.state;
    let areaId = getFieldValue(`members[${index}][areaId]`);
    areaId = areaId[areaId.length-1];
    dispatch({
      type:'project/fetch_school_list',
      payload:{
        areaId:areaId,
        start:0,
        count:100000,
        group:value
      },
      callback:(list)=>{
        school_list[`index${index}`] = list;
        this.setState({
          school_list
        })
        setFieldsValue({[`members[${index}][schoolId]`]:undefined});
      }
    })
  }

  submitBasis = () => {
    const { dispatch,form:{validateFields} } = this.props;
    const { key, status } = this.state;
    const self = this;
    validateFields(['members','categoryId','count'],(err, values) => {
      if (!err) {
        values.members.forEach(element => {
          element.areaId = element.areaId[element.areaId.length-1];
          element.schoolId = element.schoolId.key;
        });
        if(status.id){
          dispatch({
            type:'project/fetch_basis_upd',
            payload:{
             ...values,
              id:status.id,
            },
            callback:(id)=>{
              dispatch({
                type:'project/fetch_status',
                payload:{
                 id:id
                },
                callback:(data)=>{
                  self.setState({
                    status:data,
                    // key:'2'
                  })
                }
              })
              dispatch({
                type:'project/fetch_project_item',
                payload:{
                 id:id
                },
                callback:(data)=>{
                  self.setState({
                    project_item:data,
                  })
                }
              })
            }
          })
        }else{
          dispatch({
            type:'project/fetch_basis_add',
            payload:{
             ...values
            },
            callback:(id)=>{
              dispatch({
                type:'project/fetch_status',
                payload:{
                 id:id
                },
                callback:(data)=>{
                  self.setState({
                    status:data,
                    // key:'2'
                  })
                }
              })
              dispatch({
                type:'project/fetch_project_item',
                payload:{
                 id:id
                },
                callback:(data)=>{
                  self.setState({
                    project_item:data,
                  })
                }
              })
            }
          })
        }
      }
    })
  }

  submitProject = () => {
    const { dispatch,form:{validateFields} } = this.props;
    const { key } = this.state;
    const self = this;
    validateFields(['name','desc','address','telephone','project_members'],(err, values) => {
      if (!err) {
        values.id = this.state.status.id;
        values.project_members.forEach(e=>{
          e.birthday = moment(e.birthday).format('YYYY/MM/DD');
          e.imagepath = e.imagepath[e.imagepath.length-1].response.value;
        })
        values.members = values.project_members;
        delete values.project_members;
        dispatch({
          type:'project/fetch_project_add',
          payload:{
           ...values
          },
          callback:()=>{
            dispatch({
              type:'project/fetch_status',
              payload:{
               id: self.state.status.id
              },
              callback:(data)=>{
                self.setState({
                  status:data,
                })
              }
            })
          }
        })
      }
    })
  }

  submitTeachers = () => {
    const { dispatch,form:{validateFields} } = this.props;
    const { key } = this.state;
    const self = this;
    validateFields(['teachers'],(err, values) => {
      if (!err) {
        values.id = this.state.status.id;
        if(values.teachers){
          if(values.teachers.length === 2){
            if(values.teachers[0].number === values.teachers[1].number){
              message.error('指导老师身份不能相同!');
              return
            }
          }
        }else{
          values.teachers = [];
        }
        dispatch({
          type:'project/fetch_teacher_add',
          payload:{
           ...values
          },
          callback:()=>{
            dispatch({
              type:'project/fetch_status',
              payload:{
               id: self.state.status.id
              },
              callback:(data)=>{
                self.setState({
                  status:data,
                })
              }
            })
          }
        })
      }
    })
  }

  submitPath = (type) => {
    const { dispatch,form:{validateFields} } = this.props;
    const { key } = this.state;
    const self = this;
    if(type === 1){
      validateFields(['pptPath1','docPath1','videoPath1','imagePath1'],(err, values) => {
        if (!err) {
          values.id = self.state.status.id;
          values.stageType = type;
          if(values.docPath1){
            values.docPath = values.docPath1.map(e=>e.response.value);
            delete values.docPath1;
          }
          if(values.imagePath1){
            values.imagePath = values.imagePath1.map(e=>e.response.value);
            delete values.imagePath1;
          }
          if(values.pptPath1){
            values.pptPath = values.pptPath1.map(e=>e.response.value);
            delete values.pptPath1;
          }
          if(values.videoPath1){
            values.videoPath = values.videoPath1.map(e=>e.response.value);
            delete values.videoPath1;
          }
          dispatch({
            type:'project/fetch_material_add',
            payload:{
             ...values
            },
            callback:()=>{
              dispatch({
                type:'project/fetch_status',
                payload:{
                 id: self.state.status.id
                },
                callback:(data)=>{
                  self.setState({
                    status:data,
                  })
                }
              })
            }
          })
        }
      })
    }else if(type === 2){
      validateFields(['pptPath2','docPath2','videoPath2','imagePath2'],(err, values) => {
        if (!err) {
          values.id = self.state.status.id;
          values.stageType = type;
          if(values.docPath2){
            values.docPath = values.docPath2.map(e=>e.response.value);
            delete values.docPath2;
          }
          if(values.imagePath2){
            values.imagePath = values.imagePath2.map(e=>e.response.value);
            delete values.imagePath2;
          }
          if(values.pptPath2){
            values.pptPath = values.pptPath2.map(e=>e.response.value);
            delete values.pptPath2;
          }
          if(values.videoPath2){
            values.videoPath = values.videoPath2.map(e=>e.response.value);
            delete values.videoPath2;
          }
          dispatch({
            type:'project/fetch_material_add',
            payload:{
             ...values
            },
            callback:()=>{
              dispatch({
                type:'project/fetch_status',
                payload:{
                 id: self.state.status.id
                },
                callback:(data)=>{
                  self.setState({
                    status:data,
                  })
                }
              })
            }
          })
        }
      })
    }else if(type === 3){
      validateFields(['pptPath3','docPath3','videoPath3','imagePath3'],(err, values) => {
        if (!err) {
          values.id = self.state.status.id;
          values.stageType = type;
          if(values.docPath3){
            values.docPath = values.docPath3.map(e=>e.response.value);
            delete values.docPath3;
          }
          if(values.imagePath3){
            values.imagePath = values.imagePath3.map(e=>e.response.value);
            delete values.imagePath3;
          }
          if(values.pptPath3){
            values.pptPath = values.pptPath3.map(e=>e.response.value);
            delete values.pptPath3;
          }
          if(values.videoPath3){
            values.videoPath = values.videoPath3.map(e=>e.response.value);
            delete values.videoPath3;
          }
          dispatch({
            type:'project/fetch_material_add',
            payload:{
             ...values
            },
            callback:()=>{
              dispatch({
                type:'project/fetch_status',
                payload:{
                 id: self.state.status.id
                },
                callback:(data)=>{
                  self.setState({
                    status:data,
                  })
                }
              })
            }
          })
        }
      })
    }
  }

  render(){
    const { key, category_item, count, areaOption, status, project_item, school_list, project_img, teachersCount, tab_status, plan, areaId, areaIdIndex } = this.state;
    const { form:{getFieldDecorator}, project:{ category_list, project_info }, loading, match:{ params:{id} } } = this.props;
    let countOption = [],categoryCount=[],teachers=[],maxFileNum1={},maxFileNum2={},maxFileNum3={};
    for (let index = category_item.minMember; index < category_item.maxMember+1; index++) {
        countOption.push(<Option key={index} value={index}>{index}</Option>);
    }
    let disabled = false;
    Object.values(tab_status).forEach(e => {
      if(!e){
        disabled = true;
      }
    });
    category_item.stages.forEach(e => {
    if(e.type === 1){
        maxFileNum1 = e;
    }else if(e.type === 2){
        maxFileNum2 = e;
    }else if(e.type === 3){
        maxFileNum3 = e;
    }
    });
    const basis = project_info[id] && project_info[id].basis || {};
    const teacher = project_info[id] && project_info[id].teacher || {};
    const material1 = project_info[id] && project_info[id].material1 || {};
    const material2 = project_info[id] && project_info[id].material2 || {};
    const material3 = project_info[id] && project_info[id].material3 || {};
    if(count !== null){
      for (let index = 0; index < count; index++) {
        categoryCount.push(
          <Row key={index} gutter={16} style={{marginTop:12}}>
            <Col span={4} style={{paddingRight:3}}>
            </Col>
            <Col span={2}>
            <FormItem>
              {getFieldDecorator(`members[${index}][name]`, {
                  rules: [{ required: true, message: '姓名不能为空' }],
                  initialValue:basis.members[index] && basis.members[index].name || undefined,
              })(
                <Input />
              )}
            </FormItem>
            </Col>
            <Col span={3}>
            <FormItem>
              {getFieldDecorator(`members[${index}][telephone]`, {
                  rules: [{ required: true, message: '手机号不能为空' },{ pattern: /(?:^1[3456789]|^9[28])\d{9}$/, message: '请输入正确手机号' },],
                  initialValue:basis.members[index] && basis.members[index].telephone || undefined,
              })(
                <Input />
              )}
            </FormItem>
            </Col>
            <Col span={3}>
            <FormItem>
              {getFieldDecorator(`members[${index}][idCard]`, {
                  rules: [{ required: true, message: '身份证号不能为空' },{ pattern: /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/, message: '请输入正确身份证号' },],
                  initialValue:basis.members[index] && basis.members[index].idCard || undefined,
              })(
                <Input />
              )}
            </FormItem>
            </Col>
            <Col span={3}>
            <FormItem>
              {getFieldDecorator(`members[${index}][email]`, {
                  rules: [{ required: true, message: '邮箱不能为空' },{ pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/, message: '请输入正确邮箱' }],
                  initialValue:basis.members[index] && basis.members[index].email || undefined,
              })(
                <Input />
              )}
            </FormItem>
            </Col>
            <Col span={3}>
            <FormItem>
              {getFieldDecorator(`members[${index}][areaId]`, {
                  rules: [{ required: true, message: '区域不能为空' }],
                  initialValue:basis.members[index] && [basis.members[index].areas.province.id,basis.members[index].areas.city.id,basis.members[index].areas.district.id] || undefined,
              })(
                <Cascader  allowClear={false} placeholder='' onChange={(value,selectedOptions)=>this.cascaderChange(value,selectedOptions,index)} loadData={(selectedOptions)=>this.loadCascaderData(selectedOptions,index)} options={areaOption[index]} style={{width:'100%'}} />
              )}
            </FormItem>
            </Col>
            <Col span={2}>
            <FormItem>
              {getFieldDecorator(`members[${index}][group]`, {
                  rules: [{ required: true, message: '组别不能为空' }],
                  initialValue:basis.members[index] && basis.members[index].group || undefined,
              })(
                <Select onChange={(value)=>this.groupChange(value,index)}  style={{width:'100%'}}>
                {
                  areaIdIndex[`index${index}`] && category_item.group.map(e=>{
                    if(e === 1){
                      return <Option key={e} value={e}>小学</Option>
                    }else if(e === 2){
                      return <Option key={e} value={e}>初中</Option>
                    }else if(e === 3){
                      return <Option key={e} value={e}>高中</Option>
                    }else if(e === 4){
                      return <Option key={e} value={e}>中学</Option>
                    }
                  })
                }
                </Select>
              )}
            </FormItem>
            </Col>
            <Col span={4}>
            <FormItem>
              {getFieldDecorator(`members[${index}][schoolId]`, {
                  rules: [{ required: true, message: '学校不能为空' }],
                  initialValue:basis.members[index] && {key:basis.members[index].school.id,label:basis.members[index].school.name} || undefined,
              })(
                <Select  labelInValue style={{width:'100%'}} dropdownRender={menu => {
                      return (
                        <div onMouseDown={e => {
                          e.preventDefault();
                      }}>
                          {menu}
                          <Divider style={{ margin: '4px 0' }} />
                          <div onClick={()=>this.showModal(index)} style={{ padding: '8px', cursor: 'pointer' }}>
                            <Icon type="plus" /> 添加学校
                          </div>
                        </div>
                      )
                }}>
                  {
                    school_list[`index${index}`] && school_list[`index${index}`].map(e=>{
                      return (
                      <Option key={e.id} value={e.id}>
                          {e.name}
                      </Option>
                      )
                    })
                  }
                </Select>
              )}
            </FormItem>
            </Col>
        </Row>
        );
      }
    } 
    if(teachersCount !== null){
      for (let index = 0; index < teachersCount; index++) {
        teachers.push(
        <Row key={index} gutter={16} style={{marginTop:12}}>
          <Col span={4} style={{paddingRight:3}}>
          </Col>
          <Col span={4}>
          {
              teachersCount === 1 ?(
                <FormItem>
                    {getFieldDecorator(`teachers[${index}][number]`, {
                        rules: [{ required: true, message: '身份不能为空' }],
                        initialValue:teacher.teachers[index] && teacher.teachers[index].number.toString() || undefined,
                    })(
                      <Select  style={{width:'100%'}}>
                        <Option value='1'>
                          第一指导老师
                        </Option>
                      </Select>
                    )}
                  </FormItem>
              ):(
                <FormItem>
                    {getFieldDecorator(`teachers[${index}][number]`, {
                        rules: [{ required: true, message: '身份不能为空' }],
                        initialValue:teacher.teachers[index] && teacher.teachers[index].number.toString() || undefined,
                    })(
                      <Select  style={{width:'100%'}}>
                        <Option value='1'>
                          第一指导老师
                        </Option>
                        <Option value='2'>
                        第二指导老师
                      </Option>
                      </Select>
                    )}
                  </FormItem>
              )
          }
          </Col>
          <Col span={4}>
          <FormItem>
            {getFieldDecorator(`teachers[${index}][name]`, {
                rules: [{ required: true, message: '姓名不能为空' }],
                initialValue:teacher.teachers[index] && teacher.teachers[index].name || undefined,
            })(
              <Input />
            )}
          </FormItem>
          </Col>
          <Col span={4}>
          <FormItem>
            {getFieldDecorator(`teachers[${index}][gender]`, {
                rules: [{ required: true, message: '性别不能为空' }],
                initialValue:teacher.teachers[index] && teacher.teachers[index].gender || undefined,
            })(
              <Select  style={{width:'100%'}}>
                  <Option value='男'>
                      男
                  </Option>
                  <Option value='女'>
                      女
                  </Option>
              </Select>
            )}
          </FormItem>
          </Col>
          {
              category_item.paramSetting.includes(2) && (
                <Col span={4}>
                  <FormItem>
                    {getFieldDecorator(`teachers[${index}][subject]`, {
                        rules: [{ required: true, message: '教授学科不能为空' }],
                        initialValue:teacher.teachers[index] && teacher.teachers[index].subject || undefined,
                    })(
                      <Select  style={{width:'100%'}}>
                      {
                        subjects.map(e=><Option key={e} value={e}>
                          {e}
                      </Option>)
                      }
                      </Select>
                    )}
                  </FormItem>
                </Col>
              )
          }
          <Col span={4}>
          <FormItem>
            {getFieldDecorator(`teachers[${index}][telephone]`, {
                rules: [{ required: true, message: '手机号不能为空' },{ pattern: /(?:^1[3456789]|^9[28])\d{9}$/, message: '请输入正确手机号' }],
                initialValue:teacher.teachers[index] && teacher.teachers[index].telephone || undefined,
            })(
              <Input />
            )}
          </FormItem>
          </Col>
      </Row>)
      }
    }
    return (
        <div style={{background:'#fff'}}>
        <Modal
          title="添加学校"
          visible={this.state.visible}
          onOk={this.handleOk}
          confirmLoading={loading}
          onCancel={this.closeModal}
        >
          <FormItem
            {...formUploadProps}
            label="学校名称"  
          >
            {getFieldDecorator('schoolName', {
                rules: [{ required: true, message: '学校名称不能为空' }],
            })(
              <Input placeholder="请输入学校名称"/>
            )}
          </FormItem>
        </Modal>
        <div className={styles.join}>
          <Card bordered={false}>
            <Tabs
              activeKey={key}
              tabPosition='left'
              onChange={this.onChange}
            >
              <TabPane tab="基础信息" key='1'>
                <FormItem
                    {...formProps}
                    label="比赛类别"
                    >
                    {getFieldDecorator('categoryId', {
                        rules: [{ required: true, message: '比赛类别不能为空' }],
                        initialValue:basis.category && basis.category.id || undefined,
                    })(
                      <Select disabled onChange={this.categoryChange} placeholder="请选择比赛类别" style={{width:'100%'}}>
                        {
                          category_list.map(e=>{
                            return (
                              <Option key={e.id} value={e.id}>
                                  {e.name}
                              </Option>
                            )
                          })
                        }
                      </Select>
                    )}
                </FormItem>
                <FormItem
                    {...formProps}
                    label="选择成员数量"
                    >
                    {getFieldDecorator('count', {
                        rules: [{ required: true, message: '成员数量不能为空' }],
                        initialValue:basis.count,
                    })(
                        <Select  placeholder="请选择成员数量" onChange={this.countChange} style={{width:'100%'}}>
                            {countOption}
                        </Select>
                    )}
                </FormItem>
                {
                  count && (
                    <Fragment>
                      <Row gutter={16} style={{marginTop:12}}>
                          <Col span={4} style={{textAlign:'right',color:'#000',paddingRight:3}}>
                            <span className="ant-form-item-required">
                              成员基础信息 :
                            </span>
                          </Col>
                      </Row>
                      <Row gutter={16} style={{textAlign:'center'}}>
                        <Col span={4}>
                          </Col>
                          <Col span={2}>
                            姓名
                          </Col>
                          <Col span={3}>
                          {
                              category_item.telephoneType === 1?'家长手机号':'手机号'
                          }
                          </Col>
                          <Col span={3}>
                          身份证号
                          </Col>
                          <Col span={3}>
                            邮箱
                          </Col>
                          <Col span={3}>
                            区域
                          </Col>
                          <Col span={2}>
                            组别
                          </Col>
                          <Col span={4}>
                            学校
                          </Col>
                      </Row>
                      {categoryCount}
                      <Row gutter={16}>
                        <Col span={4}>
                        </Col>
                        <Col span={20}>
                          <span style={{color:'#f5222d'}}>注：姓名、手机号、身份证号不能重复</span>
                        </Col>
                      </Row>
                    </Fragment>
                  )
                }
                <div style={{textAlign:'center'}}>
                    <Button type="primary" onClick={this.submitBasis}>保存</Button>
                  </div>
              </TabPane>
              {
                status['2'] !== undefined && (
                  <TabPane tab={(category_item && category_item.name === '科幻文学赛') ?"文章基础信息": "项目基础信息"} key='2'>
                <FormItem
                    {...formProps}
                    label="比赛号码"
                    >
                    {project_item.number}
                </FormItem>
                {
                  category_item.paramSetting.includes(4) && (
                    <Fragment>
                      <FormItem
                          {...formProps}
                          label={(category_item && category_item.name === '科幻文学赛') ?"文章名称": "项目名称"}
                          >
                          {getFieldDecorator('name', {
                              rules: [{ required: true, message: '名称不能为空' }],
                              initialValue:project_item.name,
                          })(
                            <Input />
                          )}
                      </FormItem>
                      <FormItem
                          {...formProps}
                          label={(category_item && category_item.name === '科幻文学赛') ?"文章简介": "项目简介"}
                          >
                          {getFieldDecorator('desc', {
                              rules: [{ required: true, message: '简介不能为空' }],
                              initialValue:project_item.desc,
                          })(
                            <Input.TextArea  style={{minHeight:120}} placeholder="请输入项目简介"/>
                          )}
                      </FormItem>
                    </Fragment>
                  )
                }
                <Row>
                  <Col span={8}>
                    <FormItem
                      labelCol={{span: 12}}
                      wrapperCol={{span: 12}}
                      label="地区"
                      >
                          <div>{Object.values(project_item.areas).join('/')}</div>
                    </FormItem>
                  </Col>
                  <Col span={12}>
                    <FormItem>
                      <div style={{marginLeft:24}}>学校 :{project_item.school}</div>
                    </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col span={8}>
                    <FormItem
                      labelCol={{span: 12}}
                      wrapperCol={{span: 12}}
                      label="寄送地址"
                      >
                          {getFieldDecorator('address', {
                              rules: [{ required: true, message: '寄送地址不能为空' }],
                              initialValue:project_item.address,
                          })(
                            <Input />
                          )}
                    </FormItem>
                  </Col>
                  <Col span={12}>
                    <FormItem
                      labelCol={{span: 4}}
                      wrapperCol={{span: 10}}
                      label="联系方式"
                    >
                          {getFieldDecorator('telephone', {
                              rules: [{ required: true, message: '联系方式不能为空' },{ pattern: /(?:^1[3456789]|^9[28])\d{9}$/, message: '请输入正确手机号' }],
                              initialValue:project_item.telephone,
                          })(
                            <Input />
                          )}
                    </FormItem>
                  </Col>
                </Row>
                <Row gutter={16} style={{marginTop:12}}>
                   <Col span={4} style={{textAlign:'right',color:'#000',paddingRight:3}}>
                      <span className="ant-form-item-required">
                        成员详细资料 :
                      </span>
                   </Col>
                   <Col span={19}>
                    {
                      project_item.members.map((e,i)=>{
                        return (
                            <div key={i} style={{border:'1px solid #d9d9d9',padding:24,borderRadius:4,background:'#f4f4f4',marginTop:12}}>
                                <p style={{color:'#005d7e',textAlign:'center',fontSize:18,fontWeight:500}}>
                                {e.name}
                              </p>
                              <Row gutter={8}>
                              {
                                category_item.paramSetting.includes(1) && (
                                  <Col span={12}>
                                    <FormItem
                                      labelCol={{span: 8}}
                                      wrapperCol={{span: 10}}
                                      label="选择成员身份"
                                    >
                                          {getFieldDecorator(`project_members[${i}][type]`, {
                                              rules: [{ required: true, message: '成员身份不能为空' }],
                                              initialValue:e.type && e.type.toString() || undefined,
                                          })(
                                            <RadioGroup  options={[{ label: '队长', value: '1' }, { label: '成员', value: '2' }]} />
                                          )}
                                    </FormItem>
                                  </Col>
                                )
                              }
                                <Col span={12}>
                                  <div style={{display:'flex',justifyContent: 'space-around'}}>
                                      <div>
                                        <div>
                                            <img src={ project_img[`index${i}`] || e.iamgePath || imgpng} style={{marginBottom:12,height:120,width:90}} alt=""/>
                                        </div>
                                        <FormItem style={{textAlign:'center'}}>
                                                {getFieldDecorator(`project_members[${i}][imagepath]`, {
                                                    rules: [{ required: true, message: '请上传照片' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: this.normFile,
                                                    initialValue:e.iamgePath && [{
                                                        uid: '-1',
                                                        name: 'xxx.png',
                                                        status: 'done',
                                                        url:e.iamgePath,
                                                        response:{
                                                            value:e.iamgePath
                                                        }
                                                      }] || undefined,
                                                })(
                                                    <Upload                   
                                                        onChange={({file})=>{
                                                          if(file.status === 'done'){
                                                            this.setState({
                                                              project_img:{
                                                                ...this.state.project_img,
                                                                [`index${i}`]:file.response.value,
                                                              }
                                                            })
                                                          }
                                                        }}
                                                        
                                                        beforeUpload={this.normBeforeUpload}
                                                        showUploadList={false}
                                                        name="file"
                                                        listType="text"
                                                        action={'/api/file/upload?fileType=4'}
                                                        accept=".png,.jpg,.jpeg"
                                                    >
                                                        <Button  loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                    </Upload>         
                                                )}
                                          </FormItem>
                                      </div>
                                      <div style={{width:200}}>
                                      上传的照片需符合2寸免冠彩色近照，图片尺寸为高640像素，宽480像素，不超过200KB
                                    </div>
                                  </div>
                                </Col>
                              </Row>
                              <Row gutter={8} style={{marginTop:24}}>
                                <Col span={12}>
                                  <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="性别"
                                  >
                                  <span>{e.gender}</span>
                                  </FormItem>
                                </Col>
                                <Col span={12}>
                                  <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="生日"
                                  >
                                         <span>{e.birthday}</span>
                                  </FormItem>
                                </Col>
                                <Col span={12}>
                                <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="身份证号码"
                                  >
                                        <span>{e.idCard}</span>
                                  </FormItem>
                                </Col>
                                <Col span={12}>
                                  <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="城市"
                                  >
                                        <div>{Object.values(e.areas).join('/')}</div>
                                  </FormItem>
                                </Col>
                                <Col span={12}>
                                  <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="学校"
                                  >
                                        <span>{e.school}</span>
                                  </FormItem>
                                </Col>
                                <Col span={12}>
                                <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="年级"
                                  >
                                        {getFieldDecorator(`project_members[${i}][grade]`, {
                                            rules: [{ required: true, message: '年级不能为空' }],
                                            initialValue:e.grade
                                        })(
                                          e.group === 1? <Select style={{width:'100%'}}>
                                                      <Option value="一年级">一年级</Option>
                                                      <Option value="二年级">二年级</Option>
                                                      <Option value="三年级">三年级</Option>
                                                      <Option value="四年级">四年级</Option>
                                                      <Option value="五年级">五年级</Option>
                                                      <Option value="六年级">六年级</Option>
                                                  </Select> :
                                            e.group === 2?  <Select style={{width:'100%'}}>
                                                      <Option value="初一">初一</Option>
                                                      <Option value="初二">初二</Option>
                                                      <Option value="初三">初三</Option>
                                                  </Select> :
                                            e.group === 3?  <Select style={{width:'100%'}}>
                                                      <Option value="高一">高一</Option>
                                                      <Option value="高二">高二</Option>
                                                      <Option value="高三">高三</Option>
                                                  </Select> :
                                            <Select style={{width:'100%'}}>
                                              <Option value="初一">初一</Option>
                                              <Option value="初二">初二</Option>
                                              <Option value="初三">初三</Option>
                                              <Option value="高一">高一</Option>
                                              <Option value="高二">高二</Option>
                                              <Option value="高三">高三</Option>
                                            </Select>
                                        )}
                                  </FormItem>
                                </Col>
                                {
                                  category_item.paramSetting.includes(5) && (
                                <Col span={12}>
                                <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 12}}
                                    label="护照号码"
                                  >
                                        {getFieldDecorator(`project_members[${i}][passport]`, {
                                            rules: [{ pattern: /^([a-zA-z]|[0-9]){5,17}$/, message: '请输入正确护照号码' }],
                                            initialValue:e.passport
                                        })(
                                          <Input />
                                        )}
                                  </FormItem>
                                </Col>
                                  )
                                }
                              </Row>
                              {
                                  category_item.paramSetting.includes(5) && (
                                    <Fragment>
                                    <Row gutter={8}>
                                    <Col span={12}>
                                      <Col span={8} style={{color:'#000',textAlign:'right'}}>
                                        家长信息 :
                                      </Col>
                                    </Col>
                                </Row>
                                <Row gutter={8} style={{padding:'0 64px',marginTop:12}}>
                                  <Col span={12}>
                                    <FormItem
                                      labelCol={{span: 8}}
                                      wrapperCol={{span: 12}}
                                      label="父亲姓名"
                                    >
                                          {getFieldDecorator(`project_members[${i}][parent][fatherName]`, {
                                              rules: [{ required: true, message: '父亲姓名不能为空' }],
                                              initialValue:e.parent.fatherName
                                          })(
                                            <Input />
                                          )}
                                    </FormItem>
                                  </Col>
                                  <Col span={12}>
                                  <FormItem
                                      labelCol={{span: 8}}
                                      wrapperCol={{span: 12}}
                                      label="父亲电话"
                                    >
                                          {getFieldDecorator(`project_members[${i}][parent][fathertelephone]`, {
                                              rules: [{ pattern: /(?:^1[3456789]|^9[28])\d{9}$/, message: '请输入正确手机号' },{ required: true, message: '父亲电话不能为空' }],
                                              initialValue:e.parent.fathertelephone
                                          })(
                                            <Input />
                                          )}
                                    </FormItem>
                                  </Col>
                                </Row>
                                <Row gutter={8} style={{padding:'0 64px'}}>
                                  <Col span={12}>
                                    <FormItem
                                      labelCol={{span: 8}}
                                      wrapperCol={{span: 12}}
                                      label="母亲姓名"
                                    >
                                          {getFieldDecorator(`project_members[${i}][parent][motherName]`, {
                                              rules: [{ required: true, message: '母亲姓名不能为空' }],
                                              initialValue:e.parent.motherName
                                          })(
                                            <Input />
                                          )}
                                    </FormItem>
                                  </Col>
                                  <Col span={12}>
                                    <FormItem
                                      labelCol={{span: 8}}
                                      wrapperCol={{span: 12}}
                                      label="母亲电话"
                                    >
                                          {getFieldDecorator(`project_members[${i}][parent][mothertelephone]`, {
                                             rules: [{ pattern: /(?:^1[3456789]|^9[28])\d{9}$/, message: '请输入正确手机号' },{ required: true, message: '母亲电话不能为空' }],
                                             initialValue:e.parent.mothertelephone
                                          })(
                                            <Input />
                                          )}
                                    </FormItem>
                                  </Col>
                                </Row>
                                <Row gutter={8}>
                                <Col span={12}>
                                  <FormItem
                                    labelCol={{span: 8}}
                                    wrapperCol={{span: 16}}
                                    label="家庭地址"
                                  >
                                        {getFieldDecorator(`project_members[${i}][address]`, {
                                            rules: [{ required: true, message: '地址不能为空' }],
                                            initialValue:e.address
                                        })(
                                          <Input />
                                        )}
                                  </FormItem>
                                </Col>
                              </Row>
                              </Fragment>
                              )
                            }
                            </div>
                        )
                      })
                    }
                   </Col>
                </Row>
                <div style={{textAlign:'center',marginTop:24}}>
                      <Button onClick={this.submitProject} type="primary">保存</Button>
                </div>
              </TabPane>
                )
              }
              {
                status['3'] !== undefined && (
                  <TabPane tab="指导老师信息" key='3'>
                  <FormItem
                      {...formProps}
                      label="比赛号码"
                      >
                      {teacher.number}
                  </FormItem>
                  <FormItem
                      {...formProps}
                      label="选择指导老师数量"
                      >
                      {getFieldDecorator('teacher_number', {
                          rules: [{ required: true, message: '比赛类别不能为空' }],
                          initialValue:teacher.teachers && teacher.teachers.length || undefined,
                      })(
                        <Select  onSelect={this.teachersChange} style={{width:'100%'}}>
                            <Option value={0}>
                                0
                            </Option>
                            <Option value={1}>
                                1
                            </Option>
                            <Option value={2}>
                                2
                            </Option>
                        </Select>
                      )}
                  </FormItem>
                  {
                    teachersCount ?  (
                          <Fragment>
                            <Row gutter={16} style={{marginTop:12}}>
                              <Col span={4} style={{textAlign:'right',color:'#000',paddingRight:3}}>
                                  <span className="ant-form-item-required">
                                    指导老师信息 :
                                  </span>
                              </Col>
                            </Row>
                            <Row gutter={16} style={{textAlign:'center'}}>
                              <Col span={4}>
                              </Col>
                              <Col span={4}>
                                  指导老师身份
                              </Col>
                              <Col span={4}>
                                  姓名
                              </Col>
                              <Col span={4}>
                                  性别
                              </Col>
                              {
                        category_item.paramSetting.includes(2) && (
                          <Col span={4}>
                                            教授学科
                                        </Col>                  
                        )
                              }
                              <Col span={4}>
                                  手机号码
                              </Col>
                            </Row>
                            {teachers}
                            <Row gutter={16} style={{marginTop:12}}>
                              <Col span={4}>
                              </Col>
                              <Col span={20}>
                                <p style={{color:'#005d7e',fontSize:12}}>
                                  请核实您填写的信息，奖状打印以此为准
                                </p>
                              </Col>
                            </Row>
                          </Fragment>
                    ) : ''
                  }
                  <div style={{textAlign:'center',marginTop:24}}>
                      <Button onClick={this.submitTeachers} type="primary">保存</Button>
                  </div>
                </TabPane>
                )
              }
              {
                 (status['4'] !== undefined) && (
                    <TabPane tab='初赛项目资料' key='4'>
                                    <FormItem
                                        {...formProps}
                                        label="比赛号码"
                                        >
                                        {material1.number}
                                    </FormItem>
                                    <FormItem
                                        {...formProps}
                                        label="计划书"
                                        >
                                        {plan}
                                    </FormItem>
                                    <Row gutter={16} style={{marginTop:12}}>
                                      <Col span={4} style={{textAlign:'right',color:'#000',paddingRight:3}}>
                                          <span className="ant-form-item-required">
                                            项目材料 :
                                          </span>
                                      </Col>
                                      <Col span={20}>
                                      {
                                        maxFileNum1.pptNum && <FormItem
                                        {...formUploadProps1}
                                        label="PPT文件"
                                        >
                                        {getFieldDecorator('pptPath1', {
                                                rules: [{ required: true, message: '请上传PPT文件' }],
                                                valuePropName: 'fileList',
                                                getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum1.pptNum),
                                                initialValue:material1.pptPath && material1.pptPath.map((ev,i)=>{
                                                    return {
                                                        uid: i,
                                                        name: ev.split('/')[ev.split('/').length-1],
                                                        status: 'done',
                                                        url:ev,
                                                        response:{
                                                            value:ev
                                                        }
                                                    }
                                                }) || undefined
                                            })(
                                                <Upload       
                                                    name="file"
                                                    action={`/api/file/upload?fileType=1`}
                                                    accept=".ppt,.pptx"
                                                     onRemove={(file)=>this.fileMove(file,status.id,1,1)}
                                                    beforeUpload={(e)=>this.beforeUpload(e,'pptPath1',maxFileNum1.pptNum)}
                                                    
                                                >
                                                    <Button  loading={loading} type="primary">
                                                      上传
                                                    </Button>
                                                    <span style={{marginLeft:12}}>
                                                      支持格式PPT和PPTX,最多{maxFileNum1.pptNum}个
                                                    </span>
                                                </Upload>         
                                            )}
                                      </FormItem> || ''
                                      }
                                      {
                                        maxFileNum1.docNum && <FormItem
                                        {...formUploadProps1}
                                        label="WORD文件"
                                        >
                                        {getFieldDecorator('docPath1', {
                                                rules: [{ required: true, message: '请上传WORD文件' }],
                                                valuePropName: 'fileList',
                                                getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum1.docNum),
                                                initialValue:material1.docPath && material1.docPath.map((ev,i)=>{
                                                    return {
                                                        uid: i,
                                                        name: ev.split('/')[ev.split('/').length-1],
                                                        status: 'done',
                                                        url:ev,
                                                        response:{
                                                            value:ev
                                                        }
                                                    }
                                                }) || undefined
                                            })(
                                                <Upload       
                                                                
                                                    name="file"
                                                    action={`/api/file/upload?fileType=2`}
                                                    accept=".doc,.docx,.pdf"
                                                     onRemove={(file)=>this.fileMove(file,status.id,1,2)}
                                                    beforeUpload={(e)=>this.beforeUpload(e,'docPath1',maxFileNum1.docNum)}
                                                >
                                                    <Button  loading={loading} type="primary">
                                                      上传
                                                    </Button>
                                                    <span style={{marginLeft:12}}>
                                                      支持格式DOC、DOCX、PDF,最多{maxFileNum1.docNum}个
                                                    </span>
                                                </Upload>         
                                            )}
                                      </FormItem> || ''
                                      }
                                          {
                                            maxFileNum1.videoNum &&  <FormItem
                                            {...formUploadProps1}
                                            label="视频文件"
                                            >
                                            {getFieldDecorator('videoPath1', {
                                                    rules: [{ required: true, message: '请上传视频文件' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum1.videoNum),
                                                    initialValue:material1.videoPath && material1.videoPath.map((ev,i)=>{
                                                        return {
                                                            uid: i,
                                                            name: ev.split('/')[ev.split('/').length-1],
                                                            status: 'done',
                                                            url:ev,
                                                            response:{
                                                                value:ev
                                                            }
                                                        }
                                                    }) || undefined
                                                })(
                                                    <Upload                   
                                                        name="file"
                                                        action={`/api/file/upload?fileType=3`}
                                                        accept=".mp4,.wmv"
                                                         onRemove={(file)=>this.fileMove(file,status.id,1,3)}
                                                        
                                                        beforeUpload={(e)=>this.beforeUpload(e,'videoPath1',maxFileNum1.videoNum)}
                                                    >
                                                        <Button  loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                        <span style={{marginLeft:12}}>
                                                          支持格式MP4或WMV,最多{maxFileNum1.videoNum}个，不超过512MB
                                                        </span>
                                                    </Upload>         
                                                )}
                                          </FormItem> || ''
                                          }
                                         {
                                           maxFileNum1.imageNum && <FormItem
                                           {...formUploadProps1}
                                           label="图片文件"
                                           >
                                           {getFieldDecorator('imagePath1', {
                                                   rules: [{ required: true, message: '请上传图片文件' }],
                                                   valuePropName: 'fileList',
                                                   getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum1.imageNum),
                                                   initialValue:material1.imagePath && material1.imagePath.map((ev,i)=>{
                                                       return {
                                                           uid: i,
                                                           name: ev.split('/')[ev.split('/').length-1],
                                                           status: 'done',
                                                           url:ev,
                                                           response:{
                                                               value:ev
                                                           }
                                                       }
                                                   }) || undefined
                                               })(
                                                   <Upload                   
                                                       name="file"
                                                       
                                                       listType="picture-card"
                                                       action={`/api/file/upload?fileType=4`}
                                                       accept=".jpg,.jpeg,.png"
                                                        onRemove={(file)=>this.fileMove(file,status.id,1,4)}
                                                       beforeUpload={(e)=>this.beforeUpload(e,'imagePath1',maxFileNum1.imageNum)}
                                                   >
                                                       <Button   loading={loading} type="primary">
                                                         上传
                                                       </Button>
                                                   </Upload>         
                                               )}
                                               JPG、PNG格式，不超过10MB每张，最多{maxFileNum1.imageNum}张
                                         </FormItem> || ''
                                         }
                                      </Col>
                                    </Row>
                                    <div style={{textAlign:'center',marginTop:24}}>
                                            <Button onClick={()=>this.submitPath(1)} type="primary">保存</Button>
                                    </div>
                                  </TabPane>
                 )
              }
              {
                 (status['5'] !== undefined) && (
                    <TabPane tab='复赛项目资料' key='5'>
                                    <FormItem
                                        {...formProps}
                                        label="比赛号码"
                                        >
                                        {material2.number}
                                    </FormItem>
                                    <FormItem
                                        {...formProps}
                                        label="计划书"
                                        >
                                        {plan}
                                    </FormItem>
                                    <Row gutter={16} style={{marginTop:12}}>
                                      <Col span={4} style={{textAlign:'right',color:'#000',paddingRight:3}}>
                                          <span className="ant-form-item-required">
                                            项目材料 :
                                          </span>
                                      </Col>
                                      <Col span={20}>
                                      {
                                        maxFileNum2.pptNum && <FormItem
                                        {...formUploadProps1}
                                        label="PPT文件"
                                        >
                                        {getFieldDecorator('pptPath2', {
                                                rules: [{ required: true, message: '请上传PPT文件' }],
                                                valuePropName: 'fileList',
                                                getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum2.pptNum),
                                                initialValue:material2.pptPath && material2.pptPath.map((ev,i)=>{
                                                    return {
                                                        uid: i,
                                                        name: ev.split('/')[ev.split('/').length-1],
                                                        status: 'done',
                                                        url:ev,
                                                        response:{
                                                            value:ev
                                                        }
                                                    }
                                                }) || undefined
                                            })(
                                                <Upload                   
                                                    name="file"
                                                    action={`/api/file/upload?fileType=1`}
                                                    accept=".ppt,.pptx"
                                                     onRemove={(file)=>this.fileMove(file,status.id,2,1)}
                                                    beforeUpload={(e)=>this.beforeUpload(e,'pptPath2',maxFileNum2.pptNum)}
                                                >
                                                    <Button  loading={loading} type="primary">
                                                      上传
                                                    </Button>
                                                    <span style={{marginLeft:12}}>
                                                    支持格式PPT和PPTX,最多{maxFileNum2.pptNum}个
                                                    </span>
                                                </Upload>         
                                            )}
                                      </FormItem> || ''
                                      }
                                          {
                                            maxFileNum2.docNum && <FormItem
                                            {...formUploadProps1}
                                            label="WORD文件"
                                            >
                                            {getFieldDecorator('docPath2', {
                                                    rules: [{ required: true, message: '请上传WORD文件' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum2.docNum),
                                                    initialValue:material2.docPath && material2.docPath.map((ev,i)=>{
                                                        return {
                                                            uid: i,
                                                            name: ev.split('/')[ev.split('/').length-1],
                                                            status: 'done',
                                                            url:ev,
                                                            response:{
                                                                value:ev
                                                            }
                                                        }
                                                    }) || undefined
                                                })(
                                                    <Upload                   
                                                        name="file"
                                                        action={`/api/file/upload?fileType=2`}
                                                        accept=".doc,.docx,.pdf"
                                                         onRemove={(file)=>this.fileMove(file,status.id,2,2)}
                                                        
                                                        beforeUpload={(e)=>this.beforeUpload(e,'docPath2',maxFileNum2.docNum)}
                                                    >
                                                        <Button  loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                        <span style={{marginLeft:12}}>
                                                        支持格式DOC、DOCX、PDF,最多{maxFileNum2.docNum}个
                                                        </span>
                                                    </Upload>         
                                                )}
                                          </FormItem> || ''
                                          }
                                          {
                                            maxFileNum2.videoNum && <FormItem
                                            {...formUploadProps1}
                                            label="视频文件"
                                            >
                                            {getFieldDecorator('videoPath2', {
                                                    rules: [{ required: true, message: '请上传视频文件' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum2.videoNum),
                                                    initialValue:material2.videoPath && material2.videoPath.map((ev,i)=>{
                                                        return {
                                                            uid: i,
                                                            name: ev.split('/')[ev.split('/').length-1],
                                                            status: 'done',
                                                            url:ev,
                                                            response:{
                                                                value:ev
                                                            }
                                                        }
                                                    }) || undefined
                                                })(
                                                    <Upload                   
                                                        name="file"
                                                        action={`/api/file/upload?fileType=3`}
                                                        accept=".mp4,.wmv"
                                                         onRemove={(file)=>this.fileMove(file,status.id,2,3)}
                                                        beforeUpload={(e)=>this.beforeUpload(e,'videoPath2',maxFileNum2.videoNum)}
                                                    >
                                                        <Button  loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                        <span style={{marginLeft:12}}>
                                                        支持格式MP4或WMV，最多{maxFileNum2.videoNum}个，不超过512MB
                                                        </span>
                                                    </Upload>         
                                                )}
                                          </FormItem> || ''
                                          }
                                          {
                                            maxFileNum2.imageNum && <FormItem
                                            {...formUploadProps1}
                                            label="图片文件"
                                            >
                                            {getFieldDecorator('imagePath2', {
                                                    rules: [{ required: true, message: '请上传图片文件' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum2.imageNum),
                                                    initialValue:material2.imagePath && material2.imagePath.map((ev,i)=>{
                                                        return {
                                                            uid: i,
                                                            name: ev.split('/')[ev.split('/').length-1],
                                                            status: 'done',
                                                            url:ev,
                                                            response:{
                                                                value:ev
                                                            }
                                                        }
                                                    }) || undefined
                                                })(
                                                    <Upload                   
                                                        name="file"
                                                        
                                                        listType="picture-card"
                                                        action={`/api/file/upload?fileType=4`}
                                                        accept=".jpg,.jpeg,.png"
                                                         onRemove={(file)=>this.fileMove(file,status.id,2,4)}
                                                        beforeUpload={(e)=>this.beforeUpload(e,'imagePath2',maxFileNum2.imageNum)}
                                                    >
                                                        <Button  loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                    </Upload>         
                                                )}
                                                JPG、PNG格式，不超过10MB每张，最多{maxFileNum2.imageNum}张
                                          </FormItem> || ''
                                          }
                                      </Col>
                                    </Row>
                                    <div style={{textAlign:'center',marginTop:24}}>
                                            <Button onClick={()=>this.submitPath(2)} type="primary">保存</Button>
                                    </div>
                                  </TabPane>
                 )
              }
              {
                 (status['6'] !== undefined) && (
                    <TabPane tab='决赛项目资料' key='6'>
                                    <FormItem
                                        {...formProps}
                                        label="比赛号码"
                                        >
                                        {material3.number}
                                    </FormItem>
                                    <FormItem
                                        {...formProps}
                                        label="计划书"
                                        >
                                        {plan}
                                    </FormItem>
                                    <Row gutter={16} style={{marginTop:12}}>
                                      <Col span={4} style={{textAlign:'right',color:'#000',paddingRight:3}}>
                                          <span className="ant-form-item-required">
                                            项目材料 :
                                          </span>
                                      </Col>
                                      <Col span={20}>
                                      {
                                        maxFileNum3.pptNum && <FormItem
                                        {...formUploadProps1}
                                        label="PPT文件"
                                        >
                                        {getFieldDecorator('pptPath3', {
                                                rules: [{ required: true, message: '请上传PPT文件' }],
                                                valuePropName: 'fileList',
                                                getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum3.pptNum),
                                                initialValue:material3.pptPath && material3.pptPath.map((ev,i)=>{
                                                    return {
                                                        uid: i,
                                                        name: ev.split('/')[ev.split('/').length-1],
                                                        status: 'done',
                                                        url:ev,
                                                        response:{
                                                            value:ev
                                                        }
                                                    }
                                                }) || undefined
                                            })(
                                                <Upload                   
                                                    name="file"
                                                    action={`/api/file/upload?fileType=1`}
                                                    accept=".ppt,.pptx"
                                                     onRemove={(file)=>this.fileMove(file,status.id,3,1)}
                                                    beforeUpload={(e)=>this.beforeUpload(e,'pptPath3',maxFileNum3.pptNum)}
                                                >
                                                    <Button  loading={loading} type="primary">
                                                      上传
                                                    </Button>
                                                    <span style={{marginLeft:12}}>
                                                    支持格式PPT和PPTX,最多{maxFileNum3.pptNum}个
                                                    </span>
                                                </Upload>         
                                            )}
                                      </FormItem> || ''
                                      }
                                        {
                                          maxFileNum3.docNum && <FormItem
                                          {...formUploadProps1}
                                          label="WORD文件"
                                          >
                                          {getFieldDecorator('docPath3', {
                                                  rules: [{ required: true, message: '请上传WORD文件' }],
                                                  valuePropName: 'fileList',
                                                  getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum3.docNum),
                                                  initialValue:material3.docPath && material3.docPath.map((ev,i)=>{
                                                      return {
                                                          uid: i,
                                                          name: ev.split('/')[ev.split('/').length-1],
                                                          status: 'done',
                                                          url:ev,
                                                          response:{
                                                              value:ev
                                                          }
                                                      }
                                                  }) || undefined
                                              })(
                                                  <Upload                   
                                                      name="file"
                                                      action={`/api/file/upload?fileType=2`}
                                                      accept=".doc,.docx,.pdf"
                                                       onRemove={(file)=>this.fileMove(file,status.id,3,2)}
                                                      beforeUpload={(e)=>this.beforeUpload(e,'docPath3',maxFileNum3.docNum)}
                                                  >
                                                      <Button  loading={loading} type="primary">
                                                        上传
                                                      </Button>
                                                      <span style={{marginLeft:12}}>
                                                      支持格式DOC、DOCX、PDF,最多{maxFileNum3.docNum}个
                                                      </span>
                                                  </Upload>         
                                              )}
                                        </FormItem> || ''
                                        }
                                          {
                                            maxFileNum3.videoNum && <FormItem
                                            {...formUploadProps1}
                                            label="视频文件"
                                            >
                                            {getFieldDecorator('videoPath3', {
                                                    rules: [{ required: true, message: '请上传视频文件' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum3.videoNum),
                                                    initialValue:material3.videoPath && material3.videoPath.map((ev,i)=>{
                                                        return {
                                                            uid: i,
                                                            name: ev.split('/')[ev.split('/').length-1],
                                                            status: 'done',
                                                            url:ev,
                                                            response:{
                                                                value:ev
                                                            }
                                                        }
                                                    }) || undefined
                                                })(
                                                    <Upload                   
                                                        name="file"
                                                        
                                                        action={`/api/file/upload?fileType=3`}
                                                        accept=".mp4,.wmv"
                                                         onRemove={(file)=>this.fileMove(file,status.id,3,3)}
                                                        beforeUpload={(e)=>this.beforeUpload(e,'videoPath3',maxFileNum3.videoNum)}
                                                    >
                                                        <Button  loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                        <span style={{marginLeft:12}}>
                                                        支持格式MP4或WMV,最多{maxFileNum3.videoNum}个，不超过512MB
                                                        </span>
                                                    </Upload>         
                                                )}
                                          </FormItem> || ''
                                          }
                                          {
                                            maxFileNum3.imageNum && <FormItem
                                            {...formUploadProps1}
                                            label="图片文件"
                                            >
                                            {getFieldDecorator('imagePath3', {
                                                    rules: [{ required: true, message: '请上传图片文件' }],
                                                    valuePropName: 'fileList',
                                                    getValueFromEvent: (e)=>this.normFileUpload(e,maxFileNum3.imageNum),
                                                    initialValue:material3.imagePath && material3.imagePath.map((ev,i)=>{
                                                        return {
                                                            uid: i,
                                                            name: ev.split('/')[ev.split('/').length-1],
                                                            url:ev,
                                                            status: 'done',
                                                            response:{
                                                                value:ev
                                                            }
                                                        }
                                                    }) || undefined
                                                })(
                                                    <Upload                   
                                                        name="file"
                                                        
                                                        listType="picture-card"
                                                        action={`/api/file/upload?fileType=4`}
                                                        accept=".jpg,.jpeg,.png"
                                                         onRemove={(file)=>this.fileMove(file,status.id,3,4)}
                                                        beforeUpload={(e)=>this.beforeUpload(e,'imagePath3',maxFileNum3.imageNum)}
                                                    >
                                                        <Button   loading={loading} type="primary">
                                                          上传
                                                        </Button>
                                                    </Upload>         
                                                )}
                                                JPG、PNG格式，不超过10MB每张，最多{maxFileNum3.imageNum}张
                                          </FormItem> || ''
                                          }
                                      </Col>
                                     </Row>
                                    <div style={{textAlign:'center',marginTop:24}}>
                                            <Button onClick={()=>this.submitPath(3)} type="primary">保存</Button>
                                    </div>
                                  </TabPane>
                 )
              }
            </Tabs>
          </Card>
        </div>
      </div>
    )
  }
}

