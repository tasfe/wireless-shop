String.prototype.format = function(args){
    var result = this;
    if (arguments.length > 0){    
        if (arguments.length == 1 && typeof args == "object"){
            for(var key in args) {
                if(args[key] != undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }else{
        	for(var i = 0; i < arguments.length; i++){
        		if (arguments[i] != undefined) {
        			var reg= new RegExp("({)" + i + "(})", "g");
        			result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, ""); };
String.prototype.isEmpty = function(){ return this.trim().length == 0; };
$.extend(Zepto, {
	tip : {
		content : null,
		createBox : function(c){
			c.box = [
			    '<div id="'+c.id+'" class="zm-box">',
			    '<div for="title">'+c.title+'</div>',
			    '<div for="msg">'+c.msg+'</div>',
			    '</div>'
			].join('');
			return c;
		},
		init : function(){
			if(!this.content){
				$(document.body).append('<div id="zepto-msg-div" style=""><div>');
				this.content = $('#zepto-msg-div');
			}
		},
		show : function(c){
			this.init();
			var temp = {
				id : 'zm-box-' + parseInt(Math.random() * 100000),
				title : typeof c.title == 'undefined' ? '温馨提示' : c.title,
				msg : c.msg
			};
			temp = this.createBox(temp);
			this.content.append(temp.box);
			$('#'+temp.id).animate({
				opacity: 1,
				translateY: 0
			}, 200, 'ease-in', function(){
				var ci = null; 
				ci = window.setInterval(function(){
					$('#'+temp.id).animate({
						opacity: 0,
						translateY: '-100%'
					}, 
					600,
					'ease-out',
					function(e){
						$(this).remove();
						window.clearInterval(ci);
					});
				}, typeof time == 'number' ? c.time * 1000 : 2000);
			});
		}
	}
});

$.extend(Zepto, {
	msg : {
		dialongDisplay : function(c){
			if(c == null || typeof c == 'undefined' || typeof c.type == 'undefined'){
				return;
			}
			var el = $('#'+c.el), lm = $('div[for='+c.el+']');
			if(el.length <= 0){return;}
			if(lm.length <= 0){
				el.before('<div for="'+c.el+'" style="opacity:0; position: absolute; top:0; left:0; width: 100%; height: 100%; background: #DDD;"></div>');
				lm = $('div[for='+c.el+']');
			}
			if($.trim(c.type) == 'show'){
				el.css({
					zIndex: 99999,
					position: 'absolute',
					top: '50%',
					left: '50%',
					margin: '-{0}px 0 0 -{1}px'.format(el.height() / 2, el.width() / 2)
				});
				if(lm.hasClass('dialong-lm-hide')){
					lm.removeClass('dialong-lm-hide');			
				}
				if(el.hasClass('dialong-hide')){
					el.removeClass('dialong-hide');			
				}
				lm.addClass('dialong-lm-show');
				el.addClass('dialong-show');
			}else if($.trim(c.type) == 'hide'){
				lm.addClass('dialong-lm-hide');
				el.addClass('dialong-hide');
				if(typeof c.remove == 'boolean' && c.remove){
					var interval = null;
					interval = setInterval(function(){
						el.remove();
						lm.remove();
						clearInterval(interval);
						interval = null;
					}, 500);
				}
			}
		},
		/*----------------------*/
		event : [],
		interval : [],
		fireEvent : function(btn, id){
			for(var i = 0; i < this.event.length; i++){
				if(this.event[i].id == id && typeof this.event[i].fn == 'function'){
					this.event[i].fn(btn);
					break;
				}
			}
		},
		clearEvent : function(id){
			for(var i = 0; i < this.event.length; i++){
				if(this.event[i].id == id){
					this.event.splice(i, 1);
					break;
				}
			}
		},
		clearInterval : function(id){
			for(var i = 0; i < this.interval.length; i++){
				if(this.interval[i].id == id){
					clearInterval(this.interval[i].interval);
					this.interval.splice(i, 1);
					break;
				}
			}
		},
		createContent : function(c){
			var id = this.createId();
			var content = '<div id="'+id+'" class="box-vertical msg-base">'
				+ '<div data-type="title">'+(typeof c.title != 'string' || $.trim(c.title).length == 0 ? '温馨提示' : c.title)+'</div>'
				+ '<div data-type="content">'+c.msg+'</div>'
				+ (typeof c.time == 'number' ? '<div data-type="time">&nbsp;</div>' : '')
				+ '<div data-type="button" class="box-horizontal">'
					+ '<div class="div-full"></div>'
					+ '<div for="btnYes">确定</div>'
					+ (typeof c.buttons == 'string' && c.buttons.toUpperCase() == 'YESBACK' ? '<div for="btnBack" style="margin-left:20px;">返回</div>' : '')
					+ '<div class="div-full"></div>'
				+ '</div>'
				+ '</div>';
			c.id = id;
			c.content = content;
			document.body.insertAdjacentHTML('beforeEnd', content);
			$('#' + id + ' > div[data-type=button] > div[for=btnYes]').on('click', function(){ $.msg.save({event:'yes', id:id}); });
			$('#' + id + ' > div[data-type=button] > div[for=btnBack]').on('click', function(){ $.msg.save({event:'back', id:id}); });
			return c;
		},
		createId : function(){
			var id = null;
			var dom = null;
			while(true){
				id = 'zm-divMsg-' + parseInt(Math.random() * 10000);
				dom = document.getElementById(id);
				if(!dom)
					break;
			}
			dom = null;
			return id;
		},
		save : function(c){
			this.hide({
				event : 'yes',
				id : c.id
			});
		},
		hide : function(c){
			$.msg.dialongDisplay({
				el : c.id,
				type : 'hide',
				remove : true
			});
			this.fireEvent(c.event, c.id);
			this.clearEvent(c.id);
			this.clearInterval(c.id);
		},
		alert : function(c){
			var box = this.createContent({
				title : c.title,
				msg : c.msg,
				fn : c.fn,
				buttons : c.buttons,
				time : c.time
			});
			$.msg.dialongDisplay({
				el : box.id,
				type : 'show'
			});
			if(typeof c.fn == 'function'){
				this.event.push({
					id : box.id,
					fn : c.fn
				});
			}
			if(typeof c.time == 'number'){
				var to = null, t = c.time;
				to = setInterval(function(){
					if(t == 0){
						$.msg.clearInterval(c.id);
						$.msg.hide({
							event : 'yes', 
							id : box.id
						});
						to = null;
						return;
					}
					$('#'+box.id+' > div[data-type=time]').html(t + ' 秒后自动关闭.');
					t--;
				}, 1000);
				this.interval.push({
					id : box.id,
					interval : to
				});
			}
		}
	}
});

$.extend(Zepto, {
	LM : (function(){
		var lm = {
			isDisplay : false,
			id : 'lm-content-'+parseInt(Math.round(1-1000)),
			dom : null
		};
		var initDocument = function(){
			var el = '<div id="'+lm.id+'" data-type="lm-content-circular">'
				+ '<div data-type="lm-content-circular-1"></div>'
				+ '<div data-type="lm-content-circular-2"></div>'
				+ '<div data-type="lm-content-circular-3"></div>'
				+ '<div data-type="lm-content-circular-4"></div>'
				+ '<div data-type="lm-content-circular-5"></div>'
				+ '<div data-type="lm-content-circular-6"></div>'
				+ '<div data-type="lm-content-circular-7"></div>'
				+ '<div data-type="lm-content-circular-8"></div>'
			+ '</div>';
				
			document.body.insertAdjacentHTML('beforeEnd', el);
			lm.dom = document.getElementById(lm.id);
				
			lm.isInit = true;
		};
		lm.show = function(){
			if(!this.dom || !document.getElementById(this.id)){
				initDocument();
			}
			if(!this.isDisplay){
				$.msg.dialongDisplay({
					el : this.id,
					type : 'show'
				});
				this.isDisplay=true;
			}
		};
		lm.hide = function(){
			if(this.dom && this.isDisplay){
				$.msg.dialongDisplay({
					el : this.id,
					type : 'hide'
				});
				this.isDisplay=false;
			}
		};
		return lm;
	})()
});
$.extend(Zepto, {
	client : (function(){
		var BrowserDetect = {
			init: function () {
				this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
				this.version = this.searchVersion(navigator.userAgent)
					|| this.searchVersion(navigator.appVersion)
					|| "an unknown version";
				this.OS = this.searchString(this.dataOS) || "an unknown OS";
			},
			searchString: function (data) {
				for (var i=0;i<data.length;i++)	{
					var dataString = data[i].string;
					var dataProp = data[i].prop;
					this.versionSearchString = data[i].versionSearch || data[i].identity;
					if (dataString) {
						if (dataString.indexOf(data[i].subString) != -1)
							return data[i].identity;
					}
					else if (dataProp)
						return data[i].identity;
				}
			},
			searchVersion: function (dataString) {
				var index = dataString.indexOf(this.versionSearchString);
				if (index == -1) return;
				return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
			},
			dataBrowser: [{
				string: navigator.userAgent,
				subString: "Chrome",
				identity: "Chrome"
			}, {
				string: navigator.userAgent,
				subString: "OmniWeb",
				versionSearch: "OmniWeb/",
				identity: "OmniWeb"
			}, {
				string: navigator.vendor,
				subString: "Apple",
				identity: "Safari",
				versionSearch: "Version"
			}, {
				prop: window.opera,
				identity: "Opera"
			}, {
				string: navigator.vendor,
				subString: "iCab",
				identity: "iCab"
			}, {
				string: navigator.vendor,
				subString: "KDE",
				identity: "Konqueror"
			}, {
				string: navigator.userAgent,
				subString: "Firefox",
				identity: "Firefox"
			}, {
				string: navigator.vendor,
				subString: "Camino",
				identity: "Camino"
			}, {
				string: navigator.userAgent,
				subString: "Netscape",
				identity: "Netscape"
			}, {
				string: navigator.userAgent,
				subString: "MSIE",
				identity: "Explorer",
				versionSearch: "MSIE"
			}, {
				string: navigator.userAgent,
				subString: "Gecko",
				identity: "Mozilla",
				versionSearch: "rv"
			}, {
				string: navigator.userAgent,
				subString: "Mozilla",
				identity: "Netscape",
				versionSearch: "Mozilla"
			}],
			dataOS : [{
				string: navigator.platform,
				subString: "Win",
				identity: "Windows"
			},{
				string: navigator.platform,
				subString: "Mac",
				identity: "Mac"
			}, {
				string: navigator.userAgent,
				subString: "iPhone",
				identity: "iPhone/iPod"
		    }, {
				string: navigator.platform,
				subString: "Linux",
				identity: "Linux"
			}]
		};
		BrowserDetect.init();
		return {
			os : BrowserDetect.OS, 
			browser : BrowserDetect.browser, 
			isLinux :  BrowserDetect.OS.toLowerCase() == 'linux',
			isWindow :  BrowserDetect.OS.toLowerCase() == 'windows'
		};
	})()
});
$.extend(Zepto, {
	dom : function(id){
		return document.getElementById(id);
	},
	nav : {
		lsCls : 'nav-left-show',
		lhCls : 'nav-left-hide',
		toggle : function(c){
			if(c.otype == 'show'){
				this.show(c);
			}else if(c.otype == 'hide'){
				this.hide(c);
			}
		},
		show : function(c){
			var nav = $('#' + c.nav);
//			var dtime = typeof c.dtime == 'number' ? c.dtime : 300;
			// TODO 响应性能问题
			//$('#' + c.view).css('overflow-y', 'hidden');
			if(c.region == 'left'){
				nav.removeClass(this.lhCls).addClass(this.lsCls);
			}else if(c.region == 'right'){
				
			}else if(c.region == 'top'){
				
			}else if(c.region == 'bottom'){
				
			}
			if(typeof c.callback == 'function'){
				c.callback(c);
			}
		},
		hide : function(c){
			// TODO 响应性能问题
			//$('#' + c.view).css('overflow-y', 'auto');
			var nav = $('#' + c.nav);
			if(c.region == 'left'){
				nav.removeClass(this.lsCls).addClass(this.lhCls);
			}else if(c.region == 'right'){
				
			}else if(c.region == 'top'){
				
			}else if(c.region == 'bottom'){
				
			}
			if(typeof c.callback == 'function'){
				c.callback(c);
			}
		}
	}
});