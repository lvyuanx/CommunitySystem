package com.lvyx.commons.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.CommonsUrls;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.config.SystemProperties;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.commons.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * <p>
 * 文件控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-01-31 15:00:41
 */
@Api(tags = "文件控制器")
@ApiSupport(order = 2)
@RestController
@RequestMapping(CommonsUrls.FileCtrls.BASE_URL)
@Slf4j
public class FileController {

    @Resource
    private SystemProperties systemProperties;

    @LLogger(description = "获取图片流", params = {"response", "图片路径"})
    @ApiOperation("获取图片流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "图片路径")
    })
    @GetMapping(CommonsUrls.FileCtrls.GET_IMAGE_FOR_PATH)
    public void getImageForPath(HttpServletResponse response, String path) throws FileNotFoundException {
        try(BufferedInputStream inputStream = FileUtil.getInputStream(new File(systemProperties.getFilePath() + File.separator + path));
            OutputStream outputStream = response.getOutputStream();) {
            response.setContentType("image/png");
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new FileNotFoundException("图片不见了");
        }
    }


    @LLogger(description = "文件上传", params = {"文件", "文件用途"})
    @ApiOperation("文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件"),
            @ApiImplicitParam(name = "purpose", value = "文件用途")
    })
    @PostMapping(CommonsUrls.FileCtrls.FILE_UPLOAD)
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("purpose") String purpose){
        String filePath = "";
        try(InputStream in = file.getInputStream()) {
            ShiroUser shiroUser = ShiroUtils.getShiroUser();
            String dirPath = systemProperties.getFilePath() + File.separator + systemProperties.getUploadModule()  + File.separator + shiroUser.getId() + File.separator + purpose ;
            // 创建文件所在目录，不存在就创建
            FileUtil.mkdir(dirPath);
            // 防止文件重名
            String fileName = IdUtil.simpleUUID() + "_" + file.getOriginalFilename();
            // 写入文件到本地存储位置
            File outFile = new File(dirPath + File.separator + fileName);
            FileUtil.writeFromStream(in, outFile);
            filePath = systemProperties.getUploadModule()  + File.separator + shiroUser.getId() + File.separator + purpose + File.separator + fileName;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("文件上传异常");
        }
        return new SuccessResult<>("文件上传成功", filePath);
    }





}
