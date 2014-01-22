a_b_d.load = function(){
	Ext.Ajax.request({
		url : '../admin/QueryKitchen!normal.action',
		params : {
			rid : 26,
			deptId : 0
		},
		success : function(response, options){
			alert(response.responseText)
		},
		failure : function(response, options){
			alert('error');
		},
	});
};

a_b_d.initDeptWin = function(c){
	var deptWin = Ext.getCmp('a_b_d.deptWin');
	if(!deptWin){
		var id = new Ext.form.Hidden({
			value : 0
		});
		var name = new Ext.form.TextField({
			fieldLabel : '名称',
			width : 100,
			allowBlank : false
		});
		var btnSave = new Ext.Button({
			text : '保存',
			iconCls : ' ',
			handler : function(e){
				if(!name.isValid()){
					return;
				}
				e.setDisabled(true);
				btnCancel.setDisabled(true);
				Ext.Ajax.request({
					url : '../admin/OperateDept!{0}.action'.format(deptWin.otype.toLowerCase()),
					params : {
						'dept.rid' : 26,
						'dept.id' : id.getValue() == '' ? 0 : id.getValue(),
						'dept.name' : name.getValue()
					},
					success : function(res, opt){
						var jr = Ext.decode(res.responseText);
						Ext.ux.msg(jr);
						if(jr.success){
							a_b_d.deptTree.getRootNode().reload();
							deptWin.hide();
						}
						e.setDisabled(false);
						btnCancel.setDisabled(false);
					},
					fialure : function(res, opt){
						Ext.ux.msg(Ext.decode(res));
						e.setDisabled(false);
						btnCancel.setDisabled(false);
					}
				});
			}
		});
		var btnCancel = new Ext.Button({
			text : '取消',
			iconCls : ' ',
			handler : function(e){
				deptWin.hide();
			}
		});
		deptWin = new Ext.Window({
			id : 'a_b_d.deptWin',
			title : '&nbsp;',
			closable : false,
			resizable : false,
			modal : true,
			width : 200,
			constrainHeader : true,
			keys : [{
				key : Ext.EventObject.ENTER,
				scope : this,
				fn : function(){ btnSave.handler(btnSave); }
			}, {
				key : Ext.EventObject.ESC,
				scope : this,
				fn : function(){ deptWin.hide(); }
			}],
			items : [{
				layout : 'form',
				border : false,
				frame : true,
				labelWidth : 60,
				items : [id, name]
			}],
			bbar : ['->', btnSave, btnCancel],
			listeners : {
				show : function(thiz){
					if(deptWin.otype == Ext.ux.otype['insert']){
						id.setValue();
						name.setValue();
					}else if(deptWin.otype == Ext.ux.otype['update']){
						var node = a_b_d.deptTree.getSelectionModel().getSelectedNode();
						id.setValue(node.attributes.other.id);
						name.setValue(node.attributes.other.name);
					}
					
					name.clearInvalid();
					name.focus(name, 100);
				}
			}
		});
	}
	
	deptWin.otype = c.otype;
	return deptWin;
};

a_b_d.operateDept = function(c){
	if(c.otype == Ext.ux.otype['insert']){
		
		a_b_d.initDeptWin(c).show();
	}else if(c.otype == Ext.ux.otype['update']){
		var node = a_b_d.deptTree.getSelectionModel().getSelectedNode();
		if(!node || typeof node.attributes.other == 'undefined'){
			Ext.ux.msg({
				success : true,
				msg : '请选中一个部门.'
			});
			return;
		}
		
		a_b_d.initDeptWin(c).show();
	}else if(c.otype == Ext.ux.otype['delete']){
		Ext.ux.msg({
			success : true,
			msg : '此功能正在开发中, 暂不开放使用.'
		});
	}
};


