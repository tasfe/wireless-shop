var Templet = {
	FoodList : '<div class="div-fv-lb-m box-horizontal">'
		+ '<div class="div-fv-lb-m-img" style="background-image: url({img}?1);"></div>'
		+ '<div class="div-fv-lb-m-msg div-full">'
			+ '<div data-type="name">{name}</div>'
			+ '<div data-type="price">¥:&nbsp;{price}</div>'
		+ '</div>'
		+ '<div class="div-fv-lb-m-has" data-value="{id}" onclick="changeSelectFood({event:this, id:{id}})">+</div>'
		+ '</div>',
	DeptBox : '<div data-value="{id}" onclick="filterKitchenByDept({event:this, id:{id}})">{name}</div>',
	KitchenBox : '<div data-value="{id}" onclick="filterFoodByKitchen({event:this, id:{id}})">{name}</div>'
};

function initView(){
	var height = document.documentElement.clientHeight;
	$('#divFoodView').css('height', height);
	$('#divFilterKitchen').css('height', height);
	$('#divNavRight').css('height', height);
	$('#divShoppingBasket').css('height', height);
}
/**
 * 初始化点击事件, 夸平台简单优化
 * 	暂不使用优化
 */
function initEvent(){
//	alert($.client.isLinux+'  :  '+$.client.isWindow)
//	if($.client.isLinux){
//		$.dom('div-fv-bn-kitchen').ontouchstart = function(){ openateNavLeft({otype:'show', event:this}); };
//		$.dom('divFilterKitchen-mask').ontouchstart = function(){ openateNavLeft({otype:'hide', event:this}); };
//		$.dom('divFoodView-Body-Paging').ontouchstart = function(){
//			params.start += params.limit;
//			loadFoodData({
//				callback : function(data){
//					initFoodView({data:data});
//				}
//			});
//		};
//	}else if($.client.isWindow){
		$.dom('div-fv-bn-kitchen').onclick = function(){ openateNavLeft({otype:'show', event:this}); };
		$.dom('divFilterKitchen-mask').onclick = function(){ openateNavLeft({otype:'hide', event:this}); };
		$.dom('div-fv-bn-basket').onclick = function(){ openateBasket({otype:'show', event:this}); };
//	}
}

function loadFoodData(c){
	c = c == null ? {} : c;
	params.start = typeof c.start == 'number' ? c.start : params.start;
	params.limit = typeof c.limit == 'number' ? c.limit : params.limit;
	params.kitchenId = typeof c.kitchenId == 'number' ? c.kitchenId : params.kitchenId;
	$.ajax({
		url : '../mobile/QueryFood!mobile.action',
		type : 'post',
		dataType : 'json',
		data : {
			start : params.start,
			limit : params.limit,
			'food.kitchenId' : params.kitchenId,
			'food.rid' : params.rid
		},
		success : function(data, status, xhr){
			//alert('mobile: '+data)
			params.imgUrl = data.other.imgUrl;
			params.imgUrlDefault = data.other.imgUrlDefault;
			params.foodData = params.foodData.concat(data.root);
			
			$.dom('divFoodView-Body-Paging').innerHTML = data.root.length >= 10 ? '点击加载更多.' : '没有更多记录.';
			
			if(typeof c.callback == 'function'){
				c.callback(data.root, c);
			}
		},
		error : function(xhr, errorType, error){
			alert('加载菜品数据失败.');
		}
	});
}
function initFoodView(c){
	c = c == null ? {} : c;
	var html = [], temp;
	for(var i = 0; i < c.data.length; i++){
		temp = c.data[i];
		html.push(Templet.FoodList.format({
			id : temp.id,
			name : temp.name,
			price : temp.price.toFixed(2),
			img : typeof temp.img == 'string' ? '{0}/{1}/small_{2}'.format(params.imgUrl, params.rid, temp.img) : params.imgUrlDefault
		}));
	}
	$.dom('divFoodView-Body-Paging').insertAdjacentHTML('beforeBegin', html.join(''));
	
	if(typeof c.callback == 'function'){
		c.callback(c);
	}
}

/**
 * 
 * @param c
 */
function loadDeptData(c){
	c = c == null ? {} : c;
	$.ajax({
		url : '../mobile/QueryDept!mobile.action',
		type : 'post',
		dataType : 'json',
		data : {
			start : 0,
			limit : 10,
			'dept.rid' : 26
		},
		success : function(data, status, xhr){
			params.deptData = data.root;
			if(typeof c.callback == 'function'){
				c.callback(data.root, c);
			}
		},
		error : function(xhr, errorType, error){
			alert('加载部门信息失败');
		}
	});
}
function initDeptView(c){
	c = c == null ? {} : c;
	var html = [], temp;
	for(var i = 0; i < params.deptData.length; i++){
		temp = params.deptData[i];
		html.push(Templet.DeptBox.format({
			id : temp.id,
			name : temp.name
		}));
	}
	params.getDeptView().innerHTML = html.join('');
	if(typeof c.callback == 'function'){
		c.callback(c);
	}
}
/**
 * 
 * @param data
 * @returns {Array}
 */
function CKV(data){
	var html = [], kt;
	for(var j = 0; j < data.kitchens.length; j++){
		kt = data.kitchens[j];
		html.push(Templet.KitchenBox.format({
			id : kt.id,
			name : kt.name
		}));
	}
	return html;
}
function initKitchenView(c){
	c = c == null ? {} : c;
	var html = [], temp;
	if(typeof c.deptId == 'number'){
		for(var i = 0; i < params.deptData.length; i++){
			temp = params.deptData[i];
			if(c.deptId == temp.id && typeof temp.kitchens != 'undefined'){
				html = CKV(temp);
			}	
		}
	}else{
		for(var i = 0; i < params.deptData.length; i++){
			temp = params.deptData[i];
			if(typeof temp.kitchens != 'undefined'){
				html.push(CKV(temp).join(''));
			}
		}
	}
	//if(html.length == 0) $.tip.show({msg:'该部门没有分厨信息.'});
	
	$('#divFilterKitchen-kitchen > div[data-type=main]').html(html.join(''));
	
	if(typeof c.callback == 'function'){
		c.callback(c);
	}
}
/**
 * 初始化系统参数, 性能优化
 */
function initParams(){
	params.deptView = $('#divFilterKitchen-dept > div[data-type=main]')[0];
	params.getDeptView = function(){
		return params.deptView;
	};
}

$(function(){
	initParams();
	
	
	window.onresize = initView;
	window.onresize();
	$('#divNavRight').css('transformX', '0');
	
	initEvent();
	
	loadFoodData({
		start : 0,
		limit : 10,
		callback : function(data){
			initFoodView({data:data});
			loadDeptData();
		}
	});
	
});