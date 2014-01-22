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
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
};
String.prototype.isEmpty = function(){
	return this.trim().length == 0;
};
var EX = {};
//EX.checkFoodStatus
EX.cfs = {
	SPECIAL	: 1 << 0, RECOMMEND : 1 << 1, STOP : 1 << 2, GIFT : 1 << 3, COMBO : 1 << 4,
	isSpecial : function(s){return (s & this.SPECIAL) != 0;},
	isRecommend : function(s){return (s & this.RECOMMEND) != 0;},
	isStop : function(s){return (s & this.STOP) != 0;},
	isGift : function(s){return (s & this.GIFT) != 0;},
	isCombo : function(s){return (s & this.COMBO) != 0;},
	setSpecial : function(s, result){
		if(result === true){s |= this.SPECIAL;}
		else{s &= ~this.SPECIAL;}
		return s;
	},
	setRecommend : function(s, result){
		if(result === true){s |= this.RECOMMEND;}
		else{ s &= ~this.RECOMMEND; }
		return s;
	},
	setStop : function(s, result){
		if(result === true){s |= this.STOP;}
		else {s &= ~this.STOP;}
		return s;
	},
	setGift : function(s, result){
		if(result === true){s |= this.GIFT; }
		else {s &= ~this.GIFT;}
		return s;
	},
	setCombo : function(s, result){
		if(result === true){s |= this.COMBO;}
		else{s &= ~this.COMBO;}
		return s;
	},
	formatName : function(r){
		var display = r.get('name'), s = r.get('status');
		if(this.isSpecial(s)){display += '&nbsp;<img title="特价" src="../images/icon_tip_special.png" />';}
		if(this.isRecommend(s)){display += '&nbsp;<img title="推荐" src="../images/icon_tip_recommend.png" />';}
		if(this.isStop(s)){display += '&nbsp;<img title="停售" src="../images/icon_tip_stop.png" />';}
		if(this.isGift(s)){display += '&nbsp;<img title="赠送" src="../images/icon_tip_gift.png" />';}
		if(this.isCombo(s)){display += '&nbsp;<img title="赠送" src="../images/icon_tip_combo.png" />';}
		return display;
	}
};
