		
Ext.ux.format = {
	double : function(v){
		return v.toFixed(2);
	}
};

Ext.ux.otype = {
	'insert' : 'INSERT', 'INSERT' : 'INSERT',
	'update' : 'UPDATE', 'UPDATE' : 'UPDATE',
	'select' : 'SELECT', 'SELECT' : 'SELECT',
	'delete' : 'DELETE', 'DELETE' : 'DELETE',
	'set' : 'SET', 'SET' : 'SET',
	'get' : 'GET', 'GET' : 'GET'
};
/**
 * 提示信息
 */
Ext.ux.msg = function(msg){
	if(msg == null || typeof msg == 'undefined'){
		return false;
	}
	if(typeof msg == 'string')
		msg = Ext.decode(msg);
	if(msg.success){
		Ext.example.msg(typeof msg.title == 'undefined' ? '温馨提示' : msg.title, msg.msg);
	}else{
		msg.msg = String.format('响应代码:{0}<br/>响应信息:{1}', msg.code, msg.msg);
		Ext.Msg.show({
			title : msg.title,
			msg : msg.msg,
			autoWidth : true,
			buttons : Ext.Msg.OK,
			icon : typeof msg.icon != 'undefined' ? msg.icon : Ext.Msg.ERROR,
			fn : msg.callBack != null && typeof msg.callBack == 'function' ? msg.callBack : null
		});
	}
};


function StringBuilder() {
	this.__string__ = new Array();
}
StringBuilder.prototype.append = function(str) {
	this.__string__.push(str);
};
StringBuilder.prototype.toString = function() {
	return this.__string__.join("");
};

Ext.ux.PageSizePlugin = function() {
	Ext.ux.PageSizePlugin.superclass.constructor.call(this, {
		store : new Ext.data.SimpleStore({
			fields : ['text', 'value'],
			data : [['10', 10], ['20', 20], ['30', 30], ['50', 50], ['100', 100],['全部',10000]]
		}),
		mode : 'local',
		displayField : 'text',
		valueField : 'value',
		editable : false,
		allowBlank : false,
		triggerAction : 'all',
		width : 45
	});
};
Ext.extend(Ext.ux.PageSizePlugin, Ext.form.ComboBox, {
	init : function(paging) {
		paging.on('render', this.onInitView, this);
	},
	onInitView : function(paging) {
		paging.add('-', this, '-');
		this.setValue(paging.pageSize);
		this.on('select', this.onPageSizeChanged, paging);
	},

	onPageSizeChanged : function(combo) {
		this.pageSize = parseInt(combo.getValue());
		this.doLoad(0);
	}
});


/*******************************************************************************
 * 
 * 创建GridPanel
 * 
 * @param {}
 *            id GridPanel的唯一编号
 * @param {}
 *            title GridPanel的标题
 * @param {}
 *            height GridPanel的高度
 * @param {}
 *            width GridPanel的宽度
 * @param {}
 *            url 服务器地址
 * @param {}
 *            cmData 设定显示的列 ---------
 *            数据格式[[true,true]['列名','数据的字段名','列宽','指定自定义的方法去改变值的显示方式']]
 * @param {}
 *            readerData 要显示列的对应该数据的字段名 ---------
 *            数据格式['activityName','activityAddress','contact','startDate','endDate']
 * @param {}
 *            baseParams 参数集合 ---------
 *            数据格式[['key1','value1'],['key2','value2']]
 * @param {}
 *            pageSize 每面显示几条数据
 * @param {}
 *            groupName 需要分组显示时传入对应该数据的字段名(例如：'activityName')，不需要分组则不传入''
 * @param {}
 *            tbar 上方的工具条[{tbar1},{tbar2}]
 * @param {}
 *            bbar 下方的工具条（true显示，false不显示）
 * @return {}
 */
Ext.ux.cg = function(id, title, height, width, url, cmData, readerData,
		baseParams, pageSize, groupName,tbar,bbar) {
	
	var g_ckbox = new Ext.grid.CheckboxSelectionModel({
//		handleMouseDown : Ext.emptyFn	
	}); 
	var g_rowNum = new Ext.grid.RowNumberer();
	
	var g_cmData = new Array();
	if (cmData[0][0])
		g_cmData.push(g_rowNum);
	if (cmData[0][1])
		g_cmData.push(g_ckbox);
	for (var i = 1; i < cmData.length; i++) {
		data = cmData[i];
		var sb = new StringBuilder();
		sb.append("{");
		sb.append("header:'"+data[0]+"'");
		sb.append(",dataIndex:'"+data[1]+"'");
		if (data.length > 2 && data[2] != null && data[2] != NaN) {
			sb.append(",width:"+data[2]);
		}
		if (data.length > 3 && data[3] != null && data[3].trim().length > 0) {
			sb.append(",align:'"+data[3]+"'");
		}
		if (data.length > 4 && data[4] != null && data[4].trim().length > 0) {
			sb.append(",renderer:"+data[4]);
		}
		sb.append("}");
		
		eval("g_cmData.push(" + sb.toString()+")");
	}

	/** 构造列模型 * */
	var g_cm = new Ext.grid.ColumnModel(g_cmData);

	/** 支持排序 * */
	g_cm.defaultSortable = true;

	/** 服务器地址 * */
	var g_proxy = new Ext.data.HttpProxy({
		url : url
	});

	/** 数据的格式 * */
	var g_readerData = new Array();
	for (var k = 0; k < readerData.length; k++) {
		var rd = readerData[k];
		if (rd != '' && rd.length > 0) {
			var sb_rd = new StringBuilder();
			sb_rd.append("{name:'");
			sb_rd.append(rd);
			sb_rd.append("'}");
			eval("g_readerData.push(" + sb_rd.toString()+")");
		}
	}

	/** 读取返回数据 * */
	var g_reader = new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root'
	}, g_readerData);

	var g_store;