a_b_d.initKitchenWin = function(c){
	c.cid = 'a_b_d.kitchenWin';
	var win = Ext.getCmp(c.cid);
	if(!win){
		var id = new Ext.form.Hidden({
			value : 0
		});
		var name = new Ext.form.TextField({
			fieldLabel : '名称',
			width : 100,
			allowBlank : false
		});
		var dept = new Ext.form.ComboBox({
			fieldLabel : '部门',
			readOnly : true,
			forceSelection : true,
			width : 100,
			store : new Ext.data.JsonStore({
				fields : ['id','name'],
				data : a_b_d.deptData
			}),
			valueField : 'id',
			displayField : 'name',
			typeAhead : true,
			mode : 'local',
			triggerAction : 'all',
			selectOnFocus : true,
			allowBlank : false
		});
		var btnSave = new Ext.Button({
			text : '保存',
			iconCls : ' ',
			handler : function(e){
				if(!name.isValid() || !dept.isValid()){
					return;
				}
				e.setDisabled(true);
				btnCancel.setDisabled(true);
				Ext.Ajax.request({
					url : '../admin/OperateKitchen!{0}.action'.format(win.otype.toLowerCase()),
					params : {
						'kitchen.rid' : 26,
						'kitchen.id' : id.getValue() == '' ? 0 : id.getValue(),
						'kitchen.name' : name.getValue(),
						'kitchen.deptId' : dept.getValue()
					},
					success : function(res, opt){
						var jr = Ext.decode(res.responseText);
						Ext.ux.msg(jr);
						if(jr.success){
//							a_b_d.deptTree.getRootNode().reload();
							Ext.getCmp('a_b_d.grid.btnSearch').handler();
							win.hide();
						}
						e.setDisabled(false);
						btnCancel.setDisabled(false);
					},
					fialure : function(res, opt){
						Ext.ux.msg(Ext.decode(res));
						e.setDisabled(false);
						btnCancel.setDisabled(false);
					}
				});
			}
		});
		var btnCancel = new Ext.Button({
			text : '取消',
			iconCls : ' ',
			handler : function(e){
				win.hide();
			}
		});
		win = new Ext.Window({
			id : c.cid,
			title : '&nbsp;',
			closable : false,
			resizable : false,
			modal : true,
			width : 200,
			constrainHeader : true,
			keys : [{
				key : Ext.EventObject.ENTER,
				scope : this,
				fn : function(){ btnSave.handler(btnSave); }
			}, {
				key : Ext.EventObject.ESC,
				scope : this,
				fn : function(){ win.hide(); }
			}],
			items : [{
				layout : 'form',
				border : false,
				frame : true,
				labelWidth : 60,
				items : [id, name, dept]
			}],
			bbar : ['->', btnSave, btnCancel],
			listeners : {
				show : function(thiz){
					if(thiz.otype == Ext.ux.otype['insert']){
						id.setValue(0);
						name.setValue();
						dept.setValue();
					}else if(thiz.otype == Ext.ux.otype['update']){
						var data = Ext.ux.getData(a_b_d.kitchenGrid);
						id.setValue(data.id);
						name.setValue(data.name);
						dept.setValue(data.deptId);
					}
					
					name.clearInvalid();
					name.focus(name, 100);
					dept.clearInvalid();
				}
			}
		});
	}
	win.otype = c.otype;
	return win;
};

a_b_d.operateKitchen = function(c){
	if(c.otype == Ext.ux.otype['insert']){
		a_b_d.initKitchenWin(c).show();
	}else if(c.otype == Ext.ux.otype['update']){
		var data = Ext.ux.getData(a_b_d.kitchenGrid);
		if(!data){
			Ext.ux.msg({
				success : true,
				msg : '请选中一条分厨资料, 再进行操作.'
			});
			return;
		}
		c.data = data;
		a_b_d.initKitchenWin(c).show();
	}else if(c.otype == Ext.ux.otype['delete']){
		Ext.ux.msg({
			success : true,
			msg : '此功能正在开发中, 暂不开放使用.'
		});
	}
};