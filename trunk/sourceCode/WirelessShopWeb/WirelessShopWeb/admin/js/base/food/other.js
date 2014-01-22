

a_b_f.getImgFile = function(){
	var imgTypeTmp = ['jpg', 'png', 'gif'];
	var img = new Ext.form.TextField({
		xtype : 'textfield',
		id : 'a_b_f.foodImgFile',
		name : 'foodImage',
		fieldLabel : '选择图片',
		height : 22,
		inputType : 'file',
		listeners : {
			render : function(e){
				if(Ext.isIE){
					e.el.dom.size = 45;
				}else{
					e.el.dom.size = 40;
				}
				Ext.get('a_b_f.foodImgFile').on('change', function(){
					try{
						if(Ext.isIE){
	 						var img = Ext.getDom('a_b_f.foodBasic');
	 						var file = Ext.getDom('a_b_f.foodImgFile');
	 						file.select();
	 						var imgURL = document.selection.createRange().text;
	 						if(imgURL && imgURL.length > 0){
	 							var index = imgURL.lastIndexOf('.');
 	        	    			var type = imgURL.substring(index+1, img.length);
 	        	    			var check = false;
 	        	    			for(var i = 0; i < imgTypeTmp.length; i++){
 	        	    				if(type.toLowerCase() == imgTypeTmp[i].toLowerCase()){
 	        	    					check = true;
 	        	    					break;
 	        	    				}
 	        	    			}
 	        	    			if(check){
 	        	    				img.src = Ext.BLANK_IMAGE_URL;
 	        	    				img.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgURL;								 	        	    				
 	        	    			}else{
 	        	    				file.select();
 	        	    				document.execCommand('Delete');
 	        	    				Ext.example.msg('提示', '操作失败,选择的图片类型不正确,请重新选择!');
 	        	    			}
	 						}
	 					}else{
	 						var img = Ext.getDom('a_b_f.foodBasic');
	 						var file = Ext.getDom('a_b_f.foodImgFile');
	 						if(file.files && file.files[0]){
	 							var index = file.value.lastIndexOf('.');
 	        	    			var type = file.value.substring(index+1, img.length);
 	        	    			var check = false;
 	        	    			for(var i = 0; i < imgTypeTmp.length; i++){
 	        	    				if(type.toLowerCase() == imgTypeTmp[i].toLowerCase()){
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
 	        	    				Ext.example.msg('提示', '操作失败,选择的图片类型不正确,请重新选择!');
 	        	    			}
	 						}
	 					}
					} catch(e){
						Ext.example.msg('提示', '操作失败,无法获取图片信息.请换浏览器后重试.');
					}
				}, this);
			}
		}
	});
	return img;
};
a_b_f.refreshFoodImage = function(){
	var img = Ext.getDom('a_b_f.foodBasic');
	var imgFile = Ext.getCmp('a_b_f.foodImgFile');
	var imgForm = Ext.getCmp('a_b_f.foodImgFileUploadForm');
	img.src = '../images/nophoto.png';
	if(imgFile){
		imgForm.remove('a_b_f.foodImgFile');
	}
	imgFile = a_b_f.getImgFile();
	imgForm.add(imgFile);
//	imgForm.doLayout(true);
};

/**
 * 图片操作
 */
a_b_f.uploadImage = function(c){
	if(typeof c.arrt == 'undefined' || typeof(c.arrt.type) == 'undefined'){
		Ext.example.msg('提示', '操作失败, 获取图片操作类型失败, 请联系客服人员.');
		return;
	}
	if(c.arrt.type == 'upload'){
		$.LM.show();
		Ext.Ajax.request({
			url : '../admin/OperateFoodImage!upload.action',
			params : {
				'food.rid' : 26,
				'food.id' : c.id
			},
			waitTitle : '请稍候',
			waitMsg : '正在上传图片',
			isUpload : true,
			form : Ext.getCmp('a_b_f.foodImgFileUploadForm').getForm().getEl(),
			success : function(response, options){
				$.LM.hide();
				var jr = Ext.decode(response.responseText.replace(/<\/?[^>]*>/g,''));
				Ext.ux.msg(jr);
				a_b_f.initFoodWin().isUpdate = true;
				if(jr.success){
					
				}else{
					
				}
			},
			failure : function(response, options){
				$.LM.hide();
				Ext.ux.msg(Ext.decode(response.responseText.replace(/<\/?[^>]*>/g,'')));
			}
		});
	}else if(c.arrt.type == 'delete'){
		Ext.Ajax.request({
			url : '../admin/OperateFoodImage!delete.action?rid=26', 
			params : {
				
			},
			success : function(response, options){
				Ext.ux.lm.hide();
				a_b_f.initFoodWin().isUpdate = true;
			},
			failure : function(response, options){
				Ext.ux.lm.hide();
				alert('failure');
			}
		});
	}else{
		Ext.example.msg('提示', '操作失败, 获取图片操作类型失败, 请联系客服人员.');
		return;
	}
	
	
//	var btnUploadFoodImage = Ext.getCmp('btnUploadFoodImage');
//	var btnDeleteFoodImage = Ext.getCmp('btnDeleteFoodImage');
//	
//	btnUploadFoodImage.setDisabled(true);
//	btnDeleteFoodImage.setDisabled(true);
	
//	Ext.ux.lm.show();
	
	
};