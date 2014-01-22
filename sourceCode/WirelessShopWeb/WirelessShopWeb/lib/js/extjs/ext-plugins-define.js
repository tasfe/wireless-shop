/**
 * 自定义插件
 */
Ext.namespace('Ext.ux', 'Ext.ux.plugins');
Ext.ux.plugins.imgTypes = ['jpg', 'gif', 'bmp', 'png'];
Ext.ux.plugins.createImageFile = function(config){
	config = config || {};
	var img = new Ext.form.TextField({
		id : config.id,
		name : 'imgFile',
		fieldLabel : '选择图片',
		height : 22,
		inputType : 'file',
		listeners : {
			render : function(e){
				config.file = e;
				Ext.get(e.getId()).on('change', function(thiz){
					try{
						var img = Ext.getDom(config.img.getId());
						var file = Ext.getDom(config.file.getId());
						if(Ext.isIE){
	 						file.select();
	 						var imgURL = document.selection.createRange().text;
	 						if(imgURL && imgURL.length > 0){
	 							var index = imgURL.lastIndexOf('.');
 	        	    			var type = imgURL.substring(index+1, img.length);
 	        	    			var check = false;
 	        	    			for(var i = 0; i < Ext.ux.plugins.imgTypes.length; i++){
 	        	    				if(type.toLowerCase() == Ext.ux.plugins.imgTypes[i].toLowerCase()){
 	        	    					check = true;
 	        	    					break;
 	        	    				}
 	        	    			}
 	        	    			if(check){
 	        	    				img.src = Ext.BLANK_IMAGE_URL;
 	        	    				if(img.filters.length == 0){
 	        	    					img.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)';
 	        	    					img.style.width = typeof config.width == 'number' ? config.width+'px' : '400px';
 	        	    					img.style.height = typeof config.height == 'number' ? config.height+'px' : '300px';
 	        	    				}
 	        	    				img.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgURL; 	        	    					
 	        	    			}else{
 	        	    				file.select();
 	        	    				document.execCommand('Delete');
 	        	    				Ext.example.msg('提示', '操作失败, 选择的图片类型不正确, 请重新选择!');
 	        	    			}
	 						}
	 					}else{
	 						if(file.files && file.files[0]){
	 							var index = file.value.lastIndexOf('.');
 	        	    			var type = file.value.substring(index+1, img.length);
 	        	    			var check = false;
 	        	    			for(var i = 0; i < Ext.ux.plugins.imgTypes.length; i++){
 	        	    				if(type.toLowerCase() == Ext.ux.plugins.imgTypes[i].toLowerCase()){
 	        	    					check = true;
 	        	    					break;
 	        	    				}
 	        	    			}
 	        	    			if(check){
 	        	    				var reader = new FileReader();
 	        	    				reader.onload = function(evt){img.src = evt.target.result;};
 	        	    				reader.readAsDataURL(file.files[0]);
 	        	    			}else{
 	        	    				file.value = '';
 	        	    				Ext.example.msg('提示', '操作失败, 选择的图片类型不正确, 请重新选择!');
 	        	    			}
	 						}
	 					}
					} catch(e){
						Ext.example.msg('提示', '操作失败, 无法获取图片信息. 请换浏览器后重试.');
					}
				}, this);
			}
		}
	});
	img.refresh = function(){
		var img = Ext.getDom(config.img.getId());
		var file = Ext.getDom(config.file.getId());
		
		if(Ext.isIE){
			img.removeAttribute('src');
			img.style.filter = '';
			file.form.reset();
		}else{
			if(file.files.length > 0){
				var reader = new FileReader();
				reader.readAsDataURL(file.files[0]);
			}
			img.removeAttribute('src');
			file.value = '';
		}
	};
	img.setImg = function(iurl){
		var img = Ext.getDom(config.img.getId());
		if(Ext.isIE){
			img.src = Ext.BLANK_IMAGE_URL;
			if(img.filters.length == 0){
				img.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)';
				img.style.width = typeof config.width == 'number' ? config.width+'px' : '400px';
				img.style.height = typeof config.height == 'number' ? config.height+'px' : '300px';
			}
			img.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = iurl;
		}else{
			img.src = iurl;
		}
	};
	return img;
};

