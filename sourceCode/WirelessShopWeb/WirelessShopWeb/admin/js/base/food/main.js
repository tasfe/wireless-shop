
a_b_f.initFoodWin = function(c){
	c = c == null ? {} : c;
	var foodWin = Ext.getCmp('a_b_f.foodWin');
	if(!foodWin){
		var id = new Ext.form.Hidden({
			value : 0
		});
		var name = new Ext.form.TextField({
			fieldLabel : '名称',
			allowBlank : false,
			width : 230
		});
		var alias = new Ext.form.NumberField({
			fieldLabel : '编号',
			allowBlank : false,
			width : 80
		});
		var kitchen = new Ext.form.ComboBox({
			fieldLabel : '分厨',
			allowBlank : false,
			width : 80,
 	    	store : new Ext.data.JsonStore({
				fields : [ 'id', 'name' ],
				data : a_b_f.kitchenData
			}),
			valueField : 'id',
			displayField : 'name',
			mode : 'local',
			triggerAction : 'all',
			typeAhead : true,
			selectOnFocus : true,
			forceSelection : true,
			allowBlank : false,
			readOnly : true
		});
		var price = new Ext.form.NumberField({
			fieldLabel : '单价',
			allowBlank : false,
			width : 80
		});
		var desc = new Ext.form.TextArea({
			fieldLabel : '简介',
			width : 230,
			height : 100
		});
		var chbSpecial = new Ext.form.Checkbox({
			hideLabel : true,
 	    	boxLabel : '<img title="特价" src="../images/icon_tip_special.png" />'
		});
		var chbRecommend = new Ext.form.Checkbox({
			hideLabel : true,
 	    	boxLabel : '<img title="推荐" src="../images/icon_tip_recommend.png" />'
		});
		var chbStop = new Ext.form.Checkbox({
			hideLabel : true,
 	    	boxLabel : '<img title="停售" src="../images/icon_tip_stop.png" />'
		});
		var chbGift = new Ext.form.Checkbox({
			hideLabel : true,
 	    	boxLabel : '<img title="赠送" src="../images/icon_tip_gift.png" />'
		});
		var btnSave = new Ext.Button({
			text : '保存',
			iconCls : ' ',
			handler : function(e){
				if(!name.isValid() || !alias.isValid() || !kitchen.isValid() || !price.isValid())
					return;
				var data = Ext.ux.getData(a_b_f.foodGrid);
				var status = data.status;
				status = EX.cfs.setSpecial(status, chbSpecial.getValue());
				status = EX.cfs.setRecommend(status, chbRecommend.getValue());
				status = EX.cfs.setStop(status, chbStop.getValue());
				status = EX.cfs.setGift(status, chbGift.getValue());
				var params = {
					rid : 26,
					'food.rid' : 26,
					'food.name' : name.getValue(),
					'food.alias' : alias.getValue(),
					'food.kitchenId' : kitchen.getValue(),
					'food.price' : price.getValue(),
					'food.desc' : desc.getValue(),
					'food.status' : status
				};
				if(foodWin.otype == Ext.ux.otype['update']){
					params['food.id'] = id.getValue();
				}
				
				Ext.Ajax.request({
					url : '../admin/OperateFood!{0}.action'.format(foodWin.otype.toLowerCase()),
					params : params,
					success : function(res, opt){
						var jr = Ext.decode(res.responseText);
						Ext.ux.msg(jr);
						if(jr.success){
							foodWin.isUpdate = true;
							foodWin.hide();
						}
					},
					failure : function(res, opt){
						Ext.ux.msg(res.responseText);
					}
				});
			}
		});
		var btnCancel = new Ext.Button({
			text : '取消',
			iconCls : ' ',
			handler : function(e){
				foodWin.hide();
			}
		});
		var mainView = {
			xtype : 'panel',
			layout : 'border',
			frame : true,
			height : 450,
			items : [{
				xtype : 'panel',
				region : 'west',
				width : 300,
				layout : 'column',
				defaults : {
					layout : 'form',
					border : false,
					labelWidth : 40
				},
				items : [{
					columnWidth : 1,
					items : [id]
				}, {
					columnWidth : 1,
					items : [name]
				}, {
					columnWidth : .5,
					items : [alias]
				}, {
					columnWidth : .5,
					items : [kitchen]
				}, {
					columnWidth : .5,
					items : [price]
				}, {
					columnWidth : 1,
					items : [desc]
				}, {
					columnWidth : .15,
					items : [{html:'状态:'}]
				}, {
					columnWidth : .2,
					items : [chbSpecial]
				}, {
					columnWidth : .2,
					items : [chbRecommend]
				}, {
					columnWidth : .2,
					items : [chbStop]
				}, {
					columnWidth : .2,
					items : [chbGift]
				}]
			}, {
				xtype : 'panel',
				region : 'center',
				items : [ new Ext.BoxComponent({
					xtype : 'box',
			 	    id : 'a_b_f.foodBasic',
			 	    name : 'image',
			 	    width : 555,
			 	    height : 400,
			 	    autoEl : {
			 	    	src : '../images/nophoto.png',
			 	    	tag : 'img',
			 	    	title : '菜品图预览.',
			 	    	style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);'
			 	    		+ 'width:555; height:400; cursor:hand; border: 1px solid #bbb;'
			 	    }
				}), {
					tag : 'div',
			 	    height : 10
			 	}, {
			 		xtype : 'panel',
			 	    layout : 'column',
			 	    items : [{
			 	    	xtype : 'form',
			 	    	layout : 'form',
			 	    	labelWidth : 60,
			 	    	columnWidth : .7,
			 	    	url : '../admin/OperateFoodImage!upload.action?rid=26',
			 	    	id : 'a_b_f.foodImgFileUploadForm',
			 	    	fileUpload : true,
			 	    	items : [{}],
			 	    	listeners : {
			 	    		render : function(e){
			 	    			Ext.getDom(e.getId()).setAttribute('enctype', 'multipart/form-data');
			 	    			a_b_f.refreshFoodImage();
			 	    		}
			 	    	}
			 	    }, {
			 	    	xtype : 'panel',
			 	    	columnWidth : .15,
			 	    	items : [{
			 	    		xtype : 'button',
			 	    		id : 'btnUploadFoodImage',
			 	        	text : '上传图片',
			 	        	handler : function(e){
			 	        		var check = true;
			 	        		var img = '';
			 	        		if(Ext.isIE){
			 	        			var file = Ext.getDom('a_b_f.foodImgFile');
			 	        			file.select();
			 	        			img = document.selection.createRange().text;
			 	        		}else{
			 	        			var file = Ext.getDom('a_b_f.foodImgFile'); 
					 	        	img = file.value;
			 	        		}
			 	        		if(typeof img != 'undefined' && img.length > 0){
			 	        			var index = img.lastIndexOf('.');
					 	        	var type = img.substring(index+1, img.length);
					 	        	check = false;
					 	        	var imgTypeTmp = ['jpg', 'png', 'gif'];
					 	        	for(var i = 0; i < imgTypeTmp.length; i++){
					 	        		if(type.toLowerCase() == imgTypeTmp[i].toLowerCase()){
					 	        			check = true;
						 	        	   	break;
						 	        	}
					 	        	}
			 	        		}else{
			 	        			check = false;
			 	        		}
			 	        		 
			 	        		if(check){
			 	        			var data = Ext.ux.getData(a_b_f.foodGrid);
			 	        			data.arrt = {
					 	        	    type : 'upload'
					 	        	};
					 	        	a_b_f.uploadImage(data);
			 	        		}else{
			 	        			Ext.example.msg('提示', '上传图片失败, 未选择图片或图片类型正确.');
			 	        		}
			 	        	 }
			 	    	 }]
			 	    }, {
			 	    	xtype : 'panel',
			 	    	columnWidth : .15,
			 	    	items : [{
			 	    		xtype : 'button',
			 	        	id : 'btnDeleteFoodImage',
			 	        	text : '删除图片',
			 	        	handler : function(e){
			 	        		var data = Ext.ux.getData(a_b_f.foodGrid);
			 	        		if(!data)
			 	        			return;
			 	        	    
			 	        		if(data.img.indexOf('nophoto.jpg') != -1){
			 	        			Ext.example.msg('提示', '该菜品没有图片, 无需删除.');
			 	        			return;
			 	        		}
			 	        		 
			 	        		Ext.Msg.confirm('提示', '是否确定删除菜品图片?', function(e){
			 	        			if(e == 'yes'){
			 	        				data.arrt = {
			 	        				    type : 'delete'
				 	       				};
			 	        				a_b_f.uploadImage(data);
			 	        			 }
			 	        		}, this);
			 	        	}
			 	    	}]
			 		}]
				}]
			}]
		};
		
		foodWin = new Ext.Window({
			id : 'a_b_f.foodWin',
			title : '&nbsp;',
			closable : false,
			resizable : false,
			modal : true,
			width : 900,
			constrainHeader : true,
			keys : [{
				key : Ext.EventObject.ENTER,
				scope : this,
				fn : function(){ btnSave.handler(btnSave); }
			}, {
				key : Ext.EventObject.ESC,
				scope : this,
				fn : function(){ foodWin.hide(); }
			}],
			items : [mainView],
			bbar : ['->', btnSave, btnCancel],
			listeners : {
				show : function(thiz){
					thiz.center();
				},
				hide : function(thiz){
					if(thiz.isUpdate){
						Ext.getCmp('a_b_f.grid.btnSearch').handler();
					}
					thiz.clearData();
					thiz.isUpdate = false;
				}
			}
		});
		foodWin.clearData = function(data){
			foodWin.initData({});
		};
		foodWin.initData = function(data){
//			alert('data:  '+JSON.stringify(data))
//			kitchen.store.loadData(a_b_f.kitchenData);
			id.setValue(data['id']);
			name.setValue(data['name']);
			alias.setValue(data['alias']);
			kitchen.setValue(data['kitchenId']);
			price.setValue(data['price']);
			desc.setValue(data['desc']);
			
			chbSpecial.setValue(EX.cfs.isSpecial(data['status']));
			chbRecommend.setValue(EX.cfs.isRecommend(data['status']));
			chbStop.setValue(EX.cfs.isStop(data['status']));
			chbGift.setValue(EX.cfs.isGift(data['status']));
			
			name.clearInvalid();
			alias.clearInvalid();
			kitchen.clearInvalid();
			price.clearInvalid();
			desc.clearInvalid();
		};
	}
	
	foodWin.otype = typeof c.otype != 'undefined' ? c.otype : foodWin.otype;
	return foodWin;
};

a_b_f.operateFood = function(c){
	var win = null;
	if(c.otype == Ext.ux.otype['insert']){
		
		win = a_b_f.initFoodWin(c);
		win.show();
		win.clearData();
	}else if(c.otype == Ext.ux.otype['update']){
		var data = Ext.ux.getData(a_b_f.foodGrid);
		if(!data){
			Ext.ux.msg({
				success : true,
				msg : '请选中一条菜品资料, 再进行操作.'
			});
			return;
		}
		c.data = data;
		win = a_b_f.initFoodWin(c);
		win.show();
		win.initData(data);
	}else if(c.otype == Ext.ux.otype['delete']){
		Ext.ux.msg({
			success : true,
			msg : '此功能正在开发中, 暂不开放使用.'
		});
	}
};
