function foodDataPaging(){
	params.start += params.limit;
	loadFoodData({
		callback : function(data){
			initFoodView({data:data});
		}
	});
}

function openateNavLeft(c){
//	if(c.otype == 'show'){
//	}else if(c.otype == 'hide'){
//		c.callback = function(){
//			$('#divFilterKitchen-dept > div[data-type=main]').html('');
//		};
//	}
	$.nav.toggle({
		view : 'divFoodView-Body',
		region : 'left',
		nav : 'divFilterKitchen',
		otype : c.otype,
		callback : function(){
			if(c.otype == 'show'){
				var view = params.getDeptView();
				if(view.getAttribute('data-isInit') === 'false'){
					initDeptView({
						data : params.deptData
					});
					view.setAttribute('data-isInit', 'true');
				}
			}
		}
	});
}

function filterKitchenByDept(c){
	initKitchenView({
		event : c.event,
		deptId : parseInt(c.event.getAttribute('data-value')),
		callback : function(){
			var cls = 'n-l-d-main-select';
			$('#divFilterKitchen-dept > div[data-type=main] > div[class*='+cls+']').removeClass(cls);
			$(c.event).addClass(cls);
		}
	});
}
function filterFoodByKitchen(c){
	c = c == null ? {} : c;
	params.start = 0;
	
	var clone = $.dom('divFoodView-Body-Paging').cloneNode();
	$.dom('divFoodView-Body').innerHTML = '';
	$.dom('divFoodView-Body').appendChild(clone);
	
	var cls = 'n-l-k-main-select';
	$('#divFilterKitchen-kitchen > div[class*=n-l-k-main] > div[class*='+cls+']').removeClass(cls);
	$(c.event).addClass(cls);
	
	loadFoodData({
		kitchenId : parseInt(c.event.getAttribute('data-value')),
		callback : function(data){
			if(data.length == 0){
				$.tip.show({msg:'该类别下没有菜品, 请重新选择.'});
			}else{
				$.dom('divFilterKitchen-mask').onclick();
				
				initFoodView({data:data});
			}
		}
	});
}
/**
 * 选择或取消选中菜品
 * @param c
 */
function changeSelectFood(c){
	var cls = 'div-fv-lb-m-has-select';
	var event = $(c.event);
	if(event.hasClass(cls)){
		event.removeClass(cls).html('+');
		operateOrderFood({
			id : c.id,
			otype : 'remove'
		});
	}else{
		event.addClass(cls).html('-');
		operateOrderFood({
			id : c.id,
			otype : 'add'
		});
	}
}
/**
 * 操作菜品
 * @param c
 *  {id:foodId, otype:operateType}
 */
function operateOrderFood(c){
	if(c.otype == 'add'){
		var has = false, add = null;
		for(var i = 0; i < params.orderData.length; i++){
			if(params.orderData[i].id == c.id){
				add = params.orderData[i];
				has = true;
				break;
			}
		}
		if(has){
			add.count++;
		}else{
			add = null;
			for(var i = 0; i < params.foodData.length; i++){
				add = params.foodData[i];
				if(add.id == c.id){
					params.orderData.push({
						id : add.id,
						name : add.name,
						count : 1,
						price : add.price,
						desc : add.desc,
						img : add.img
					});
					add = params.orderData[params.orderData.length - 1];
					break;
				}
			}
		}
	}else if(c.otype == 'delete'){
		// 删除菜品数量
	}else if(c.otype == 'remove'){
		// 移除菜品
		for(var i = 0; i < params.orderData.length; i++){
			if(params.orderData[i].id == c.id){
				params.orderData.splice(i,1);
				break;
			}
		}
	}else if(c.otype == 'claer'){
		// 清空已选菜品
		params.orderData = [];
	}
	
	var display = $.dom('spanShoppingBasket');
	display.innerHTML = params.orderData.length;
	
	if(params.orderData.length > 0){
		display.style.visibility = 'visible';
	}else{
		display.style.visibility = 'hidden';
	}
}

function openateBasket(c){
	$.nav.toggle({
		view : 'divFoodView-Body',
		region : 'left',
		nav : 'divShoppingBasket',
		otype : c.otype,
		callback : function(){
			var body = $('#divSB-Body');
			if(c.otype == 'show'){
				
			}else if(c.otype == 'hide'){
				body.html('');
			}
		}
	});
}