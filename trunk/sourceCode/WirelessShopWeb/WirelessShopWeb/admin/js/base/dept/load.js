a_b_d.gridOperateRenderer = function(){
	return '<a href="javascript:a_b_d.operateKitchen({otype:Ext.ux.otype[\'update\']});">修改</a>'
		+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		+ '<a href="javascript:a_b_d.operateKitchen({otype:Ext.ux.otype[\'delete\']});">删除</a>';
};

Ext.onReady(function(){
	a_b_d.deptTree = new Ext.tree.TreePanel({
		title : '部门资料',
		width : 260,
		region : 'west',
		frame : true,
		tbar : new Ext.Toolbar({
			height : 26,
			items : ['->', {
				text : '添加',
				iconCls : ' ',
				handler : function(){
					a_b_d.operateDept({otype:Ext.ux.otype['insert']});
				}
			}, {
				text : '修改',
				iconCls : ' ',
				handler : function(){
					a_b_d.operateDept({otype:Ext.ux.otype['update']});
				}
			}, {
				text : '删除',
				iconCls : ' ',
				handler : function(){
					a_b_d.operateDept({otype:Ext.ux.otype['delete']});
				}
			}, {
				text : '刷新',
				iconCls : ' ',
				handler : function(){
					a_b_d.deptTree.getRootNode().reload();
				}
			}]
		}),
		loader : new Ext.tree.TreeLoader({
			url : '../admin/QueryDept!tree.action',
			baseParams : {
				'dept.rid' : 26
			}
		}),
		root : new Ext.tree.AsyncTreeNode({
			text : '所有部门',
			listeners : {
				load : function(e){
					a_b_d.deptData = [];
					for(var i = 0; i < e.childNodes.length; i++){
						a_b_d.deptData.push(e.childNodes[i].attributes.other);
					}
					Ext.getCmp('a_b_d.grid.btnSearch').handler();
				}
			}
		}),
		listeners : {
			render : function(thiz){
				thiz.getRootNode().reload();
			},
			dblclick : function(){
				Ext.getCmp('a_b_d.grid.btnSearch').handler();
			}
		}
	});
	
	var gtbar = new Ext.Toolbar({
		height : 26,
		items : [{
			xtype : 'tbtext',
			text : '部门:'
		}, '->', {
			id : 'a_b_d.grid.btnSearch',
			text : '搜索',
			iconCls : ' ',
			handler : function(){
				var node = a_b_d.deptTree.getSelectionModel().getSelectedNode();
				var gs = a_b_d.kitchenGrid.getStore();
				gs.baseParams['deptId'] = node && node.attributes.other && node.attributes.other.id > 0 ? node.attributes.other.id : 0;
				gs.load({
					params : {
						start : 0,
						limit : a_b_d.kitchenGrid.getBottomToolbar().getSize()
					}
				});
			}
		}, {
			text : '添加',
			iconCls : ' ',
			handler : function(){
				a_b_d.operateKitchen({otype:Ext.ux.otype['insert']});
			}
		}, {
			text : '修改',
			iconCls : ' ',
			handler : function(){
				a_b_d.operateKitchen({otype:Ext.ux.otype['update']});
			}
		}, {
			text : '删除',
			iconCls : ' ',
			handler : function(){
				a_b_d.operateKitchen({otype:Ext.ux.otype['delete']});
			}
		}]
	});
	a_b_d.kitchenGrid = Ext.ux.cg(
		'',
		'分厨资料',
		'',
		'',
		'../admin/QueryKitchen!normal.action',
		[[true,true,false,true],
		    ['分厨编号', 'id'],
		    ['分厨名称', 'name'],
		    ['归属部门', 'dept.name'],
		    ['操作', 'deptId',,'center','a_b_d.gridOperateRenderer']
		],
		['id', 'name', 'deptId', 'dept.name'],
		[['rid', 26], ['deptId', 0]],
		20,
		null,
		gtbar
	);
	a_b_d.kitchenGrid.region = 'center';
	a_b_d.kitchenGrid.on('rowdblclick', function(){
		a_b_d.operateKitchen({otype:Ext.ux.otype['update']});
	});
	
	var view = Ext.getCmp(a_b_d.parentContentId);
	view.add({
		border : false,
		layout : 'border',
		items : [a_b_d.deptTree, a_b_d.kitchenGrid]
	});
	view.doLayout();
	
});
