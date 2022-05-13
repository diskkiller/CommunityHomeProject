package com.wildma.idcardcamera.global;

import com.wildma.idcardcamera.utils.FileUtils;

import java.io.File;

/**
 * Desc	        ${常量}
 */
public class Constant {
    public static final String APP_NAME = "IDCardCamera";//app名称
    public static final String BASE_DIR = APP_NAME + File.separator;//WildmaIDCardCamera/
    public static final String DIR_ROOT = FileUtils.getRootPath() + File.separator + Constant.BASE_DIR;//文件夹根目录 /storage/emulated/0/WildmaIDCardCamera/
}