//	var b_groupBtn = null;

//	if (!groupName || groupName == '') {
		/** 普通数据源 * */
		g_store = new Ext.data.Store({
			proxy : g_proxy,
			reader : g_reader
		});
//	} else {
//		/** 分组数据源 * */
//		g_store = new Ext.data.GroupingStore({
//			proxy : g_proxy,
//			reader : g_reader,
//			sortInfo : {
//				field : groupName,
//				direction : "ASC"
//			},
//			groupField : groupName
//		});
//
//		/** 工具条中的分组按钮 * */
//		b_groupBtn = new Ext.Button({
//			text : '一般显示',
//			pressed : true,
//			enableToggle : true,
//			cls : 'x-btn-text-icon details',
//			iconCls : 'g_btn_preview',
//			tooltip : '<h2>一般显示：</h2>按顺序排列显示列表内容<br/><h2>分组显示：</h2>对数据列表内容进行分类显示',
//			toggleHandler : function(btn, pressed) {						
//				if (pressed) {
//					b_groupBtn.setText("一般显示");
//					b_groupBtn.setIconClass('g_btn_preview');
//					g_store.groupBy(groupName, false);// 使用分组显示
//				} else {
//					b_groupBtn.setText("分组显示");
//					b_groupBtn.setIconClass('g_btn_groupview');
//					g_store.clearGrouping();// 不分组显示
//				};
//			}
//		});
//	}

	/** 分组小标题 * */
//	var g_groupView = new Ext.grid.GroupingView({
//		forceFit : true,
//		enableGroupingMenu : false,
//        hideGroupedColumn : false,
//        startCollapsed : true,
//        showGroupName : false,
//		groupTextTpl : '{text} ({[values.rs.length]} {[values.rs.length > 0 ? "条" : "无数据"]})'
//	});

	/** 条件查询参数 * */
	for (var n = 0; n < baseParams.length; n++) {
		var param = baseParams[n];
		g_store.baseParams[param[0]] = param[1];
	}

	/** 构造下工具条 * */
	var g_bbar = "";
//	if(bbar == false){
		g_bbar = new Ext.PagingToolbar({
			pageSize : pageSize,
			store : g_store
		});
//	}else{
//		g_bbar = new Ext.PagingToolbar({
//			pageSize : pageSize,
//			store : g_store,
//			plugins : new Ext.ux.PageSizePlugin(),
//			displayInfo : true,
//			items : [groupName == '' ? '' : b_groupBtn]
//		});
//	}
	/** 构造数据列表 * */
	var g_gridPanel = new Ext.grid.GridPanel({
		id : id,
		title : title,
		ds : g_store,
		cm : g_cm,
		sm : cmData[0][1] ? g_ckbox : null,
		stripeRows : true,
		loadMask : { msg: '数据请求中，请稍后...' },
		border : true,
		frame : true,
		animCollapse : false,
		animate : false,
		autoScroll : true,
		height : height, 
		width : width, 
		trackMouseOver : true,
		autoSizeColumns: true,
		viewConfig : { forceFit : true }, 
		margins : { top : 0, bottom : 0, right : 0, left : 0 },
		cmargins : { top : 0, bottom : 0, right : 0, left : 0 },
//		view : groupName == '' ? new Ext.grid.GridView : g_groupView, 
		bbar : cmData[0][3] ? g_bbar : null,
		tbar : typeof tbar != 'undefined' ? (Ext.isArray(tbar)==true?tbar[0]:tbar) :null 
	});
	
	//添加多条工具条
	g_gridPanel.on('render',function(){
		if(typeof tbar != 'undefined' && tbar!=''){
			if(Ext.isArray(tbar)==true){
				for(var i=1; i<tbar.length; i++){
					var bar = tbar[i];
					bar.render(g_gridPanel.tbar);
				}
				tbar = null;
			}
		}
	});
	
	/** 加载数据 * */
	if (cmData[0][2]){
		g_store.load({params : { start:0, limit:pageSize} });
	}
	return g_gridPanel;
};

Ext.ux.getData = function(_cmp){
	if(typeof _cmp == 'string'){
		var s_g = Ext.getCmp(_cmp);
		if(!s_g){
			return false;
		}else{
			var rs = s_g.getSelectionModel().getSelections();
			if(rs.length == 0 || rs.length > 1){
				return false;
			}
			return rs[0].data;
		}
	}else if(typeof _cmp == 'object'){
		var rs = _cmp.getSelectionModel().getSelections();
		if(rs.length == 0 || rs.length > 1){
			return false;
		}
		return rs[0].data;
	}else{
		return false;
	}
};