Ext.ux.plugins.HEInsertImage = function(config){
	config = config || {};
	Ext.apply(this, config);
	
	var heObject = null;
	this.init = function(htmlEditor){
        this.editor = htmlEditor;
        heObject = htmlEditor;
        this.editor.on('render', onRender, this);
    };  
    function onRender(){  
    	this.editor.tb.add('-');
        this.editor.tb.add({  
            itemId : 'htmlEditorImage',
            cls : 'x-btn-icon x-edit-insertimage',  
            enableToggle : false,  
            scope : this,  
            handler : function(){  
            	this.showWin();
            },  
            clickEvent : 'mousedown',  
            tooltip : config.buttonTip ||
            {  
                title : '插入图片',  
                text : '插入图片到编辑器',  
                cls : 'x-html-editor-tip'  
            },  
            tabIndex : -1  
        });
    }
    this.win = null;
    this.showWin = function(c){
    	if(!this.win){
    		var box = new Ext.BoxComponent({
				xtype : 'box',
		 	    columnWidth : 1,
		 	    height : 300,
		 	    autoEl : {
		 	    	tag : 'img',
		 	    	title : '菜品图预览.'
		 	    }
			});
    		var imgFile = Ext.ux.plugins.createImageFile({
    			img : box
    		});
    		var uploadMask = new Ext.LoadMask(document.body, {
    			msg : '正在上传图片...'
    		});
    		var btnUpload = new Ext.Button({
	 	        text : '上传图片',
	 	        listeners : {
	 	        	render : function(thiz){
	 	        		thiz.getEl().setWidth(100, true);
	 	        	}
	 	        },
	 	        handler : function(e){
	 	        	var check = true, img = '';
 	        		if(Ext.isIE){
 	        			Ext.getDom(imgFile.getId()).select();
 	        			img = document.selection.createRange().text;
 	        		}else{
		 	        	img = Ext.getDom(imgFile.getId()).value;
 	        		}
 	        		if(typeof(img) != 'undefined' && img.length > 0){
		 	        	var type = img.substring(img.lastIndexOf('.') + 1, img.length);
		 	        	check = false;
		 	        	for(var i = 0; i < Ext.ux.plugins.imgTypes.length; i++){
		 	        		if(type.toLowerCase() == Ext.ux.plugins.imgTypes[i].toLowerCase()){
		 	        			check = true;
			 	        	   	break;
			 	        	}
		 	        	}
		 	        	if(!check){
			 	        	Ext.example.msg('提示', '图片类型不正确.');
			 	        	return;
	 	        		}
 	        		}else{
 	        			Ext.example.msg('提示', '未选择图片.');
		 	        	return;
 	        		}
	 	        	uploadMask.show();
	 	        	Ext.Ajax.request({
		 	   			url : config.url,
		 	   			isUpload : true,
		 	   			form : form.getForm().getEl(),
		 	   			success : function(response, options){
		 	   				uploadMask.hide();
		 	   				var jr = Ext.decode(response.responseText.replace(/<\/?[^>]*>/g,''));
		 	   				if(jr.success){
		 	   					config.win.hide();
		 	   					if(!heObject.activated){ heObject.onFirstFocus(); };
		 	   					var imsg = '<div align="center" style="width:100%;"><img src="' + jr.other.url + '" style="max-width:95%;"/></div>';
		 	   					heObject.focus();
		 	   					if(Ext.isIE){
		 	   						heObject.setValue(heObject.getValue() + imsg);
		 	   					}else{
		 	   						heObject.execCmd('InsertHTML', '<br/>' + imsg + '<br/>');
		 	   					}
		 	   				}else{
		 	   					Ext.ux.showMsg(jr);
		 	   				}
		 	   			},
		 	   			failure : function(response, options){
		 	   				uploadMask.hide();
		 	   				Ext.ux.showMsg(Ext.decode(response.responseText.replace(/<\/?[^>]*>/g,'')));
		 	   			}
	 	        	});
	 	        }
			});
    		var btnClose = new Ext.Button({
	 	        text : '关闭',
	 	        listeners : {
	 	        	render : function(thiz){
	 	        		thiz.getEl().setWidth(100, true);
	 	        	}
	 	        },
	 	        handler : function(e){
	 	        	config.win.hide();
	 	        }
			});
    		var form = new Ext.form.FormPanel({
    			columnWidth : 1,
				labelWidth : 60,
				fileUpload : true,
				items : [imgFile],
				listeners : {
	 	    		render : function(e){
	 	    			Ext.getDom(e.getId()).setAttribute('enctype', 'multipart/form-data');
		 	  		}
	 	    	},
	 	    	buttonAlign : 'center',
	 	    	buttons : [btnUpload, btnClose]
    		});
    		
    		this.win = new top.Ext.Window({
    			title : '插入图片',
    			modal : true,
    			resizable : false,
    			closable : false,
    			width : 400,
    			items : [{
    				frame : true,
    				layout : 'column',
    				items : [box, {
    					columnWidth: 1, 
    					height: 20,
    					html : '<sapn style="font-size:13px;color:green;">提示: 单张图片大小不能超过100KB.</span>'
    				}, form]
    			}],
    			listeners : {
    				hide : function(){
    					imgFile.refresh();
    				}
    			}
    		});
    		config.win = this.win;
    	}
    	this.win.show();
    	this.win.center();
    };
};