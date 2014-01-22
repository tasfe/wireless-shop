a_b_f.grid = {
	imageRenderer : function(v){
		if(v != null && !v.trim().isEmpty()){
			return '<a href="javascript:" style="color:green;">已上传</a>';
		}else{
			return '<a href="javascript:">未设置</a>';
		}
	},
	operateRenderer	: function(){
		return '<a href="javascript:a_b_f.operateFood({otype:Ext.ux.otype[\'update\']});">修改</a>'
			+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
			+ '<a href="javascript:a_b_f.operateFood({otype:Ext.ux.otype[\'delete\']});">删除</a>';
	}
};

Ext.onReady(function(){
	a_b_f.kitchenTree = new Ext.tree.TreePanel({
		title : '分厨',
		width : 200,
		region : 'west',
		autoScroll : true,
		frame : true,
		lines : false,
		bodyStyle : 'backgroundColor:#FFFFFF; border:1px solid #99BBE8;',
		tbar : new Ext.Toolbar({
			height : 26,
			items : ['->', {
				text : '刷新',
				iconCls : ' ',
				handler : function(){
					a_b_f.kitchenTree.getRootNode().reload();
				}
			}]
		}),
		loader : new Ext.tree.TreeLoader({
			url : '../admin/QueryKitchen!tree.action',
			baseParams : {
				rid : 26,
				deptId : 0
			}
		}),
		root : new Ext.tree.AsyncTreeNode({
			text : '所有分厨',
			listeners : {
				load : function(e){
					a_b_f.kitchenData = [];
					for(var i = 0; i < e.childNodes.length; i++){
						a_b_f.kitchenData.push(e.childNodes[i].attributes.other);
					}
					Ext.getCmp('a_b_f.grid.btnSearch').handler();
				}
			}
		}),
		listeners : {
			render : function(thiz){
				thiz.getRootNode().reload();
			},
			dblclick : function(){
				Ext.getCmp('a_b_f.grid.btnSearch').handler();
			}
		}
	});
	
	var gtbar = new Ext.Toolbar({
		height : 26,
		items : [{
			xtype : 'tbtext',
			text : '分厨:'
		}, '->', {
			id : 'a_b_f.grid.btnSearch',
			text : '搜索',
			iconCls : ' ',
			handler : function(){
				var node = a_b_f.kitchenTree.getSelectionModel().getSelectedNode();
				var gs = a_b_f.foodGrid.getStore();
				gs.baseParams['food.kitchenId'] = node && node.attributes.other && node.attributes.other.id > 0 ? node.attributes.other.id : 0;
				gs.load({
					params : {
						start : 0,
						limit : a_b_f.foodGrid.getBottomToolbar().pageSize
					}
				});
			}
		}, {
			text : '添加',
			iconCls : ' ',
			handler : function(){
				a_b_f.operateFood({otype:Ext.ux.otype['insert']});
			}
		}, {
			text : '修改',
			iconCls : ' ',
			handler : function(){
				a_b_f.operateFood({otype:Ext.ux.otype['update']});
			}
		}, {
			text : '删除',
			iconCls : ' ',
			handler : function(){
				a_b_f.operateFood({otype:Ext.ux.otype['delete']});
			}
		}]
	});
	a_b_f.foodGrid = Ext.ux.cg(
		'',
		'食品资料',
		'',
		'',
		'../admin/QueryFood!normal.action',
		[[true,true,false,true],
		    ['编号', 'alias', 50],
		    ['名称', 'name', 150, '', 'function(v,md,r){return EX.cfs.formatName(r);}'],
		    ['单价', 'price', 60, 'right', 'function(v){return v.toFixed(2);}'],
		    ['归属分厨', 'kitchen.name'],
		    ['图片', 'img',,'center','a_b_f.grid.imageRenderer'],
		    ['操作', 'operate',,'center','a_b_f.grid.operateRenderer']
		],
		['id', 'name', 'price', 'alias', 'img', 'status', 'kitchenId', 'kitchen.name'],
		[['food.rid', 26], ['food.kitchenId', 0]],
		20,
		null,
		gtbar
	);
	a_b_f.foodGrid.region = 'center';
	a_b_f.foodGrid.on('rowdblclick', function(){
		a_b_f.operateFood({otype:Ext.ux.otype['update']});
	});
	
//	a_b_f.foodGrid.on('contextmenu', function(e){
//		var menu = Ext.getCmp('a_b_f.gridPanel.menu');
//		if(!menu){
//			alert(2222)
//			menu = new Ext.menu.Menu({
//				id : 'a_b_f.gridPanel.menu',
//				items : [{
//					text : '添加',
//					handler : function(e){
//						a_b_f.operateFood({otype:Ext.ux.otype['insert']});
//					}
//				}, {
//					text : '修改',
//					handler : function(e){
//						a_b_f.operateFood({otype:Ext.ux.otype['update']});
//					}
//				}, {
//					text : '删除',
//					handler : function(e){
//						a_b_f.operateFood({otype:Ext.ux.otype['delete']});
//					}
//				}]
//			});	
//		}
//		menu.showAt(e.getPoint());
//	});
	
	var view = Ext.getCmp(a_b_f.parentContentId);
	view.add({
		border : false,
		layout : 'border',
		items : [a_b_f.kitchenTree, a_b_f.foodGrid]
	});
	view.doLayout();
	
});