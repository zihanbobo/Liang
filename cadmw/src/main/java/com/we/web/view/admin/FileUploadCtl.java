package com.we.web.view.admin;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.we.common.Constant.ReCode;
import com.we.core.web.ReMsg;
import com.we.service.cloud.StorageService;

@Controller
@RequestMapping("/file")
public class FileUploadCtl extends AdminBaseCtl {

	@Autowired
	StorageService storageService;


	@ResponseBody
	@RequestMapping("/sysPic")
	public ReMsg uploadPic(@RequestParam MultipartFile file, String ptoken, String timestamp,
			HttpServletRequest request, HttpServletResponse response) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.ACCESS_TOKEN_ERR);
		}
		return storageService.uploadPic(file, ptoken, timestamp);
	}
	
	@ResponseBody
	@RequestMapping(value="/uploadImg",method=RequestMethod.POST)
	public Map<String,Object> uploadImg(@RequestParam MultipartFile file) {
		Map<String,Object> map = getImgMap();
		long uid = super.getUid();
		if (uid < 1) {
			map.put("state",ReCode.ACCESS_TOKEN_ERR);
			return map;
		}
		ReMsg reMsg= storageService.uploadMyImg(file, "uploadImg", null);
		if(ReCode.OK.reCode()==reMsg.getCode()) {
			map.put("state", "SUCCESS");
			map.put("url", ""+map.get("imgDomain")+reMsg.getData());
		}else {
			map.put("state",reMsg.getMsg());
		}
		return map;
	}


}
