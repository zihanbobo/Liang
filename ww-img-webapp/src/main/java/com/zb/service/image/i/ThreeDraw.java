package com.zb.service.image.i;

import java.io.IOException;

import com.zb.service.cloud.StorageService;

public interface ThreeDraw {

	String draw(String one, String two, String three, String tmpBackPic,String count)
			throws IOException;

	void setStorageService(StorageService storageService);
}
