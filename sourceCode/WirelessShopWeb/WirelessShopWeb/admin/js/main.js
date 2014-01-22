/**
 * 重写请求响应, 处理登陆超时事件
 */
Ext.override(Ext.data.Connection, {
	validResponse : function(response, status){
		this.transId = false;
        var options = response.argument.options;
        response.argument = options ? options.argument : null;
        this.fireEvent("requestcomplete", this, response, options);
        
        var temp, ct = response.getResponseHeader['Content-Type'];
        if(typeof ct != 'string')
        	return;
        ct = ct.toLowerCase().trim();
        ct = ct.split(';');
        for(var i = 0; i < ct.length; i++){
        	temp = ct[i].split('/');
        	if(temp[0] == 'text') ct = temp[1];
        }
        if(ct == 'plain'){
        	var jr = Ext.decode(response.responseText);
        	if(jr.code == 9998){
        		this.abort();
        		var loginFN = function(){
        			window.location.href = "./index.htm";
        		};
        		var time = 2;
				window.setInterval(function(){
					if(time <= 0){
						time = null;
						loginFN();
					}
					Ext.getDom('fontCountdown').innerHTML = time;
					time--;
        		}, 2000);
        		Ext.Msg.show({
        			title : '提示',
        			msg : jr.msg + ' <font id="fontCountdown" color="red">3</font> 秒后自动跳转至登陆页面.',
        			buttons : Ext.Msg.OK,
        			icon : Ext.Msg.ERROR,
        			fn : function(){
        				loginFN();
        			}
        		});
        	}else if(jr.code == 9997){
        		Ext.ux.msg(jr);
        	}else{
        		Ext.callback(options.success, options.scope, [response, options]);
        		Ext.callback(options.callback, options.scope, [options, status, response]);
        	}
        }else if(ct == 'html'){
        	Ext.callback(options.success, options.scope, [response, options]);
        	Ext.callback(options.callback, options.scope, [options, status, response]);
        }
	},
	handleResponse : function(response){
        this.validResponse(response, true);
    },
    handleFailure : function(response, e){
    	this.validResponse(response, false);
    },
});

Ext.onReady(function(){
	Ext.BLANK_IMAGE_URL = "../lib/js/extjs/resources/images/default/s.gif";
	
	Ext.ux.lm = new Ext.LoadMask(document.body, {
		msg  : '操作请求中, 请稍候......',
		remove : false
	});
	
	var northPanel = new Ext.Panel({
		region : 'north',
		height : 50,
		html : 'north'
	});
	
	var southPanel = new Ext.Panel({
		region : 'south',
		height : 50,
		html : 'south'
	});
	
	// 主操作区域
	var centerPanel = new Ext.TabPanel({
		region : 'center',
		activeTab : 0,
		items : [{
			title : '主页',
			html : 'this is index'
		}]
	});
	
	// 后台功能导航
	var westPanel = new Ext.tree.TreePanel({
		region : 'west',
		title : '功能导航',
		width : 200,
		layout : 'fit',
		rootVisible : false,
		collapsible : true,
		loader : new Ext.tree.TreeLoader({}),
		root : new Ext.tree.AsyncTreeNode({
			text : 'root',
			expanded : true,
			children : [{
				text : '基本设置',
				expanded : true,
				children : [{
					text : '食品资料',
					fid : 'tree-base-foodBasic',
					url : './base/food.jsp',
					leaf : true
				}, {
					text : '部门管理',
					fid : 'tree-base-deptManagement',
					url : './base/dept.jsp',
					leaf : true
				}, {
					text : '员工管理',
					fid : 'tree-base-staffManagement',
//					url : './base/staff.jsp',
					leaf : true
				}, {
					text : '折扣方案',
					fid : 'tree-base-discountPlan',
//					url : './base/discountPlan.jsp',
					leaf : true
				}]
			}, {
				text : '报表查询',
				expanded : true,
				children : [{
					text : '当天账单',
					leaf : true
				}, {
					text : '历史账单',
					leaf : true
				}]
			}, {
				text : '会员管理',
				expanded : true,
				children : [{
					text : '会员类型',
					leaf : true
				}, {
					text : '会员资料',
					leaf : true
				}]
			}, {
				text : '微信公众平台',
				expanded : true,
				children : [{
					text : '会员资料',
					leaf : true
				}, {
					text : '餐厅形象',
					leaf : true
				}, {
					text : '餐厅简介',
					leaf : true
				}, {
					text : '促销信息',
					leaf : true
				}]
			}]
		}),
		listeners : {
			click : function(e){
				if(e.leaf){
					if(typeof e.attributes.url == 'undefined'){
						Ext.ux.msg({
							success : true,
							msg : '该功能建设中, 请稍微重试......'
						});
					}else{
						var tab = Ext.getCmp(e.attributes.fid);
						if(!tab){
							tab = new Ext.Panel({
								id : e.attributes.fid,
								title : e.text,
								closable : true,
								border : false,
								layout : 'fit',
								autoLoad : {
									url : e.attributes.url,
									params : {
										id : e.attributes.fid
									},
									scripts: true,
									nocache : true
								}
							});
							centerPanel.add(tab);
						}
						centerPanel.setActiveTab(tab);
					}
				}
			}
		}
	});
	
	new Ext.Viewport({
		layout : 'border',
		items : [northPanel, southPanel, westPanel, centerPanel]
	});
	
//	Ext.getDoc().on('contextmenu', function(e){
//		e.stopEvent();
//		e.preventDefault();
//	});
	
});