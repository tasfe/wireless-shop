
Ext.QuickTips.init();
Ext.lib.Ajax.defaultPostHeader += '; charset=utf-8';

Ext.override(Ext.tree.TreePanel, {
	autoScroll : true,
	bodyStyle : 'backgroundColor:#FFFFFF; border:1px solid #99BBE8;',
	lines : false
});
/*************************************
 * 解决DateField在Firefox宽度过长问题
 */
Ext.override(Ext.menu.DateMenu,{   
    render : function(){   
        Ext.menu.DateMenu.superclass.render.call(this);   
        this.picker.el.dom.childNodes[0].style.width = '178px';   
        this.picker.el.dom.style.width = '178px';   
    }   
});
/**
 * 重写HttpProxy.loadResponse()
 * 	增加回传参数,响应结果(ajax.response.responseText)
 */
Ext.override(Ext.data.HttpProxy, {
	loadResponse : function(o, success, response){
        delete this.activeRequest;
        if(!success){
            this.fireEvent("loadexception", this, o, response);
            o.request.callback.call(o.request.scope, null, o.request.arg, false, response);
            return;
        }
        var result;
        try {
            result = o.reader.read(response);
        }catch(e){
            this.fireEvent("loadexception", this, o, response, e);
            o.request.callback.call(o.request.scope, null, o.request.arg, false, response);
            return;
        }
        this.fireEvent("load", this, o, o.request.arg, response);
        o.request.callback.call(o.request.scope, result, o.request.arg, true, response);
    }
});
/**
 * 重写Store.loadRecords()
 * 	增加回传参数,响应结果(ajax.response.responseText)
 */
Ext.override(Ext.data.Store, {
	loadRecords : function(o, options, success, response){
		if(!o || success === false){
			if(success !== false){
                this.fireEvent("load", this, [], options, response);
            }
            if(options.callback){
                options.callback.call(options.scope || this, [], options, false, response);
            }
            return;
        }
        var r = o.records, t = o.totalRecords || r.length;
        if(!options || options.add !== true){
            if(this.pruneModifiedRecords){
                this.modified = [];
            }
            for(var i = 0, len = r.length; i < len; i++){
                r[i].join(this);
            }
            if(this.snapshot){
                this.data = this.snapshot;
                delete this.snapshot;
            }
            this.data.clear();
            this.data.addAll(r);
            this.totalLength = t;
            this.applySort();
            this.fireEvent("datachanged", this);
        }else{
            this.totalLength = Math.max(t, this.data.length+r.length);
            this.add(r);
        }
        this.fireEvent("load", this, r, options, response);
        if(options.callback){
            options.callback.call(options.scope || this, r, options, true, response);
        }
    }
});
/**
 * 禁止双击收缩/打开
 */
Ext.override(Ext.tree.TreeNodeUI, {
	onDblClick : function(e){
		e.preventDefault();
		if(this.disabled){
			return;
		}
		if(this.checkbox){
			this.toggleCheck();
		}
		if(!this.animating && this.node.hasChildNodes()){
			var isExpand = this.node.ownerTree.doubleClickExpand;
			if(isExpand){
				this.node.toggle();
			};
		}
		this.fireEvent("dblclick", this.node, e);
	}    
});