/**
 * 
 * @param {Object} $
 * @param {Object} owner
 */
(function($, owner) {
	
	/**
	 *  server 地址
	 */
	
		var serverAddr ="https:";

	/**
	 * server path
	 */
	 owner.apiConstant ={
	 	//user
 			   login:'/app/login',
		      logout:'/app/logout',
    		  getDynamicKey:'/app/dynamicKey',
	 	userRegister:"/app/register",
			 sendSms:"/app/sendSms",
		userForget:"/app/forget",
	    shareFriends:"/app/share/invitingFriends",
	    getPersionInfo:"/app/my/personalMine",
	    getAllMyWallet:"/app/my/wallet",
	    getAccountDetail:'/app/my/accountDetails',
	    getMyFriendDownLine:'/app/my/invitingFriendDownline',
	    getNewsInfo:'/app/news',
	    getNoticesInfo:'/app/notices',
	    getArticle:'/app/article',
	    getShareNews:'/app/shareNews',
	    getNotice:'/app/notice',
	    getBanners:'/app/banners',
	    getTasks:'/app/task',
	    updateSign:'/app/my/sign',
	    updatePersonalReward:'/app/my/takeReward',
	    getPersonalTask:'/app/my/personalTask',
	    uploadTaskInfo:'/app/my/upload',
	    getPersionInfo:'/app/my/personalMine',
	    uploadPersonalImg:'/app/my/updatePersonalImg',
	    uploadImg:'/app/my/uploadImg',
	    updateDoMyTask:'/app/my/doTask',
	    updateDoMyTaskReward:'/app/my/taskRewards',
	    updatePersonUsername:'/app/my/updatePersonUsername',
	    updateSettingPersonal:'/app/my/updateSettingPersonal',
	    updatePersonSex:'/app/my/updatePersonSex',
	    getMyAccountInfo:'/app/my/settingAccount',
	    getOriginalPwd:'/app/my/getOriginalPwd',
	    updateOriginalPwd:'/app/my/updateOriginalPwd',
	    updateOriginalEmail:'/app/my/updateOriginalEmail',
	    getSettingEmailInfo:'/app/my/settingEmail',
	    getAccountLoginRecord:'/app/my/getAccountLoginRecord',
	    getUploadKycRecord:'/app/my/settingkyc',
	    updateSettingkyc:'/app/my/updateSettingkyc',
	    getInvitingLevel:'/app/my/invitingLevel',
	    updateApplyForBusiness:'/app/my/applyForBusiness',
	    queryNoticeActivitys:'/app/queryNoticeActivitys',
	    getSharePic:'/app/my/sharePic',
	    getShareText:'/app/shareText',
	    queryUid:'/app/queryUid',
	    
	  
		/////////\\\==name=path=====
		constantName:"path error.."
	 }
	 
	 owner.serverApi =function(apiPath){
	 	var rootPath = serverAddr;
	 	if(name&&(name.indexOf("https://")>0||name.indexOf("http://")>0)){
	 		rootPath="";
	 	}
	 	if (apiPath&&null!=apiPath) {
	 		return rootPath+apiPath;
	 	}
	 	return rootPath;
	 }
	 owner.getObjsValue = function(objs){
	 	var obj=null;
	 	if(objs){
	 		objs.forEach(function(item){
				if(item.checked){
					obj=item.value;
				}
			})
	 		return obj;
	 	}
	 	return obj;
	 }
	 
	 owner.back =function(name,json){
	 	var self = plus.webview.currentWebview();
	 	var view = self.opener();
		mui.fire(view,name,json);
		self.close();
		view.show();
	 }
	 
	 owner.hengFymdhm =function(longDate){
	 	if(longDate&&(longDate+'').length>5){
	 		var date =new Date();
	 		date.setTime(longDate);
	 		return date.getFullYear()+"/"+getMonth(date)+"/"+getDay(date)+" "+getHours(date)+":"+getMinutes(date);
	 	}
	 	return '';
	 }
	 
	 owner.longFymdhm =function(longDate){
	 	if(longDate&&(longDate+'').length>5){
	 		var date =new Date();
	 		var tempTime = new Date(date.getFullYear(),getMM(date),getDay(date)).getTime();
	 		if(tempTime&&longDate>tempTime){
	 			date.setTime(longDate);
	 			return " "+getHours(date)+":"+getMinutes(date);
	 		}else{
	 			date.setTime(longDate);
	 			return date.getFullYear()+"-"+getMonth(date)+"-"+getDay(date)+" "+getHours(date)+":"+getMinutes(date);
	 		}
	 		
	 	}
	 	return '';
	 }
	 
	  owner.longFymdhms =function(longDate){
	 	if(longDate&&(longDate+'').length>5){
	 		var date =new Date();
	 		date.setTime(longDate);
	 		return date.getFullYear()+"-"+getMonth(date)+"-"+getDay(date)+" "+getHours(date)+":"+getMinutes(date);
	 	}
	 	return '';
	 }
	 
	 function getMM(date){  
	    var month = "";  
	    month = date.getMonth();  
	    if(month<10){  
	        month = "0" + month;  
	    }  
	    return month;  
	} 
	
	 function getMonth(date){  
	    var month = "";  
	    month = date.getMonth() + 1;  
	    if(month<10){  
	        month = "0" + month;  
	    }  
	    return month;  
	} 
	function getDay(date){  
	    var day = "";  
	    day = date.getDate();  
	    if(day<10){  
	        day = "0" + day;  
	    }  
	    return day;  
	}
	function getHours(date){
	    var hours = "";
	    hours = date.getHours();
	    if(hours<10){  
	        hours = "0" + hours;  
	    }  
	    return hours; 
	}
	function getMinutes(date){
	    var minute = "";
	    minute = date.getMinutes();
	    if(minute<10){  
	        minute = "0" + minute;  
	    }  
	    return minute;  
	}
	function getSeconds(date){
	    var second = "";
	    second = date.getSeconds();
	    if(second<10){  
	        second = "0" + second;  
	    }  
	    return second;  
	}
	 
	/**
	 * 
	 */
	owner.beginCount=function(val){
		if(!val)return;
	 		var num =Number(val);
			var waitForDoc = document.getElementById("waitFor");
			var getSmsDoc = document.getElementById("getSms");
			waitForDoc.innerHTML=num+"";
			if((num--)<=0){
				waitForDoc.style.display="none";
				getSmsDoc.style.display="inline";
				return;
			}else{
				if(num>0){
				  getSmsDoc.style.display="none";
				  waitForDoc.style.display="inline";
				}
				
			}
		
	};
	/**
	 * userid
	 */
	owner.getAccessUserId = function(){
		var getUser =localStorage.getItem("accessToken")
		if(getUser){
			return getUser;
		}
		return null;
	};
	/**
	 * 
	 * @param {Object} id
	*/	
	owner.validPhone = function(id) {
		if(!id)id="account";
		var $phone = document.getElementById(id);
		const regNum = /(\D+)*/g;
		var context = $phone.value;
		context = context.replace(regNum, "");
		context = context.substring(0, 11);
		$phone.value = context;
	}
	/**
	 * 
	 * @param {Object} id
	 */
	owner.validCode = function(id){
		if(!id)id="yzmcode";
		var $code = document.getElementById(id);
		var context = $code.value;
		context = context.substring(0, 8);
		$code.value = context;
	}
	
	/**
	 * 用户登录
	 **/
	owner.login = function(loginInfo, callback) {
		callback = callback || $.noop;
		loginInfo = loginInfo || {};
		loginInfo.username = loginInfo.username || '';
		loginInfo.password = loginInfo.password || '';
		if(!(loginInfo.username) || (loginInfo.username) == "") {
			return callback("올바른 휴대폰 번호를 입력해주세요");
		}
		if (loginInfo.password.length == 0) {
			return callback('비밀번호를 입력해주세요');
		}
		if (loginInfo.password.length < 6) {
			return callback('비밀번호가 올바르지 않습니다');
		}
		
		return callback();
	};

	owner.createState = function(name, callback) {
		var state = owner.getState();
		state.account = name;
		state.token = "token123456789";
		owner.setState(state);
		return callback();
	};
	
	owner.respondingTo = function(code,callback){
		if(11000==code){
			 $.clearLocalCache(); 
			 $.openWindow({
				id:'noId',
				url:'/login.html',
				show: {
					aniShow: 'pop-in'
				},
				waiting: {
					autoShow: true
				},
				extras: {
					$out:'1'
				}
			});
	 	 }
		return callback(code);
	}

	/**
	 * 新用户注册
	 **/
	owner.reg = function(regInfo, callback) {
		callback = callback || $.noop;
		regInfo = regInfo || {};
		var res = toVaild(regInfo);
		if(res&&null!=res){
			return callback(res);
		}
		if(!res&&null!=res){
			return callback("param error..");
		}
		return callback();
	};
	
	/**
	 * 
	 * @param {Object} val
	 * @param {Object} callback
	 */
	owner.setLocalItem = function(val,callback){
		if(val&&null!=val&&''!=val){
			localStorage.setItem("accessToken",val);
			return callback();
		}
		return callback("try again later..");
	}
	
	/**
	 * 
	 */
	$.clearLocalCache = function(){
		localStorage.clear();
		plus.navigator.removeAllCookie();
	}
	
	/**
	 * 
	 * @param {Object} phone
	 */
	owner.testPhone = function(phone){
		return regTestPhone(phone);
	}
	/**
	 * 
	 * @param {Object} regInfo
	 */
	function toVaild(regInfo) {
			if(!(regInfo.phone) || (regInfo.phone) == "") {
				return "올바른 휴대폰 번호를 입력해주세요";
			}
			if(!regTestPhone(regInfo.phone)){
				return "올바른 휴대폰 번호를 입력해주세요";
			}
			if(!regInfo.code || regInfo.code == "") {
				return "인증번호를 입력해주세요";
			}
			if(!regInfo.password || regInfo.password == "") {
				return "비밀번호를 입력해주세요";
			}
			if((regInfo.password).length < 6) {
				return "비밀번호가 올바르지 않습니다";
			}
			if(regInfo.password != regInfo.passwordConfirm) {
				return "비밀번호가 불일치 합니다";
			}
		return null;
	}
	/**
	 * 
	 */
	var regTestPhone = function(phone){
		phone+="";
		if(phone&&phone.length>1){
			var reg_zh =/^1[3|4|5|7|8]\d{9}$/;
			var reg_ko =/^01\d{9}$/;
			var reg_ko2=/^10\d{8}$/;
			var reg_ko3=/^\d{8}$/;
			if(!reg_zh.test(phone)&&!(reg_ko.test(phone)||reg_ko2.test(phone)||reg_ko3.test(phone))){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 拍照
	 */
	owner.getMobileCameraImage =function(id,callback){
		var mSize="10485760";
		var mobileCamera = plus.camera.getCamera();
		mobileCamera.captureImage(function(e) {
			plus.io.resolveLocalFileSystemURL(e, function(entry) {
				plus.io.resolveLocalFileSystemURL(e, function(entry) {
					entry.getMetadata(function (m) {
		               if(m.size>parseInt(mSize)){
							plus.nativeUI.toast('파일 크기는10M이내로 선택해주세요. ');
							return;
						}
						var path = entry.toLocalURL() + '?version=' + new Date().getTime();
						if(!callback||null==id){
							id(path);
						}else{
							callback(id,path);
						}
			        },function(e){
				        	var uploadErr=returnCameraCodeToMsg(e);
						mui.toast(uploadErr);
						console.log("拍照.: "+e.code+"=="+e.message);
			        });
				});
			}, function(e) {
				console.log("upload fail..");
			});
		}, function(e) {
			var uploadErr=returnCameraCodeToMsg(e);
			mui.toast(uploadErr);
			console.log("拍照.: "+e.code+"~~~"+e.message);
		});
	}
	/**
	 * 
	 */
	owner.getGalleryImage =function(id,callback){
		var mSize="10485760";
		plus.gallery.pick(function(url) {
			plus.io.resolveLocalFileSystemURL(url, function(entry) {
				entry.getMetadata(function (m) {
	               if(m.size>parseInt(mSize)){
						plus.nativeUI.toast('파일 크기는10M이내로 선택해주세요. ');
						return;
					}
					var path = entry.toLocalURL() + '?version=' + new Date().getTime();
					//uploadHeadImg(path);
					if(!callback||null==id){
						id(path);
					}else{
						callback(id,path);
					}
		        },function(e){
			        var uploadErr=returnCodeToMsg(e);
					mui.toast(uploadErr);
					console.log("图片上传: "+e.code+"=="+e.message);
		        });
			},function(e){
				mui.toast("not enough space.. ")
			});
		}, function(e) {
			var uploadErr=returnCodeToMsg(e);
			mui.toast(uploadErr);
			console.log("--图片上传: "+e.code+"~~~"+e.message);
		},{
			filter: 'image'
		});
	}

	/**
	 * 获取当前状态
	 **/
	owner.getState = function() {
		var stateText = localStorage.getItem('$state') || "{}";
		return JSON.parse(stateText);
		
	};
	/**
	 * 
	 **/
	owner.getLoginObj = function(def,key) {
		var auth =localStorage.getItem('$weshowAuth');
		if(auth&&null!=auth){
			auth=JSON.parse(auth)
			if(!auth[def]){
				if(!key)return '';
				return auth[key];
			}
			return auth[def];
		}
		return "";
	};
	/**
	 * 
	 **/
	owner.getAuth = function() {
		var auth =localStorage.getItem('$weshowAuth');
		if(auth&&null!=auth){
			auth=JSON.parse(auth)
			if(auth['username']&&auth['password']&&''!=auth['password']){
				return auth;
			}
			if(auth['phone']&&auth['password']&&''!=auth['password']){
				return auth;
			}
		}
		return null;
	};
	
	/**
	 * 
	 **/
	owner.setAuth = function(auth) {
		if(auth&&null!=auth){
			localStorage.setItem('$weshowAuth', JSON.stringify(auth));
		}
		//var settings = owner.getSettings();
		//settings.gestures = '';
		//owner.setSettings(settings);
	};

	/**
	 * 设置当前状态
	 **/
	owner.setState = function(state) {
		state = state || {};
		localStorage.setItem('$state', JSON.stringify(state));
		//var settings = owner.getSettings();
		//settings.gestures = '';
		//owner.setSettings(settings);
	};

	var checkEmail = function(email) {
		email = email || '';
		return (email.length > 3 && email.indexOf('@') > -1);
	};
	
	var backButtonPress = 0;
	owner.backQuit = function(){
		mui.back = function(event) {
			backButtonPress++;
			if (backButtonPress > 1) {
				plus.runtime.quit();
			} else {
				plus.nativeUI.toast('두번 클릭하여 로그아웃하기');
			}
			setTimeout(function() {
				backButtonPress = 0;
			}, 1000);
			return false;
		};
	}

	/**
	 * 找回密码
	 **/
	owner.forgetPassword = function(email, callback) {
		callback = callback || $.noop;
		if (!checkEmail(email)) {
			return callback('邮箱地址不合法');
		}
		return callback(null, '新的随机密码已经发送到您的邮箱，请查收邮件。');
	};

	owner.setSelfAvatar = function(avatar) {
		localStorage.setItem('$selfAvatar', avatar);
	};
	
	owner.getSelfAvatar = function() {
		return localStorage.getItem('$selfAvatar');
	}
	/**
	 * 获取应用本地配置
	 **/
	owner.setSettings = function(settings) {
		settings = settings || {};
		localStorage.setItem('$settings', JSON.stringify(settings));
	}
	/**
	 * 
	 */
	owner.download =function(url){
		if(url&&url!=''){
			var dtask = plus.downloader.createDownload(url,{},function(d,status){
		        	 if(status==200){
		        	 	plus.gallery.save(d.filename,function(){
		        	 		mui.toast("SUCCESS");
		        	 	},function(e){
		        	 		alert(e.message);
		        	 	});
		        	 }else{
		        	 	mui.toast("download fail..");
		        	 }
	        });        
			dtask.start();
		}
	}
	/**
	 * 设置应用本地配置
	 **/
	owner.getSettings = function() {
			var settingsText = localStorage.getItem('$settings') || "{}";
			return JSON.parse(settingsText);
		}
		/**
		 * 获取本地是否安装客户端
		 **/
//	owner.isInstalled = function(id) {
//		if (id === 'qihoo' && mui.os.plus) {
//			return true;
//		}
//		if (mui.os.android) {
//			var main = plus.android.runtimeMainActivity();
//			var packageManager = main.getPackageManager();
//			var PackageManager = plus.android.importClass(packageManager)
//			var packageName = {
//				"qq": "com.tencent.mobileqq",
//				"weixin": "com.tencent.mm",
//				"sinaweibo": "com.sina.weibo"
//			}
//			try {
//				return packageManager.getPackageInfo(packageName[id], PackageManager.GET_ACTIVITIES);
//			} catch (e) {}
//		} else {
//			switch (id) {
//				case "qq":
//					var TencentOAuth = plus.ios.import("TencentOAuth");
//					return TencentOAuth.iphoneQQInstalled();
//				case "weixin":
//					var WXApi = plus.ios.import("WXApi");
//					return WXApi.isWXAppInstalled()
//				case "sinaweibo":
//					var SinaAPI = plus.ios.import("WeiboSDK");
//					return SinaAPI.isWeiboAppInstalled()
//				default:
//					break;
//			}
//		}
//	}
	
	function catchErrCode(code){
		if(code&&DRAFTS[0][code]&&DRAFTS[0][code]!=""){
			return DRAFTS[0][code];
		}else{
			return "err code:"+code;
		}
	}
	
	function returnCodeToMsg(e){
		var uploadErr=null;
		switch(e.code){
			case -1:
			  uploadErr="err code:"+e.code;
			  break;
			case -2:
			  uploadErr=catchErrCode(200002);
			  break;
		    case -3:
			  uploadErr="err code:"+e.code;
			  break;
			 default:
			 uploadErr="err code:"+e.code;
		}
		return uploadErr;
	}
	
	function returnCameraCodeToMsg(){
		var uploadErr=null;
		switch(e.code){
			case 1:
			  uploadErr="err code:"+e.code;
			  break;
			case 2:
			  uploadErr=catchErrCode(200002);
			  break;
		    case 3:
			  uploadErr="err code:"+e.code;
			  break;
			 default:
			 uploadErr="err code:"+e.code;
		}
		return uploadErr;
	}
	
	owner.getJoinAmount = function(amount) {
		var str=amount+"";
		var decimalPlace="";
		if(str.indexOf(".")>0){
			var strArr = str.split(".");
			str=""+strArr[0];
			decimalPlace="."+strArr[1];
		}
		
		var pos=3;
		var arr=[];
		for(var i=1;;i++){
			if(str.length<pos){
	            arr.unshift(str.substr(0,str.length));
	            break;
	         }
			var posStart = str.length-pos*i;
			var posres =str.substr(posStart,pos);
			arr.unshift(posres);
			if(posStart<pos){
			  if(posStart>0){
			    arr.unshift(str.substr(0,posStart));
			   }
			  break;
			}
		}
		return arr.join(",")+decimalPlace;
	}
//文字
    owner.getDraft = function(code){
	    	  if(!code||code.length<6){
	    	  	return " -- ";
	    	  }
    		return DRAFTS[0][code];
    }
    
    
	var DRAFTS =[{
		"100001":"",
		"100002":"",
		"100003":"",
		"100004":"",
		"100005":"",
		"100006":"",
		"100007":"",
		"100008":"",
		"100009":"",
		"100010":"",
		"100011":"",
		"200001":"",
		"200002":"업로드 취소.",//取消上传图片操作.
		"200003":"",
		"210001":"",
		"210002":"촬영 취소.",//取消拍照操作.
		"210003":"",
		
		
	},{
//	   "100001":"您确定退出该账号.",
//		"100002":"退出登陆",
//		"100003":"取消",
//		"100004":"注册协议",
//		"100005":"登陆或注册即表示同意本站的用户协议和隐私政策.",
//		"100006":"",
//		"100007":"",
//		"100008":"",
//		"100009":"",
//		"100010":"",
//		"100011":"",
	}]

}(mui, window.app = {}));